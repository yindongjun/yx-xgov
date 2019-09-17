package cn.com.yeexun.qualityTask.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.common.ssm.engine.utils.DateUtil;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataElement.service.IDataElementService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.dispatch.entity.DispatchHistory;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.service.IDispatchHistoryService;
import cn.com.yeexun.dispatch.service.IDispatchTaskService;
import cn.com.yeexun.qualityTask.dao.IQualityTaskDetailDao;
import cn.com.yeexun.qualityTask.datacheck.DataCheck;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityTask.entity.QualityTaskLog;
import cn.com.yeexun.qualityTask.service.IDesignTableInfoService;
import cn.com.yeexun.qualityTask.service.IQualityTaskDetailService;
import cn.com.yeexun.qualityTask.service.IQualityTaskLogService;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.FileUtil;
import cn.com.yeexun.utils.PropertyPlaceholder;
import tk.mybatis.mapper.entity.Example;

@Service
public class QualityTaskDetailService 
		extends BaseService<QualityTaskDetail> 
		implements IQualityTaskDetailService{
	
	private static final Logger logger = LoggerFactory.getLogger(QualityTaskDetailService.class);
	
	private static final int DEBUG = 1;
	
	@Autowired
	private IQualityTaskDetailDao qualityTaskDetailDao;
	
	@Autowired
	private IDesignTableInfoService designTableInfoService;
	
	@Autowired
	private IDispatchTaskService dispatchTaskService;
	
	@Autowired
	private IDataElementService dataElementService;
	
	@Autowired
	private IDispatchHistoryService dispatchHisService;
	
	@Autowired
	private IQualityTaskLogService qualityTaskLogService;
	
//	@Autowired
//	private IDataSourceDao datasourceDao;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	

	@Override
	public List<QualityTaskDetail> findQualituTask(QualityTaskDetail qualityTaskDetail) {
		List<QualityTaskDetail> qualityTaskDetails = qualityTaskDetailDao.select(qualityTaskDetail);
		return qualityTaskDetails;
	}

	@Override
	public List<QualityTaskDetail> findByDesignTableId(Long designTableId, String tableName) {
		List<QualityTaskDetail> qualityTaskDetails 
				= qualityTaskDetailDao.findByDesignTableId(designTableId, tableName);
		return qualityTaskDetails;
	}
	
	@Override
	public QualityTaskDetail findInitNode(Long datasourceId, String tableName) {
		
		Example example = new Example(QualityTaskDetail.class);
		example.createCriteria().andEqualTo("datasourceId", datasourceId).andEqualTo("tableName", tableName)
				.andEqualTo("deleteFlag", "0").andEqualTo("verifyType", QualityTaskDetail.DEFAULT_TYPE);
		List<QualityTaskDetail> qualityTaskDetails = qualityTaskDetailDao.selectByExample(example);
		return qualityTaskDetails.get(0);
	}

	@Override
	public List<QualityTaskLog> execute(Long designTableInfoId, Integer isDebug) {
		List<QualityTaskLog> logs = null;
		DataCheck dataCheck = null;
		if (isDebug == null) {
			dataCheck = new DataCheck(designTableInfoId, null, null);
			dataCheck.check();
		} else {
			// 先判断是否绑定了非审核通过状态的数据元
			DesignTableInfo tableInfo;
			try {
				tableInfo = designTableInfoService.getById(designTableInfoId);
			} catch (Exception e) {
				throw new CommonException("根据id查询designtableinfo异常，designtableInfoID=" + designTableInfoId
						, e);
			}
			List<QualityTaskDetail> qualityTasks = qualityTaskDetailDao.findByDesignTableId(
					tableInfo.getDatasourceId(), tableInfo.getTableName());
			Set<Long> set = new HashSet<>();
			for (QualityTaskDetail taskDetail : qualityTasks) {
				if (taskDetail.getElementId() != null) {
					set.add(taskDetail.getElementId());
				}
			}
			for (Long elementId : set) {
				DataElementEntity element;
				try {
					element = dataElementService.getById(elementId);
				} catch (Exception e) {
					throw new CommonException(e.getMessage());
				}
				/*if (!Integer.valueOf(DataElementEntity.STATUS_AUDITED).equals(element.getStatus())) {
					throw new CommonException("该表配置了未通过审核的数据元，请先审核数据元！");
				}*/
			}
			// 创建dispatch_history
			final DispatchHistory dispatchHistory = new DispatchHistory();
//			dispatchHistory.setTaskId(null);
//			dispatchHistory.setTaskType(null);
			dispatchHistory.setStartTime(new Date());
			dispatchHistory.setStatus(DispatchHistory.STATUS_ON);//正在进行中
			try {
				dispatchHisService.save(dispatchHistory);
			} catch (Exception e) {
				throw new CommonException("调试出错！", e);
			}
			dataCheck = new DataCheck(designTableInfoId, dispatchHistory.getId(), null);
			dataCheck.setDebugSize(1);
			try {
				dataCheck.check();
				designTableInfoService.saveQualityTask(tableInfo.getId());
			} catch (Exception e) {
			
			} finally {
				dispatchHistory.setEndTime(new Date());
				dispatchHistory.setSpent(DateUtil.subDate1ToDate2(dispatchHistory.getEndTime(),dispatchHistory.getStartTime()).toString());
				dispatchHistory.setStatus(DispatchHistory.STATUS_SUCCEED);
//				designTableInfoService.saveQualityTask(designTableInfoId);
				logs = qualityTaskLogService.getLogsByTable(dispatchHistory.getId(), tableInfo.getTableName());
			}
		}
		
		
		return logs;
	}
	
	

	@Override
	public void addToTask(DesignTableInfo designTableInfo) throws Exception {
		
		/*long id = designTableInfo.getId();
		DesignTableInfo oldDesignTableInfo = null;
		try {
			oldDesignTableInfo = designTableInfoService.getById(id);
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		}
		if (oldDesignTableInfo == null) {
			throw new CommonException("没有获取到对象：" + id);
		}
		try {
			designTableInfo.setIsDispatch(1);
			designTableInfo.setCreateTime(new Date());
			designTableInfoService.update(designTableInfo);
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		}
		
		DispatchTask query = new DispatchTask();
		query.setTaskId(id);
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
		dispatchTask.setTaskId(designTableInfo.getId());
		dispatchTask.setTaskName(designTableInfo.getTaskName());
		dispatchTask.setTaskType(DispatchTask.QUALITY_TASK);
		dispatchTask.setCycleType(designTableInfo.getCycleType());
		dispatchTask.setJobCron(designTableInfo.getJobCron());
		dispatchTask.setTriggerTime(designTableInfo.getTriggerTime());
		Map<String, String> cronExpression 
				= quartzServer.calcuLateExpression(designTableInfo.getJobCron(), designTableInfo.getTriggerTime());
		dispatchTask.setCronExpression(cronExpression.get("cronExpression"));
		dispatchTask.setCronDescription(cronExpression.get("cronDiscription"));
		dispatchTask.setStatus(DispatchTask.STATUS_RUNNING);
		dispatchTask.setAlarmId(designTableInfo.getAlarmId());
		dispatchTask.setCreateTime(new Date());
		dispatchTask.setLastModifyTime(new Date());
		dispatchTask.setDeleteFlag(Constant.NOT_DELETE);
//		try {
//			dispatchTaskService.update(dispatchTask);
//		} catch (Exception e) {
//			throw new CommonException(e.getMessage(), e);
//		}
		dispatchTaskService.saveOrUpdate(dispatchTask);*/
	}

	@Override
	public List<MetadataDatasource> findSource2Task() {
		List<QualityTaskDetail> qualityTaskDetails = qualityTaskDetailDao.findSource2Task();
		String sourceIds = qualityTaskDetails.stream().mapToLong(QualityTaskDetail :: getDatasourceId)
				.mapToObj(sourceId -> String.valueOf(sourceId)).collect(Collectors.joining(","));
		List<MetadataDatasource> sources = datasourceService2.getSourceByIds(sourceIds);
		return sources;
	}

	@Override
	public List<DesignTableInfo> findTableNamePage(Long sourceId, String tableName, Page<DesignTableInfo> page) {
		List<DesignTableInfo> designTableInfos = qualityTaskDetailDao.findTableNamePage(sourceId, tableName, page);
		return designTableInfos;
	}
	
	/**
	 * 根据数据源id、表名、任务id 查询出表名
	 * @param sourceId
	 * @param tableName
	 * @param taskId
	 * @return
	 */
	@Override
	public List<DesignTableInfo> findTaskTables(Long sourceId,String tableName,Long taskId) throws Exception{
		return qualityTaskDetailDao.findTaskTables(sourceId, tableName, taskId);
	}

	@Override
	public List<QualityTaskDetail> getVerifyDetailByDesignTableId(Long designTableId) {
		return qualityTaskDetailDao.getVerifyDetailByDesignTableId(designTableId);
	}

	

	@Override
	public void removeQualityDetail(Long tableDesignId) {
		qualityTaskDetailDao.removeQualityDetail(tableDesignId);
	}

	@Override
	public void removeQualityOnTableByType(Long datasourceId, String tableName, String columnName, String verifyType) {
		qualityTaskDetailDao.removeQualityOnTableByType(datasourceId, tableName, columnName, verifyType);
	}
	
	@Override
	public void deleteByRelationIds(List<Long> relationIds) {
		qualityTaskDetailDao.deleteByRelationIds(relationIds);
	}

	@Override
	public List<QualityTaskDetail> selectByElementId(Long id) {
		return qualityTaskDetailDao.selectByElementId(id);
	}
	
	@Override
	public void delete(Long datasourceId, String tableName) {
		qualityTaskDetailDao.deleteBySourceIdAndTableName(datasourceId, tableName);
	}

	@Override
	public String debugAndSave(Long datasourceId) {
//		Map<String, String> resultMap = new HashMap();
		
		DesignTableInfo example = new DesignTableInfo();
		example.setDatasourceId(datasourceId);
		example.setStatus(DesignTableInfo.STATUS_DRAFT);
		example.setDeleteFlag(String.valueOf(0));
		List<DesignTableInfo> tableInfos = null;
		try {
			tableInfos = designTableInfoService.query(example);
		} catch (Exception e) {
			throw new CommonException("根据条件查询DesignTableInfo异常，example=" + example);
		}
		Map<String, List<QualityTaskLog>> tableLogs = new HashMap<>();
		if (tableInfos != null && tableInfos.size() > 0) {
			for (DesignTableInfo tableInfo : tableInfos) {
				List<QualityTaskLog> logs = null;
				// 先判断这张表绑定的数据元的状态是否都是审核通过
				List<QualityTaskDetail> qualityTasks = qualityTaskDetailDao.findByDesignTableId(datasourceId
						, tableInfo.getTableName());
				Set<Long> set = new HashSet<>();
				for (QualityTaskDetail taskDetail : qualityTasks) {
					if (taskDetail.getElementId() != null) {
						set.add(taskDetail.getElementId());
					}
//					set.add(taskDetail.getElementId());
				}
				boolean allElementOk = true;
				for (Long elementId : set) {
					DataElementEntity element;
					try {
						element = dataElementService.getById(elementId);
					} catch (Exception e) {
						throw new CommonException(e.getMessage());
					}
					/*if (!Integer.valueOf(DataElementEntity.STATUS_AUDITED).equals(element.getStatus())) {
						// 有未审核通过的数据元
						allElementOk = false;
						break;
					}*/
				}
				if (allElementOk) {
					try {
						logs = execute(tableInfo.getId(), DEBUG);
//						designTableInfoService.saveQualityTask(tableInfo.getId());
						tableLogs.put(tableInfo.getTableName(), logs);
					} catch (Exception e) {
//						resultMap.put("", value)
//						return tableInfo.getTableName();
					}
				}
				/*try {
					execute(tableInfo.getId(), DEBUG);
					designTableInfoService.saveQualityTask(tableInfo.getId());
				} catch (Exception e) {
					return tableInfo.getTableName();
				}*/
			}
		}
		return JSON.toJSONString(tableLogs);
//		return null;
	}

	@Override
	public void updateEnable(String jsonString, Long designTableId) {
		JSONArray jsonArr = JSON.parseArray(jsonString);
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			QualityTaskDetail taskDetail = qualityTaskDetailDao.selectByPrimaryKey(jsonObj.getLong("id"));
			taskDetail.setEnable(jsonObj.getInteger("enable"));
			qualityTaskDetailDao.updateByPrimaryKeySelective(taskDetail);
		}
		// 将与之相关的调度暂停，将状态改为草稿
		try {
			DesignTableInfo tableInfo = designTableInfoService.getById(designTableId);
			if (tableInfo != null) {
				tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
				designTableInfoService.update(tableInfo);
				DispatchTask dispatchTask = dispatchTaskService.findByTaskIdAndType(tableInfo.getId(), "1");
				if (dispatchTask != null) {
					dispatchTaskService.pause(dispatchTask.getId());
				}
			}
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		}
	}

	@Override
	public List<QualityTaskDetail> findByElementDistinct(Long elementId) {
		return qualityTaskDetailDao.findByElementDistinct(elementId);
	}

	@Override
	public List<QualityTaskDetail> findByRelationId(Long relationId) {
		return qualityTaskDetailDao.findByRelationId(relationId);
	}

	@Override
	public void remove(Long dataSoureId, String tableName, String column) {
		
		qualityTaskDetailDao.remove(dataSoureId, tableName, column);
		
	}

	@Override
	public void deleteByElementId(Long elementId) {
		qualityTaskDetailDao.deleteByElementId(elementId);
	}

	@Override
	public void deleteBySourceIdAndTabName(Long datasourceId, String tableName, String verifyType) {
		
		qualityTaskDetailDao.deleteBySourceIdAndTabName(datasourceId, tableName, verifyType);
		
	}

	@Override
	public void removeQualityById(String qualityTaskDetailIds) {
		qualityTaskDetailDao.removeQualityById(qualityTaskDetailIds);
	}

	@Override
	public List<QualityTaskDetail> findByDatasourceId(Long sourceId) {
		return qualityTaskDetailDao.findByDatasourceId(sourceId);
	}
	
	@Override
	public String exportQualityTaskDetail(Long sourceId) {
		
//		DataSource datasource = datasourceDao.selectByPrimaryKey(sourceId);
		DataSource datasource = datasourceService2.getDatasourceById(sourceId);
		
		List<QualityTaskDetail> taskDetails = qualityTaskDetailDao.findByDatasourceId(sourceId);
		if (taskDetails == null || taskDetails.size() <= 0) {
			logger.info("该数据源下没有表配置校验规则，数据源Id：" + sourceId + ",数据源名称：" 
					+ datasource.getDatasourceName());
			throw new CommonException("该数据源下没有表配置校验规则");
		}
		Map<String, Map<String, String>> tableVerifyMap = new HashMap<>();
		for (QualityTaskDetail taskDetail : taskDetails) {
			String tableName = taskDetail.getTableName();
			Map<String, String> columnVerifyMap = tableVerifyMap.get(tableName);
			if (columnVerifyMap == null) {
				columnVerifyMap = new HashMap<>();
				columnVerifyMap.put(taskDetail.getColumnName(), taskDetail.getName());
				tableVerifyMap.put(tableName, columnVerifyMap);
			} else {
				String columnName = taskDetail.getColumnName();
				String verifyTypes = columnVerifyMap.get(columnName);
				if (verifyTypes == null) {
					verifyTypes = taskDetail.getName();
					columnVerifyMap.put(columnName, verifyTypes);
				} else {
					verifyTypes = verifyTypes + "," + taskDetail.getName();
					columnVerifyMap.put(columnName, verifyTypes);
				}
			}
		}
		// 写Excel
		SXSSFWorkbook wb = new SXSSFWorkbook();
		SXSSFSheet sheet = wb.createSheet(datasource.getDatasourceName());
		SXSSFRow row1 = sheet.createRow(0);
		SXSSFCell cell11 = row1.createCell(0);
		SXSSFCell cell12 = row1.createCell(1);
		SXSSFCell cell13 = row1.createCell(2);
		cell11.setCellValue("表名");
		cell12.setCellValue("字段名");
		cell13.setCellValue("校验规则");
		int startRow = 1;
		for (Map.Entry<String, Map<String, String>> tableVerifyEntry : tableVerifyMap.entrySet()) {
			int offset = 0;
			String tableName = tableVerifyEntry.getKey();
			Map<String, String> columnVerifyMap = tableVerifyEntry.getValue();
			for (Map.Entry<String, String> columnVerifyEntry : columnVerifyMap.entrySet()) {
				SXSSFRow row = sheet.createRow(startRow + offset);
				row.createCell(0);
				SXSSFCell columnCell = row.createCell(1);
				SXSSFCell verifyTypeCell = row.createCell(2);
				columnCell.setCellValue(columnVerifyEntry.getKey());
				verifyTypeCell.setCellValue(columnVerifyEntry.getValue());
				offset += 1;
			}
			SXSSFRow startR = sheet.getRow(startRow);
			startR.getCell(0).setCellValue(tableName);
			if (offset > 1) {
				CellRangeAddress region = new CellRangeAddress(startRow, startRow + offset - 1, 0, 0);
				sheet.addMergedRegion(region);
			}
			startRow += offset;
		}
		
		FileOutputStream fos = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String tempFileName = datasource.getDatasourceName() + "_" + sdf.format(new Date()) + "_规则导出" 
				+ ".xlsx";
		String tempFileDir = FileUtil.getJarRootDir()
				+ PropertyPlaceholder.getContextProperty("errorDataTempDir");
		File tempDir = new File(tempFileDir);
		if (tempDir.exists()) {
			if (!tempDir.isDirectory()) {
				tempDir.delete();
				tempDir.mkdirs();
			}
		} else {
			tempDir.mkdirs();
		}
		logger.info("规则设计导出文件：" + tempFileDir + tempFileName);
		try {
			File file = new File(tempFileDir + tempFileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
		} catch (IOException e) {
			try {
				wb.close();
			} catch (IOException e1) {
			}
			throw new CommonException("创建问题数据临时文件异常：", e);
		}
		try {
			wb.write(fos);
			wb.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tempFileName;
		
	}

	

}
