package cn.com.yeexun.mail.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.yeexun.mail.entity.MailConfig;
import cn.com.yeexun.mail.service.IMailService;
import cn.com.yeexun.user.entity.UserDto;
import cn.com.yeexun.user.service.UserClient;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.ResponseUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("mail")
public class MailController {
	
	private static final Logger logger = LoggerFactory.getLogger(MailController.class);
	
	@Autowired
	private IMailService mailService;
	
	@ResponseBody
	@RequestMapping(value = "/testConnect", method = RequestMethod.POST)
	@ApiOperation("")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "", value = "分页起始记录", required = true, dataType = "int")
	})
	public String testConnect(MailConfig config, UserDto currentUser){
		Map<String, Object> map = null;
		try {
			mailService.testConnect(config, currentUser);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			logger.warn("配置的邮箱服务器异常：", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation("")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "", value = "分页起始记录", required = true, dataType = "int")
	})
	public String save(MailConfig config){
		Map<String, Object> map = null;
		try {
			mailService.save(config);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
		
	}

}
