package cn.com.yeexun.dispatch.quzrtz;

import java.util.Date;
import java.util.Map;

import org.quartz.Job;

public interface IQuartzService {

	/**
	 * addJob(方法描述：添加一个定时任务) <br />
	 * (方法适用条件描述： – 可选)
	 * 
	 * @param jobName
	 *            作业名称
	 * @param jobGroupName
	 *            作业组名称
	 * @param triggerName
	 *            触发器名称
	 * @param triggerGroupName
	 *            触发器组名称
	 * @param cls
	 *            定时任务的class
	 * @param cron
	 *            时间表达式 void
	 * @exception @since
	 *                1.0.0
	 */
	void addJob(String jobName, String jobGroupName
			, String triggerName, String triggerGroupName
			, Class<? extends Job> cls, String cron, Date startAt, Date endAt
			, Map<String, Object> jobDataMap);

	/**
	 * 
	 * @param oldjobName
	 *            原job name
	 * @param oldjobGroup
	 *            原job group
	 * @param oldtriggerName
	 *            原 trigger name
	 * @param oldtriggerGroup
	 *            原 trigger group
	 * @param jobName
	 * @param jobGroup
	 * @param triggerName
	 * @param triggerGroup
	 * @param cron
	 */
	boolean modifyJobTime(String oldjobName, String oldjobGroup, String oldtriggerName, String oldtriggerGroup,
			String jobName, String jobGroup, String triggerName, String triggerGroup, String cron, Date startTime, Date endTime,
			String taskType, Map<String, Object> jobDataMap);

	/**
	 * 修改触发器调度时间
	 * 
	 * @param triggerName
	 *            触发器名称
	 * @param triggerGroupName
	 *            触发器组名称
	 * @param cron
	 *            cron表达式
	 */
	void modifyJobTime(String triggerName, String triggerGroupName, String cron);

	/**
	 * 暂停指定的任务
	 * 
	 * @param jobName
	 *            任务名称
	 * @param jobGroupName
	 *            任务组名称
	 * @return
	 */
	void pauseJob(String jobName, String jobGroupName);

	/**
	 * 恢复指定的任务
	 * 
	 * @param jobName
	 *            任务名称
	 * @param jobGroupName
	 *            任务组名称
	 * @return
	 */
	void resumeJob(String jobName, String jobGroupName);

	/**
	 * 删除指定组任务
	 * 
	 * @param jobName
	 *            作业名称
	 * @param jobGroupName
	 *            作业组名称
	 * @param triggerName
	 *            触发器名称
	 * @param triggerGroupName
	 *            触发器组名称
	 */
	void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName);

	/**
	 * 开始所有定时任务。启动调度器
	 */
	void startSchedule();

	/**
	 * 关闭调度器
	 */
	void shutdownSchedule();
	
	/**
	 * 根据触发周期和出发时间生成 quartz cron 表达式。
	 * @param triggerCycle
	 * @param triggerTime
	 * @return
	 */
	Map<String, String> calcuLateExpression(String triggerCycle, String triggerTime);

	void pauseTrigger(String triggerName, String triggerGroupName);

	void resumeTrigger(String triggerName, String triggerGroupName);

	
}
