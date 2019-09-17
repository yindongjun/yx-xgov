package cn.com.yeexun.qualityTask.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;

public interface IQualityTaskDetailDao extends BaseDao<QualityTaskDetail>{
	
	List<QualityTaskDetail> findByDesignTableId(@Param("datasourceId") Long datasourceId
			, @Param("tableName") String tableName);

	List<QualityTaskDetail> findSource2Task();

	List<DesignTableInfo> findTableNamePage(@Param("sourceId") Long sourceId
			, @Param("tableName") String tableName, @Param("page") Page<DesignTableInfo> page);
	
	List<DesignTableInfo> findTaskTables(@Param("sourceId") Long sourceId, @Param("tableName") String tableName,
			@Param("taskId") Long taskId) throws Exception;
	
	List<QualityTaskDetail> getVerifyDetailByDesignTableId(@Param("designTableId") Long designTableId);
	
//	List<DesignTaskInfo> getQualityTasksPage(@Param("page") Page<DesignTaskInfo> page, @Param("taskName") String taskName);

	void removeQualityDetail(Long tableDesignId);

	void removeQualityOnTableByType(@Param("datasourceId") Long datasourceId
			, @Param("tableName") String tableName, @Param("columnName") String columnName
			, @Param("verifyType") String verifyType);
	
	void deleteByRelationIds(List<Long> relationIds);

	List<QualityTaskDetail> selectByElementId(Long id);

	void deleteBySourceIdAndTableName(@Param("datasourceId") Long datasourceId
			, @Param("tableName") String tableName);


	List<QualityTaskDetail> findByElementDistinct(Long elementId);

	List<QualityTaskDetail> findByRelationId(Long relationId);

	void remove(@Param("dataSoureId") Long dataSoureId, @Param("tableName") String tableName
			, @Param("column") String column);

	void deleteByElementId(Long elementId);

	void deleteBySourceIdAndTabName(@Param("datasourceId") Long datasourceId
			, @Param("tableName") String tableName, @Param("verifyType") String verifyType);

	void removeQualityById(@Param("qualityTaskDetailIds") String qualityTaskDetailIds);

	List<QualityTaskDetail> findByDatasourceId(Long sourceId);

//	void disableRules(@Param("qualityTaskDetailIds") String qualityTaskDetailIds
//			, @Param("enable") Integer enable);
	

}
