/**
 * @file MythNotice.java
 * @date 2016年8月11日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.bean.evolve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * “封神榜”
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年8月11日 下午6:13:47
 */
public class MythNotice {

	private static Map<Class<?>, List<SuperPowerGene>> notice = new HashMap<Class<?>, List<SuperPowerGene>>();

	public static void regist(Class<?> race, SuperPowerGene gene) {
		if (!notice.containsKey(race)) {
			notice.put(race, new ArrayList<SuperPowerGene>());
		}
		notice.get(gene).add(gene);// 暂时不管顺序
	}

	public static List<SuperPowerGene> obtainPower(Class<?> race) {
		return notice.get(race);
	}
}
