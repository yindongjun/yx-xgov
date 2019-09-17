package cn.com.yeexun.qualityTask.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.yeexun.qualityTask.entity.QualityTaskLog;
import cn.com.yeexun.qualityTask.service.IQualityTaskLogService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("qualityTaskLog")
@Api("质量任务执行历史")
public class QualityTaskLogController {
	
	private Logger logger = Logger.getLogger(QualityTaskLogController.class);
	
	@Autowired
	private IQualityTaskLogService taskLogService;
	
	@ResponseBody
	@RequestMapping(value = "/getLogsByTable",method = RequestMethod.POST)
	@ApiOperation("保存或者编辑多表校核任务")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "taskHisId", value = "执行历史id", required = true, dataType = "long"),
	      @ApiImplicitParam(name = "tableName", value = "表名", required = true, dataType = "string")
	  })
	public String getLogsByTable(Long taskHisId, String tableName){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<QualityTaskLog> logs = taskLogService.getLogsByTable(taskHisId, tableName);
			map.put(CodeUtil.RESULT_DATA, logs);
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

}
