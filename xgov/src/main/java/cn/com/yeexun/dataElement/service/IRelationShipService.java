package cn.com.yeexun.dataElement.service;

import java.util.List;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataElement.entity.RelationShipEntity;

public interface IRelationShipService extends IBaseService<RelationShipEntity>{
	
	/**
	 * 获取数据元的关联关系
	 * @param elementId 数据元id
	 * @return
	 * @throws Exception
	 */
	public List<RelationShipEntity> getByElementId(Long elementId)throws Exception;
	
	/**
	 * 查找与字段相关的关联关系
	 * @param sourceId 数据源id
	 * @param tablename 表名
	 * @param fildname 字段名
	 * @return
	 * @throws Exception
	 */
	public RelationShipEntity getByColumn(Long sourceId, 
			String tablename, String fildname)throws Exception;
	

	//判断数据元关联关系与该数据源是否相关
	public boolean elementRelation(Long sourceId) throws Exception;

	List<DataElementEntity> getElementByDatasourceId(Long id);
}
