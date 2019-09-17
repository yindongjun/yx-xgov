package cn.com.yeexun.mail.service;

import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.mail.entity.MailConfig;
import cn.com.yeexun.user.entity.UserDto;
import cn.com.yeexun.utils.CommonException;

@Service
public class MailService extends BaseService<MailConfig> implements IMailService {
	
	@Autowired
    private TemplateEngine templateEngine;
	
//	@Autowired
//	private MailConfigDao mailConfigDao;
	
	@Override
	public void testConnect(MailConfig config, UserDto currentUser) {
		try {
			sendSampleMail(config, config.getUsername(), new String[]{currentUser.getEmail()}
					, "测试邮件", "测试连通性");
		} catch (Exception e) {
			throw new CommonException("无法连接！");
		}
	}
	
	@Override
	public void sendMailUseHtmlTemplate(MailConfig config, String from, String[] toMails
			, String subject, String tempate, Map<String, Object> valueMap) throws Exception {
		
		Context context = new Context();
		context.setVariables(valueMap);
		String content = templateEngine.process(tempate, context);
		Session session = getSession(config);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		InternetAddress[] toAddrs = toInternetAddresses(toMails);
		message.setRecipients(Message.RecipientType.TO, toAddrs);
		message.setSubject(subject);
		message.setContent(content, "text/html;charset=utf-8");
		Transport transport = session.getTransport();
		transport.connect(config.getUsername(), config.getPassword());
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
	
	@Override
	public void sendSampleMail(MailConfig config, String from, String[] toMails
			, String subject, String text) throws Exception {
		
		Session session = getSession(config);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		InternetAddress[] toAddrs = toInternetAddresses(toMails);
		message.setRecipients(Message.RecipientType.TO, toAddrs);
		message.setSubject(subject);
		message.setText(text);
		Transport transport = session.getTransport();
		transport.connect(config.getUsername(), config.getPassword());
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
	
	private Session getSession(MailConfig config) {
		Properties properties = new Properties();
		properties.put("mail.transport.protocol", config.getProtocol());
		properties.put("mail.smtp.host", config.getHost());
		properties.put("mail.smtp.port", config.getPort());
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", config.getEnableSsl());
		properties.put("mail.debug", "true");
		Session session = Session.getInstance(properties);
		return session;
	}
	
	private InternetAddress[] toInternetAddresses(String[] stringAddres) throws AddressException {
		InternetAddress[] addrs = new InternetAddress[stringAddres.length];
		for (int i = 0; i < stringAddres.length; i++) {
			addrs[i] = new InternetAddress(stringAddres[i]);
		}
		return addrs;
	}
	

}
