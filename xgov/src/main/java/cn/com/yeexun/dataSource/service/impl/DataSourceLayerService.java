package cn.com.yeexun.dataSource.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dataSource.dao.DataSourceLayerDao;
import cn.com.yeexun.dataSource.dao.DatasourceSourceLayerDao;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.DataSourceLayer;
import cn.com.yeexun.dataSource.entity.DatasourceSourceLayerEntity;
import cn.com.yeexun.dataSource.entity.HbaseSource;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.dataSource.service.IDataSourceLayerService;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import tk.mybatis.mapper.entity.Example;

@Service
public class DataSourceLayerService 
		extends BaseService<DataSourceLayer> 
		implements IDataSourceLayerService {
	
	@Autowired
	private DataSourceLayerDao layerDao;
	
	@Autowired
	private DatasourceSourceLayerDao sourceLayerDao;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	@Override
	public void saveOrUpdateLayer(DataSourceLayer layer) {
		DataSourceLayer example = new DataSourceLayer();
		example.setParentId(layer.getParentId());
		example.setName(layer.getName());
		example.setDeleteFlag(String.valueOf(Constant.NOT_DELETE));
		List<DataSourceLayer> existLayers = layerDao.search(example);
		if (existLayers != null && existLayers.size() > 0) {
			throw new CommonException("数据源分层名称重复");
		}
		if (layer.getId() == 0) {
			layer.setCreateTime(new Date());
			layer.setLastModifyTime(new Date());
			if (layer.getParentId() == null) {
				layer.setParentId(-1L);
			}
			layer.setDeleteFlag(String.valueOf(Constant.NOT_DELETE));
			layerDao.insert(layer);
		} else {
			layer.setLastModifyTime(new Date());
			layerDao.updateByPrimaryKeySelective(layer);
		}
	}

	@Override
	public void deleteLayer(Long layerId) {
		// 先查他的下级
		DataSourceLayer example = new DataSourceLayer();
		example.setParentId(layerId);
		example.setDeleteFlag(String.valueOf(Constant.NOT_DELETE));
		List<DataSourceLayer> childs = layerDao.search(example);
		// 还有下级，继续往下查下级
		if (childs != null && childs.size() > 0) {
			for (DataSourceLayer child : childs) {
				deleteLayer(child.getId());
			}
		} else {
			// 已经是最下级了，删除中间表
			Example deleteExample = new Example(DatasourceSourceLayerEntity.class);
			deleteExample.createCriteria().andEqualTo("layerId", String.valueOf(layerId));
			sourceLayerDao.deleteByExample(deleteExample);
		}
		// 删除自己
		DataSourceLayer layer = layerDao.selectByPrimaryKey(layerId);
		layer.setDeleteFlag(String.valueOf(Constant.IS_DELETE));
		layerDao.updateByPrimaryKeySelective(layer);
	}

	@Override
	public void addSourceOnLayer(Long layerId, Long sourceId) {
		Example example = new Example(DatasourceSourceLayerEntity.class);
		example.createCriteria().andEqualTo("layerId", layerId).andEqualTo("datasourceId", sourceId);
		List<DatasourceSourceLayerEntity> exist = sourceLayerDao.selectByExample(example);
		if (exist != null && exist.size() > 0) {
			throw new CommonException("该数据源已经配置了分层！");
		}
		DatasourceSourceLayerEntity sourceLayer = new DatasourceSourceLayerEntity();
		sourceLayer.setDatasourceId(sourceId);
		sourceLayer.setLayerId(layerId);
		sourceLayerDao.insert(sourceLayer);
		
	}

	@Override
	public void removeSourceFromLayer(Long layerId, Long sourceId) {
		Example example = new Example(DatasourceSourceLayerEntity.class);
		example.createCriteria().andEqualTo("layerId", layerId).andEqualTo("datasourceId", sourceId);
		sourceLayerDao.deleteByExample(example);
	}

	@Override
	public List<MetadataDatasource> getSourceByLayerId(Long layerId, Page<DatasourceSourceLayerEntity> page) {
		List<DatasourceSourceLayerEntity> layerSources = sourceLayerDao.getByLayerIdPage(layerId, page);
		String sourceIds = layerSources.stream().map(DatasourceSourceLayerEntity :: getDatasourceId)
				.map(sourceId -> String.valueOf(sourceId)).collect(Collectors.joining(","));
		List<MetadataDatasource> sources = datasourceService2.getSourceByIds(sourceIds);
		if (sources != null && sources.size() > 0) {
			for (MetadataDatasource source : sources) {
				if(StringUtils.isNotBlank(source.getDbname()) && isRDBMS(source.getDatabaseType()))
					source.setSourceAddress( source.getDbname() + "@" + source.getIp() );
				else if(source.getDatabaseType().equalsIgnoreCase(Constant.HBASE) ){
					HbaseSource hbaseSource = datasourceService2.getHbaseSource(source.getId());
					source.setSourceAddress(source.getDbname() + "@" + hbaseSource.getZkQuorum());
				}else if(source.getDatabaseType().equalsIgnoreCase(Constant.HIVE)){
					source.setSourceAddress(source.getDbname() + "@" + source.getIp() );
				}else {
					source.setSourceAddress( source.getIp() );
				}
			}
		}
		return sources;
	}
	
	private Boolean isRDBMS(String dbType) {
		if(Constant.MYSQL.equalsIgnoreCase(dbType) ||
				Constant.ORACLE.equalsIgnoreCase(dbType) ||
				Constant.SQLSERVER.equalsIgnoreCase(dbType) ||
				Constant.TERADATA.equalsIgnoreCase(dbType) ||
				Constant.SYBASE.equalsIgnoreCase(dbType) ||
				Constant.DB2.equalsIgnoreCase(dbType) ||
				Constant.POSTGRESQL.equalsIgnoreCase(dbType)){
			return true;
		}
		return false;
	}

	@Override
	public List<MetadataDatasource> getSources() {
		
		List<DataSource> allDataSource = datasourceService2.getAllDataSource(null);
		List<DatasourceSourceLayerEntity> allSourceLayers = sourceLayerDao.selectAll();
		final List<Long> existSourceIds = allSourceLayers.stream()
				.map(DatasourceSourceLayerEntity :: getDatasourceId).collect(Collectors.toList());
		String sourceIds = allDataSource.stream().map(DataSource :: getId)
				.filter(sourceId -> !existSourceIds.contains(sourceId))
				.map(sourceId -> String.valueOf(sourceId)).collect(Collectors.joining(","));
		List<MetadataDatasource> sources = datasourceService2.getSourceByIds(sourceIds);
		return sources;
	}

	@Override
	public List<DataSourceLayer> getLayers() {
		
		Example example = new Example(DataSourceLayer.class);
		example.createCriteria().andEqualTo("deleteFlag", Constant.NOT_DELETE);
		List<DataSourceLayer> layers = layerDao.selectByExample(example);
		
		DataSourceLayer dataSourceLayer = new DataSourceLayer();
		dataSourceLayer.setId(-1L);
		List<DataSourceLayer> targetLayers = new ArrayList<>();
		buildLayerTree(dataSourceLayer, layers, targetLayers);
		return targetLayers;
	}
	
	private void buildLayerTree(DataSourceLayer parentLayer, List<DataSourceLayer> source, List<DataSourceLayer> target) {
		for (DataSourceLayer layer : source) {
			if (parentLayer.getId() == -1 && layer.getParentId() == parentLayer.getId()) {
				target.add(layer);
				buildLayerTree(layer, source, target);
			} else if (layer.getParentId() == parentLayer.getId()) {
				List<DataSourceLayer> children = parentLayer.getChildren();
				if (children == null) {
					children = new ArrayList<>();
					parentLayer.setChildren(children);
				}
				children.add(layer);
				buildLayerTree(layer, source, target);
			}
		}
	}

	@Override
	public String getLayerPath(Long layerId) {
		StringBuilder sb = new StringBuilder();
		getPath(layerId, sb);
		return sb.substring(0, sb.length() - 1);
	}

	private void getPath(Long layerId, StringBuilder sb) {
		DataSourceLayer layer = layerDao.selectByPrimaryKey(layerId);
		if (layer.getParentId() != -1) {
			getPath(layer.getParentId(), sb);
//			sb.append(layer.getName()).append("/");
		}/* else {
			sb.append(layer.getName()).append("/");
		}*/
		sb.append(layer.getName()).append("/");
	}
	
	

	
	
}
