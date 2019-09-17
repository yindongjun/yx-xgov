package cn.com.common.ssm.engine.listener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * 
 * spring上下文加载完成后的 初始化处理的统一入口
 * 
 * @author yong.zhou
 * @version 1.0.0, 2016年8月10日 上午11:38:06
 */
@Service
public class InitAfterContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

	// TODO 顺序问题
	public static List<Initializer> initializerList = new ArrayList<Initializer>();

	public static void regist(Initializer Initializer) {
		initializerList.add(Initializer);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (event.getApplicationContext().getParent() == null) {// 根context加载完成
			for (Initializer initializer : initializerList) {
				initializer.init();
				initializer.init(event);
			}
		}
		if (event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")) {
		}
	}
}