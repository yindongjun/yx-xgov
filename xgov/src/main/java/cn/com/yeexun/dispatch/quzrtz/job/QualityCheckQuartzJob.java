package cn.com.yeexun.dispatch.quzrtz.job;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import cn.com.common.ssm.engine.utils.DateUtil;
import cn.com.yeexun.XgovApplication;
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
import cn.com.yeexun.mail.service.IMailService;
import cn.com.yeexun.qualityTask.dao.IDesignTaskTableDao;
import cn.com.yeexun.qualityTask.datacheck.DataCheck;
import cn.com.yeexun.qualityTask.entity.DesignTaskTable;
import cn.com.yeexun.user.service.UserClient;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.SpringContextHelper;
import cn.com.yeexun.utils.ThymeleafUtil;

public class QualityCheckQuartzJob implements Job {
	
	private static Logger logger = LoggerFactory.getLogger(MetadataCollectQuartzJob.class);
	
	private IDispatchTaskService dispatchService;
	
	private IDispatchHistoryService dispatchHisService;
	
	private IDesignTaskTableDao designTaskTableDao;
	
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	private EmailService emailService;
	
	private IDispatchAlarmService dispatchAlarmService;
	
	private ThymeleafUtil thymeleafUtil;
	
	public QualityCheckQuartzJob() {
		dispatchService = (IDispatchTaskService) SpringContextHelper.getBean(DispatchTaskService.class);
		dispatchHisService = (IDispatchHistoryService) SpringContextHelper.getBean(DispatchHistoryService.class);
		designTaskTableDao = (IDesignTaskTableDao) SpringContextHelper.getBean(IDesignTaskTableDao.class);
		threadPoolTaskExecutor = (ThreadPoolTaskExecutor) SpringContextHelper.getBean(ThreadPoolTaskExecutor.class);
		emailService = (EmailService) SpringContextHelper.getBean(EmailService.class);
		dispatchAlarmService = (IDispatchAlarmService) SpringContextHelper.getBean(DispatchAlarmService.class);
		thymeleafUtil = (ThymeleafUtil) SpringContextHelper.getBean(ThymeleafUtil.class);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		JobDataMap dataMap = context.getMergedJobDataMap();
		long dispatchId = dataMap.getLong("dispatchId");
		DispatchTask dispatch = null;
		try {
			dispatch = dispatchService.getById(dispatchId);
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		}
		// 创建历史记录
		final DispatchHistory dispatchHistory = new DispatchHistory();
		dispatchHistory.setTaskId(dispatch.getTaskId());
		dispatchHistory.setTaskType(dispatch.getTaskType());
		dispatchHistory.setStartTime(new Date());
		dispatchHistory.setStatus(DispatchHistory.STATUS_ON);//正在进行中
		try {
			dispatchHisService.insert(dispatchHistory);
		} catch (Exception e) {
			logger.error("创建调度执行历史发生异常！");
			throw new CommonException(e.getMessage(), e);
		}
		final Long taskId = dispatch.getTaskId();
		List<DesignTaskTable> taskTables = designTaskTableDao.findByTaskId(dispatch.getTaskId());
		final CountDownLatch countDownLatch = new CountDownLatch(taskTables.size());
		final AtomicInteger ai = new AtomicInteger(0);
		try {
			for (final DesignTaskTable taskTable : taskTables) {
				
				threadPoolTaskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							DataCheck dataCheck = new DataCheck(taskTable.getTableId(), dispatchHistory.getId()
									, taskId);
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
		} catch (Exception e) {
			// 质量校验过程发生未知异常。
			logger.error("质量校验过程发生异常！");
			dispatchHistory.setEndTime(new Date());
			dispatchHistory.setStatus(DispatchHistory.STATUS_FILED);
			try {
				dispatchHisService.update(dispatchHistory);
			} catch (Exception e1) {
				throw new CommonException(e1.getMessage(), e1);
			}
			// 告警
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
						valueMap.put("taskType", "质量校验任务");
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
			throw new CommonException(e.getMessage(), e);
		}
		// 执行完成，记录历史。
		// 告警
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
					valueMap.put("taskType", "质量校验任务");
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
