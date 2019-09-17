package cn.com.yeexun.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;

public class HttpClientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientService.class);
	

	/**
	* 发送GET请求
	* @param url   请求url
	* @param nameValuePairList    请求参数
	* @return JSON或者字符串
	* @throws Exception
	*/
	public static CloseableHttpResponse sendGet(String url, List<NameValuePair> nameValuePairList) throws Exception{
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		/**
		* 创建HttpClient对象
		*/
		client = HttpClients.createDefault();
		/**
		* 创建URIBuilder
		*/
		URIBuilder uriBuilder = new URIBuilder(url);
		/**
		* 设置参数
		*/
		uriBuilder.addParameters(nameValuePairList);
		/**
		* 创建HttpGet
		*/
		HttpGet httpGet = new HttpGet(uriBuilder.build());
		/**
		* 设置请求头部编码
		*/
		httpGet.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
		/**
		* 设置返回编码
		*/
		httpGet.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
		
//			/**
//			 * 设置xgov token
//			 */
//			httpGet.setHeader(new BasicHeader("Authorization", token));
		/**
		* 请求服务
		*/
		response = client.execute(httpGet);
			
		return response;
	}

	/**
	* 发送POST请求
	* @param url
	* @param nameValuePairList
	* @return JSON或者字符串
	* @throws Exception
	*/
	public static CloseableHttpResponse sendPost(String url, List<NameValuePair> nameValuePairList) throws Exception{
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		/**
		*  创建一个httpclient对象
		*/
		client = HttpClients.createDefault();
		/**
		* 创建一个post对象
		*/
		HttpPost post = new HttpPost(url);
		/**
		* 包装成一个Entity对象
		*/
		StringEntity entity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
		/**
		* 设置请求的内容
		*/
		post.setEntity(entity);
		/**
		* 设置请求的报文头部的编码
		*/
		post.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
		/**
		* 设置请求的报文头部的编码
		*/
		post.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
		
		
//			/**
//			 * 设置xdata token
//			 */
//			post.setHeader(new BasicHeader("Authorization", token));
		
		/**
		* 执行post请求
		*/
		response = client.execute(post);
			
		return response;
	}

	/**
	* 组织请求参数{参数名和参数值下标保持一致}
	* @param params    参数名数组
	* @param values    参数值数组
	* @return 参数对象
	*/
	public static List<NameValuePair> getParams(Object[] params, Object[] values){
		/**
		* 校验参数合法性
		*/
		boolean flag = params.length>0 && values.length>0 &&  params.length == values.length;
		if (flag){
			List<NameValuePair> nameValuePairList = new ArrayList<>();
			for(int i =0; i<params.length; i++){
				nameValuePairList.add(new BasicNameValuePair(params[i].toString(),null != values[i] ? values[i].toString() : ""));
			}
			return nameValuePairList;
		}else{
			LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 197, "请求参数为空且参数长度不一致");
		}
		return null;
	}
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws Exception{
        String url = "要请求的url地址";
        /**
         * 参数值
         */
        Object [] params = new Object[]{"param1","param2"};
        /**
         * 参数名
         */
        Object [] values = new Object[]{"value1","value2"};
        /**
         * 获取参数对象
         */
        List<NameValuePair> paramsList = HttpClientService.getParams(params, values);
        /**
         * 发送get
         */
      /*  Object result = HttpClientService.sendGet(url, paramsList);
        *//**
         * 发送post
         *//*
        Object result2 = HttpClientService.sendPost(url, paramsList);

        System.out.println("GET返回信息：" + result);
        System.out.println("POST返回信息：" + result2);*/
    }
	
	
	
	
	
	
	
	
	
}
