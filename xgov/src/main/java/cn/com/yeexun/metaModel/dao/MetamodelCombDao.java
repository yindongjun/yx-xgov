package cn.com.yeexun.metaModel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.metaModel.entity.MetamodelCombEntity;

public interface MetamodelCombDao extends BaseDao<MetamodelCombEntity> {
	
	public List<MetamodelCombEntity> getByComModelId(@Param("modelId") Long modelId);
	public List<MetamodelCombEntity> getAllByComModelId(@Param("modelId") Long modelId);
	

}