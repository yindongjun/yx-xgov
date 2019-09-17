package cn.com.yeexun.utils;

import cn.com.common.ssm.engine.utils.MD5Util;

public class SignUtil {
//	private static final Logger logger = (Logger) LoggerFactory.getLogger(SignUtil.class);
	
	private static final String sign = "&yeexunup=true";
	
	public static String toSign(String appId,String timestamp,String version){
		String url = "&appId="+appId+"&timestamp="+timestamp+"&version="+version+sign;
		return MD5Util.MD5(url);
	}
	
	public static String createToken(String appId,String version,String imei,String uid){
		String url = "userid="+uid+"&appId="+appId+"&version="+version+"&imsi="+imei;
		return MD5Util.MD5(url);
	}
	
	public static void main(String[] args){
//		A1908ADD062B76D34604D4F30E7FD30E
//		83F6334A13B6A4790DE59802212B90C0
//		System.out.println(toSign("520030303920842","app_gmp_test_my_infobip_1776", "1479276591644", "1"));
		System.out.println(MD5Util.MD5("imsi=520030303920842&appId=app_gmp_test_my_infobip_1776&timestamp=1479278418765&version=1&pdfjpgimggif=true"));
	}
	
}
