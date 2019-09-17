package cn.com.yeexun.user.service;

import org.springframework.stereotype.Component;

@Component
public class UserClientHystric implements UserClient {

	@Override
	public String getUserById(Long id) {
		return null;
	}

	@Override
	public String currentUser(String token) {
		return null;
	}

}
