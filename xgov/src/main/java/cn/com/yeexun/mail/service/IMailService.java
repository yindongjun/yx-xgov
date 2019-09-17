package cn.com.yeexun.mail.service;

import java.util.Map;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.mail.entity.MailConfig;
import cn.com.yeexun.user.entity.UserDto;

public interface IMailService extends IBaseService<MailConfig> {

	void sendMailUseHtmlTemplate(MailConfig config, String from, String[] toMails, String subject, String tempate,
			Map<String, Object> valueMap) throws Exception;


	void sendSampleMail(MailConfig config, String from, String[] toMails, String subject, String conten)
			throws Exception;


	void testConnect(MailConfig config, UserDto currentUser);

}
