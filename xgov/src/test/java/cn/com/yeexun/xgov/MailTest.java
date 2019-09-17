package cn.com.yeexun.xgov;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.com.yeexun.mail.entity.MailConfig;
import cn.com.yeexun.mail.service.MailService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {
	@Autowired
	MailService mailService;
	
	MailConfig config;
	
	@Before
	public void before() {
		config = new MailConfig();
		config.setHost("smtp.163.com");
		config.setPort(994);
		config.setUsername("15686461821@163.com");
		config.setPassword("lj1047721797");
		config.setProtocol("smtp");
		config.setEnableSsl("true");
	}
	
	
	@Test
	public void testName() throws Exception {
		mailService.sendSampleMail(config, config.getUsername()
				, new String[]{"jin.huo11@yeexun.com.cn"}, "测试", "测试邮件aaaa");
	}
}
