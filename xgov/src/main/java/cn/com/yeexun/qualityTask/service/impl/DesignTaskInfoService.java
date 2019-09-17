package cn.com.yeexun.qualityTask.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.quzrtz.IQuartzService;
import cn.com.yeexun.dispatch.service.IDispatchTaskService;
import cn.com.yeexun.qualityTask.dao.IDesignTaskInfoDao;
import cn.com.yeexun.qualityTask.dao.IDesignTaskTableDao;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.DesignTaskInfo;
import cn.com.yeexun.qualityTask.entity.DesignTaskTable;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityTask.service.IDesignTableInfoService;
import cn.com.yeexun.qualityTask.service.IDesignTaskInfoService;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import tk.mybatis.mapper.entity.Example;

@Service
public class DesignTaskInfoService extends BaseService<DesignTaskInfo> implements IDesignTaskInfoService{
	
	@Autowired
	private IDesignTaskInfoDao designTaskInfoDao;
	
	@Autowired
	private IDesignTaskTableDao designTaskTableDao;
	
	@Autowired
	private IDispatchTaskService dispatchTaskService;
	
	@Autowired
	private IQuartzService quartzService;
	
	@Autowired
	private IDesignTableInfoService designTableInfoService;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	/**
	 * 新增或编辑质量任务
	 * @param tableIds
	 * @param taskInfo
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void saveDesignTasks(String tableIds, DesignTaskInfo taskInfo) throws Exception {
		List<DesignTableInfo> tableInfos = designTableInfoService.findByIds(tableIds);
		if (tableIds != null && tableInfos.size() > 1) {
			throw new CommonException("一个质量任务中只能配置同一个数据源的表！");
		}
		Example example = new Example(DesignTaskInfo.class);
		if (taskInfo.getId() > 0) {  // 编辑
			example.createCriteria().andEqualTo("taskName", taskInfo.getTaskName())
					.andEqualTo("deleteFlag", String.valueOf(Constant.NOT_DELETE))
					.andNotEqualTo("id", taskInfo.getId());
		} else {
			example.createCriteria().andEqualTo("taskName", taskInfo.getTaskName())
				.andEqualTo("deleteFlag", String.valueOf(Constant.NOT_DELETE));
		}
		List<DesignTaskInfo> designTaskInfos = designTaskInfoDao.selectByExample(example);
		if (designTaskInfos != null && designTaskInfos.size() > 0) {
			throw new CommonException("任务名称重复！");
		}
		if (taskInfo.getId() == 0) {
			// 新增
			taskInfo.setDeleteFlag(Constant.NOT_DELETE);
			taskInfo.setCreateTime(new Date());
			taskInfo.setUpdateTime(new Date());
			designTaskInfoDao.insertSelective(taskInfo);
		} else {
			// 编辑
			taskInfo.setUpdateTime(new Date());
			designTaskInfoDao.updateByPrimaryKeySelective(taskInfo);
		}
		// 将原来的关系表数据全部删除，重新添加
		designTaskTableDao.deleteByTaskId(taskInfo.getId());
		String[] designTableIds = tableIds.split(",", -1);
		List<DesignTaskTable> taskTables = new ArrayList<>();
 		for (String designTableId : designTableIds) {
			DesignTaskTable designTaskTable = new DesignTaskTable();
			designTaskTable.setTableId(Long.valueOf(designTableId));
			designTaskTable.setTaskId(taskInfo.getId());
			designTaskTable.setCreateTime(new Date());
			designTaskTable.setUpdateTime(new Date());
			designTaskTable.setIsDel(Integer.valueOf(Constant.NOT_DELETE));
			taskTables.add(designTaskTable);
		}
 		if (taskTables.size() > 0) {
			designTaskTableDao.insertList(taskTables);
		}
 		// 添加dispatch和quartz
 		DispatchTask query = new DispatchTask();
		query.setTaskId(taskInfo.getId());
		query.setTaskType(DispatchTask.QUALITY_TASK);
		query.setDeleteFlag(Constant.NOT_DELETE);
		List<DispatchTask> oldDispatchTask = null;
		try {
			oldDispatchTask = dispatchTaskService.query(query);
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		}
		DispatchTask dispatchTask = new DispatchTask();
		if (oldDispatchTask != null && oldDispatchTask.size() > 0) {
			dispatchTask.setId(oldDispatchTask.get(0).getId());
		}
		dispatchTask.setTaskId(taskInfo.getId());
		dispatchTask.setTaskName(taskInfo.getTaskName());
		dispatchTask.setTaskType(DispatchTask.QUALITY_TASK);
		dispatchTask.setCycleType(taskInfo.getCycleType());
		dispatchTask.setJobCron(taskInfo.getJobCron());
		dispatchTask.setTriggerTime(taskInfo.getTriggerTime());
		Map<String, String> cronExpression 
				= quartzService.calcuLateExpression(taskInfo.getJobCron(), taskInfo.getTriggerTime());
		dispatchTask.setCronExpression(cronExpression.get("cronExpression"));
		dispatchTask.setCronDescription(cronExpression.get("cronDiscription"));
		dispatchTask.setStatus(DispatchTask.STATUS_RUNNING);
		dispatchTask.setAlarmId(taskInfo.getAlarmId());
		dispatchTask.setStartTime(taskInfo.getStartTime());
		dispatchTask.setEndTime(taskInfo.getEndTime());
		dispatchTask.setCreateTime(new Date());
		dispatchTask.setLastModifyTime(new Date());
		dispatchTask.setDeleteFlag(Constant.NOT_DELETE);
		dispatchTaskService.saveOrUpdate(dispatchTask);
	}

	@Override
	public List<DesignTaskInfo> getQualityTasksPage(Page<DesignTaskInfo> page, String taskName) 
			throws Exception {
		List<DesignTaskInfo> qualityTasks = designTaskInfoDao.getQualityTasksPage(page, taskName);
		String sourceIds = qualityTasks.stream().mapToLong(DesignTaskInfo :: getDatasourceId).distinct()
				.mapToObj(sourceId -> String.valueOf(sourceId)).collect(Collectors.joining(","));
		List<MetadataDatasource> datasources = datasourceService2.getSourceByIds(sourceIds);
		for (DesignTaskInfo taskInfo : qualityTasks) {
			for (MetadataDatasource source : datasources) {
				if (taskInfo.getDatasourceId() == source.getId()) {
					taskInfo.setDatasourceName(source.getDatasourceName());
				}
			}
		}
		return qualityTasks;
	}

	@Override
	public Long getDatasourceIdByTaskId(Long taskId) {
		return designTaskInfoDao.getDatasourceIdByTaskId(taskId);
	}

	@Override
	public DesignTaskInfo findById(Long taskInfoId) {
		DesignTaskInfo taskInfo = designTaskInfoDao.selectByPrimaryKey(taskInfoId);
		// 还要获取到这个任务下所有的design_table_info表的主键id
		List<DesignTaskTable> taskTables = designTaskTableDao.findByTaskId(taskInfoId);
		StringBuilder tableInfoIdsBuilder = new StringBuilder();
		for (DesignTaskTable taskTable : taskTables) {
			tableInfoIdsBuilder.append(taskTable.getTableId()).append(",");
		}
		taskInfo.setTableInfoIds(tableInfoIdsBuilder.substring(0, tableInfoIdsBuilder.length() - 1));
		return taskInfo;
	}

	@Override
	public List<DesignTableInfo> getTaskTables(Long taskId) {
		return designTaskInfoDao.getTaskTables(taskId);
	}

	@Override
	public List<QualityTaskDetail> getTaskTablesDetail(Long taskId, String tableName) {
		return designTaskInfoDao.getTaskTablesDetail(taskId, tableName);
	}


}
