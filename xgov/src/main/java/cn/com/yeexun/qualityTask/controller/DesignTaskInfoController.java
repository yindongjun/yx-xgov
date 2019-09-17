package cn.com.yeexun.qualityTask.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.DesignTaskInfo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityTask.service.IDesignTaskInfoService;
import cn.com.yeexun.qualityTask.service.IQualityTaskDetailService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.DebugException;
import cn.com.yeexun.utils.FileUtil;
import cn.com.yeexun.utils.PropertyPlaceholder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("qualityTask")
@Api("质量任务_质量校验表")
public class DesignTaskInfoController {

	private Logger logger = Logger.getLogger(DesignTaskInfoController.class);
	
	@Autowired
	private IDesignTaskInfoService taskInfoService;
	
	@Autowired
	private IQualityTaskDetailService qualityTaskDetailService;
	
	@Autowired
	private IDesignTaskInfoService designTaskInfoService;
	
	
	@ResponseBody
	@RequestMapping(value = "/saveDesignTasks",method = RequestMethod.POST)
	@ApiOperation("保存或者编辑多表校核任务")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "tableIds", value = "选择的多表的id", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "taskInfo", value = "质量调度任务", required = true, dataType = "DesignTaskInfo")
	  })
	public String saveDesignTasks(String tableIds, DesignTaskInfo taskInfo){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			taskInfoService.saveDesignTasks(tableIds, taskInfo);
		} catch (CommonException e) {
			logger.error("保存校核任务设计:", e);
		    map = CodeUtil.getErrorRequestMap();
		    map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("保存校核任务设计:", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("执行质量任务")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "designTableInfoId", value = "", required = true, dataType = "")
	  })
	@RequestMapping(value = "/execute",method = RequestMethod.GET)
	@ResponseBody
	public String excute(Long designTableInfoId, Integer isDebug){
		Map<String, Object> map = null;
		try {
			List<QualityTaskDetail> findQualituTask =  null; //qualityTaskDetailService.execute(designTableInfoId, isDebug);
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
	
	@ApiOperation("查询出可以添加到调度中的质量任务的表,以及已添加的表")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "sourceId", value = "数据源id", required = false, dataType = "Long"),
	      @ApiImplicitParam(name = "tableName", value = "模糊搜索的表名", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "taskId", value = "任务id,如果是新增调度时，taskId=-1", required = true, dataType = "Long")
	  })
	@RequestMapping(value = "/findTaskTables",method = RequestMethod.POST)
	@ResponseBody
	public String findTaskTables(Long sourceId, String tableName,Long taskId){
		Map<String, Object> map = null;
		try {
			Page<DesignTableInfo> page = new Page<>();
			//查询该任务已配置的表
			List<DesignTableInfo> tables = qualityTaskDetailService.findTaskTables(sourceId, tableName, taskId);
			List<DesignTableInfo> allTables = qualityTaskDetailService.findTaskTables(sourceId, tableName, null);
			if(tables != null && tables.size() > 0 
					&& allTables != null && allTables.size() > 0){
				//去除已配置的表
				allTables.removeAll(tables);
			}
//			Long datasourceId = taskInfoService.getDatasourceIdByTaskId(taskId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, allTables);
			map.put("tables", tables);
//			map.put("datasourceId", datasourceId);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ApiOperation("查询质量调度列表")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "分页起始", value = "start", required = true, dataType = "int"),
	      @ApiImplicitParam(name = "分页长度", value = "length", required = true, dataType = "int")
	  })
	@RequestMapping(value = "/qualitytasks",method = RequestMethod.POST)
	@ResponseBody
	public String getQualityTasks(Integer start, Integer length, String taskName){
		Map<String, Object> map = null;
		try {
			Page<DesignTaskInfo> page = new Page<>();
			page.setStart(start);
			page.setLength(length);
//			List<DesignTableInfo> tables = qualityRuleDetailService.getQualityTasksPage(page, taskName);
			List<DesignTaskInfo> tables = designTaskInfoService.getQualityTasksPage(page, taskName);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, tables);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("根据质量任务id获取对应的表信息")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "质量任务id", value = "taskId", required = true, dataType = "long")
	  })
	@RequestMapping(value = "/getTaskTables",method = RequestMethod.POST)
	@ResponseBody
	public String getTaskTables(Long taskId){
		Map<String, Object> map = null;
		try {
			List<DesignTableInfo> tables = designTaskInfoService.getTaskTables(taskId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, tables);
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("根据质量任务id和表名获取对应的质量规则信息和字段信息")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "质量任务id", value = "taskId", required = true, dataType = "long"),
	      @ApiImplicitParam(name = "表名", value = "tableName", required = true, dataType = "string")
	  })
	@RequestMapping(value = "/getTaskTablesDetail",method = RequestMethod.POST)
	@ResponseBody
	public String getTaskTablesDetail(Long taskId, String tableName){
		Map<String, Object> map = null;
		try {
			List<QualityTaskDetail> qualityTaskDetails = designTaskInfoService.getTaskTablesDetail(taskId, tableName);
			Map<String, String> verifyTypeOnColumn = new HashMap<>();
			for (QualityTaskDetail taskDetail : qualityTaskDetails) {
				String verifyType = verifyTypeOnColumn.get(taskDetail.getColumnName());
				if (verifyType == null) {
					verifyType = taskDetail.getName();
					verifyTypeOnColumn.put(taskDetail.getColumnName(), verifyType);
				} else {
					verifyType = verifyType + "," + taskDetail.getName();
					verifyTypeOnColumn.put(taskDetail.getColumnName(), verifyType);
				}
			}
			List<Map<String, String>> columns = new ArrayList<>();
			for (Map.Entry<String, String> entry : verifyTypeOnColumn.entrySet()) {
				Map<String, String> column = new HashMap<>();
				column.put("fieldName", entry.getKey());
				column.put("fieldRegular", entry.getValue());
				columns.add(column);
			}
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, columns);
		} catch (Exception e) {
			logger.error("执行质量任务出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ApiOperation("根据数据源id导出规则设计Excel")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "数据源id", value = "sourceId", required = true, dataType = "long")
	  })
	@RequestMapping(value = "/exportQualityTaskDetail",method = RequestMethod.GET)
	@ResponseBody
	public String exportQualityTaskDetail(Long sourceId) {
		Map<String, Object> map = null;
		try {
			String fileName = qualityTaskDetailService.exportQualityTaskDetail(sourceId);
//			FileUtil.downloadFileToBrowser(FileUtil.getJarRootDir() 
//					+ PropertyPlaceholder.getContextProperty("errorDataTempDir") + fileName
//					, fileName, response);
//			
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, fileName);
		} catch (CommonException e) {
			logger.error("导出规则设计出错：", e);
			map = CodeUtil.getErrorRequestMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("导出规则设计出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ApiOperation("根据数据源id导出规则设计Excel")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "数据源id", value = "sourceId", required = true, dataType = "long")
	  })
	@RequestMapping(value = "/downloadQualityTaskDetail",method = RequestMethod.POST)
	@ResponseBody
	public String downloadQualityTaskDetail(String fileName, HttpServletResponse response) {
		Map<String, Object> map = null;
		try {
			FileUtil.downloadFileToBrowser(FileUtil.getJarRootDir() 
					+ PropertyPlaceholder.getContextProperty("errorDataTempDir") + fileName
					, fileName, response);
			
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			logger.error("导出规则设计出错：", e);
			map = CodeUtil.getErrorRequestMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("导出规则设计出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	
	
	
	
}
