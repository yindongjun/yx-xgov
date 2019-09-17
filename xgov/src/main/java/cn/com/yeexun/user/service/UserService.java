package cn.com.yeexun.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.user.entity.UserDto;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.FeignException;
import cn.com.yeexun.utils.ResponseUtil;

@Service
public class UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserClient userClient;

	public UserDto currentUser(String token) {
		String userResp = userClient.currentUser(token);
		if (null == userResp) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
		}
		if (ResponseUtil.isError(userResp)) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
		}
		UserDto currentUser = ResponseUtil.getObjectData(userResp, UserDto.class);
		return currentUser;
	}

}
