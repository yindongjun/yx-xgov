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
import cn.com.yeexun.log.entity.SignInLog;
import cn.com.yeexun.log.service.ISignInService;
import cn.com.yeexun.utils.CodeUtil;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("sign_in_log")
public class SignInController extends BaseController<SignInLog> {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private ISignInService signInService;
	
	@Override
	public IBaseService<SignInLog> service() {
		return signInService;
	}
	
	/**
	 * 登录日志查询
	 * @param personnel 登录名
	 * @param startTime(signInTime) 开始时间
	 * @param endTime(signOutTime) 结束时间
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/search_action", method = RequestMethod.GET)
	public String searchSignInLog(String personnel, String startTime,
			String endTime, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = transferRequestParamToMap(request);
			Page<SignInLog> page = new Page<SignInLog>();
			page.setStart(MapUtils.getIntValue(paramMap, "start", 0));
			page.setLength(MapUtils.getIntValue(paramMap, "length", 10));
			List<SignInLog> list = signInService
					.searchSignInLogPage(personnel,startTime, endTime, page);
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

	