package cn.com.yeexun.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ResponseUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(ResponseUtil.class);
	
	public static <T> T dataToJavaObj(String response, Class<T> klass) {
		JSONObject jsonObj = JSON.parseObject(response);
		T data = jsonObj.getObject(CodeUtil.RESULT_DATA, klass);
		return data;
	}
	
	public static Integer getResponseCode(String response) {
		JSONObject jsonObj = JSON.parseObject(response);
		Integer code = jsonObj.getInteger(CodeUtil.CODE_TEXT);
		return code;
	}
	
	public static <T> T getObjectData(String response, Class<T> klass) {
		
		if (getResponseCode(response) != 200) {
			throw new FeignException(null, "接口返回状态异常，response body：" + response);
		}
		JSONObject jsonObj = JSON.parseObject(response);
		T data = jsonObj.getObject(CodeUtil.RESULT_DATA, klass);
		return data;
		
	}
	
	public static String getStringData(String response) {
		
		if (getResponseCode(response) != 200) {
			throw new FeignException(null, "接口返回状态异常，response body：" + response);
		}
		JSONObject jsonObj = JSON.parseObject(response);
		String data = jsonObj.getString(CodeUtil.RESULT_DATA);
		return data;
		
	}
	
	public static <T> List<T> getListData(String response, Class<T> klass) {
		
		if (getResponseCode(response) != 200) {
			throw new FeignException(null, "接口返回状态异常，response body：" + response);
		}
		JSONObject jsonObj = JSON.parseObject(response);
		String dataString = jsonObj.getString(CodeUtil.RESULT_DATA);
		List<T> data = JSON.parseArray(dataString, klass);
		return data;
		
	}

    /**
     * 判断返回的code值是否是200
     * @param response
     * @return
     */
	public static boolean isError(String response) {
        Integer code = getResponseCode(response);
        if (200 == code) {
            return false;
        } else {
        	LOG.debug("response body:" + response);
            return true;
        }
    }

    /**
     * 校验从其他服务获取接口返回内容
     * @param res
     */
	public static void validateResponse(String res) {
        if (null == res) {
            throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
        }
        if (ResponseUtil.isError(res)) {
            throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
        }
    }

}
