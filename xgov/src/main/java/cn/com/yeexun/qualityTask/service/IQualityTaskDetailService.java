package cn.com.yeexun.qualityTask.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.DesignTaskInfo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityTask.entity.QualityTaskLog;

public interface IQualityTaskDetailService extends IBaseService<QualityTaskDetail>{
	
	List<QualityTaskDetail> findQualituTask(QualityTaskDetail qualityTaskDetail);
	
//	List<QualityTaskDetail> findByDesignTableId(Long designTableId);

//	QualityTaskDetail findInitNode(Long designTableId);
	
	List<QualityTaskDetail> getVerifyDetailByDesignTableId(Long designTableId);

	List<QualityTaskLog> execute(Long designTableInfoId, Integer isDebug);

	void addToTask(DesignTableInfo designTableInfo)throws Exception;

	List<MetadataDatasource> findSource2Task();

	/**
	 * 只查出同步design_table_info表中status为2的表名
	 * @param sourceId
	 * @param tableName
	 * @param page
	 * @return
	 */
	List<DesignTableInfo> findTableNamePage(Long sourceId, String tableName, Page<DesignTableInfo> page);
	
	/**
	 * 根据数据源id、表名、任务id 查询出表名
	 * @param sourceId
	 * @param tableName
	 * @param taskId
	 * @return
	 */
	List<DesignTableInfo> findTaskTables(Long sourceId,String tableName,Long taskId) throws Exception;

	void removeQualityDetail(Long tableDesignId);

	void removeQualityOnTableByType(Long datasourceId, String tableName, String columnName, String verifyType);

	void deleteByRelationIds(List<Long> relationIds);

	List<QualityTaskDetail> selectByElementId(Long id);

	void delete(Long datasourceId, String tableName);

//	void disableRules(String qualityTaskDetailIds, Integer enable);

	String debugAndSave(Long datasourceId);

	List<QualityTaskDetail> findByDesignTableId(Long designTableId, String tableName);

	QualityTaskDetail findInitNode(Long datasourceId, String tableName);

	void updateEnable(String jsonString, Long designTableId);

	List<QualityTaskDetail> findByElementDistinct(Long id);

	List<QualityTaskDetail> findByRelationId(Long relationId);

	void remove(Long dataSoureId, String tableName, String column);

	void deleteByElementId(Long elementId);

	void deleteBySourceIdAndTabName(Long datasourceId, String tableName, String verifyType);

	void removeQualityById(String qualityTaskDetailIds);

	List<QualityTaskDetail> findByDatasourceId(Long sourceId);

//	List<DesignTaskInfo> getQualityTasksPage(Page<DesignTaskInfo> page, String taskName);
	
	String exportQualityTaskDetail(Long sourceId);

}
