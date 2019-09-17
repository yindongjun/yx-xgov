package cn.com.yeexun.dispatch.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.common.ssm.engine.utils.DateUtil;
//import cn.com.yeexun.collectTask.entity.CollectTask;
import cn.com.yeexun.collectTask.service.ICollectTaskService;
import cn.com.yeexun.dispatch.dao.IDispatchHistoryDao;
import cn.com.yeexun.dispatch.dao.IDispatchTaskDao;
import cn.com.yeexun.dispatch.entity.DispatchHistory;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.entity.TaskOverviewVo;
import cn.com.yeexun.dispatch.quzrtz.IQuartzService;
import cn.com.yeexun.dispatch.quzrtz.job.MetadataCollectQuartzJob;
import cn.com.yeexun.dispatch.quzrtz.job.QualityCheckQuartzJob;
import cn.com.yeexun.dispatch.service.IDispatchTaskService;
import cn.com.yeexun.qualityTask.dao.IDesignTaskTableDao;
import cn.com.yeexun.qualityTask.datacheck.DataCheck;
import cn.com.yeexun.qualityTask.entity.DesignTaskInfo;
import cn.com.yeexun.qualityTask.entity.DesignTaskTable;
import cn.com.yeexun.qualityTask.service.IDesignTableInfoService;
import cn.com.yeexun.qualityTask.service.IDesignTaskInfoService;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import tk.mybatis.mapper.entity.Example;

@Service
public class DispatchTaskService extends BaseService<DispatchTask> implements IDispatchTaskService{
	
	private static final Logger logger = LoggerFactory.getLogger(DispatchAlarmService.class);

	@Autowired
	private IDispatchTaskDao dispatchTaskDao;
	
	@Autowired
	private IDispatchHistoryDao dispatchHistoryDao;
	
	@Autowired
	@Lazy
	private ICollectTaskService collectTaskService;
	
	@Autowired
	@Lazy
	private IDesignTableInfoService designTableInfoService;
	
	@Autowired
	private IQuartzService quartzService;
	
	@Autowired
	private IDesignTaskInfoService designTaskInfoService;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	@Autowired
	private IDesignTaskTableDao designTaskTableDao;
	
	@Override
	public void excuteTask(long taskId) throws Exception {
		final DispatchTask task = dispatchTaskDao.selectByPrimaryKey(taskId);
		//先创建调度历史
		final DispatchHistory dispatchHistory = new DispatchHistory();
		dispatchHistory.setTaskId(task.getTaskId());
		dispatchHistory.setTaskType(task.getTaskType());
		dispatchHistory.setStartTime(new Date());
		dispatchHistory.setStatus(DispatchHistory.STATUS_ON);//正在进行中
		dispatchHistoryDao.insert(dispatchHistory);
		try {
			switch (task.getTaskType()) {
			case Constant.DISPATCH_TYPE_COLLECT:
//				collectTaskService.collectMetadata(task.getTaskId(), dispatchHistory.getId());
				break;
			case Constant.DISPATCH_TYPE_QUALITY:
				List<DesignTaskTable> taskTables = designTaskTableDao.findByTaskId(task.getTaskId());
				final CountDownLatch countDownLatch = new CountDownLatch(taskTables.size());
				final AtomicInteger ai = new AtomicInteger(0);
				for (final DesignTaskTable taskTable : taskTables) {
					threadPoolTaskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								DataCheck dataCheck = new DataCheck(taskTable.getTableId(), dispatchHistory.getId()
										, task.getTaskId());
								dataCheck.setCountDownLatch(countDownLatch);
								dataCheck.check();
							} catch (Exception e) {
								ai.getAndIncrement();
								logger.warn("校验执行异常,ai = " + ai.get());
							}
							
						}
					});
				}
				// 等待这批创建的所有线程执行结束。
				countDownLatch.await();
				if (ai.get() > 0) {
					logger.warn("质量校验发生异常，ai = " + ai.get());
					throw new CommonException("质量校验过程发生异常！");
				}
				break;

			default:
				throw new CommonException("不支持的任务类型！");
			}
			dispatchHistory.setEndTime(new Date());
			dispatchHistory.setSpent(DateUtil.subDate1ToDate2(dispatchHistory.getEndTime(),dispatchHistory.getStartTime()).toString());
			dispatchHistory.setStatus(DispatchHistory.STATUS_SUCCEED);
			dispatchHistoryDao.updateByPrimaryKey(dispatchHistory);
		} 
//		catch (CommonException e) {
//			throw e;
//		} 
		catch (Exception e) {
			dispatchHistory.setEndTime(new Date());
			dispatchHistory.setStatus(DispatchHistory.STATUS_FILED);
			dispatchHistoryDao.updateByPrimaryKey(dispatchHistory);
			if (e instanceof CommonException) {
				throw new CommonException(e.getMessage(), e);
			}
			throw new CommonException("任务执行失败 ： ", e);
		}
	}

	@Override
	public List<DispatchTask> findDispatchTaskPage(DispatchTask dispatchTask, String taskName, Page<DispatchTask> page) {
		
		List<DispatchTask> dispatchTasks = dispatchTaskDao.findDispatchTaskPage(dispatchTask, taskName, page);
		return dispatchTasks;
	}

	@Override
	@Transactional
	public void saveOrUpdate(DispatchTask dispatchTask) throws Exception {
		if ("0".equals(dispatchTask.getCycleType())) {
			if (Long.parseLong(dispatchTask.getTriggerTime()) < System.currentTimeMillis()) {
				throw new CommonException("触发时间不能早于当前时间！");
			}
		} else {
			Date currentDay = new Date();
			if (dispatchTask.getEndTime().compareTo(currentDay) <= 0) {
				throw new CommonException("调度结束时间必须大于当前时间！");
			}
		}
		if (dispatchTask.getId() == 0) {  // 新增
			Map<String, String> calcuLateExpression = quartzService.calcuLateExpression(
					dispatchTask.getJobCron(), dispatchTask.getTriggerTime());
			dispatchTask.setCronExpression(calcuLateExpression.get("cronExpression"));
			dispatchTask.setCronDescription(calcuLateExpression.get("cronDiscription"));
			dispatchTask.setCreateTime(new Date());
			dispatchTask.setLastModifyTime(new Date());
			dispatchTask.setStatus(DispatchTask.STATUS_RUNNING);
			dispatchTaskDao.insertSelective(dispatchTask);
			// 添加到 quartz job
			addJobToQuartz(dispatchTask);
		} else {  // 修改
			DispatchTask oldDispatch = dispatchTaskDao.selectByPrimaryKey(dispatchTask.getId());
			Map<String, String> calcuLateExpression = quartzService.calcuLateExpression(
					dispatchTask.getJobCron(), dispatchTask.getTriggerTime());
			dispatchTask.setCronExpression(calcuLateExpression.get("cronExpression"));
			dispatchTask.setCronDescription(calcuLateExpression.get("cronDiscription"));
			dispatchTask.setLastModifyTime(new Date());
			dispatchTaskDao.updateByPrimaryKeySelective(dispatchTask);
			//同步修改信息至质量任务or采集任务中去
			String type = oldDispatch.getTaskType();
			long taskId = oldDispatch.getTaskId();
			switch(type){
				//采集任务
				case "0":
//					CollectTask collect = collectTaskService.getById(taskId);
//					collect.setCycleType(dispatchTask.getCycleType());
//					collect.setStartTime(dispatchTask.getStartTime());
//					collect.setEndTime(dispatchTask.getEndTime());
//					collect.setTriggerTime(dispatchTask.getTriggerTime());
//					collect.setJobCron(dispatchTask.getJobCron());
//					collect.setLastModifyTime(new Date());
//					collectTaskService.update(collect);
					break;
				//质量任务
				case "1":
					DesignTaskInfo taskInfo = designTaskInfoService.getById(taskId);
					taskInfo.setCycleType(dispatchTask.getCycleType());
					taskInfo.setStartTime(dispatchTask.getStartTime());
					taskInfo.setEndTime(dispatchTask.getEndTime());
					taskInfo.setTriggerTime(dispatchTask.getTriggerTime());
					taskInfo.setJobCron(dispatchTask.getJobCron());
					taskInfo.setUpdateTime(new Date());
					designTaskInfoService.update(taskInfo);
					break;
				default:
					break;
			}
			// 修改quartz job信息
			String oldjobName = dispatchTask.getId() + "";
			String oldjobGroup = oldjobName + "__G";
			String oldtriggerName = oldDispatch.getId() + "";
			String oldtriggerGroup = oldtriggerName + "__G";
			String triggerName = dispatchTask.getId() + "";
			String triggerGroupName = dispatchTask.getId() + "__G";
			String jobName = dispatchTask.getId() + "";
			String jobGroupName = jobName + "__G";
			Map<String, Object> jobDataMap = new HashMap<>();
			jobDataMap.put("taskId", dispatchTask.getTaskId());
			jobDataMap.put("dispatchId", dispatchTask.getId());
			quartzService.modifyJobTime(oldjobName, oldjobGroup, oldtriggerName, oldtriggerGroup
						, jobName, jobGroupName, triggerName, triggerGroupName
						, dispatchTask.getCronExpression()
						, dispatchTask.getStartTime() == null ? new Date() : dispatchTask.getStartTime()
						, dispatchTask.getEndTime(),dispatchTask.getTaskType(),jobDataMap);
			// 判断之前的状态是暂停还是运行中，若是暂停，还要修改quartz trigger的状态
			if (DispatchTask.STATUS_PAUSED.equals(oldDispatch.getStatus())) {
				// 暂停触发器
				quartzService.pauseTrigger(triggerName, triggerGroupName);
			}else if(DispatchTask.STATUS_FINALIZED.equals(oldDispatch.getStatus())){
				DispatchTask dispatch = dispatchTaskDao.selectByPrimaryKey(dispatchTask.getId());
				dispatch.setStatus(DispatchTask.STATUS_RUNNING);
				save(dispatch);
			}
		}
	}

	@Override
	public void addJobToQuartz(DispatchTask dispatchTask) {
		String triggerName = dispatchTask.getId() + "";
		String triggerGroupName = triggerName + "__G";
		String jobName = dispatchTask.getId() + "";
		String jobGroupName = jobName + "__G";
		Map<String, Object> jobDataMap = new HashMap<>();
		jobDataMap.put("taskId", dispatchTask.getTaskId());
		jobDataMap.put("dispatchId", dispatchTask.getId());
		quartzService.addJob(jobName, jobGroupName, triggerName, triggerGroupName
				, getQuartzJobClass(dispatchTask.getTaskType()), dispatchTask.getCronExpression()
				,dispatchTask.getStartTime() == null ? new Date() : dispatchTask.getStartTime()
				, dispatchTask.getEndTime(), jobDataMap);
		if (DispatchTask.STATUS_PAUSED.equals(dispatchTask.getStatus())) {
			// 暂停触发器
			quartzService.pauseTrigger(triggerName, triggerGroupName);
		}
	}
	
	@Override
	public List<DispatchTask> getDispatchByStatus(String status) {
		if (status == null || status.trim().length() <= 0) {
			throw new CommonException("参数缺失！");
		}
		Example example = new Example(DispatchTask.class);
		example.createCriteria().andNotEqualTo("deleteFlag", "1").andEqualTo("status", status);
		List<DispatchTask> dispatchTasks = dispatchTaskDao.selectByExample(example);
		return dispatchTasks;
	}
	
	@Override
	@Transactional
	public void pause(Long dispatchId) {
		DispatchTask dispatchTask = dispatchTaskDao.selectByPrimaryKey(dispatchId);
		String triggerName = dispatchTask.getId() + "";
		String triggerGroupName = triggerName + "__G";
		dispatchTask.setStatus(DispatchTask.STATUS_PAUSED);
		dispatchTaskDao.updateByPrimaryKeySelective(dispatchTask);
		quartzService.pauseTrigger(triggerName, triggerGroupName);
	}
	
	@Override
	@Transactional
	public void resume(Long dispatchId) {
		DispatchTask dispatchTask = dispatchTaskDao.selectByPrimaryKey(dispatchId);
		String triggerName = dispatchTask.getId() + "";
		String triggerGroupName = triggerName + "__G";
		dispatchTask.setStatus(DispatchTask.STATUS_RUNNING);
		dispatchTaskDao.updateByPrimaryKeySelective(dispatchTask);
		quartzService.resumeTrigger(triggerName, triggerGroupName);
	}
	
	
	
	
	private Class<? extends Job> getQuartzJobClass(String taskType) {
		Class<? extends Job> clazz = null;
		switch (taskType) {
		case "0":
			clazz = MetadataCollectQuartzJob.class;
			break;
		case "1":
			clazz = QualityCheckQuartzJob.class;
			break;
		default:
			throw new CommonException("不支持的任务类型！");
		}
		return clazz;
	}

	@Override
	public TaskOverviewVo overview() {
		TaskOverviewVo overview = dispatchTaskDao.overview();
		return overview;
	}

	@Override
	public DispatchTask getTaskById(long id) {
		return dispatchTaskDao.getTaskById(id);
	}

	@Override
	public DispatchTask findByTaskIdAndType(Long taskId, String type) {
		return dispatchTaskDao.findByTaskIdAndType(taskId, type);
	}

	
}
