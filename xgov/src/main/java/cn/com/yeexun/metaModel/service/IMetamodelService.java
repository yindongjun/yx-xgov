package cn.com.yeexun.metaModel.service;

import java.util.List;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.metaModel.entity.MetamodelEntity;

public interface IMetamodelService extends IBaseService<MetamodelEntity>{
	/**
	 * 根据modelid查找菜单
	 * @param modelId
	 * @return
	 * @throws Exception
	 */
	public List<MetamodelEntity> getMetamodelMenu(Long modelId)throws Exception;
	
	/**
	 * 模糊查询metamodel
	 */
	public List<List> fuzzyQueryMetamodel(String name,String code);

	public List<MetamodelEntity> itemListMetamodel(Long id);
	
	
}
