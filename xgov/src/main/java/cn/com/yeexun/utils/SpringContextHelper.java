package cn.com.yeexun.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHelper implements ApplicationContextAware{
	private static ApplicationContext context = null;  
	  
    @Override  
    public void setApplicationContext(ApplicationContext applicationContext)  
            throws BeansException {  
        context = applicationContext;  
    }  
      
    public static Object getBean(String name){  
        return context.getBean(name);  
    }
    
    public static Object getBean(Class cls){
    	return context.getBean(cls);
    }
}
