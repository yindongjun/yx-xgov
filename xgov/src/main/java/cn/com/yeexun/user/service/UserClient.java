package cn.com.yeexun.user.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.yeexun.dataSource.service.impl.DatasourceClientHystric;

@FeignClient(name= "${xgov.serviceName.auth}", fallback = UserClientHystric.class)
public interface UserClient {
	
	@RequestMapping(value = "user/getById",method = RequestMethod.GET)
	String getUserById(@RequestParam(value= "id") Long id);
	
	/**
     * 通过token获取当前user
     * @param token
     * @return
     */
	@RequestMapping(value = "/outer/getUserByToken",method = RequestMethod.POST)
	String currentUser(@RequestParam(value = "token") String token);
	
}
