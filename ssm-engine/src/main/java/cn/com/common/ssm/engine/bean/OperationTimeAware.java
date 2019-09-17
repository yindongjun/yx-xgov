/**
 * @file OperationTimeAware.java
 * @date 2016年7月13日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.bean;

import java.util.Date;

/**
 * 时间操作感应器
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年7月13日 上午10:56:02
 */
public interface OperationTimeAware {

	void setCreateTime(Date createTime);

	void setLastModifyTime(Date lastModifyTime);
}
