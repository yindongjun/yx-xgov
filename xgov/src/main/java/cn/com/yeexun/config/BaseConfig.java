package cn.com.yeexun.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import cn.com.yeexun.utils.PropertyPlaceholder;
import cn.com.yeexun.utils.SpringContextHelper;

@Configuration
public class BaseConfig {
	
//	@Value("${env}")
//	String env;
	
	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(10);
		threadPoolTaskExecutor.setMaxPoolSize(20);
		threadPoolTaskExecutor.setQueueCapacity(200);
		threadPoolTaskExecutor.setKeepAliveSeconds(300);
		threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return threadPoolTaskExecutor;
	}
	
	/*@Bean
	public EntityHolder entityHolder() {
		EntityHolder entityHolder = new EntityHolder();
		entityHolder.setBasePackage("cn.com.**.entity");
		return entityHolder;
	}*/
	
	@Bean
	public SpringContextHelper springContextHelper() {
		return new SpringContextHelper();
	}
	
	@Bean
	public PropertyPlaceholder property() {
		PropertyPlaceholder property = new PropertyPlaceholder();
		property.setIgnoreResourceNotFound(true);
		property.setIgnoreUnresolvablePlaceholders(true);
		property.setLocations(new ClassPathResource("/dev/config.properties")
				/*, new ClassPathResource("/dev/jdbc.properties")*/);
		return property;
	}
	
	/*@Bean
	public PropertyPlaceholderConfigurer propertyConfigurer() {
		PropertyPlaceholderConfigurer propertyConfigurer = new PropertyPlaceholderConfigurer();
		propertyConfigurer.setIgnoreUnresolvablePlaceholders(true);
		propertyConfigurer.setLocations(new ClassPathResource("/dev/config.properties")
				, new ClassPathResource("/dev/jdbc.properties")
				, new ClassPathResource("/dev/redis.properties"));
		return propertyConfigurer;
	}*/

}
