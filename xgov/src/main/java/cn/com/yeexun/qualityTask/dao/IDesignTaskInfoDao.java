package cn.com.yeexun.qualityTask.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.DesignTaskInfo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;

public interface IDesignTaskInfoDao extends BaseDao<DesignTaskInfo>{
	/**
	 * 按质量任务名查询质量列表
	 * @param tableIds
	 * @param taskInfo
	 * @throws Exception
	 */
	List<DesignTaskInfo> getQualityTasksPage(@Param("page") Page<DesignTaskInfo> page
			, @Param("taskName") String taskName) throws Exception;

	Long getDatasourceIdByTaskId(Long taskId);

	List<DesignTableInfo> getTaskTables(Long taskId);

	List<QualityTaskDetail> getTaskTablesDetail(@Param("taskId") Long taskId
			, @Param("tableName") String tableName);
}
