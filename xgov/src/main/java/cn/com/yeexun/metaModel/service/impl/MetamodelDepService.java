package cn.com.yeexun.metaModel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.metaModel.dao.MetamodelDepDao;
import cn.com.yeexun.metaModel.entity.MetamodelDepEntity;
import cn.com.yeexun.metaModel.service.IMetamodelDepService;

@Service
public class MetamodelDepService extends BaseService<MetamodelDepEntity> implements IMetamodelDepService{

	@Autowired
	private MetamodelDepDao metamodelDepDao;
	
	@Override
	public List<MetamodelDepEntity> getByModelId(Long modelId) throws Exception {
		return metamodelDepDao.getByModelId(modelId);
	}

}
