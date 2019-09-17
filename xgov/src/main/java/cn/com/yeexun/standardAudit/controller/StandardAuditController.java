package cn.com.yeexun.standardAudit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.common.controller.BaseController;
import cn.com.yeexun.standardAudit.entity.AuditDetail;
import cn.com.yeexun.standardAudit.entity.StandardAudit;
import cn.com.yeexun.standardAudit.service.IAuditDetailService;
import cn.com.yeexun.standardAudit.service.IStandardAuditService;
import cn.com.yeexun.user.entity.UserDto;
import cn.com.yeexun.user.service.UserService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("standard_audit")
public class StandardAuditController extends BaseController<StandardAudit> {
	
	private Logger logger = Logger.getLogger(StandardAuditController.class);
	
	@Autowired
	private IStandardAuditService standardAuditService;
	@Autowired
	private IAuditDetailService auditDetailService;
	
	@Autowired
	private UserService userService;

	@ResponseBody
	@RequestMapping(value = "/listAll", method = RequestMethod.POST)
	@ApiOperation("查询标准审核记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "start", value = "分页起始记录", required = true, dataType = "int"),
			@ApiImplicitParam(name = "length", value = "每页条数", required = true, dataType = "int"),
			@ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "String"),
			@ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String"),
			@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest") })
	public String listAll(int start,int length,String status,String auditresult,String type,
			String taskName, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		Page<StandardAudit> page = new Page<StandardAudit>();
		Map<String, Object> paramMap = transferRequestParamToMap(request);
		page.setStart(MapUtils.getIntValue(paramMap, "start", start));
		page.setLength(MapUtils.getIntValue(paramMap, "length", length));
		
		try {
			List<StandardAudit> list = standardAuditService.listStandardAuditPage(status,auditresult,type,taskName,page);
			map.put(CodeUtil.RESULT_DATA, list);
			map.put("code",CodeUtil.CODE_SUCCESS);
			map.put("message", CodeUtil.CODE_SUCCESS_MESSAGE);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/changeDetail", method = RequestMethod.POST)
	@ApiOperation("审核，可以批量审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "要审核的id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "type", value = "审核对象的类型", required = true, dataType = "String")
	})
	public String changeDetail(Long id, String type){
		
		Map<String, Object> map = null;
		
		try {
			StandardAudit standardAudit = standardAuditService.changeDetail(id, type);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, standardAudit);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/updataAuditStatus", method = RequestMethod.POST)
	@ApiOperation("审核，可以批量审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "要审核的id，多个之间用英文逗号分隔", required = true, dataType = "String"),
		@ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "String"),
		@ApiImplicitParam(name = "comment", value = "描述", required = true, dataType = "String")
	})
	public String updataAuditStatus(String ids, String status, String comment, HttpServletRequest request){
		
		Map<String, Object> map = null;
		if (StringUtils.isBlank(ids)) {
			return JSON.toJSONString(CodeUtil.getBadRequestMap());
		}
		String[] item = ids.split(",");
		List<Long> list = new ArrayList<Long>(item.length);
		for (String idStr : item) {
			Long id = Long.valueOf(idStr);
			list.add(id);
		}
		try {
			String token = request.getHeader("token");
			UserDto currentUser = userService.currentUser(token);
			standardAuditService.updataAuditStatus(list,status,comment, currentUser);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
		
	}
	@ResponseBody
	@RequestMapping(value = "/listAuditDetail", method = RequestMethod.GET)
	@ApiOperation("查询标准审核记录详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "start", value = "分页起始记录", required = true, dataType = "int"),
			@ApiImplicitParam(name = "length", value = "每页条数", required = true, dataType = "int"),
			@ApiImplicitParam(name = "id", value = "记录id", required = true, dataType = "Long"),
			@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest") })
	public String listAuditDetail(int start,int length,Long id,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		Page<AuditDetail> page = new Page<AuditDetail>();
		Map<String, Object> paramMap = transferRequestParamToMap(request);
		page.setStart(MapUtils.getIntValue(paramMap, "start", start));
		page.setLength(MapUtils.getIntValue(paramMap, "length", length));
		try {
			List<AuditDetail> list = auditDetailService.listAuditDetailPage(id, page);
			map.put(CodeUtil.RESULT_DATA, list);
			map.put("code",CodeUtil.CODE_SUCCESS);
			map.put("message", CodeUtil.CODE_SUCCESS_MESSAGE);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	@Override
	public IBaseService<StandardAudit> service() {
		return standardAuditService;
	}

}
