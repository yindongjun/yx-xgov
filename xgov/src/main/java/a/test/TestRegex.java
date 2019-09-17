package a.test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.yeexun.utils.PropertyPlaceholder;

public class TestRegex {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
//		^[a-zA-Z0-9\u4e00-\u9fa5]+$
//		String regex = "[0-9]{8} [0-9]{6}";
//		String text = "8989883 4345667";
//		Pattern pattern = Pattern.compile(regex);
//		Matcher matcher = pattern.matcher(text);
//		boolean isMatches = matcher.matches();
//		System.out.println(isMatches);
//		System.out.println(text.length());
//		System.out.println(text.toCharArray().length);
//		System.out.println(Arrays.toString(text.getBytes("UTF-8")));
//		System.out.println(Arrays.toString(text.getBytes("GB2312")));
//		System.out.println(new String(text.getBytes("GB2312"), "GB2312"));
		
//		System.out.println(PropertyPlaceholder.getContextProperty("test"));
		
		System.out.println("12".matches("\\d+(,\\d+)"));
	}

}
