package cn.com.yeexun.dataSource.service;

import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.DataSourceLayer;
import cn.com.yeexun.dataSource.entity.DatasourceSourceLayerEntity;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;

import java.util.List;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;

public interface IDataSourceLayerService extends IBaseService<DataSourceLayer> {

	void saveOrUpdateLayer(DataSourceLayer layer);

	void deleteLayer(Long layerId);

	void addSourceOnLayer(Long layerId, Long sourceId);

	void removeSourceFromLayer(Long layerId, Long sourceId);

	List<MetadataDatasource> getSourceByLayerId(Long layerId, Page<DatasourceSourceLayerEntity> page);

	List<MetadataDatasource> getSources();

	List<DataSourceLayer> getLayers();

	String getLayerPath(Long layerId);

}
