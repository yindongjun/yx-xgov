package cn.com.yeexun.log.controller;

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

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.common.controller.BaseController;
import cn.com.yeexun.log.entity.OperationLog;
import cn.com.yeexun.log.service.IOperationService;
import cn.com.yeexun.utils.CodeUtil;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("operation_log")
public class OperationController extends BaseController<OperationLog> {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private IOperationService operationService;
	
	@Override
	public IBaseService<OperationLog> service() {
		return operationService;
	}
	
	/**
	 * 查询操作日志
	 * @param personnel 操作人
	 * @param startTime(operationTime) 开始时间
	 * @param endTime(operationTime) 结束时间
	 * @param operationMode 操作模块
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/search_action", method = RequestMethod.GET)
	public String searchOperationlog(String personnel, String operationMode,
			String startTime, String endTime, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = transferRequestParamToMap(request);
			Page<OperationLog> page = new Page<OperationLog>();
			page.setStart(MapUtils.getIntValue(paramMap, "start", 0));
			page.setLength(MapUtils.getIntValue(paramMap, "length", 10));
			List<OperationLog> list = operationService.searchOperationLogPage(personnel, 
					operationMode, startTime, endTime, page);
			page.setData(list);
			map = CodeUtil.getSuccessMap();
			map.put("data", list);
			map.put("draw", request.getParameter("draw"));
			map.put("recordsFiltered", page.getCount());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
}
