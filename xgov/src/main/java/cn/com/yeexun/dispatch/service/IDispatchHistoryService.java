package cn.com.yeexun.dispatch.service;

import java.util.List;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dispatch.entity.DispatchHistory;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.entity.TaskHisQueryVo;
import cn.com.yeexun.dispatch.entity.TaskOverviewVo;

public interface  IDispatchHistoryService extends IBaseService<DispatchHistory>{
	
	TaskOverviewVo overview();

	List<DispatchHistory> listLastestHistoriesPage(DispatchHistory dispatchHistory, Page<DispatchTask> page);

	List<DispatchHistory> listHistoriesByTaskIdPage(TaskHisQueryVo hisQueryVo, Page<DispatchTask> page);

}
