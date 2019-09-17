package cn.com.yeexun.dataSource.dao;

import cn.com.yeexun.dataSource.entity.DatasourceSourceLayerEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;

public interface DatasourceSourceLayerDao extends BaseDao<DatasourceSourceLayerEntity>{

	List<DatasourceSourceLayerEntity> getByLayerIdPage(@Param("layerId") Long layerId, @Param("page") Page<DatasourceSourceLayerEntity> page);

}
