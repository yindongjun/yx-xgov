package cn.com.yeexun.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCORSFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCORSFilter.class);
    private boolean isCross = false;

    @Override
    public void destroy() {
        isCross = false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (isCross) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            LOGGER.debug("拦截请求: " + httpServletRequest.getServletPath());
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE");
            httpServletResponse.setHeader("Access-Control-Max-Age", "86400");
            httpServletResponse.setHeader("Access-Control-Allow-Headers",
                    "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,Authorization,token");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.setHeader("XDomainRequestAllowed", "1");
            httpServletResponse.setHeader("Content-Type", "application/x-www-form-urlencoded");
            if (httpServletRequest.getMethod().equals("OPTIONS")) {
              httpServletResponse.setStatus(200);
              return;
          }

        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String isCrossStr = filterConfig.getInitParameter("IsCross");
        isCross = isCrossStr.equals("true") ? true : false;
//        System.out.println(isCrossStr);
    }

}
