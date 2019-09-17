package cn.com.yeexun.metaModel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.metaModel.entity.MetamodelAttrEntity;

public interface MetamodelAttrDao extends BaseDao<MetamodelAttrEntity> {
	
	List<MetamodelAttrEntity> findMetamodelAttrByModelCode(String modelCode);
	
	List<MetamodelAttrEntity> findMetamodelAttrByid(@Param("modelId") Long modelId);

    
}