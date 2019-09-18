package cn.com.yeexun.qualityTask.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.collectTask.service.ICollectTaskService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.ExcuteResult;
import cn.com.yeexun.dataSource.entity.HiveSource;
import cn.com.yeexun.dataSource.entity.ImpalaSource;
import cn.com.yeexun.dataSource.entity.Meta;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.dispatch.dao.IDispatchTaskDao;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.quzrtz.IQuartzService;
import cn.com.yeexun.meta_data.collector.HbaseMetadataCollector;
import cn.com.yeexun.meta_data.collector.HiveMetadataCollector;
import cn.com.yeexun.meta_data.collector.JdbcHelper;
import cn.com.yeexun.meta_data.collector.MetadataCollector;
import cn.com.yeexun.meta_data.entity.HbaseMetadataInfo;
import cn.com.yeexun.meta_data.entity.HiveMetadataInfo;
import cn.com.yeexun.qualityTask.dao.IDesignTableInfoDao;
import cn.com.yeexun.qualityTask.dao.IDesignTaskTableDao;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.DesignTaskInfo;
import cn.com.yeexun.qualityTask.entity.InitNode;
import cn.com.yeexun.qualityTask.entity.MappingVerifyDetail;
import cn.com.yeexun.qualityTask.entity.QualityTableCountVo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityTask.entity.MappingVerifyDetail.MatchDetail;
import cn.com.yeexun.qualityTask.service.IDesignTableInfoService;
import cn.com.yeexun.qualityTask.service.IDesignTaskInfoService;
import cn.com.yeexun.qualityTask.service.IQualityTaskDetailService;
import cn.com.yeexun.utils.CheckTypeEnum;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;

@Service
public class DesignTableInfoService extends BaseService<DesignTableInfo> implements IDesignTableInfoService{

	@Autowired
	private IDesignTableInfoDao tableInfoDao;
	@Autowired
	private IQualityTaskDetailService qualityTaskDetailService;
	
	@Autowired
	private IDesignTableInfoDao designTableInfoDao;
	
	@Autowired
	private ICollectTaskService collectTaskService;
	
//	@Autowired
//	private IDataSourceDao dataSourceDao;
	
//	@Autowired
//	private IDataSourceService dataSourceService;
	
	@Autowired
	private IDispatchTaskDao dispatchTaskDao;
	
	@Autowired
	private IQuartzService quartzService;
	
	@Autowired
	private IDesignTaskTableDao designTaskTableDao;
	
	@Autowired
	private IDesignTaskInfoService designTaskInfoService;
	
//	@Autowired
//	private IImpalaSourceService impalaSourceService;
//	
//	@Autowired
//	private IHiveSourceService hiveSourceService;
	
//	@Autowired
//	private IDatasourceClient datasourceClient;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	
	@Override
	public List<DesignTableInfo> getTableDesignList(long sourceInfoId)
			throws Exception {
		//直接查询数据库中存储的design列表信息
		List<DesignTableInfo> tableList = tableInfoDao.getTableDesignList(sourceInfoId);
		return tableList;
	}

	private Map<String, List<String>> checkTableDiff(List<Meta> tables,
			List<DesignTableInfo> tableList) {
		Map<String, List<String>> diffResult = new HashMap<String, List<String>>();
		List<String> newTable = new ArrayList<String>();
		List<String> delateTable = new ArrayList<String>();
		List<String> oldTable = new ArrayList<String>();
		for (Meta table : tables) {
			String tableName = table.getName();
			boolean isExist = false;
			for (DesignTableInfo designTableInfo : tableList) {
				String designTable = designTableInfo.getTableName();
				if(tableName.equals(designTable)){
					isExist = true;
					break;
				}
			}
			if(!isExist){
				newTable.add(tableName);
			}
		}
		
		for (DesignTableInfo designTableInfo : tableList) {
			String designTable = designTableInfo.getTableName();
			boolean isExist = false;
			for (Meta table : tables) {
				String tableName = table.getName();
				if (tableName.equals(designTable)) {
					isExist = true;
					break;
				}
				
			}
			if (!isExist) {
				delateTable.add(designTable);
			}else{
				oldTable.add(designTable);
			}
		}
		diffResult.put("newTable", newTable);
		diffResult.put("delateTable", delateTable);
		diffResult.put("oldTable", oldTable);
		return diffResult;
	}

	@Override
	public List<DesignTableInfo> refreshTableDesignList(long sourceId)
			throws Exception {
//		DataSource source = dataSourceDao.selectByPrimaryKey(sourceId);
		
		DataSource source = datasourceService2.getDatasourceById(sourceId);
		
		//先去查询一下表是否有变动，有删除则标记为已删除，有新增则增加DesignTableInfo
		List<Meta> tables = new ArrayList<>();
		if (collectTaskService.isRDBMS(source)) {
			MetadataCollector collector = collectTaskService.getMetadataCollector(source.getDatabaseType());
			tables = collector.getTables(source);
		} else if (Constant.IMPALA.equals(source.getDatabaseType()) 
				|| Constant.HIVE.equals(source.getDatabaseType())) {
			HiveMetadataCollector hiveMetadataCollector = new HiveMetadataCollector();
			Connection conn = JdbcHelper.getConnection(source.getDriver(), source.getUrl()
					, source.getUserName(), source.getPassword());
			Connection conn1 = null;
			if (Constant.HIVE.equals(source.getDatabaseType())) {
//				HiveSource hiveSource = hiveSourceService.getByDataSourceId(source.getId());
				HiveSource hiveSource = datasourceService2.getHiveSource(source.getId());
				conn1 = JdbcHelper.getConnection(hiveSource.getDriver()
						, hiveSource.getUrl(), hiveSource.getHiveMetaDataUserName()
						, hiveSource.getHiveMetaDataPassword());
			} else {
//				ImpalaSource impalaSource = impalaSourceService.getByDataSourceId(source.getId());
				ImpalaSource impalaSource = datasourceService2.getImpalaSource(source.getId());
				conn1 = JdbcHelper.getConnection(impalaSource.getDriver()
						, impalaSource.getUrl(), impalaSource.getUserName(), impalaSource.getPassword());
			}
			
			hiveMetadataCollector.setMetaConnection(conn1);
			hiveMetadataCollector.setConnection(conn);
			List<HiveMetadataInfo> hiveMetaTables = hiveMetadataCollector.getTables(source);
			for (int i = 0; i < hiveMetaTables.size(); i++) {
				HiveMetadataInfo hiveMetadataInfo = hiveMetaTables.get(i);
				Meta meta = new Meta(); 
				meta.setName(hiveMetadataInfo.getName());
				meta.setType(hiveMetadataInfo.getTableType());
				meta.setTypename(hiveMetadataInfo.getName());
				meta.setIsParent("true");
				tables.add(meta);
			}
		} else if (Constant.HBASE.equals(source.getDatabaseType())) {
			HbaseMetadataCollector hbaseMetadataCollector = new HbaseMetadataCollector();
			List<HbaseMetadataInfo> hbaseMetaTables = hbaseMetadataCollector.getTables(source);
			for (int i = 0; i < hbaseMetaTables.size(); i++) {
				HbaseMetadataInfo hbaseMetadataInfo = hbaseMetaTables.get(i);
				Meta meta = new Meta(); 
				meta.setName(hbaseMetadataInfo.getName());
				meta.setTypename(hbaseMetadataInfo.getName());
				meta.setIsParent("true");
				tables.add(meta);
			}
		}
		List<DesignTableInfo> tableList = tableInfoDao.getTableDesignList(sourceId);
		//比较两次的表的相同和不同
		Map<String, List<String>> diff = checkTableDiff(tables, tableList);
		List<String> newTable = diff.get("newTable");
		List<String> deleteTable = diff.get("delateTable");
		//将没有质量规则的表名也要显示到页面上
		List<DesignTableInfo> newTableInfos = new ArrayList<DesignTableInfo>();
		if (newTable != null && newTable.size() > 0) {
			for (String newTabName : newTable) {
				DesignTableInfo newTableInfo = new DesignTableInfo();
				newTableInfo.setStatus(DesignTableInfo.STATUS_UNCOMMIT);
				newTableInfo.setTableName(newTabName);
				newTableInfos.add(newTableInfo);
			}
		}
		
		//将原有的表判断表的规则状态，是否是待配置的状态，如果是则要根据当前的质量规则的启用状态重新进行质量设计
		//将删除的表在list中移除，并且标记为已删除
		List<DesignTableInfo> deleteTableList = new ArrayList<DesignTableInfo>();
		if (deleteTable != null && deleteTable.size() > 0) {
			for (String delTable : deleteTable) {
				for (DesignTableInfo designTableInfo : tableList) {
					if (delTable.equals(designTableInfo.getTableName())) {
						// 删除与之先关的quality_task_detail记录
						qualityTaskDetailService.delete(designTableInfo.getDatasourceId()
								, designTableInfo.getTableName());
						designTableInfo.setDeleteFlag(Constant.IS_DELETE);
						deleteTableList.add(designTableInfo);
						tableInfoDao.updateByPrimaryKey(designTableInfo);
						break;
					}
				}
			}
		}
		tableList.removeAll(deleteTableList);
		tableList.addAll(newTableInfos);
		return tableList;
	}


	@Override
	public QualityTaskDetail createTransByTable(long tableDesignId)
			throws Exception {
		/*DesignTableInfo tableInfo = tableInfoDao.selectByPrimaryKey(tableDesignId);
//		DesignSourceInfo sourceInfo = sourceInfoDao.selectByPrimaryKey(tableInfo.getDesignSourceId());
		DataSource source = dataSourceDao.selectByPrimaryKey(tableInfo.getDatasourceId());
		MetadataCollector collector = collectTaskService.getMetadataCollector(source.getDatabaseType());
		String sql = collector.createSelectSql(source, tableInfo.getTableName());
		QualityTaskDetail detail = new QualityTaskDetail();
		detail.setDeleteFlag(Constant.NOT_DELETE);
//		detail.setDesignTableId(tableDesignId);
		detail.setVerifyType(CheckTypeEnum.DEFAULT_TYPE.getCode());
		detail.setName(tableInfo.getTableName() + "质量校验");
		JSONObject taskDetail = new JSONObject();
		taskDetail.put("sql", sql);
		taskDetail.put("extractType", Constant.extractType_full);//默认为全量
		detail.setVerifyDetail(taskDetail.toJSONString());
		return detail;*/
		DesignTableInfo tableInfo = tableInfoDao.selectByPrimaryKey(tableDesignId);
		QualityTaskDetail initNode = qualityTaskDetailService.findInitNode(tableInfo.getDatasourceId(), tableInfo.getTableName());
		return initNode;
	}
	
	@Override
	public List<QualityTaskDetail> buildTransByTable(DesignTableInfo designTableInfo)
			throws Exception {
		List<QualityTaskDetail> list = new ArrayList<QualityTaskDetail>();
		DesignTableInfo tableInfo = tableInfoDao.selectByPrimaryKey(designTableInfo.getId());
//		DesignSourceInfo sourceInfo = sourceInfoDao.selectByPrimaryKey(tableInfo.getDesignSourceId());
//		DataSource source = dataSourceDao.selectByPrimaryKey(tableInfo.getDatasourceId());
		DataSource source = datasourceService2.getDatasourceById(tableInfo.getDatasourceId());
		
		//初始节点，获取表中的数据
		MetadataCollector collector = collectTaskService.getMetadataCollector(source.getDatabaseType());
		String sql = collector.createSelectSql(source, tableInfo.getTableName());
		QualityTaskDetail detail = new QualityTaskDetail();
		detail.setDeleteFlag(Constant.NOT_DELETE);
//		detail.setDesignTableId(designTableInfo.getId());
		detail.setVerifyType(CheckTypeEnum.DEFAULT_TYPE.getCode());
		InitNode initNode = new InitNode();
		initNode.setSql(sql);
		initNode.setExtractType(Constant.extractType_full);
//		initNode.setFetchSize(20);  // 默认20条
		detail.setVerifyDetail(initNode.toJsonString());
		list.add(detail);
		//对taskDetail进行保存
		qualityTaskDetailService.insertList(list);
		
		// 将状态修改为草稿
		tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
		designTableInfoDao.updateByPrimaryKeySelective(tableInfo);
		
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getResultMap(
			List<QualityTaskDetail> tasks) throws Exception {
		Map<String, List<QualityTaskDetail>> result = new TreeMap<String, List<QualityTaskDetail>>();
		for (QualityTaskDetail qualityTaskDetail : tasks) {
//			String verifyType = CheckTypeEnum.getCheckType(qualityTaskDetail.getVerifyType());
			String verifyType = qualityTaskDetail.getVerifyType();
			if(result.containsKey(verifyType)){
				List<QualityTaskDetail> list = result.get(verifyType);
				list.add(qualityTaskDetail);
			}else{
				List<QualityTaskDetail> newlist = new ArrayList<QualityTaskDetail>();
				newlist.add(qualityTaskDetail);
				result.put(verifyType, newlist);
			}
		}
		List<Map<String, Object>> list = new ArrayList<>();
		for (Map.Entry<String, List<QualityTaskDetail>> entry : result.entrySet()) {
			Map<String, Object> map = new HashMap<>();
			map.put("type", entry.getKey());
			map.put("rules", entry.getValue());
			map.put("name", entry.getValue().get(0).getName());
			list.add(map);
		}
		return list;
		
	}
	

	@Override
	public Map<String, Object> getTablePreviewData(String tableName,
			Long sourceId) throws Exception {
//		DesignSourceInfo designSourceInfo = designSourceInfoService.getById(designSourceId);
//		DataSource dataSource = dataSourceService.getById(sourceId);
		DataSource dataSource = datasourceService2.getDatasourceById(sourceId);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Meta> tableColumns = new ArrayList<Meta>();
		ExcuteResult result = new ExcuteResult();
		if(collectTaskService.isRDBMS(dataSource)){
			String dbType = dataSource.getDatabaseType();
			MetadataCollector m = collectTaskService.getMetadataCollector(dbType);
			tableColumns = m.getColumns(tableName, dataSource);
			result = m.showDataPage(dataSource, tableName, "0", "20");
			map = CodeUtil.getSuccessMap();
			map.put("tableColumns", tableColumns);
			if (result != null) {
				map.put(CodeUtil.RESULT_DATA, result.getDatamap());
			}
			
		} else if (Constant.IMPALA.equals(dataSource.getDatabaseType()) 
				|| Constant.HIVE.equals(dataSource.getDatabaseType())) {
			HiveMetadataCollector hiveMetadataCollector = new HiveMetadataCollector();
			Connection conn = JdbcHelper.getConnection(dataSource.getDriver(), dataSource.getUrl()
					, dataSource.getUserName(), dataSource.getPassword());
			Connection conn1 = null;
			if (Constant.HIVE.equals(dataSource.getDatabaseType())) {
//				HiveSource hiveSource = hiveSourceService.getByDataSourceId(dataSource.getId());
				HiveSource hiveSource = datasourceService2.getHiveSource(dataSource.getId());
				conn1 = JdbcHelper.getConnection(hiveSource.getDriver()
						, hiveSource.getUrl(), hiveSource.getHiveMetaDataUserName()
						, hiveSource.getHiveMetaDataPassword());
			} else {
//				ImpalaSource impalaSource = impalaSourceService.getByDataSourceId(dataSource.getId());
				ImpalaSource impalaSource = datasourceService2.getImpalaSource(dataSource.getId());
				conn1 = JdbcHelper.getConnection(impalaSource.getDriver()
						, impalaSource.getUrl(), impalaSource.getUserName(), impalaSource.getPassword());
			}
			hiveMetadataCollector.setMetaConnection(conn1);
			hiveMetadataCollector.setConnection(conn);
			result = hiveMetadataCollector.showDataPage(dataSource, tableName, "0", "20");
			tableColumns = hiveMetadataCollector.getColumn(tableName, dataSource);
			map = CodeUtil.getSuccessMap();
			map.put("tableColumns", tableColumns);
			map.put(CodeUtil.RESULT_DATA, result.getDatamap());
		} else if (Constant.HBASE.equals(dataSource.getDatabaseType())) {
			HbaseMetadataCollector hbaseMetadataCollector = new HbaseMetadataCollector();
			result = hbaseMetadataCollector.getHbaseData(tableName, dataSource, null, null, null, null, 20L);
			map = CodeUtil.getSuccessMap();
//			tableColumns = JSON.parseArray(result.getColList())
			List<String> colList = result.getColList();
			for (String col : colList) {
				Meta meta = new Meta();
				meta.setName(col);
				tableColumns.add(meta);
			}
			map.put("tableColumns", tableColumns);
			map.put(CodeUtil.RESULT_DATA, result.getDatamap());
		}
		return map;
	}


	@Override
	public void saveQualityTask(Long tableDesignId) {
		
		// 将状态修改为已提交
		DesignTableInfo designTableInfo = designTableInfoDao.selectByPrimaryKey(tableDesignId);
		designTableInfo.setStatus(DesignTableInfo.STATUS_COMMITED);
		designTableInfoDao.updateByPrimaryKeySelective(designTableInfo);
		
	}
	

	@Override
	public void deleteQualityTasks(String tableDesignIds) {
		
		if (tableDesignIds != null && tableDesignIds.trim().length() > 0) {
			String[] tableDesignIdArr = tableDesignIds.split(",", -1);
			for (String idStr : tableDesignIdArr) {
				deleteQualityTask(Long.valueOf(idStr));
			}
		}
		
	}
	
	@Override
	public void deleteQualityTask(Long id) {
		// 先停用并删除与改采集的调度
		DispatchTask byTaskIdAndType = null;
		try {
			byTaskIdAndType = dispatchTaskDao.getByTaskIdAndType(id, "1");
		} catch (Exception e) {
			throw new CommonException("获取与id为 " + id + " 的采集任务关联的调度任务时发生异常： ", e);
		}
		if (byTaskIdAndType != null) {
			// 停用quartz作业
			String triggerName = byTaskIdAndType.getId() + "";
			String triggerGroupName = triggerName + "__G";
			String jobName = byTaskIdAndType.getId() + "";
			String jobGroupName = jobName + "__G";
			quartzService.removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
			// 删调度
			DispatchTask dispatchTask = new DispatchTask();
			dispatchTask.setDeleteFlag("1");
			dispatchTask.setId(byTaskIdAndType.getId());
			dispatchTaskDao.updateByPrimaryKeySelective(dispatchTask);
		}
		// 删除collectTask本身
		designTaskTableDao.deleteByTaskId(id);
		DesignTaskInfo taskInfo = null;
		try {
			taskInfo = designTaskInfoService.getById(id);
		} catch (Exception e) {
			throw new CommonException("根据id查询designTaskInfo异常：id = " + id);
		}
		taskInfo.setDeleteFlag(Constant.NOT_DELETE);
		try {
			designTaskInfoService.update(taskInfo);
		} catch (Exception e) {
			throw new CommonException("根据id修改designTaskInfo异常：id= " + id);
		}
	}


	@Override
	public void removeQualityOnTableByType(Long datasourceId, String tableName, String columnName, String verifyType) {
		qualityTaskDetailService.removeQualityOnTableByType(datasourceId, tableName, columnName, verifyType);
	}

	@Override
	public DesignTableInfo getBySourceAndTable(Long datasourceId, String name) {
		return designTableInfoDao.getBySourceAndTable(datasourceId, name);
	}

	@Override
	public void toDraft(Long tableInfoId) {
		DesignTableInfo tableInfo = designTableInfoDao.selectByPrimaryKey(tableInfoId);
		if (tableInfo != null) {
			tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
			designTableInfoDao.updateByPrimaryKeySelective(tableInfo);
			/*DispatchTask dispatchTask = dispatchTaskService.findByTaskIdAndType(tableInfo.getId(), "1");
			if (dispatchTask != null) {
				dispatchTaskService.pause(dispatchTask.getId());
			}*/
		}
	}

	@Override
	public List<QualityTableCountVo> countQualityTable() {
		return designTableInfoDao.countQualityTable();
	}

	@Override
	public List<DesignTableInfo> findByIds(String ids) {
		return tableInfoDao.findByIds(ids);
	}

	/*@Override
	public void addUserDefineQuality(Long tableInfoId, QualityTaskDetail taskDetail) {
		try {
			if (taskDetail.getId() > 0) {  // 更新
				qualityTaskDetailService.update(taskDetail);
			} else {  // 新增
				taskDetail.setDeleteFlag(Constant.NOT_DELETE);
				qualityTaskDetailService.insert(taskDetail);
			}
		} catch (Exception e) {
			throw new CommonException("编辑自定义规则异常：", e);
		}
	}*/

	@Override
	public void addUserDefineQuality(Long tableInfoId, List<QualityTaskDetail> taskDetails) {
		try {
			/*if (taskDetails == null || taskDetails.size() <= 0) {
				throw new CommonException("参数异常！");
			}*/
			if (tableInfoId == null) {
				DesignTableInfo tableInfo = new DesignTableInfo();
				tableInfo.setDatasourceId(taskDetails.get(0).getDatasourceId());
				tableInfo.setTaskName(taskDetails.get(0).getTableName() + "_QualityTask");
				tableInfo.setTableName(taskDetails.get(0).getTableName());
				tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
				tableInfo.setCreateTime(new Date());
	//			tableInfo.setCreateUser("admin");
				tableInfo.setUpdateTime(new Date());
				tableInfo.setDeleteFlag(Constant.NOT_DELETE);
				tableInfo.setIsDispatch(0);
				designTableInfoDao.insert(tableInfo);
				
//				DataSource source = dataSourceDao.selectByPrimaryKey(taskDetails.get(0).getDatasourceId());
				DataSource source = datasourceService2.getDatasourceById(taskDetails.get(0).getDatasourceId());
				String sql = null;
				if (collectTaskService.isRDBMS(source)) {
					MetadataCollector collector = collectTaskService.getMetadataCollector(
							source.getDatabaseType());
					sql = collector.createSelectSql(source, taskDetails.get(0).getTableName());
				} else if (Constant.IMPALA.equals(source.getDatabaseType())) {
//					ImpalaSource impalaSource = impalaSourceService.getByDataSourceId(source.getId());
					ImpalaSource impalaSource = datasourceService2.getImpalaSource(source.getId());
					HiveMetadataCollector hiveMetadataCollector = new HiveMetadataCollector();
					Connection conn = JdbcHelper.getConnection(source.getDriver(), source.getUrl()
							, source.getUserName(), source.getPassword());
					Connection conn1 = JdbcHelper.getConnection(impalaSource.getDriver()
							, impalaSource.getUrl(), impalaSource.getUserName(), impalaSource.getPassword());
					hiveMetadataCollector.setMetaConnection(conn1);
					hiveMetadataCollector.setConnection(conn);
					sql = hiveMetadataCollector.createSelectSql(source, taskDetails.get(0).getTableName());
				} else if (Constant.HIVE.equals(source.getDatabaseType())) {
//					HiveSource hiveSource = hiveSourceService.getByDataSourceId(source.getId());
					HiveSource hiveSource = datasourceService2.getHiveSource(source.getId());
					HiveMetadataCollector hiveMetadataCollector = new HiveMetadataCollector();
					Connection conn = JdbcHelper.getConnection(source.getDriver(), source.getUrl()
							, source.getUserName(), source.getPassword());
					Connection conn1 = JdbcHelper.getConnection(hiveSource.getDriver()
							, hiveSource.getUrl(), hiveSource.getHiveMetaDataUserName(), hiveSource.getHiveMetaDataPassword());
					hiveMetadataCollector.setMetaConnection(conn1);
					hiveMetadataCollector.setConnection(conn);
					sql = hiveMetadataCollector.createSelectSql(source, taskDetails.get(0).getTableName());
				}
				if (sql == null || sql.trim().length() <= 0) {
					throw new CommonException("生成查询SQL异常：SQL=" + sql);
				}
				InitNode initNode = new InitNode();
				initNode.setSql(sql);
				initNode.setExtractType(Constant.extractType_full);
				QualityTaskDetail initDetail = new QualityTaskDetail();
				initDetail.setName("初始化");
//				initDetail.setDesignTableId(tableInfo.getId());
				initDetail.setVerifyType("1");
				initDetail.setVerifyDetail(JSON.toJSONString(initNode));
				initDetail.setDeleteFlag(Constant.NOT_DELETE);
				initDetail.setEnable(QualityTaskDetail.ENABLE);
				initDetail.setDatasourceId(source.getId());
				initDetail.setTableName(taskDetails.get(0).getTableName());
				qualityTaskDetailService.insert(initDetail);
				for (QualityTaskDetail taskDetail : taskDetails) {
					if (taskDetail.getId() > 0) {  // 更新
	//					qualityTaskDetailService.update(taskDetail);
						taskDetail.setId(0);
					}
					taskDetail.setDeleteFlag(Constant.NOT_DELETE);
					taskDetail.setIsStandard("N");
					if(taskDetail.getVerifyType().equals(QualityTaskDetail.MAPPING_VERIFY)){
						taskDetail.setVerifyDetail(JSON.toJSONString(createMappingVerifyDetail(taskDetail)));
					}
					qualityTaskDetailService.insert(taskDetail);
				}
			} else {
				
				// 先删除该表已经有的quality_task_detail记录
				DesignTableInfo tableInfo = tableInfoDao.selectByPrimaryKey(tableInfoId);
	//				QualityTaskDetail example = new QualityTaskDetail();
	//				example.setDatasourceId(tableInfo.getDatasourceId());
	//				example.setTableName(tableInfo.getTableName());
				String verifyType = null;
				QualityTaskDetail detail = null;
				if (taskDetails == null || taskDetails.size() <= 0) {
					verifyType = "32";
					qualityTaskDetailService.deleteBySourceIdAndTabName(tableInfo.getDatasourceId()
							, tableInfo.getTableName(), verifyType);
					List<QualityTaskDetail> details = qualityTaskDetailService.findByDesignTableId(
							tableInfo.getDatasourceId(), tableInfo.getTableName());
					if (details == null || details.size() <= 0) {
						tableInfo.setStatus(DesignTableInfo.STATUS_UNCOMMIT);
						tableInfoDao.updateByPrimaryKeySelective(tableInfo);
					}
				} else {
					detail = taskDetails.get(0);
					if (QualityTaskDetail.UNIQUE_VERIFY.equals(detail.getVerifyType()) 
							|| QualityTaskDetail.RELATION_VERIFY.equals(detail.getVerifyType())
							|| QualityTaskDetail.MAPPING_VERIFY.equals(detail.getVerifyType())
							|| QualityTaskDetail.NOTEQUAL_VERIFY.equals(detail.getVerifyType())) {
						verifyType = detail.getVerifyType();
						qualityTaskDetailService.deleteBySourceIdAndTabName(tableInfo.getDatasourceId()
								, tableInfo.getTableName(), verifyType);
					}
//					verifyType = detail.getVerifyType();
					
//					DataSource source = dataSourceDao.selectByPrimaryKey(tableInfo.getDatasourceId());
					DataSource source = datasourceService2.getDatasourceById(tableInfo.getDatasourceId());
					String sql = null;
					if (collectTaskService.isRDBMS(source)) {
						MetadataCollector collector = collectTaskService.getMetadataCollector(
								source.getDatabaseType());
						sql = collector.createSelectSql(source, tableInfo.getTableName());
					} else if (Constant.IMPALA.equals(source.getDatabaseType())) {
//						ImpalaSource impalaSource = impalaSourceService.getByDataSourceId(source.getId());
						ImpalaSource impalaSource = datasourceService2.getImpalaSource(source.getId());
						HiveMetadataCollector hiveMetadataCollector = new HiveMetadataCollector();
						Connection conn = JdbcHelper.getConnection(source.getDriver(), source.getUrl()
								, source.getUserName(), source.getPassword());
						Connection conn1 = JdbcHelper.getConnection(impalaSource.getDriver()
								, impalaSource.getUrl(), impalaSource.getUserName(), impalaSource.getPassword());
						hiveMetadataCollector.setMetaConnection(conn1);
						hiveMetadataCollector.setConnection(conn);
						sql = hiveMetadataCollector.createSelectSql(source, tableInfo.getTableName());
					} else if (Constant.HIVE.equals(source.getDatabaseType())) {
//						HiveSource hiveSource = hiveSourceService.getByDataSourceId(source.getId());
						HiveSource hiveSource = datasourceService2.getHiveSource(source.getId());
						HiveMetadataCollector hiveMetadataCollector = new HiveMetadataCollector();
						Connection conn = JdbcHelper.getConnection(source.getDriver(), source.getUrl()
								, source.getUserName(), source.getPassword());
						Connection conn1 = JdbcHelper.getConnection(hiveSource.getDriver()
								, hiveSource.getUrl(), hiveSource.getHiveMetaDataUserName(), hiveSource.getHiveMetaDataPassword());
						hiveMetadataCollector.setMetaConnection(conn1);
						hiveMetadataCollector.setConnection(conn);
						sql = hiveMetadataCollector.createSelectSql(source, taskDetails.get(0).getTableName());
					}
					if (sql == null || sql.trim().length() <= 0) {
						throw new CommonException("生成查询SQL异常：SQL=" + sql);
					}
					InitNode initNode = new InitNode();
					initNode.setSql(sql);
					initNode.setExtractType(Constant.extractType_full);
					QualityTaskDetail initDetail = new QualityTaskDetail();
					initDetail.setName("初始化");
//					initDetail.setDesignTableId(tableInfo.getId());
					initDetail.setVerifyType("1");
					initDetail.setVerifyDetail(JSON.toJSONString(initNode));
					initDetail.setDeleteFlag(Constant.NOT_DELETE);
					initDetail.setEnable(QualityTaskDetail.ENABLE);
					initDetail.setDatasourceId(source.getId());
					initDetail.setTableName(tableInfo.getTableName());
					qualityTaskDetailService.insert(initDetail);
					for (QualityTaskDetail taskDetail : taskDetails) {
						if (taskDetail.getId() > 0) {  // 更新
		//						qualityTaskDetailService.update(taskDetail);
							taskDetail.setId(0);
						}
						taskDetail.setDeleteFlag(Constant.NOT_DELETE);
						taskDetail.setIsStandard("N");
						if(taskDetail.getVerifyType().equals(QualityTaskDetail.MAPPING_VERIFY)){
							taskDetail.setVerifyDetail(JSON.toJSONString(createMappingVerifyDetail(taskDetail)));
						}
						qualityTaskDetailService.insert(taskDetail);
					}
					if (!DesignTableInfo.STATUS_DRAFT.equals(tableInfo.getStatus())) {
						tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
						tableInfoDao.updateByPrimaryKeySelective(tableInfo);
					}
				}
			}
		} catch (Exception e) {
			throw new CommonException("编辑自定义规则异常："+e.getMessage(), e);
		}
		
		
	}
	
	private MappingVerifyDetail createMappingVerifyDetail(QualityTaskDetail taskDetail) throws Exception{
		MappingVerifyDetail mapDetail = JSON.parseObject(taskDetail.getVerifyDetail(), MappingVerifyDetail.class);
		String delimiter = mapDetail.getDelimiter();
		String[] matchStr = mapDetail.getMatchChar().split(delimiter, -1);
		String[] targetVal = mapDetail.getTargetValue().split(delimiter, -1);
		if(matchStr.length != targetVal.length){
			throw new Exception("检查值和映射字段值分割后的字段个数不匹配！");
		}
		List<MatchDetail> matchDetails = new ArrayList<MatchDetail>();
		for(int i= 0; i<matchStr.length; i++){
			MatchDetail tmp = new MatchDetail();
			tmp.setMatchChar(matchStr[i]);
			tmp.setMatchType(mapDetail.getMatchType());
			if(StringUtils.isNotBlank(targetVal[i])){
				tmp.setTargetValue(targetVal[i]);
			}
			matchDetails.add(tmp);
		}
		mapDetail.setMappingRule(matchDetails);
		return mapDetail;
	}

	@Override
	public List<Map<String, Object>> listReportTables() {
		List<DesignTableInfo> tableInfos = tableInfoDao.listReportTables();
		String sourceIds = tableInfos.stream().mapToLong(DesignTableInfo :: getDatasourceId).distinct()
				.mapToObj(sourceId -> String.valueOf(sourceId)).collect(Collectors.joining(","));
		List<MetadataDatasource> sources = datasourceService2.getSourceByIds(sourceIds);
		for (DesignTableInfo tableInfo : tableInfos) {
			for (MetadataDatasource source : sources) {
				if (tableInfo.getDatasourceId() == source.getId()) {
					tableInfo.setSourceName(source.getDatasourceName());
				}
			}
		}
		Map<String, List<DesignTableInfo>> sourceTables = new HashMap<>();
		for (DesignTableInfo tableInfo : tableInfos) {
			if (sourceTables.containsKey(tableInfo.getSourceName())) {
				sourceTables.get(tableInfo.getSourceName()).add(tableInfo);
			} else {
				List<DesignTableInfo> tables = new ArrayList<>();
				tables.add(tableInfo);
				sourceTables.put(tableInfo.getSourceName(), tables);
			}
		}
		List<Map<String, Object>> treeList = new ArrayList<>();
		for (Map.Entry<String, List<DesignTableInfo>> sourceTable : sourceTables.entrySet()) {
			Map<String, Object> tree = new HashMap<>();
			tree.put("id", sourceTable.getKey());
			tree.put("name", sourceTable.getKey());
			tree.put("children", sourceTable.getValue());
			treeList.add(tree);
		}
		return treeList;
	}

	@Override
	public void removeQualityById(String qualityTaskDetailIds) {
		qualityTaskDetailService.removeQualityById(qualityTaskDetailIds);
	}

	@Override
	public List<QualityTaskDetail> getQualityTaskIdBySourceId(Long sourceId) {
		return designTableInfoDao.getQualityTaskIdBySourceId(sourceId);
	}


	
}
