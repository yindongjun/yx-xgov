/**
 * @file Variation.java
 * @date 2016年8月11日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.bean.evolve;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

/**
 * 
 * TODO <br/>
 * “封神”系统暂时搁浅,未解决的问题：无法通过代理或其他方式无污染地扩展 entity的get、set方法
 * 
 * @author yong.zhou
 * @version 1.0.0, 2016年8月11日 下午5:27:04
 */
public abstract class VariationBean implements Serializable {

	protected boolean evolvable = false;

	private final Logger logger = Logger.getLogger(getClass());

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4541201423157716192L;

	public VariationBean() {
		if (evolvable) {
			List<SuperPowerGene> mySuperPowers = MythNotice.obtainPower(this.getClass());
			if (CollectionUtils.isNotEmpty(mySuperPowers)) {
				for (SuperPowerGene superPowerGene : mySuperPowers) {
					if (logger.isDebugEnabled()) {
						logger.info("class【" + this.getClass() + "】开始加载扩展功能：【" + superPowerGene.describe() + "【" + superPowerGene.getClass() + "】");
					}
					superPowerGene.evolve(this);
				}
			}
		}
	}
}
