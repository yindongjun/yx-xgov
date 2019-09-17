package cn.com.yeexun.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name= "${xgov.serviceName.auth}")
public interface IAuthService {

	
	@RequestMapping(value = "/auth/verify",method = RequestMethod.POST)
	public String verifyAuth(@RequestParam(value= "authRequest") AuthRequest authRequest);
	
}
