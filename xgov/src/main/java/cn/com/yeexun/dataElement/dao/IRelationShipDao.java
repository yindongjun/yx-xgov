package cn.com.yeexun.dataElement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.dataElement.entity.RelationShipEntity;

public interface IRelationShipDao extends BaseDao<RelationShipEntity>{

	/**
	 * 根据表名称和数据源id查询改表所匹配到的规则
	 * @param sourceId
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	List<RelationShipEntity> getRelationBytable(@Param("sourceId")Long sourceId, @Param("tableName")String tableName)throws Exception;

	public List<RelationShipEntity> getByElementId(@Param("elementId") Long elementId) throws Exception;
	
	public RelationShipEntity getByColumn(@Param("sourceId") Long sourceId, 
			@Param("tablename") String tablename, @Param("fildname") String fildname) throws Exception;
	
	public Integer getRelEleCount() throws Exception;
	
	public List<RelationShipEntity> getRelTop10() throws Exception;

//	List<RelationShipEntity> getByTableName(@Param("sourceId") long sourceId,  @Param("tableName")String tableName)throws Exception;
	
	public Integer getBySourceId(@Param("sourceId") long sourceId)throws Exception;

	
}
