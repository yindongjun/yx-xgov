package cn.com.yeexun.dispatch.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.entity.TaskOverviewVo;
import cn.com.yeexun.dispatch.service.IDispatchTaskService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("dispatchCenter")
@Api("调度中心")
public class DispatchTaskController {

	private Logger logger = Logger.getLogger(DispatchTaskController.class);
	
	@Autowired
	private IDispatchTaskService dispatchTaskService;
	
	@ResponseBody
	@RequestMapping(value = "/excuteTask",method = RequestMethod.GET)
	@ApiOperation(value = "立即执行调度任务", consumes="application/x-www-form-urlencoded")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "taskId", value = "任务id", required = true, dataType = "long")
	})
	public String getTaskList(long taskId, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			dispatchTaskService.excuteTask(taskId);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e){
	    	logger.error("立即执行调度任务", e);
	    	map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
	    } catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ApiOperation(value = "查询调度列表", consumes="application/x-www-form-urlencoded")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "taskType", value = "任务类型,0-采集任务 1-质量任务", required = false, dataType = "string"),
		@ApiImplicitParam(name = "cycleType", value = "调度类型,0-一次性 1-周期性", required = false, dataType = "string"),
		@ApiImplicitParam(name = "taskName", value = "模糊搜索名字", required = false, dataType = "string"),
		@ApiImplicitParam(name = "status", value = "状态，状态：1-启用中 2-暂停 3-已完成", required = false, dataType = "string"),
		@ApiImplicitParam(name = "start", value = "起始下标", required = false, dataType = "int"),
		@ApiImplicitParam(name = "length", value = "每页条数", required = false, dataType = "int")
	})
	@RequestMapping(value = "/manager/tasks",method = RequestMethod.POST)
	@ResponseBody
	public String getTaskList(DispatchTask dispatchTask, Integer start, Integer length, String taskName){
		Map<String, Object> result = null;
		Page<DispatchTask> page = new Page<>();
		page.setStart(start);
		page.setLength(length);
		try {
			List<DispatchTask> dispatchTasks = dispatchTaskService.findDispatchTaskPage(dispatchTask, taskName, page);
			result = CodeUtil.getSuccessMap();
			result.put(CodeUtil.RESULT_DATA, dispatchTasks);
			result.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("查询调度列表出错：", e);
			result = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(result);
	}
	
	@ApiOperation(value = "调度配置"/*, consumes="application/x-www-form-urlencoded"*/)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "调度id，tb_dispatch_task主键", required = true, dataType = "long"),
		@ApiImplicitParam(name = "taskType", value = "任务类型,0-采集任务 1-质量任务", required = false, dataType = "string"),
		@ApiImplicitParam(name = "cycleType", value = "调度类型,0-一次性 1-周期性", required = false, dataType = "string"),
		@ApiImplicitParam(name = "taskName", value = "模糊搜索名字", required = false, dataType = "string")
	})
	
	@RequestMapping(value = "/manager/config",method = RequestMethod.POST)
	@ResponseBody
	public String config(DispatchTask dispatchTask) {
		Map<String, Object> result = null;
		try {
			dispatchTaskService.saveOrUpdate(dispatchTask);
			result = CodeUtil.getSuccessMap();
		} catch (CommonException e){
	    	logger.error("配置调度出错：", e);
	    	result = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
		} catch (Exception e) {
			logger.error("配置调度出错：", e);
			result = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(result);
	}

	@ApiOperation(value = "暂停调度", consumes="application/x-www-form-urlencoded")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "调度id，tb_dispatch_task主键", required = true, dataType = "long")
	})
	@RequestMapping(value = "/manager/pause",method = RequestMethod.POST)
	@ResponseBody
	public String pause(Long id) {
		Map<String, Object> result = null;
		try {
			dispatchTaskService.pause(id);
			result = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("暂停调度出错：", e);
			result = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(result);
	}
	
	@ApiOperation(value = "启动暂停的调度", consumes="application/x-www-form-urlencoded")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "调度id，tb_dispatch_task主键", required = true, dataType = "long")
	})
	@RequestMapping(value = "/manager/resume",method = RequestMethod.POST)
	@ResponseBody
	public String resume(Long id) {
		Map<String, Object> result = null;
		try {
			dispatchTaskService.resume(id);
			result = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("启动暂停的调度出错：", e);
			result = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(result);
	}
	
	@ApiOperation(value = "调度统计", consumes="application/x-www-form-urlencoded")
	@RequestMapping(value = "/manager/overview",method = RequestMethod.GET)
	@ResponseBody
	public String overview() {
		Map<String, Object> result = null;
		try {
			TaskOverviewVo overview = dispatchTaskService.overview();
			result = CodeUtil.getSuccessMap();
			result.put(CodeUtil.RESULT_DATA, overview);
		} catch (Exception e) {
			logger.error("调度统计出错：", e);
			result = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(result);
	}
	@ApiOperation(value = "根据id获取调度任务", consumes="application/x-www-form-urlencoded")
	@RequestMapping(value = "/manager/getTaskById",method = RequestMethod.GET)
	@ResponseBody
	public String getById(Long id) {
		Map<String, Object> result = null;
		try {
			DispatchTask task = dispatchTaskService.getTaskById(id);
			result = CodeUtil.getSuccessMap();
			result.put(CodeUtil.RESULT_DATA, task);
		} catch (Exception e) {
			logger.error("调度统计出错：", e);
			result = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(result);
	}
}
