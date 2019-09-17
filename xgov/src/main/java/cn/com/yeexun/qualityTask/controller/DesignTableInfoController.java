package cn.com.yeexun.qualityTask.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.collectTask.service.ICollectTaskService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.qualityTask.entity.CheckResult;
import cn.com.yeexun.qualityTask.entity.CheckResultCount;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.DesignTaskInfo;
import cn.com.yeexun.qualityTask.entity.QualityTableCountVo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityTask.entity.QualityTaskLog;
import cn.com.yeexun.qualityTask.service.ICheckResultCountService;
import cn.com.yeexun.qualityTask.service.ICheckResultDetailService;
import cn.com.yeexun.qualityTask.service.IDesignTableInfoService;
import cn.com.yeexun.qualityTask.service.IDesignTaskInfoService;
import cn.com.yeexun.qualityTask.service.IQualityTaskDetailService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import cn.com.yeexun.utils.DebugException;
import cn.com.yeexun.utils.FileUtil;
import cn.com.yeexun.utils.PropertyPlaceholder;
import cn.com.yeexun.utils.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("qualityDesignTable")
@Api("质量设计_对质量任务的详细设计")
public class DesignTableInfoController {

	private Logger logger = Logger.getLogger(DesignTableInfoController.class);
	
	@Autowired
	private IDesignTableInfoService tableInfoService;
	
	@Autowired
	private ICheckResultCountService checkResultCountService;
	
	@Autowired
	private ICheckResultDetailService checkResultDetailService;
	
	@Autowired
	private IQualityTaskDetailService qualityTaskDetailService;
	
//	@Autowired
//	private IDataSourceService dataSourceService;
	
	@Autowired
	private ICollectTaskService collectTaskService;
	
	@Autowired
	private IDesignTaskInfoService designTaskInfoService;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	@ResponseBody
	@RequestMapping(value = "/refreshTableDesignList",method = RequestMethod.GET)
	@ApiOperation("刷新质量设计的列表信息")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "sourceId", value = "数据源的id", required = true, dataType = "Long")
	  })
	public String refreshDesignList(long sourceId, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DesignTableInfo> list = tableInfoService.refreshTableDesignList(sourceId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
		} catch (CommonException e) {
			logger.warn("刷新质量设计列表异常：", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("刷新质量设计列表出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getTableChange",method = RequestMethod.GET)
	@ApiOperation("获取一个表的规则信息的变更详情")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "tableInfoId", value = "数据源质量设计的id", required = true, dataType = "Long")
	  })
	public String getTableChange(long tableInfoId, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
//			List<ChangeColumnRule> list = tableInfoService.getTableChangeInfo(tableInfoId);
			map = CodeUtil.getSuccessMap();
//			map.put(CodeUtil.RESULT_DATA, list);
		} catch (Exception e) {
			logger.error("获取一个表的规则信息的变更详情:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getColumnChange",method = RequestMethod.GET)
	@ApiOperation("获取一个表中一个字段的规则信息的变更详情")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "beforeDetailId", value = "之前版本的id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "currentDetailId", value = "现在版本的id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "columnName", value = "字段名称", required = true, dataType = "Long")
	  })
	public String getColumnChange(long beforeDetailId, long currentDetailId, String columnName, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
//			List<ColumnDiffDetail> list = tableInfoService.getColumnChange(beforeDetailId, currentDetailId, columnName);
			map = CodeUtil.getSuccessMap();
//			map.put(CodeUtil.RESULT_DATA, list);
		} catch (Exception e) {
			logger.error("获取一个表的规则信息的变更详情:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/createTransByTable",method = RequestMethod.GET)
	@ApiOperation("生成所选择的表的校核任务设计")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "tableDesignId", value = "选择的表的id", required = true, dataType = "Long")
	  })
	public String createTransByTable(long tableDesignId, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 不存放数据库中，只是生成一个默认的校核节点，主要任务是根据不同的数据库类型生成不同的sql查询语句
			QualityTaskDetail task = tableInfoService.createTransByTable(tableDesignId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, task);
		} catch (Exception e) {
			logger.error("生成所选择的表的校核任务设计:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	 
	@ResponseBody
	@RequestMapping(value = "/buildTransByTable",method = RequestMethod.GET)
	@ApiOperation("自动根据质量规则生成所选择的表的校核任务设计")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "tableDesignId", value = "选择的表的id", required = true, dataType = "Long")
	  })
	public String buildTransByTable(DesignTableInfo designTableInfo){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 自动根据质量规则生成校核任务，会入库
			List<QualityTaskDetail> tasks = tableInfoService.buildTransByTable(designTableInfo);
			//将所有detail归类，按照类型的划分返回给前端
			List<Map<String, Object>> resultMap = tableInfoService.getResultMap(tasks);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, resultMap);
		} catch (Exception e) {
			logger.error("自动根据质量规则生成所选择的表的校核任务设计:", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/submitQualityTask",method = RequestMethod.GET)
	@ApiOperation("自动根据质量规则生成所选择的表的校核任务设计")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "saveQualityTask", value = "选择的表的id", required = true, dataType = "Long")
	  })
	public String submitQualityTask(long tableDesignId, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			
		} catch (Exception e) {
			logger.error("自动根据质量规则生成所选择的表的校核任务设计:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getTablePreviewData",method = RequestMethod.GET)
	@ApiOperation("数据预览") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tableName", value = "表名", required = true, dataType = "String"),
		@ApiImplicitParam(name = "sourceId", value = "数据源id", required = true, dataType = "Long"),
	  })
	public String getTablePreviewData(String tableName,Long sourceId) {
		Map<String, Object> map = CodeUtil.getSuccessMap();
		try {
			//map = CodeUtil.getSuccessMap();
			map = tableInfoService.getTablePreviewData(tableName ,sourceId);
			//map.put(CodeUtil.RESULT_DATA, list);
		} catch(CommonException e) {
			logger.error("查找表数据时候出错:", e);
			map = CodeUtil.getErrorRequestMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("查找表数据时候出错:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("查看每一步的校验信息")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "", value = "", required = true, dataType = "")
	  })
	@RequestMapping(value = "/eachstepinfo",method = RequestMethod.GET)
	@ResponseBody
//	public String findStepInfo(Long tableDesignId, String verifyType){
	public String findStepInfo(Long datasourceId, String tableName, String verifyType){
		Map<String, Object> map = null;
		try {
			QualityTaskDetail qualityTaskDetail = new QualityTaskDetail();
			qualityTaskDetail.setDatasourceId(datasourceId);
			qualityTaskDetail.setVerifyType(verifyType);
			qualityTaskDetail.setTableName(tableName);
			List<QualityTaskDetail> findQualituTask = qualityTaskDetailService.findQualituTask(qualityTaskDetail);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, findQualituTask);
		} catch (Exception e) {
			logger.error("查看每一步的校验信息出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	/*
	@ApiOperation("删除校验信息")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "tableDesignId", value = "表设计id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "verifyType", value = "校验类型", required = true, dataType = "String")
	  })
	@RequestMapping(value = "/deletestepinfo",method = RequestMethod.GET)
	@ResponseBody
	public String deletestepinfo(Long tableDesignId, String verifyType){
		Map<String, Object> map = null;
		try {
			QualityTaskDetail qualityTaskDetail = new QualityTaskDetail();
//			qualityTaskDetail.setDesignTableId(tableDesignId);
			qualityTaskDetail.setVerifyType(verifyType);
			List<QualityTaskDetail> findQualituTask = qualityTaskDetailService.findQualituTask(qualityTaskDetail);
			for (QualityTaskDetail qualityTaskDetail2 : findQualituTask) {
				qualityTaskDetail2.setDeleteFlag(Constant.IS_DELETE);
				qualityTaskDetailService.update(qualityTaskDetail2);
			}
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("查看每一步的校验信息出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}*/
	
	@ApiOperation("执行质量任务")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "designTableInfoId", value = "", required = true, dataType = "")
	  })
	@RequestMapping(value = "/execute",method = RequestMethod.GET)
	@ResponseBody
	public String excute(Long designTableInfoId, Integer isDebug){
		Map<String, Object> map = null;
		try {
			List<QualityTaskLog> findQualituTask = qualityTaskDetailService.execute(designTableInfoId, isDebug);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, findQualituTask);
		} catch (DebugException e) {
			map = CodeUtil.getErrorRequestMap();
			map.put(CodeUtil.CODE_TEXT, CodeUtil.CODE_BAD_REQUEST);
			map.put(CodeUtil.RESULT_DATA, e);
		    map.put(CodeUtil.MESSAGE_TEXT, "调试出错，请检查配置。");
		} catch (CommonException e) {
			 map = CodeUtil.getErrorRequestMap();
			 map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		    map.put(CodeUtil.MESSAGE_TEXT, "调试出错，请检查配置。");
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("保存质量任务的调度信息")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "", value = "", required = true, dataType = "")
	  })
	@RequestMapping(value = "/add2task",method = RequestMethod.POST)
	@ResponseBody
	public String addToTask(DesignTableInfo designTableInfo){
		Map<String, Object> map = null;
		try {
			qualityTaskDetailService.addToTask(designTableInfo);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e){
	    	logger.error("新增质量任务", e);
	    	map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("查询出可以添加到调度中的质量任务的数据源")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "", value = "", required = true, dataType = "")
	  })
	@RequestMapping(value = "/source-task",method = RequestMethod.POST)
	@ResponseBody
	public String findSource2Task(){
		Map<String, Object> map = null;
		try {
			List<MetadataDatasource> sources = qualityTaskDetailService.findSource2Task();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, sources);
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("查询出可以添加到调度中的质量任务的表")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "", value = "", required = true, dataType = "")
	  })
	@RequestMapping(value = "/findtables",method = RequestMethod.POST)
	@ResponseBody
	public String findTableName(Long sourceId, String tableName, Integer start, Integer length){
		Map<String, Object> map = null;
		try {
			Page<DesignTableInfo> page = new Page<>();
			page.setStart(start);
			page.setLength(length);
			List<DesignTableInfo> tables = qualityTaskDetailService.findTableNamePage(sourceId, tableName, page);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, tables);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/queryProblemData",method = RequestMethod.POST)
	@ApiOperation("查询问题数据")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "status", value = "处理状态", required = true, dataType = "Integer"),
	      @ApiImplicitParam(name = "sourceName", value = "数据源名称", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "ownerId", value = "问题责任人id", required = true, dataType = "Long")
	  })
	public String queryProblemData(int start,int length, Integer status,Long sourceId,Long ownerId
			,String tableName, String taskName){
		Map<String, Object> map = new HashMap<String, Object>();
		Page<CheckResultCount> page = new Page<>();
		page.setStart(start);
		page.setLength(length);
		try {
			List<CheckResultCount> checkResultList = checkResultCountService.queryProblemDataPage(status,sourceId,ownerId
					,tableName,taskName, page);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, checkResultList);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAllDBList",method = RequestMethod.GET)
	@ApiOperation("查询所属数据源名称")
	@ApiImplicitParams({
	  })
	public String getAllDBList(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DataSource> list = checkResultCountService.getAllDBList();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
		} catch (Exception e) {
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/dealProblemData",method = RequestMethod.POST)
	@ApiOperation("处理问题数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "dealComment", value = "处理方式", required = true, dataType = "String")
	  })
	public String dealProblemData(String ids, String dealPeople,String dealComment){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			checkResultCountService.dealProblemData(ids,dealPeople, dealComment);
			map = CodeUtil.getSuccessMap();
			//map.put(CodeUtil.RESULT_DATA, list);
		} catch (Exception e) {
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/problemDetail",method = RequestMethod.GET)
	@ApiOperation("问题详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "start", value = "start", required = true, dataType = "int"),
		@ApiImplicitParam(name = "length", value = "length", required = true, dataType = "int")
	  })
	public String problemDetail(Long id, int start, int length){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Page<CheckResult> page = new Page<>();
			page.setStart(start);
			page.setLength(length);
			int count = checkResultDetailService.count(id);
			List<CheckResult> checkResultList = checkResultDetailService.getCheckResultDetail(id, page);
			map = CodeUtil.getSuccessMap();
			if(checkResultList != null && checkResultList.size() > 0){
				/*String tablehead = checkResultList.get(0).getTableHead().replace("[", "").replace("]", "").replace("\"", "");
				String[] item = tablehead.split(",");
				List<String> list = new ArrayList<String>(item.length);
				for (String str : item) {
					list.add(str);
				}*/
				CheckResultCount resultCount = checkResultCountService.getById(checkResultList.get(0).getResultCountId());
				List<JSONObject> cloumnData = new ArrayList<JSONObject>();
				for(CheckResult checkResult:checkResultList){
					cloumnData.add(JSONObject.parseObject(checkResult.getDataDetail()));
				}
				map.put(CodeUtil.RESULT_DATA, cloumnData);
				map.put(CodeUtil.RESULT_DATA_COUNT, count);
				map.put("tableHead",JSON.parseArray(resultCount.getColumnNames()));
			}
		} catch (Exception e) {
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/operationRecord",method = RequestMethod.GET)
	@ApiOperation("查看操作记录")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long")
	  })
	public String operationRecord(Long id){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			CheckResultCount checkResultCount = checkResultCountService.operationRecord(id);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, checkResultCount);
		} catch (Exception e) {
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getVerifyDetail",method = RequestMethod.GET)
	@ApiOperation("校验详情回显")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "designTableId", value = "id", required = true, dataType = "Long")
	  })
	public String getVerifyDetail(Long designTableId){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<QualityTaskDetail> qtList = qualityTaskDetailService.getVerifyDetailByDesignTableId(designTableId);
			
			Map<String, List<QualityTaskDetail>> groupByTypeMap = new HashMap<>();
			for (QualityTaskDetail qualityTaskDetail : qtList) {
				List<QualityTaskDetail> list = groupByTypeMap.get(qualityTaskDetail.getVerifyType());
				if (list == null) {
					list = new ArrayList<>();
					list.add(qualityTaskDetail);
					groupByTypeMap.put(qualityTaskDetail.getVerifyType(), list);
				} else {
					list.add(qualityTaskDetail);
				}
			}
			List<Map<String, Object>> resultList = new ArrayList<>();
			for (Map.Entry<String, List<QualityTaskDetail>> entry : groupByTypeMap.entrySet()) {
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("type", entry.getKey());
				resultMap.put("rules", entry.getValue());
				resultMap.put("name", entry.getValue().get(0).getName());
				resultList.add(resultMap);
			}
//			return resultList;
			
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, resultList);
		} catch (Exception e) {
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	/*@ApiOperation("查询质量调度列表")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "分页起始", value = "start", required = true, dataType = "int"),
	      @ApiImplicitParam(name = "分页长度", value = "length", required = true, dataType = "int")
	  })
	@RequestMapping(value = "/qualitytasks",method = RequestMethod.GET)
	@ResponseBody
	public String getQualityTasks(Integer start, Integer length, String taskName){
		Map<String, Object> map = null;
		try {
			Page<DesignTaskInfo> page = new Page<>();
			page.setStart(start);
			page.setLength(length);
//			List<DesignTableInfo> tables = qualityRuleDetailService.getQualityTasksPage(page, taskName);
			List<DesignTaskInfo> tables = qualityTaskDetailService.getQualityTasksPage(page, taskName);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, tables);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}*/
	
	@ApiOperation("查询质量调度详情")
	@ApiImplicitParams({
//	      @ApiImplicitParam(name = "分页起始", value = "start", required = true, dataType = "int"),
//	      @ApiImplicitParam(name = "分页长度", value = "length", required = true, dataType = "int")
	  })
	@RequestMapping(value = "/qualitytaskdetail",method = RequestMethod.GET)
	@ResponseBody
	public String getQualityTasks(Long taskInfoId){
		Map<String, Object> map = null;
		try {
			DesignTaskInfo taskInfo = designTaskInfoService.findById(taskInfoId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, taskInfo);
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("删除质量调度，可以批量删除")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "tableDesignId", value = "tableDesignId", required = true, dataType = "int")
	  })
	@RequestMapping(value = "/deleteQualityTask",method = RequestMethod.GET)
	@ResponseBody
	public String deleteQualityTasks(String tableDesignId){
		Map<String, Object> map = null;
		try {
			// 还要从quartz 中移除
			tableInfoService.deleteQualityTasks(tableDesignId);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	/*@ApiOperation("移除表上的校验规则")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "tableDesignId", value = "tableDesignId", required = true, dataType = "int")
	  })
	@RequestMapping(value = "/removeQualityOnTable",method = RequestMethod.GET)
	@ResponseBody
	public String removeQualityOnTable(Long tableDesignId){
		Map<String, Object> map = null;
		try {
//			tableInfoService.removeQualityOnTable(tableDesignId);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e){
			logger.error("移除质量任务出错：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}*/
	
	/*@ApiOperation("移除表上指定类型的校验规则详情")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "tableDesignId", value = "tableDesignId", required = true, dataType = "int"),
	      @ApiImplicitParam(name = "verifyType", value = "verifyType", required = true, dataType = "string")
	  })
	@RequestMapping(value = "/removeQualityOnTableByType",method = RequestMethod.POST)
	@ResponseBody
	public String removeQualityOnTable(Long datasourceId, String tableName, String verifyType){
		Map<String, Object> map = null;
		try {
			tableInfoService.removeQualityOnTableByType(datasourceId, tableName, verifyType);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}*/
	
	@ApiOperation("移除表上指定类型的校验规则详情")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "tableDesignId", value = "tableDesignId", required = true, dataType = "int"),
	      @ApiImplicitParam(name = "verifyType", value = "verifyType", required = true, dataType = "string")
	  })
	@RequestMapping(value = "/removeQualityOnTableByType",method = RequestMethod.POST)
	@ResponseBody
	public String removeQualityOnTable(Long datasourceId, String tableName, String columnName, String verifyType){
		Map<String, Object> map = null;
		try {
			tableInfoService.removeQualityOnTableByType(datasourceId, tableName, columnName, verifyType);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("导出问题数据到Excel")
	@ApiImplicitParams({
//	      @ApiImplicitParam(name = "tableDesignId", value = "tableDesignId", required = true, dataType = "int"),
//	      @ApiImplicitParam(name = "verifyType", value = "verifyType", required = true, dataType = "string")
	  })
	@RequestMapping(value = "/exportToExcel",method = RequestMethod.GET)
	@ResponseBody
	public String exportToExcel(Long resultCountId, HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			String fileName = checkResultDetailService.exportToExcel(resultCountId, request);
			FileUtil.downloadFileToBrowser(FileUtil.getJarRootDir() 
					+ PropertyPlaceholder.getContextProperty("errorDataTempDir") + fileName
					, fileName, response);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("导出问题数据出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("禁用或者启用指定表上的规则")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "jsonString", value = "格式：[{id:1,enable:1},...]", required = true, dataType = "string"),
		@ApiImplicitParam(name = "designTableId", value = "design_table_info表的主键id", required = true, dataType = "Long")
	})
	@RequestMapping(value = "/updateEnable",method = RequestMethod.POST)
	@ResponseBody
	public String updateEnable(String jsonString, Long designTableId){
		Map<String, Object> map = null;
		try {
			DesignTableInfo tableInfo = tableInfoService.getById(designTableId);
			if (tableInfo == null) {
				map = CodeUtil.getErrorRequestMap();
			    map.put(CodeUtil.MESSAGE_TEXT, "校验规则不存在，不能修改！");
			    return JSON.toJSONString(map);
			}
			if (!DesignTableInfo.STATUS_DRAFT.equals(tableInfo.getStatus())) {
			    map = CodeUtil.getErrorRequestMap();
			    map.put(CodeUtil.MESSAGE_TEXT, "该表状态不是草稿状态，不能修改！");
			    return JSON.toJSONString(map);
			}
		} catch (Exception e1) {
			logger.error("修改质量规则的启用状态时异常：", e1);
		    map = CodeUtil.getErrorRequestMap();
		    return JSON.toJSONString(map);
		}
		try {
			qualityTaskDetailService.updateEnable(jsonString, designTableId);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("修改质量规则的启用状态时异常：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("批量调试并保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "datasourceId", value = "数据源id", required = true, dataType = "long")
	})
	@RequestMapping(value = "/debugAndSave",method = RequestMethod.POST)
	@ResponseBody
	public String debugAndSave(Long datasourceId){
		Map<String, Object> map = null;
		try {
			String errorTableName = qualityTaskDetailService.debugAndSave(datasourceId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, errorTableName);
		} catch (Exception e) {
			logger.error("修改质量规则的启用状态时异常：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("已提交状态退回到草稿状态")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "datasourceId", value = "数据源id", required = true, dataType = "long")
	})
	@RequestMapping(value = "/toDraft",method = RequestMethod.POST)
	@ResponseBody
	public String toDraft(Long tableInfoId){
		Map<String, Object> map = null;
		try {
			tableInfoService.toDraft(tableInfoId);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("退后草稿状态时异常：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("展示规则设计页面的数据源")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "datasourceId", value = "数据源id", required = true, dataType = "long")
	})
	@RequestMapping(value = "/listQualitySource",method = RequestMethod.POST)
	@ResponseBody
	public String listQualitySource(int start, int length, String sourceName){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
//			Page<DataSource> page = new Page<DataSource>();
//			page.setStart(start);
//			page.setLength(length);
			// 根据角色显示数据源 管理员：全查 其它：对应用户ID查询
//			Long userId = (long) 2;
			String types 
					= "'MySQL','Oracle','PostgreSQL','SqlServer','DB2','Sybase','Teradata','Impala','Hive'";
			String result = datasourceService2.getDataSourceByTypes(types, sourceName, start, length);
			List<DataSource> list = ResponseUtil.getListData(result, DataSource.class);
			if(list != null && list.size() > 0) {
				//生成数据库的服务地址，形如：数据库名@地址   hdfs只显示地址  hive显示
				for( DataSource ds:list ){
					if(StringUtils.isNotBlank(ds.getDbname()) 
							&& (collectTaskService.isRDBMS(ds) 
							|| Constant.IMPALA.equals(ds.getDatabaseType()))) {
						ds.setSourceAddress( ds.getDbname() + "@" + ds.getIp() );
					} else {
						ds.setSourceAddress( ds.getIp() );
					}
				}
			}
			JSONObject jsonObj = JSON.parseObject(result);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
			map.put(CodeUtil.RESULT_DATA_COUNT, jsonObj.getInteger(CodeUtil.RESULT_DATA_COUNT));
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		} 
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("统计配置了规则的表的个数")
	@ApiImplicitParams({
//		@ApiImplicitParam(name = "datasourceIds", value = "数据源id", required = true, dataType = "long")
	})
	@RequestMapping(value = "/countQualityTable",method = RequestMethod.POST)
	@ResponseBody
	public String countQualityTable() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<QualityTableCountVo> count = tableInfoService.countQualityTable();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, count);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		} 
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("增加用户自定义规则")
	@ApiImplicitParams({
//		@ApiImplicitParam(name = "datasourceIds", value = "数据源id", required = true, dataType = "long")
	})
	@RequestMapping(value = "/addUserDefineQuality",method = RequestMethod.POST)
	@ResponseBody
	public String addUserDefineQuality(Long tableInfoId, String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<QualityTaskDetail> taskDetails = JSON.parseArray(jsonStr, QualityTaskDetail.class);
			tableInfoService.addUserDefineQuality(tableInfoId, taskDetails);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			logger.error("warn:", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("移除表上指定类型的校验规则详情")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "tableDesignId", value = "tableDesignId", required = true, dataType = "int"),
	      @ApiImplicitParam(name = "verifyType", value = "verifyType", required = true, dataType = "string")
	  })
	@RequestMapping(value = "/removeQualityById",method = RequestMethod.POST)
	@ResponseBody
	public String removeQualityById(String qualityTaskDetailIds){
		Map<String, Object> map = null;
		try {
			tableInfoService.removeQualityById(qualityTaskDetailIds);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	
}
