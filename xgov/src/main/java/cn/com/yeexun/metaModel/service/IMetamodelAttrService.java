package cn.com.yeexun.metaModel.service;

import java.util.List;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.metaModel.entity.MetamodelAttrEntity;
import cn.com.yeexun.metaModel.entity.MetamodelEntity;

public interface IMetamodelAttrService 
		extends IBaseService<MetamodelAttrEntity> {
	
	List<MetamodelAttrEntity> findMetamodelAttrByModelCode(String modelCode);

	List<MetamodelAttrEntity> findMetamodelAttrByid(Long modelId);

}
