package cn.com.yeexun.dataElement.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dataElement.dao.IDataElementDao;
import cn.com.yeexun.dataElement.dao.IRelationShipDao;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataElement.entity.RelationShipEntity;
import cn.com.yeexun.dataElement.service.IRelationShipService;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;

@Service
public class RelationShipService extends BaseService<RelationShipEntity> implements IRelationShipService{

	@Autowired
	private IRelationShipDao relationShipDao;
	
	@Autowired
	private IDataElementDao dataElementDao;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	@Override
	public RelationShipEntity getByColumn(Long sourceId, String tablename,
			String fildname) throws Exception {
		return relationShipDao.getByColumn(sourceId, tablename, fildname);
	}

	@Override
	public List<RelationShipEntity> getByElementId(Long elementId)
			throws Exception {
		List<RelationShipEntity> relations = relationShipDao.getByElementId(elementId);
		String sourceIds = relations.stream().mapToLong(RelationShipEntity :: getSourceId)
				.distinct().mapToObj(sourceId -> String.valueOf(sourceId)).collect(Collectors.joining(","));
		List<MetadataDatasource> datasources = datasourceService2.getSourceByIds(sourceIds);
		for (RelationShipEntity relation : relations) {
			for (MetadataDatasource source : datasources) {
				if (relation.getSourceId() == source.getId()) {
					relation.setSourceName(source.getDatasourceName());
				}
			}
		}
		return relations;
	}

	@Override
	public boolean elementRelation(Long sourceId) throws Exception {
		int count = relationShipDao.getBySourceId(sourceId);
		if(count>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<DataElementEntity> getElementByDatasourceId(Long datasourceId) {
		return dataElementDao.getElementByDatasourceId(datasourceId);
	}
}
