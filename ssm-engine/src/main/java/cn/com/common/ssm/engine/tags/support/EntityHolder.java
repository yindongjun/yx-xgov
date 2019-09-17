package cn.com.common.ssm.engine.tags.support;

import static org.springframework.util.Assert.notNull;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Table;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

public class EntityHolder implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

	private static Map<String, Class<?>> entityMap = new HashMap<String, Class<?>>();

	private String basePackage = "cn.com.**.entity";

	private ApplicationContext applicationContext;

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void afterPropertiesSet() throws Exception {
		notNull(this.basePackage, "Property 'basePackage' is required");
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		// left intentionally blank
	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

		EntityScanner scanner = new EntityScanner(registry);
		scanner.setAnnotationClass(Table.class);
		scanner.setResourceLoader(this.applicationContext);
		scanner.registerFilters();
		scanner.doScan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}

	public static void registEntity(String simpleBeanName, Class<?> clazz) {
		entityMap.put(simpleBeanName, clazz);
	}

	public static Class<?> getEntityClass(String simpleBeanName) {
		return entityMap.get(simpleBeanName);
	}

}
