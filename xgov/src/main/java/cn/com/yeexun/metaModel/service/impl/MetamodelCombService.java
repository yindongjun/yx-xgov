package cn.com.yeexun.metaModel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.metaModel.dao.MetamodelCombDao;
import cn.com.yeexun.metaModel.entity.MetamodelCombEntity;
import cn.com.yeexun.metaModel.service.IMetamodelCombService;

@Service
public class MetamodelCombService extends BaseService<MetamodelCombEntity> implements IMetamodelCombService{

	@Autowired
	private MetamodelCombDao metamodelCombDao;
	
	@Override
	public List<MetamodelCombEntity> getByComModelId(Long modelId)
			throws Exception {
		return metamodelCombDao.getByComModelId(modelId);
	}

	@Override
	public List<MetamodelCombEntity> getAllByComModelId(Long modelId) throws Exception {
		
		return  metamodelCombDao.getAllByComModelId(modelId);
	}

}
