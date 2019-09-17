package cn.com.yeexun.metaModel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.metaModel.entity.MetamodelDepEntity;

public interface MetamodelDepDao extends BaseDao<MetamodelDepEntity> {
	
	public List<MetamodelDepEntity> getByModelId(@Param("modelId") Long modelId)throws Exception;
}