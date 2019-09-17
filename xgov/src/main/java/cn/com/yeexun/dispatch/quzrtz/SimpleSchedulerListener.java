package cn.com.yeexun.dispatch.quzrtz;

import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.service.IDispatchTaskService;
import cn.com.yeexun.utils.CommonException;

@Component
public class SimpleSchedulerListener implements SchedulerListener {
	
	@Autowired
	private IDispatchTaskService dispatchTaskService;
	
	@Override
	public void jobScheduled(Trigger trigger) {
		
	}

	@Override
	public void jobUnscheduled(TriggerKey triggerKey) {
		
	}

	@Override
	public void triggerFinalized(Trigger trigger) {
		JobDataMap jobDataMap = trigger.getJobDataMap();
		// 需要在创建trigger时，将dispatchId存入trigger的jobDataMap中。
		Long dispatchId = jobDataMap.getLong("dispatchId");
		// 调度结束（trigger过期）
		modifyDispatchTaskStatus(dispatchId, DispatchTask.STATUS_FINALIZED);
	}

	@Override
	public void triggerPaused(TriggerKey triggerKey) {
		/*Trigger trigger = null;
		try {
			trigger = quartzScheduler.getTrigger(triggerKey);
		} catch (SchedulerException e) {
			throw new CommonException("获取trigger发生异常： ", e);
		}
		if (trigger == null) {
			throw new CommonException("根据triggerKey : " + trigger + " 获取的trigger为空！");
		}
		JobDataMap jobDataMap = trigger.getJobDataMap();
		String dispatchId = jobDataMap.getString("dispatchId");
		modifyDispatchTaskStatus(dispatchId, QuartzConstant.TRIGGER_PAUSED);*/
		
	}

	@Override
	public void triggersPaused(String triggerGroup) {
		
	}

	@Override
	public void triggerResumed(TriggerKey triggerKey) {
		/*Trigger trigger = null;
		try {
			trigger = quartzScheduler.getTrigger(triggerKey);
		} catch (SchedulerException e) {
			throw new CommonException("获取trigger发生异常： ", e);
		}
		if (trigger == null) {
			throw new CommonException("根据triggerKey : " + trigger + " 获取的trigger为空！");
		}
		String dispatchId = trigger.getJobDataMap().getString("dispatchId");
		modifyDispatchTaskStatus(dispatchId, QuartzConstant.TRIGGER_RUNNING);*/
	}

	@Override
	public void triggersResumed(String triggerGroup) {
		
	}

	@Override
	public void jobAdded(JobDetail jobDetail) {
		
	}

	@Override
	public void jobDeleted(JobKey jobKey) {
		
	}

	@Override
	public void jobPaused(JobKey jobKey) {
		
	}

	@Override
	public void jobsPaused(String jobGroup) {
		
	}

	@Override
	public void jobResumed(JobKey jobKey) {
		
	}

	@Override
	public void jobsResumed(String jobGroup) {
		
	}

	@Override
	public void schedulerError(String msg, SchedulerException cause) {
		
	}

	@Override
	public void schedulerInStandbyMode() {
		
	}

	@Override
	public void schedulerStarted() {
		
	}

	@Override
	public void schedulerStarting() {
		
	}

	@Override
	public void schedulerShutdown() {
		
	}

	@Override
	public void schedulerShuttingdown() {
		
	}

	@Override
	public void schedulingDataCleared() {
		
	}
	
	private void modifyDispatchTaskStatus(Long dispatchId, String status) {
		DispatchTask dispatchTask = new DispatchTask();
		dispatchTask.setId(dispatchId);
		dispatchTask.setStatus(status);
		dispatchTask.setLastModifyTime(new Date());
		try {
			dispatchTaskService.update(dispatchTask);
		} catch (Exception e) {
			throw new CommonException("修改调度任务状态时发生异常", e);
		}
	}

}
