/**
 * @file PreHandleInterceptor.java
 * @date 2016年8月17日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.interceptor.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年8月17日 下午5:53:02
 */
public interface PreHandleInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
