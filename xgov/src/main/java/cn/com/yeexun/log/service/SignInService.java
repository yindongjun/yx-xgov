package cn.com.yeexun.log.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.log.dao.ISignInDao;
import cn.com.yeexun.log.entity.SignInLog;

/**
 * @author liming.xu
 * @version 2016年9月18日 17:52:02
 */
@Service
public class SignInService extends BaseService<SignInLog> implements ISignInService {

	@Autowired
	private ISignInDao signInDao;
	
	/**
	 * 查询登录日志
	 * @param personnel 用户名
	 * @param startTime 开始时间(登入时间)
	 * @param endTime 结束时间(登出时间)
	 * @param page 
	 * @return
	 */
	public List<SignInLog> searchSignInLogPage(String personnel, 
			String startTime, String endTime, Page<SignInLog> page) {
		return signInDao.searchSignInLogPage(personnel, startTime, endTime, page);
	}
	
}
