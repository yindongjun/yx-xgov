package cn.com.yeexun.dispatch.quzrtz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.resource.spi.CommException;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.quzrtz.job.MetadataCollectQuartzJob;
import cn.com.yeexun.dispatch.quzrtz.job.QualityCheckQuartzJob;
import cn.com.yeexun.dispatch.service.IDispatchTaskService;
import cn.com.yeexun.utils.CommonException;

@Service
public class QuartzService implements IQuartzService {

	private static Logger logger = LoggerFactory.getLogger(QuartzService.class);
	
	private static final DateFormat df = new SimpleDateFormat("yyyy-M-d H:m:s");

	private static String asterisk = "*";

	private static String qMark = "?";

	@Autowired
	private Scheduler quartzScheduler;
	
	@Autowired
	private SimpleSchedulerListener simpleSchedulerListener;
	
	@Autowired
	private IDispatchTaskService dispatchTaskService;
	
	@PostConstruct
	public void init() {
		
		// 初始化时加载所有调度任务到quartz
		// 加载数据库中的所有有效任务，实现服务器重启后自动重启调度
		List<DispatchTask> runningTasks = dispatchTaskService.getDispatchByStatus(DispatchTask.STATUS_RUNNING);
		List<DispatchTask> pauseTasks = dispatchTaskService.getDispatchByStatus(DispatchTask.STATUS_PAUSED);
		runningTasks.addAll(pauseTasks);
		for (DispatchTask task : runningTasks) {
			// 当调度类型为只执行一次时，有可能在重新加载时，调度已经过期。所以需要处理。
			if (DispatchTask.JOBCRON_ONLYONCE.equals(task.getJobCron()) 
					&& System.currentTimeMillis() > Long.parseLong(task.getTriggerTime())) {
				logger.warn("调度已经过期或即将过期，忽略加载，id：" + task.getId() + "，name：" + task.getTaskName());
				task.setStatus(DispatchTask.STATUS_FINALIZED);
				try {
					dispatchTaskService.update(task);
				} catch (Exception e) {
					throw new CommonException(e.getMessage(), e);
				}
			} else {
				// 过期调度忽略加载，还要修改数据库中的状态为已完成。
				try {
					dispatchTaskService.addJobToQuartz(task);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
		
		try {
			// 绑定监听器
			quartzScheduler.getListenerManager().addSchedulerListener(simpleSchedulerListener);
		} catch (SchedulerException e) {
			throw new CommonException("添加SimpleSchedulerListener发生异常：", e);
		}
	}

	@Override
	public void addJob(String jobName, String jobGroupName
			, String triggerName, String triggerGroupName
			, Class<? extends Job> cls, String cron, Date startAt, Date endAt
			, Map<String, Object> jobDataMap) {
		try {
			logger.info("添加调度,jobGroupName.jobName:" + jobGroupName + "." + jobName 
					+ ",triggerGroupName.triggerName:" + triggerGroupName + "." + triggerName);
			Scheduler sched = quartzScheduler;
			JobDetail job = JobBuilder.newJob(cls).withIdentity(jobName, jobGroupName).build();
			// 该trigger暂停之后重启，不会补偿暂停期间错过的任务。
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
					.withSchedule(CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing())
					.startAt(startAt).endAt(endAt).build();
			
            Date time0 = new Date();
            Date time1 = trigger.getFireTimeAfter(time0);
            if (time1 == null) {
				throw new CommonException("添加调度失败，生效时间内调度次数为0。请检查调度配置是否正确！");
			}
			
			if (jobDataMap != null && jobDataMap.size() > 0) {
				job.getJobDataMap().putAll(jobDataMap);
				trigger.getJobDataMap().putAll(jobDataMap);
			}
			boolean jobExist = sched.checkExists(job.getKey());
			boolean triggerExist = sched.checkExists(trigger.getKey());
			logger.info("jobExist:" + jobExist + ", triggerExist:" + triggerExist);
			sched.scheduleJob(job, trigger);
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch(SchedulerException e) {
			throw new CommonException("添加调度作业失败！检查调度时间是否正确。", e);
			
		} catch (Exception e) {
			logger.info("添加调度作业失败！");
			// 打印出所有已经加载成功的job信息，debug用。
			try {
				for (String groupName : quartzScheduler.getJobGroupNames()) {
					for (JobKey jobKey : quartzScheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					    String jobName1 = jobKey.getName();
					    String jobGroup = jobKey.getGroup();
					    logger.warn("************************************************************");
					    logger.warn("jobGroup.job:" + jobGroup + "." + jobName1);
					    List<Trigger> triggers = (List<Trigger>) quartzScheduler.getTriggersOfJob(jobKey);
					    for (Trigger trigger : triggers) {
					    	TriggerKey key = trigger.getKey();
					    	String group = key.getGroup();
					    	String name = key.getName();
					    	logger.warn("triggerGroup.trigger:" + group + "." + name);
					    	JobDataMap jobDataMap2 = trigger.getJobDataMap();
					    	logger.warn("jobdatamap:" + JSON.toJSONString(jobDataMap2));
						}
					}
				}
            } catch (SchedulerException e1) {
            	e1.printStackTrace();
			}
			throw e;
		}
	}

	/**
	 * 修改定时器任务信息
	 */
	@Override
	public boolean modifyJobTime(String oldjobName, String oldjobGroup, 
			String oldtriggerName, String oldtriggerGroup
			,String jobName, String jobGroup, String triggerName
			, String triggerGroup, String cron, Date startTime, Date endTime, String taskType
			,Map<String, Object> jobDataMap) {
		try {
			Scheduler sched = quartzScheduler;
			/*CronTrigger trigger = (CronTrigger) sched
					.getTrigger(TriggerKey.triggerKey(oldtriggerName, oldtriggerGroup));
			if (trigger == null) {
				return false;
			}*/
			logger.info("修改调度,jobGroupName.jobName:" + oldjobGroup + "." + oldjobName 
					+ ",triggerGroupName.triggerName:" + oldtriggerGroup + "." + oldtriggerName);
			JobKey jobKey = JobKey.jobKey(oldjobName, oldjobGroup);
			TriggerKey triggerKey = TriggerKey.triggerKey(oldtriggerName, oldtriggerGroup);

			JobDetail job = sched.getJobDetail(jobKey);
			if(null==job){
				if("0".equals(taskType)){
					addJob(jobName, jobGroup, triggerName, triggerGroup, MetadataCollectQuartzJob.class, cron, startTime, endTime, jobDataMap);
					return true;
				}else{
					addJob(jobName, jobGroup, triggerName, triggerGroup, QualityCheckQuartzJob.class, cron, startTime, endTime, jobDataMap);
					return true;
				}
			}
			Class<? extends Job> jobClass = job.getJobClass();
			jobDataMap = job.getJobDataMap();
			sched.pauseTrigger(triggerKey);
			sched.unscheduleJob(triggerKey);
			sched.deleteJob(jobKey);

			addJob(jobName, jobGroup, triggerName, triggerGroup, jobClass, cron, startTime, endTime, jobDataMap);

			return true;
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		}

	}

	@Override
	public void modifyJobTime(String triggerName, String triggerGroupName, String time) {
		try {
			Scheduler sched = quartzScheduler;
			CronTrigger trigger = (CronTrigger) sched.getTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				CronTrigger ct = (CronTrigger) trigger;
				ct.getTriggerBuilder().withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
				sched.resumeTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = quartzScheduler;
			sched.pauseTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
			sched.unscheduleJob(TriggerKey.triggerKey(triggerName, triggerGroupName));
			sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void startSchedule() {
		try {
			Scheduler sched = quartzScheduler;
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void shutdownSchedule() {
		try {
			Scheduler sched = quartzScheduler;
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void pauseJob(String jobName, String jobGroupName) {
		try {
			quartzScheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void resumeJob(String jobName, String jobGroupName) {
		try {
			quartzScheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void pauseTrigger(String triggerName, String triggerGroupName) {
		try {
			quartzScheduler.pauseTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
		} catch (SchedulerException e) {
			throw new CommonException(
					"暂停trigger异常---triggerName:" + triggerName + " triggerGroupName:" + triggerGroupName, e);
		}
	}
	
	@Override
	public void resumeTrigger(String triggerName, String triggerGroupName) {
		try {
			quartzScheduler.resumeTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
		} catch (SchedulerException e) {
			throw new CommonException(
					"重启trigger异常---triggerName:" + triggerName + " triggerGroupName:" + triggerGroupName, e);
		}
	}

	@Override
	public Map<String, String> calcuLateExpression(String triggerCycle, String triggerTime) {
		StringBuilder experssion = new StringBuilder();
		StringBuilder experssion2 = new StringBuilder();
		Map<String, String> result = new HashMap<>();
		if (StringUtils.isEmpty(triggerTime)) {
			logger.info("参数缺失！");
		} else {
			if (DispatchTask.JOBCRON_ONLYONCE.equals(triggerCycle)) {
				// 先将前端传入的毫秒数转成':'分隔的格式字符串
				Long triggerTimeMills = Long.valueOf(triggerTime);
				String timeStr = df.format(new Date(triggerTimeMills));
				timeStr = timeStr.replace('-', ':');
				timeStr = timeStr.replace(' ', ':');
				triggerTime = timeStr;
			}
			String[] dataStringArr = triggerTime.split(":");
			if (DispatchTask.JOBCRON_ONLYONCE.equals(triggerCycle)) {
				// 在指定时间执行一次
				for (int i = dataStringArr.length - 1; i >= 0; i--) {
					if (i == 0) {
						experssion.append(qMark + " ");
					}
					experssion.append(dataStringArr[i]).append(" ");
				}
				experssion.deleteCharAt(experssion.length() - 1);
				experssion2.append("在  " + triggerTime + " 执行一次");
			} else if (DispatchTask.JOBCRON_HOUR.equals(triggerCycle)) {
				// 每小时
				experssion.append("0 ");
				if (triggerTime.equals("-all-")) {
					experssion.append(asterisk + " ");
				} else {
					experssion.append(triggerTime + " ");
				}
				experssion.append(asterisk + " ");
				experssion.append(asterisk + " ");
				experssion.append(asterisk + " ");
				experssion.append(qMark + " ");
				experssion.append(asterisk);
				experssion2.append("每小时的" + triggerTime + "执行");
			} else if (dataStringArr.length >= 3) {
				int length = dataStringArr.length;
				experssion.append(dataStringArr[length - 1] + " ");
				experssion.append(dataStringArr[length - 2] + " ");
				experssion.append(dataStringArr[length - 3] + " ");
				if (DispatchTask.JOBCRON_DAY.equals(triggerCycle)) {
					// 每天
					experssion.append(asterisk + " ");
					experssion.append(asterisk + " ");
					experssion.append(qMark);

					experssion2.append("每天的" + triggerTime + "执行");
				} else if (dataStringArr.length == 4) {
					if (DispatchTask.JOBCRON_WEEK.equals(triggerCycle)) {
						// 每周
						String str = new String();
						experssion.append(qMark + " ");
						experssion.append(asterisk + " ");
						experssion.append(dataStringArr[0]);

						switch (dataStringArr[0]) {
						case "SUN":
							str = "每周日的";
							break;
						case "MON":
							str = "每周一的";
							break;
						case "TUE":
							str = "每周二的";
							break;
						case "WED":
							str = "每周三的";
							break;
						case "THU":
							str = "每周四的";
							break;
						case "FRI":
							str = "每周五的";
							break;
						case "SAT":
							str = "每周六的";
							break;
						}
						experssion2.append(str + triggerTime.substring(triggerTime.indexOf(":") + 1) + "执行");
					} else if (DispatchTask.JOBCRON_MONTH.equals(triggerCycle)) {
						// 每月
						experssion.append(dataStringArr[0] + " ");
						experssion.append(asterisk + " ");
						experssion.append(qMark);

						experssion2.append("每月的" + dataStringArr[0] + "号"
								+ triggerTime.substring(triggerTime.indexOf(":") + 1) + "执行");
					}
				}
			}
		}
		result.put("cronExpression", experssion.toString());
		result.put("cronDiscription", experssion2.toString());
		return result;
	}

}
