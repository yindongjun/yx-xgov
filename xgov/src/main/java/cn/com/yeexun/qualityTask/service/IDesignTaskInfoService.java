package cn.com.yeexun.qualityTask.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.DesignTaskInfo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;

public interface IDesignTaskInfoService extends IBaseService<DesignTaskInfo>{
	/**
	 * 保存质量调度任务
	 * @param tableIds
	 * @param taskInfo
	 * @throws Exception
	 */
	void saveDesignTasks(String tableIds,DesignTaskInfo taskInfo) throws Exception;
	
	/**
	 * 按质量任务名查询质量列表
	 * @param tableIds
	 * @param taskInfo
	 * @throws Exception
	 */
	List<DesignTaskInfo> getQualityTasksPage(@Param("page") Page<DesignTaskInfo> page, @Param("taskName") String taskName) throws Exception;

	Long getDatasourceIdByTaskId(Long taskId);

	DesignTaskInfo findById(Long taskInfoId);

	List<DesignTableInfo> getTaskTables(Long taskId);

	List<QualityTaskDetail> getTaskTablesDetail(Long taskId, String tableName);

}
