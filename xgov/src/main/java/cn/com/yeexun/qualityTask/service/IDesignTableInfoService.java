package cn.com.yeexun.qualityTask.service;

import java.util.List;
import java.util.Map;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.qualityTask.entity.CheckResultCount;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.DesignTaskInfo;
import cn.com.yeexun.qualityTask.entity.QualityTableCountVo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityTask.entity.UniqueVerifyDetail;
import cn.com.yeexun.qualityTask.vo.ChangeColumnRule;
import cn.com.yeexun.qualityTask.vo.ColumnDiffDetail;

public interface IDesignTableInfoService extends IBaseService<DesignTableInfo>{

	/**
	 * 根据数据源级别设计的id查找该数据源下所有的表的设计列表
	 * @param sourceInfoId
	 * @return
	 * @throws Exception
	 */
	List<DesignTableInfo> getTableDesignList(long sourceInfoId)throws Exception;

	/**
	 * 根据数据源级别设计id刷新该数据源下所有表的设计
	 * @param sourceInfoId
	 * @return
	 * @throws Exception
	 */
	List<DesignTableInfo> refreshTableDesignList(long sourceInfoId)throws Exception;

	/**
	 * 根据数据源设计的id查找到该数据源中有哪些表的规则信息进行了变更
	 * @param designSourceId
	 * @return
	 * @throws Exception
	 */
//	List<DesignTableInfo> getSourceChangeInfo(Long designSourceId)throws Exception;

	/**
	 * 跟据tableInfoId查询出changeInfo中两个版本的的不同改变
	 * @param tableInfoId
	 * @return
	 * @throws Exception
	 */
//	List<ChangeColumnRule> getTableChangeInfo(long tableInfoId)throws Exception;

	/**
	 * 根据返回的两次不同版本的detailId，获取两次不同的校验规则，进行比对
	 * @param beforeDetailId
	 * @param currentDetailId
	 * @param columnName
	 * @return
	 * @throws Exception
	 */
//	List<ColumnDiffDetail> getColumnChange(long beforeDetailId,
//			long currentDetailId, String columnName)throws Exception;

	/**
	 * 根据前端返回的designTableId创建出默认的质量校核节点 --即用sql进行查询
	 * @param tableDesignId
	 * @return
	 * @throws Exception
	 */
	QualityTaskDetail createTransByTable(long tableDesignId)throws Exception;

	/**
	 * 根据质量规则自动生成校核任务
	 * @param tableDesignId
	 * @return
	 * @throws Exception
	 */
	List<QualityTaskDetail> buildTransByTable(DesignTableInfo designTableInfo)throws Exception;

	/**
	 * 将taskDetail按照校验类型归类，返回给前端
	 * @param tasks
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getResultMap(
			List<QualityTaskDetail> tasks)throws Exception;

	/**
	 * 数据预览方法
	 * @param tableName
	 * @param designSourceId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getTablePreviewData(String tableName, Long sourceId)throws Exception;

//	DesignTableInfo findBySourceId(Long sourceId);

	void saveQualityTask(Long tableDesignId);
	
	void deleteQualityTasks(String tableDesignIds);

//	void removeQualityOnTable(Long tableDesignId) throws Exception;

//	void removeQualityOnTableByType(Long datasourceId, String tableName, String verifyType);
	void removeQualityOnTableByType(Long datasourceId, String tableName, String columnName, String verifyType);

//	void updateTableRuleId(long sourceId, String tableName, Long ruleId) throws Exception;

	public void deleteQualityTask(Long id);

	DesignTableInfo getBySourceAndTable(Long datasourceId, String name);

	void toDraft(Long tableInfoId);

	List<QualityTableCountVo> countQualityTable();

	List<DesignTableInfo> findByIds(String ids);

	void addUserDefineQuality(Long tableInfoId, List<QualityTaskDetail> taskDetails);

	List<Map<String, Object>> listReportTables();

	void removeQualityById(String qualityTaskDetailIds);

	List<QualityTaskDetail> getQualityTaskIdBySourceId(Long id);

	
	

}
