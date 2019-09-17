package cn.com.yeexun.dispatch.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
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
import cn.com.yeexun.dispatch.entity.DispatchAlarm;
import cn.com.yeexun.dispatch.service.IDispatchAlarmService;
import cn.com.yeexun.dispatch.service.impl.DispatchAlarmService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("dispatch_alarm")
@Api("报警配置")
public class DispatchAlarmController extends BaseController<DispatchAlarm> {
	
	private Logger logger = Logger.getLogger(DispatchAlarmController.class);
	
	@Autowired
	private DispatchAlarmService dispatchAlarmService;
	

	@ResponseBody
	@RequestMapping(value="/queryAlarm",method=RequestMethod.POST)
	@ApiOperation("获取所有报警配置")
	public String queryAlarm(String alarmName, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			Map<String, Object> paramMap = transferRequestParamToMap(request);
			Page<DispatchAlarm> page = new Page<DispatchAlarm>();
			page.setStart(MapUtils.getIntValue(paramMap, "start", 0));
			page.setLength(MapUtils.getIntValue(paramMap, "length", 10));
			List<DispatchAlarm> list = dispatchAlarmService.getAllAlarm(page, alarmName);
			map=CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA,list);
//			map.put(CodeUtil.RESULT_DRAW, request.getParameter("draw"));
//			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		}catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	

	@ResponseBody
	@RequestMapping(value="/getAlarmById",method=RequestMethod.GET)
	@ApiOperation("获取单个报警配置")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "alarmId" ,value = "配置id" , dataType = "Long")
			)
	public String getAlarmById(Long id) {
		Map<String ,Object> map = new HashMap<String ,Object>();
		try {
			DispatchAlarm dispatchAlarm = dispatchAlarmService.getById(id);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA,dispatchAlarm);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value= "/deleteAlarm",method=RequestMethod.GET)
	@ApiOperation("删除报警配置")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id" ,value = "删除时的id集合" , dataType = "Long")
	})
	public String deleteAlarm(String id) {
		String[] idS = id.split(",");
		List<Long> ids = new ArrayList<Long>();
		for (String string : idS) {
			ids.add(Long.valueOf(string));
		}
		Map<String ,Object> map = new HashMap<String ,Object>();
		try {
			int status = dispatchAlarmService.delete(ids);
			if(status>0) {
				map=CodeUtil.getSuccessMap();
			}
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value="/updateAlarm", method = RequestMethod.POST)
	@ApiOperation("添加更新报警配置") //recive_people用逗号隔开
	@ApiImplicitParams({
			@ApiImplicitParam(name="alarmId" ,value="报警Id"),
			@ApiImplicitParam(name="alarmName" ,value="报警名字"),
			@ApiImplicitParam(name="alarmReasion" ,value="报警原因"),
			@ApiImplicitParam(name="alarmMethod" ,value="报警方法"),
			@ApiImplicitParam(name="receivePeople" ,value="接收人ID,用逗号隔开"),
			@ApiImplicitParam(name="status" ,value="报警状态：0-关、1-开"),
			@ApiImplicitParam(name="isUsed" ,value="是否被引用：0-关、1-开"),
			@ApiImplicitParam(name="createPeople" ,value="创建人"),
			@ApiImplicitParam(name="updatePeople" ,value="修改人")
	})
	public String updateAlarm(Long alarmId ,String alarmName, String alarmReasion, String alarmMethod, String receivePeople,
			Integer status, Integer isUsed) {
		Map<String ,Object> map = new HashMap<String ,Object>();
		
/*//		Integer reasion = 0;
//		Integer method = 0;
		Date createTime = new Date();
		Date updateTime = createTime;
		//获取创建人id
		Long createPeople = 1L;
		Long updatePeople = createPeople;*/
		try {
			dispatchAlarmService.saveOrUpdate(alarmId ,alarmName,  alarmReasion,  alarmMethod,  receivePeople,
					 status,  isUsed);
			map=CodeUtil.getSuccessMap();
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
	@RequestMapping(value="/verifyOpen" ,method=RequestMethod.GET)
	@ApiOperation("检查报警配置是否被引用，在改变报警配置时调用。如果状态可以被改变则返回true，如果修改状态需要用户确定则返回false")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "alarmId" ,value = "报警配置的Id" ),
		@ApiImplicitParam(name = "isOpen" ,value = "是否是开启的操作，是：true，不是：false" ,dataType="Boolean")
	})
	public String verifyOpen(String alarmId,Boolean isOpen) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//状态从false变为true前判断是否被引用。
			if(!isOpen) {
				int alarm = dispatchAlarmService.verifyOpen(Long.valueOf(alarmId));
				if(alarm != 0 && alarm >0){
					map = CodeUtil.getSuccessMap();
					map.put("status",false);
				}else if(alarm == 0) {
					map = CodeUtil.getSuccessMap();
					map.put("status", true);
				}
			}else if(isOpen) {
				map = CodeUtil.getSuccessMap();
				map.put("status", true);
			}else {
				map = CodeUtil.getSystemErrorMap();
			}
			
			
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}

	
	@ResponseBody
	@RequestMapping(value="/open" ,method=RequestMethod.GET)
	@ApiOperation("打开或关闭报警配置")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "alarmId" ,value = "报警配置Id",required = true ,dataType="String"),
		@ApiImplicitParam(name = "isOpen" ,value = "报警配置Id",required = true ,dataType="Boolean")
	})
	public String open(String alarmId,Boolean isOpen) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//状态从false变为true前判断是否被引用。
			if(!isOpen) {
				boolean result = dispatchAlarmService.changeStatus(Long.valueOf(alarmId), 0);
				map = CodeUtil.getSuccessMap();
				map.put("status", true);
			}else if(isOpen) {
				boolean result =dispatchAlarmService.changeStatus(Long.valueOf(alarmId), 1);
				map = CodeUtil.getSuccessMap();
				map.put("status", true);
			}else {
				map = CodeUtil.getSystemErrorMap();
				map.put("status", false);
			}
			
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	@Override
	public IBaseService<DispatchAlarm> service() {
		return null;
	}
	
}
