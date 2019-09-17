package cn.com.yeexun.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {
	
	@Bean
	public SchedulerFactoryBean quartzScheduler() throws IOException {
		
		SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
//		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
//		propertiesFactoryBean.afterPropertiesSet();
//		Properties properties = propertiesFactoryBean.getObject();
		Properties properties = new Properties();
		properties.load(QuartzConfig.class.getResourceAsStream("/quartz.properties"));
		quartzScheduler.setQuartzProperties(properties);
		return quartzScheduler;
		
	}

}
