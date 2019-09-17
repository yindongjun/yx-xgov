package cn.com.yeexun.metaModel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.metaModel.entity.MetamodelEntity;

public interface MetamodelDao extends BaseDao<MetamodelEntity> {
	/**
	 * 获取元模型菜单,根据id正序
	 * 
	 * @param 
	 * @return List<MetamodelEntity>
	 */
	public List<MetamodelEntity> queryMetamodel();
	
	/**
	 * 根据名字或code模糊搜索元模型
	 * 
	 * @param name
	 * @return List<MetamodelEntity>
	 */
	public List<MetamodelEntity> fuzzyQueryMetamodel(@Param("name")String name,@Param("code")String code);
	
	
	/**
	 * 通过元模型id查找元模型
	 * @param modelIds
	 */
	public List<MetamodelEntity> findMetamodelById(@Param("modelId")Long modelId);
	
	/**
	 * 找到元模型子菜单
	 */
	public List<MetamodelEntity> findChildrenMetamodel(@Param("parentId")Long parentId);

	
}