package cn.com.yeexun.utils;

/**
 * 
 * @author wangchen
 *
 */
public class VerifyUtil {
	
	public static String formatType(String type) {
		if (type.endsWith(")")) {
			return type.substring(0, type.indexOf("("));
		}
		return type;
	}

}
