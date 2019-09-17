/**
 * @file BaseController.java
 * @date 2016年4月9日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * ssm框架通用controller
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年4月9日 上午10:38:42
 * @since yeexun
 */

public abstract class CommonController {

	protected ModelAndView transferRequestParamToMdv(HttpServletRequest request) {
		return transferRequestParamToMdv(new ModelAndView(), request);
	}

	protected ModelAndView transferRequestParamToMdv(ModelAndView mav, HttpServletRequest request) {
		if (request != null && MapUtils.isNotEmpty(request.getParameterMap())) {
			for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
				if (entry.getValue() != null && entry.getValue().length > 1) {
					mav.addObject(entry.getKey(), request.getParameterValues(entry.getKey()));
				} else {
					mav.addObject(entry.getKey(), request.getParameter(entry.getKey()));
				}
			}
		}
		return mav;
	}
	
	/**
	 * 将请求参数转成map
	 * @param request
	 * @return
	 */
	protected Map<String, Object> transferRequestParamToMap(HttpServletRequest request) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if (request != null && MapUtils.isNotEmpty(request.getParameterMap())) {
			for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
				if (entry.getValue() != null && entry.getValue().length > 1) {
					paramMap.put(entry.getKey(), request.getParameterValues(entry.getKey()));
				} else {
					paramMap.put(entry.getKey(), request.getParameter(entry.getKey()));
				}
			}
		}
		return paramMap;
	}

}
