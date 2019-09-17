package cn.com.yeexun.auth;

import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;


@Component
public class AuthInterceptor extends HandlerInterceptorAdapter{
	
	private final static String[] excludeUrl = {"/auth/verify","/xmonitorLogin","/login","/logout"};
	
	@Autowired
	private IAuthService authService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//判断是否为options请求，若是则直接通过
		if("OPTIONS".equalsIgnoreCase(request.getMethod())){
			return true;
		}
		String uri = request.getRequestURI();
		String token = request.getHeader("token");
		//如果是鉴权接口放行
		if(Arrays.asList(excludeUrl).contains(uri) || uri.startsWith("/outer")){
			return true;
		}else{
			AuthRequest authRequest = new AuthRequest();
			authRequest.setSessionId(token);
			authRequest.setUrl(uri);
			authRequest.setOs("xgov");
			String result = authService.verifyAuth(authRequest);
			AuthResponse res = JSONObject.parseObject(result,AuthResponse.class);
			if(!"200".equals(res.getCode())){
				response.setCharacterEncoding("utf-8");   
				response.setContentType("text/html; charset=gb2312");
				PrintWriter out = response.getWriter();
				JSONObject json = new JSONObject();
				json.put("code", res.getCode());
				json.put("message", res.getMessage());
				out.write(json.toString());
				out.flush();
				return false;
			}
		}

		return true;
	}

}