package cn.com.common.ssm.engine.helper;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

@Service
public class SpringContextHolder implements ApplicationContextAware, ServletContextAware {

	private static ApplicationContext context;

	private static ServletContext servletContext;

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringContextHolder.context = context;

	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		SpringContextHolder.servletContext = servletContext;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

}