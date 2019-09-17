package cn.com.yeexun.metaModel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.metaModel.dao.MetamodelAttrDao;
import cn.com.yeexun.metaModel.entity.MetamodelAttrEntity;
import cn.com.yeexun.metaModel.entity.MetamodelEntity;
import cn.com.yeexun.metaModel.service.IMetamodelAttrService;

@Service
public class MetamodelAttrService 
		extends BaseService<MetamodelAttrEntity> 
		implements IMetamodelAttrService {
	
	@Autowired
	private MetamodelAttrDao metamodelAttrDao;

	@Override
	public List<MetamodelAttrEntity> findMetamodelAttrByModelCode(String modelCode) {
		return metamodelAttrDao.findMetamodelAttrByModelCode(modelCode);
	}

	@Override
	public List<MetamodelAttrEntity> findMetamodelAttrByid(Long modelId) {
		return  metamodelAttrDao.findMetamodelAttrByid(modelId);
	}
	
	

}
