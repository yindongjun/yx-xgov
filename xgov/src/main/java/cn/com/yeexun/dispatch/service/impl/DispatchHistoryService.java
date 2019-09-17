package cn.com.yeexun.dispatch.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dispatch.dao.IDispatchHistoryDao;
import cn.com.yeexun.dispatch.entity.DispatchHistory;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.entity.TaskHisQueryVo;
import cn.com.yeexun.dispatch.entity.TaskOverviewVo;
import cn.com.yeexun.dispatch.service.IDispatchHistoryService;
import cn.com.yeexun.utils.CommonException;

@Service
public class DispatchHistoryService extends BaseService<DispatchHistory> implements IDispatchHistoryService{

	@Autowired
	private IDispatchHistoryDao dispatchHistoryDao;
	
	@Override
	public TaskOverviewVo overview() {
		TaskOverviewVo overview = dispatchHistoryDao.overview();
		return overview;
	}

	@Override
	public List<DispatchHistory> listLastestHistoriesPage(DispatchHistory dispatchHistory
			, Page<DispatchTask> page) {
		List<DispatchHistory> lastestHistories 
				= dispatchHistoryDao.listLastestHistoriesPage(dispatchHistory, page);
		return lastestHistories;
	}

	@Override
	public List<DispatchHistory> listHistoriesByTaskIdPage(TaskHisQueryVo hisQueryVo
			, Page<DispatchTask> page) {
		
		if (hisQueryVo.getTaskId() == null) {
			throw new CommonException("缺失参数-->任务id : taskId");
		}
		if (!((hisQueryVo.getStartTimeLower() == null && hisQueryVo.getStartTimeUpper() == null) 
				|| (hisQueryVo.getStartTimeLower() != null && hisQueryVo.getStartTimeUpper() != null))) {
			throw new CommonException("开始时间范围参数错误，时间上限和下限必须同时存在或同时不存在！");
		}
		if (!((hisQueryVo.getEndTimeUpper() == null && hisQueryVo.getEndTimeUpper() == null) 
				|| (hisQueryVo.getEndTimeUpper() != null && hisQueryVo.getEndTimeUpper() != null))) {
			throw new CommonException("结束时间范围参数错误，时间上限和下限必须同时存在或同时不存在！");
		}
		List<DispatchHistory> lastestHistories 
				= dispatchHistoryDao.listHistoriesByTaskIdPage(hisQueryVo, page);
		return lastestHistories;
	}

}
