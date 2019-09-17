package cn.com.yeexun.dispatch.quzrtz.job;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.common.ssm.engine.utils.DateUtil;
import cn.com.yeexun.XgovApplication;
import cn.com.yeexun.collectTask.service.ICollectTaskService;
//import cn.com.yeexun.collectTask.service.impl.CollectTaskService;
import cn.com.yeexun.dispatch.entity.DispatchAlarm;
import cn.com.yeexun.dispatch.entity.DispatchHistory;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.service.IDispatchAlarmService;
import cn.com.yeexun.dispatch.service.IDispatchHistoryService;
import cn.com.yeexun.dispatch.service.IDispatchTaskService;
import cn.com.yeexun.dispatch.service.impl.DispatchAlarmService;
import cn.com.yeexun.dispatch.service.impl.DispatchHistoryService;
import cn.com.yeexun.dispatch.service.impl.DispatchTaskService;
import cn.com.yeexun.mail.service.EmailService;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.SpringContextHelper;
import cn.com.yeexun.utils.ThymeleafUtil;

public class MetadataCollectQuartzJob implements Job {
	
	private static Logger logger = LoggerFactory.getLogger(MetadataCollectQuartzJob.class);
	
	private IDispatchTaskService dispatchService;
	
	private ICollectTaskService collectTaskService;
	
	private IDispatchHistoryService dispatchHisService;
	
	private ThymeleafUtil thymeleafUtil;
	private EmailService emailService;
	private IDispatchAlarmService dispatchAlarmService;
	
	public MetadataCollectQuartzJob() {
		dispatchService = (IDispatchTaskService) SpringContextHelper.getBean(DispatchTaskService.class);
//		collectTaskService = (ICollectTaskService) SpringContextHelper.getBean(CollectTaskService.class);
		dispatchHisService = (IDispatchHistoryService) SpringContextHelper.getBean(DispatchHistoryService.class);
		thymeleafUtil = (ThymeleafUtil) SpringContextHelper.getBean(ThymeleafUtil.class);
		emailService = (EmailService) SpringContextHelper.getBean(EmailService.class);
		dispatchAlarmService = (IDispatchAlarmService) SpringContextHelper.getBean(DispatchAlarmService.class);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		JobDataMap dataMap = context.getMergedJobDataMap();
//		long taskId = dataMap.getLong("taskId");
		long dispatchId = dataMap.getLong("dispatchId");
//		CollectTask collectTask = null;
		DispatchTask dispatch = null;
		logger.info("开始执行调度任务，dispatchId = " + dispatchId);
		try {
//			collectTask = collectTaskService.getById(taskId);
			dispatch = dispatchService.getById(dispatchId);
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		}
		// 创建历史记录
		DispatchHistory dispatchHistory = new DispatchHistory();
		dispatchHistory.setTaskId(dispatch.getTaskId());
		dispatchHistory.setTaskType(dispatch.getTaskType());
		dispatchHistory.setStartTime(new Date());
		dispatchHistory.setStatus(DispatchHistory.STATUS_ON);//正在进行中
		try {
			logger.info("插入调度执行记录，dispatchHistory = " + dispatchHistory);
			dispatchHisService.insert(dispatchHistory);
		} catch (Exception e) {
			logger.error("创建调度执行历史发生异常！");
			throw new CommonException(e.getMessage(), e);
		}
		try {
//			collectTaskService.collectMetadata(dispatch.getTaskId(), dispatchHistory.getId());
		} catch (Exception e) {
			// 采集过程发生未知异常。
			// 发邮件
			if (dispatch.getAlarmId() != null) {
				DispatchAlarm alarm;
				try {
					alarm = dispatchAlarmService.getById(dispatch.getAlarmId());
				} catch (Exception e1) {
					throw new CommonException(e1.getMessage(), e1);
				}
				if (alarm.getStatus() == DispatchAlarm.STATUS_OPEN && 
						(DispatchAlarm.REASON_FAIL == alarm.getAlarmReasion() 
						|| DispatchAlarm.REASON_ALL == alarm.getAlarmReasion())) {
					try {
						Map<String, Object> valueMap = new HashMap<>();
						valueMap.put("taskName", dispatch.getTaskName());
						valueMap.put("taskType", "采集任务");
						valueMap.put("executeStatus", "失败");
						valueMap.put("startTime", dispatchHistory.getStartTime());
						valueMap.put("alarmTime", new Date());
						valueMap.put("ip", XgovApplication.env.getProperty("xgov.alarm.front"));
						String content = thymeleafUtil.processTemplate("alarm", valueMap);
						emailService.send(alarm.getReceivePeople(), EmailService.DISPATCH_ALARM_SUBJECT, content
								, "text/html;charset=utf-8");
					} catch (Exception e2) {
						logger.warn("邮件发送异常：", e2);
					}
					
				}
				
			}
			logger.error("采集过程发生异常！");
			dispatchHistory.setEndTime(new Date());
			dispatchHistory.setStatus(DispatchHistory.STATUS_FILED);
			try {
				dispatchHisService.update(dispatchHistory);
			} catch (Exception e1) {
				throw new CommonException(e1.getMessage(), e1);
			}
			throw new CommonException(e.getMessage(), e);
		}
		// 执行完成，记录历史。
		// 发邮件
		if (dispatch.getAlarmId() != null) {
			DispatchAlarm alarm;
			try {
				alarm = dispatchAlarmService.getById(dispatch.getAlarmId());
			} catch (Exception e1) {
				throw new CommonException(e1.getMessage(), e1);
			}
			if (alarm.getStatus() == DispatchAlarm.STATUS_OPEN && 
					(DispatchAlarm.REASON_SUCCESS == alarm.getAlarmReasion() 
					|| DispatchAlarm.REASON_ALL == alarm.getAlarmReasion())) {
				try {
					Map<String, Object> valueMap = new HashMap<>();
					valueMap.put("taskName", dispatch.getTaskName());
					valueMap.put("taskType", "采集任务");
					valueMap.put("executeStatus", "成功");
					valueMap.put("startTime", dispatchHistory.getStartTime());
					valueMap.put("alarmTime", new Date());
					valueMap.put("ip", XgovApplication.env.getProperty("xgov.alarm.front"));
					String content = thymeleafUtil.processTemplate("alarm", valueMap);
					emailService.send(alarm.getReceivePeople(), EmailService.DISPATCH_ALARM_SUBJECT, content
							, "text/html;charset=utf-8");
				} catch (Exception e2) {
					logger.warn("邮件发送异常：", e2);
				}
			}
			
		}
		dispatchHistory.setEndTime(new Date());
		dispatchHistory.setSpent(DateUtil.subDate1ToDate2(dispatchHistory.getEndTime(),dispatchHistory.getStartTime()).toString());
		dispatchHistory.setStatus(DispatchHistory.STATUS_SUCCEED);
		try {
			dispatchHisService.update(dispatchHistory);
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		}
	}

}
