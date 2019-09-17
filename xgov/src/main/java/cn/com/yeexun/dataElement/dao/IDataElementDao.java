package cn.com.yeexun.dataElement.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dataElement.entity.DataElementEntity;

public interface IDataElementDao extends BaseDao<DataElementEntity>{
	
	public DataElementEntity getByUniqueCode(@Param("uniqueCode") String uniqueCode)throws Exception;

	public List<DataElementEntity> showListByStatusPage(@Param("dataSourceId") Long dataSourceId,
			@Param("tableName") String tableName, @Param("status") Integer status, 
			@Param("dataElementType") String dataElementType,
			@Param("dataElementName") String dataElementName,
			@Param("page") Page<DataElementEntity> page)throws Exception;
	
	public List<DataElementEntity> getByIds(@Param("ids") String ids)throws Exception;
	
	public List<DataElementEntity> getAllElement(@Param("dataSourceId") Long dataSourceId,
			@Param("tableName") String tableName, 
			@Param("dataElementType") String dataElementType,
			@Param("dataElementName") String dataElementName)throws Exception;
	
	public Integer getElementSum()throws Exception;
	
	public List<DataElementEntity> getElementTop5()throws Exception;
	
	public List<DataElementEntity> getRelElementTop20()throws Exception;
	
	public Integer getCountByTime(@Param("date") Date date) throws Exception;
	
	public Integer checkRelation(@Param("sourceId") Long sourceId,
			@Param("tableName") String tableName, @Param("column") String column)throws Exception;
	
	List<DataElementEntity> getElementByDatasourceId(Long datasourceId);
}
