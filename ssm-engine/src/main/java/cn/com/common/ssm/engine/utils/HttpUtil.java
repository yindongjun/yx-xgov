package cn.com.common.ssm.engine.utils;


import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;


public class HttpUtil {
    
    public static String doGet(String url) {
            HttpGet httpRequest = new HttpGet(url);
            String strResult = null;
            try {
                    HttpClient httpClient = getHttpClient();
                    HttpResponse httpResponse = httpClient.execute(httpRequest);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            strResult = EntityUtils.toString(httpResponse.getEntity());
                    } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                            strResult = "error";
                    } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
                            strResult = "error";
                    } else {
                            strResult = "error";
                    }
            } catch (Exception e) {
                  e.printStackTrace();  
            }
            return strResult;
    }
        
                
        public static HttpClient getHttpClient() {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams,
                                20* 1000);
                HttpConnectionParams.setSoTimeout(httpParams,
                                20* 1000);
                HttpConnectionParams.setSocketBufferSize(httpParams,
                                4096);
                HttpClientParams.setRedirecting(httpParams, true);
                return new DefaultHttpClient(httpParams);
        }
        
        /**
         * 把数据post到服务器，获取返回的结果
         * 
         * @param uriAPI
         *            ： API 所在的路径
         * @param params
         *            ： 传递的数据
         */
        public static String postToServer(String uriAPI, List<NameValuePair> params) {
                /* 建立HTTPost对象 */
                HttpPost httpRequest = new HttpPost(uriAPI);
                try {
                        /* 添加请求参数到请求对象 */
                        httpRequest.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
                        /* 发送请求并等待响应 */
                        HttpResponse httpResponse = new DefaultHttpClient()
                                        .execute(httpRequest);
                        /* 若状态码为200 ok */
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                /* 读返回数据 */
                                return EntityUtils.toString(httpResponse.getEntity());

                        } else {
                                return "Error Response: "
                                                + httpResponse.getStatusLine().toString();
                        }
                } catch (Exception e) {
                        return "post failure :caused by-->" + e.getMessage().toString();
                }

        }
        
        /**
    	 * 获取request用户 IP
    	 * @param request
    	 * @return
    	 */
    	public static String getIpAddr(HttpServletRequest request) { 
    		String ip = request.getHeader("x-forwarded-for"); 
    		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
    		   ip = request.getRemoteAddr(); 
    		} 
    		return ip; 
    	} 
}

