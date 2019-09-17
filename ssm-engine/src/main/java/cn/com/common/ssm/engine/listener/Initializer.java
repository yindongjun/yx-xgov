/**
 * @file Initializer.java
 * @date 2016年8月10日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.listener;

import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年8月10日 上午11:39:56
 */

public interface Initializer {

	public void init();

	public void init(ContextRefreshedEvent event);

}
