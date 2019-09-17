package cn.com.yeexun.metaModel.service;

import java.util.List;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.metaModel.entity.MetamodelDepEntity;

public interface IMetamodelDepService extends IBaseService<MetamodelDepEntity>{

	public List<MetamodelDepEntity> getByModelId(Long modelId)throws Exception;
}
