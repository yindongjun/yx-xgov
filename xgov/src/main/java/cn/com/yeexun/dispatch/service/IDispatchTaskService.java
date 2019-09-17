package cn.com.yeexun.dispatch.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.entity.TaskOverviewVo;

public interface  IDispatchTaskService extends IBaseService<DispatchTask>{

	void excuteTask(long taskId)throws Exception;
	
	List<DispatchTask> findDispatchTaskPage(DispatchTask dispatchTask, String taskName, Page<DispatchTask> page);
	
	void saveOrUpdate(DispatchTask dispatchTask)throws Exception;

	List<DispatchTask> getDispatchByStatus(String status);

	void addJobToQuartz(DispatchTask dispatchTask);

	void pause(Long dispatchId);

	void resume(Long dispatchId);
	
	TaskOverviewVo overview();
	
	DispatchTask getTaskById(long id);

	DispatchTask findByTaskIdAndType(Long taskId, String type);
}
