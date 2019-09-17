package a.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.ibm.db2.jcc.am.s;

public class A {
	
	public void func() {
		System.out.println("aaaaaaaaaaaa");
	}
	
	public static void main(String[] args) {
		List<String> list1 = Arrays.asList("a", "b", "c", "d");
		final List<String> list2 = Arrays.asList("b", "c", "d", "e");
		
		List<String> list3 = list1.stream().map(s1 -> list2.stream().anyMatch(s2 -> s1.equals(s2)))
				.map(s -> s + "1").collect(Collectors.toList());
		
		System.out.println(list3);
	}

}
