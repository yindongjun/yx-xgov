package cn.com.yeexun.dispatch.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dispatch.entity.DispatchHistory;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.entity.TaskHisQueryVo;
import cn.com.yeexun.dispatch.entity.TaskOverviewVo;
import cn.com.yeexun.dispatch.service.IDispatchHistoryService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api("调度中心/调度监控")
@Controller
@RequestMapping("dispatchCenter/monitor")
public class DispatchHistoryController {
	
	private static Logger logger = LoggerFactory.getLogger(DispatchHistoryController.class);
	
	@Autowired
	private IDispatchHistoryService dispatchHistoryService;
	
	@ApiOperation("调度任务统计概览")
	@RequestMapping(value = "/overview", method = RequestMethod.GET)
	@ResponseBody
	public String getTaskList(){
		Map<String, Object> result = null;
		try {
			TaskOverviewVo overview = dispatchHistoryService.overview();
			result = CodeUtil.getSuccessMap();
			result.put(CodeUtil.RESULT_DATA, overview);
		} catch (Exception e) {
			logger.error("调度任务统计概览出错：", e);
			result = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(result);
	}
	
	@ApiOperation(value = "查询最新调度列表", consumes="application/x-www-form-urlencoded")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "taskType", value = "任务类型,0-采集任务 1-质量任务", required = false, dataType = "string"),
		@ApiImplicitParam(name = "status", value = "状态：0-执行中 1-成功 2-失败", required = false, dataType = "string"),
		@ApiImplicitParam(name = "taskName", value = "模糊搜索名字", required = false, dataType = "string"),
		@ApiImplicitParam(name = "start", value = "起始下标", required = false, dataType = "int"),
		@ApiImplicitParam(name = "length", value = "每页条数", required = false, dataType = "int")
	})
	@RequestMapping(value = "/histories/lastest",method = RequestMethod.POST)
	@ResponseBody
	public String listLastestHis(DispatchHistory dispatchHistory, Integer start, Integer length, String taskName) {
		Map<String, Object> result = null;
		Page<DispatchTask> page = new Page<>();
		page.setStart(start);
		page.setLength(length);
		try {
			List<DispatchHistory> lastestHistories = dispatchHistoryService.listLastestHistoriesPage(dispatchHistory, page);
			result = CodeUtil.getSuccessMap();
			result.put(CodeUtil.RESULT_DATA, lastestHistories);
			result.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("查询最新调度列表出错：", e);
			result = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(result);
	}
	
	@ApiOperation(value = "根据taskId查询该任务的执行历史信息", consumes="application/x-www-form-urlencoded")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "taskId", value = "对应task的id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "status", value = "状态：0-执行中 1-成功 2-失败", required = false, dataType = "string"),
		@ApiImplicitParam(name = "startTimeLower", value = "开始时间下限", required = false, dataType = "date"),
		@ApiImplicitParam(name = "startTimeUpper", value = "开始时间上限", required = false, dataType = "date"),
		@ApiImplicitParam(name = "endTimeLower", value = "结束时间下限", required = false, dataType = "date"),
		@ApiImplicitParam(name = "endTimeUpper", value = "结束时间上限", required = false, dataType = "date"),
		@ApiImplicitParam(name = "start", value = "起始下标", required = false, dataType = "int"),
		@ApiImplicitParam(name = "length", value = "每页条数", required = false, dataType = "int")
	})
	@RequestMapping(value = "/histories",method = RequestMethod.POST)
	@ResponseBody
	public String listHisByTaskId(TaskHisQueryVo hisQueryVo, Integer start, Integer length) {
		Map<String, Object> result = null;
		Page<DispatchTask> page = new Page<>();
		page.setStart(start);
		page.setLength(length);
		try {
			List<DispatchHistory> lastestHistories 
					= dispatchHistoryService.listHistoriesByTaskIdPage(hisQueryVo, page);
			result = CodeUtil.getSuccessMap();
			result.put(CodeUtil.RESULT_DATA, lastestHistories);
			result.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (CommonException e) {
			logger.info("查询执行历史出错：", e);
			result = CodeUtil.getBadRequestMap();
			result.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("查询最新调度列表出错：", e);
			result = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(result);
	} 
	
}
