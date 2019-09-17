package cn.com.yeexun.utils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class ThymeleafUtil {
	
	@Autowired
    private TemplateEngine templateEngine;
	
	public String processTemplate(String template, Map<String, Object> valueMap) {
		
		Context context = new Context();
		context.setVariables(valueMap);
		String content = templateEngine.process(template, context);
		return content;
	}

}
