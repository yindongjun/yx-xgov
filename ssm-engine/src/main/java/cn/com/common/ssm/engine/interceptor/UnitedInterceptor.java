/**
 * @file UnitedInterceptor.java
 * @date 2016年8月17日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.com.common.ssm.engine.interceptor.api.AfterCompletionInterceptor;
import cn.com.common.ssm.engine.interceptor.api.PostHandleInterceptor;
import cn.com.common.ssm.engine.interceptor.api.PreHandleInterceptor;

/**
 * springmvc 资源请求拦截器统一入口
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年8月17日 下午5:19:16
 */
public class UnitedInterceptor implements HandlerInterceptor {
	private final Logger logger = Logger.getLogger(getClass());

	public final static String INTERCEPTOR_LIST = "interceptor_list";

	private static Map<String, List<Object>> interceptorMap = new HashMap<String, List<Object>>();

	public static void regist(String regex, Object interceptor) {
		if (interceptorMap != null) {
			if (!interceptorMap.containsKey(regex)) {
				interceptorMap.put(regex, new ArrayList<Object>());
			}
			interceptorMap.get(regex).add(interceptor);
		}

	}

	@SuppressWarnings("unchecked")
	private List<Object> getInterceptors(HttpServletRequest request) {

		// postHandle、afterCompletion 可以直接取 preHandle取出的interceptors避免重复执行
		Object param = request.getAttribute(INTERCEPTOR_LIST);
		if (param != null) {
			return (ArrayList<Object>) param;
		}

		List<Object> interceptServices = new ArrayList<Object>();

		String key = request.getServletPath();

		if (interceptorMap != null) {
			for (Map.Entry<String, List<Object>> entry : interceptorMap.entrySet()) {
				String regExpKey = entry.getKey();
				Pattern pattern = Pattern.compile(regExpKey);
				Matcher m = pattern.matcher(key);
				if (m.matches()) {
					interceptServices.addAll(entry.getValue());
				}
			}
		}
		return interceptServices;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		List<Object> interceptorList = getInterceptors(request);
		request.setAttribute(INTERCEPTOR_LIST, interceptorList);
		for (Object interceptorObject : interceptorList) {
			try {
				if (interceptorObject instanceof PreHandleInterceptor) {
					if (!((PreHandleInterceptor) interceptorObject).preHandle(request, response)) {
						return false;
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		List<Object> interceptorList = getInterceptors(request);
		for (Object interceptorObject : interceptorList) {
			try {
				if (interceptorObject instanceof PostHandleInterceptor) {
					((PostHandleInterceptor) interceptorObject).postHandle(request, response, modelAndView);
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		List<Object> interceptorList = getInterceptors(request);
		request.removeAttribute(INTERCEPTOR_LIST);
		for (Object interceptorObject : interceptorList) {
			try {
				if (interceptorObject instanceof AfterCompletionInterceptor) {
					((AfterCompletionInterceptor) interceptorObject).afterCompletion(request, response, ex);
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

}
