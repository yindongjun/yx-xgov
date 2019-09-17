/**
 * @file SuperPowerGene.java
 * @date 2016年8月11日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.bean.evolve;

/**
 * 
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年8月11日 下午7:08:27
 */
public interface SuperPowerGene {

	public String describe();

	public void evolve(Object o);
}
