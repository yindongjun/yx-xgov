/**
 * @file PostHandleInterceptor.java
 * @date 2016年8月17日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.interceptor.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * 
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年8月17日 下午5:53:02
 */
public interface PostHandleInterceptor {

	public void postHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception;

}
