package cn.com.yeexun.qualityreport.controller;

import java.util.Date;
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
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.service.IDesignTableInfoService;
import cn.com.yeexun.qualityreport.entity.QualityGradeEntity;
import cn.com.yeexun.qualityreport.entity.QualityMeasureEntity;
import cn.com.yeexun.qualityreport.entity.QualityReportEntity;
import cn.com.yeexun.qualityreport.entity.QualityReportTemplateEntity;
import cn.com.yeexun.qualityreport.service.IQualityReportService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/quality-report")
@Api("质量报告")
public class QualityReportController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityReportController.class);
	
	@Autowired
	private IDesignTableInfoService tableInfoService;
	
	@Autowired
	private IQualityReportService reportService;
	
	@ResponseBody
	@RequestMapping(value = "test1", method = RequestMethod.POST)
	public String test1() {
		return "你好，世界！";
	}
	@RequestMapping(value = "test2", method = RequestMethod.GET)
	@ResponseBody
	public String test2() {
		return "你好，java！";
	}
	
	@ResponseBody
	@RequestMapping(value = "/showReportTemplte", method = RequestMethod.POST)
	@ApiOperation("创建报告模板")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "grades", value = "元模型的id", required = true, dataType = "array", paramType = "body")
	})
	public String showReportTemplte(Long templateId) {
		
		Map<String, Object> result = CodeUtil.getSuccessMap();
		try {
			QualityReportTemplateEntity template = reportService.showReportTemplte(templateId);
			result.put(CodeUtil.RESULT_DATA, template);
		} catch (Exception e) {
			LOGGER.warn("查询质量报告模板时异常：", e);
			result = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(result);
	}
	
	@ResponseBody
	@RequestMapping(value = "/addReportTemplte", method = RequestMethod.POST)
	@ApiOperation("创建报告模板")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "grades", value = "元模型的id", required = true, dataType = "array", paramType = "body")
	})
	public String addReportTemplte(String grades, String measureIndexs, String exportModules) {
		Map<String, Object> result = CodeUtil.getSuccessMap();
		try {
			List<QualityGradeEntity> gradesList = JSON.parseArray(grades, QualityGradeEntity.class);
			List<QualityMeasureEntity> measureIndexsList = JSON.parseArray(measureIndexs, QualityMeasureEntity.class);
			reportService.addReportTemplte(gradesList, measureIndexsList, exportModules);
		} catch (Exception e) {
			LOGGER.warn("查询生成质量报告的表异常：", e);
			result = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "createReport", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("生成质量报告。")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tableInfoIds", value = "多个tableInfoId，英文逗号分隔", required = true, dataType = "string", paramType = "body")
	})
	public String createReport(String tableInfoIds, String name) {
		
		Map<String, Object> result = CodeUtil.getSuccessMap();
		try {
			QualityReportEntity report = reportService.createQualityReport(tableInfoIds, name);
			result.put(CodeUtil.RESULT_DATA, report);
		} catch (CommonException e) {
			LOGGER.warn("查询生成质量报告的表异常：", e);
			result = CodeUtil.getSystemErrorMap();
			result.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			LOGGER.warn("查询生成质量报告的表异常：", e);
			result = CodeUtil.getSystemErrorMap();
		}
		
		return JSON.toJSONString(result);
		
	}
	
	@RequestMapping(value = "listReports", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("生成质量报告。")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tableInfoIds", value = "多个tableInfoId，英文逗号分隔", required = true, dataType = "string", paramType = "body")
	})
	public String listReports(Integer start, Integer length, Long startDate, Long endDate) {
		
		Page<DesignTableInfo> page = new Page<>();
		page.setStart(start);
		page.setLength(length);
		
		Map<String, Object> result = CodeUtil.getSuccessMap();
		try {
			List<QualityReportEntity> reports = reportService.listReports(page
					, startDate == null ? null : new Date(startDate)
					, endDate == null ? null : new Date(endDate));
			result.put(CodeUtil.RESULT_DATA, reports);
			result.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			LOGGER.warn("查询生成质量报告的表异常：", e);
			result = CodeUtil.getSystemErrorMap();
		}
		
		return JSON.toJSONString(result);
		
	}
	
	@RequestMapping(value = "listTables", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("展示所有执行过校验任务，可以生成报告的designTableInfo记录")
	public String listTables() {
		Map<String, Object> result = CodeUtil.getSuccessMap();
		try {
			List<Map<String, Object>> reportTables = tableInfoService.listReportTables();
			result.put(CodeUtil.RESULT_DATA, reportTables);
		} catch (Exception e) {
			LOGGER.warn("查询生成质量报告的表异常：", e);
			result = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "showReport", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation("列表显示所有可以生成报告的表，树形展示")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "grades", value = "元模型的id", required = true, dataType = "array", paramType = "body")
	})
	public String showReport(Long reportId) {
		
		Map<String, Object> result = CodeUtil.getSuccessMap();
		
		try {
			QualityReportEntity report = reportService.showReport(reportId);
			result.put(CodeUtil.RESULT_DATA, report);
		} catch (Exception e) {
			LOGGER.warn("查询生成质量报告的表异常：", e);
			result = CodeUtil.getSystemErrorMap();
		}
		
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "deleteReport", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("删除报告列表，支持批量")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "reportIds", value = "reportIds 英文逗号分隔", required = true, dataType = "string", paramType = "body")
	})
	public String deleteReport(String reportIds) {
		
		Map<String, Object> result = CodeUtil.getSuccessMap();
		
		try {
			reportService.deleteReport(reportIds);
		} catch (Exception e) {
			LOGGER.warn("查询生成质量报告的表异常：", e);
			result = CodeUtil.getSystemErrorMap();
		}
		
		return JSON.toJSONString(result);
	}
	
	

}
