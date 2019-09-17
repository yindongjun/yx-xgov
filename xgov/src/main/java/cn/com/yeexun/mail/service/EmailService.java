package cn.com.yeexun.mail.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name= "${xgov.serviceName.auth}")
public interface EmailService {
	
	public static final String DISPATCH_ALARM_SUBJECT = "【xgov】任务告警提醒";
	
	@RequestMapping(value = "mail/send",method = RequestMethod.POST)
	String send(@RequestParam(value= "toUserIds") String toUserIds
			, @RequestParam(value= "subject") String subject
			, @RequestParam(value= "content") String content
			, @RequestParam(value= "contentType") String contentType);
	
}
