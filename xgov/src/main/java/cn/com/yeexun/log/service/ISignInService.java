package cn.com.yeexun.log.service;

import java.util.List;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.log.entity.SignInLog;


public interface ISignInService extends IBaseService<SignInLog> {
	
	List<SignInLog> searchSignInLogPage(String personnel,
			String startTime, String endTime,
			Page<SignInLog> page) throws Exception;
	
}
