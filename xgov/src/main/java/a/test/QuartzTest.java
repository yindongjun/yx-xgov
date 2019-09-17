package a.test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.alibaba.fastjson.JSON;


public class QuartzTest {
	
	private static Scheduler scheduler;
	
	static {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			scheduler = schedulerFactory.getScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SchedulerException, InterruptedException {
		
		Map<String, Object> jobDataMap = new HashMap<>();
		jobDataMap.put("say", "hello, quartz!");
		
		String jobName = "helloJob";
		String jobGroupName = "helloJob__G";
		String triggerName = "helloTrigger";
		String triggerGroupName = "helloTrigger__G";
		// 每秒执行一次，5秒后开始，20秒后结束。
		addJob(jobName, jobGroupName, triggerName, triggerGroupName
				, HelloJob.class, "*/10 * * * * ?", new Date(new Date().getTime() + 3000)
				, new Date(new Date().getTime() + 50000), jobDataMap);
		System.out.println("++++++++++++++++" + new Date());
		System.out.println("======================job已启动");
//		Thread.sleep(18000);
//		pauseJob(jobName, jobGroupName);
//		Thread.sleep(20000);
//		resumeJob(jobName, jobGroupName);
		try {
			List<String> jobGroupNames = scheduler.getJobGroupNames();
			for (String groupName : scheduler.getJobGroupNames()) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
				    String jobName1 = jobKey.getName();
				    String jobGroup = jobKey.getGroup();
				    System.out.println("************************************************************");
				    System.out.println("jobGroup.job:" + jobGroup + "." + jobName1);
				    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
				    for (Trigger trigger : triggers) {
				    	TriggerKey key = trigger.getKey();
				    	String group = key.getGroup();
				    	String name = key.getName();
				    	System.out.println("triggerGroup.trigger:" + group + "." + name);
				    	JobDataMap jobDataMap2 = trigger.getJobDataMap();
				    	System.out.println("jobdatamap:" + JSON.toJSONString(jobDataMap2));
					}
				}
			}
        } catch (SchedulerException e1) {
        	e1.printStackTrace();
		}
		
	}
	
	public static void main1(String[] args) throws SchedulerException, InterruptedException {
		
		Map<String, Object> jobDataMap = new HashMap<>();
		jobDataMap.put("say", "hello, quartz!");
		
		String jobName = "helloJob";
		String jobGroupName = "helloJob__G";
		String triggerName = "helloTrigger";
		String triggerGroupName = "helloTrigger__G";
		// 每秒执行一次，5秒后开始，20秒后结束。
		addJob(jobName, jobGroupName, triggerName, triggerGroupName
				, HelloJob.class, "*/10 * * * * ?", new Date(new Date().getTime() + 5000)
				, new Date(new Date().getTime() + 50000), jobDataMap);
		System.out.println("++++++++++++++++" + new Date());
		System.out.println("======================job已启动");
		Thread.sleep(18000);
		pauseTrigger(triggerName, triggerGroupName);
		TriggerState triggerState = scheduler.getTriggerState(TriggerKey.triggerKey(triggerName, triggerGroupName));
		System.out.println(triggerState.name());
		
		Thread.sleep(20000);
		resumeTrigger(triggerName, triggerGroupName);
		
	}
	
	public static void pauseTrigger(String triggerName, String triggerGroupName) {
		try {
			scheduler.pauseTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void resumeTrigger(String triggerName, String triggerGroupName) {
		try {
			scheduler.resumeTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 只是暂停运行本次的job，当调用resumeJob方法后，会继续执行这个job剩下的代码。
	 * 并不会是trigger暂停
	 * @param jobName
	 * @param jobGroupName
	 */
	public static void pauseJob(String jobName, String jobGroupName) {
		try {
			scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	public static void resumeJob(String jobName, String jobGroupName) {
		try {
			scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void addJob(String jobName, String jobGroupName
			, String triggerName, String triggerGroupName
			, Class cls, String cron, Date startAt, Date endAt
			, Map<String, Object> jobDataMap) {
		try {
			Scheduler sched = scheduler;
			JobDetail job = JobBuilder.newJob(cls).withIdentity(jobName, jobGroupName).build();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
					.withSchedule(CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing())
					.startAt(startAt).endAt(endAt).build();
			if (jobDataMap != null && jobDataMap.size() > 0) {
				job.getJobDataMap().putAll(jobDataMap);
				trigger.getJobDataMap().putAll(jobDataMap);
			}
			sched.scheduleJob(job, trigger);
			// 添加监听器
			sched.getListenerManager().addSchedulerListener(new SchedulerListenerTest());
			
			
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}



// 1 37 16 21 11 ? 2018 只在指定时间执行一次（2018.11.21 16:37:01）
//new Date(new Date().getTime() + 10000)
//addJob("helloJob", "helloJob_G", "helloTrigger", "helloTrigger__G"
//		, HelloJob.class, "* * * * * ? *", new Date(new Date().getTime() + 5000)
//		, null, jobDataMap);
