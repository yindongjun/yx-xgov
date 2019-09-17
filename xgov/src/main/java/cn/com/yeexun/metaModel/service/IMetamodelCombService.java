package cn.com.yeexun.metaModel.service;

import java.util.List;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.metaModel.entity.MetamodelCombEntity;


public interface IMetamodelCombService extends IBaseService<MetamodelCombEntity>{

	public List<MetamodelCombEntity> getByComModelId(Long modelId)throws Exception;
	
	public List<MetamodelCombEntity> getAllByComModelId(Long modelId)throws Exception;
}
