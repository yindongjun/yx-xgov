package cn.com.yeexun.utils;

import java.util.Iterator;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
 
public class JsonCompareUtil {
  
    public static boolean compareJson(JSONObject json1, JSONObject json2, String key) {
        boolean flag = true;
        if(json1.size()!=json2.size()){
        	return false;
        }
    	Iterator<String> i = json1.keySet().iterator();
        while (i.hasNext()) {
            key = i.next();
            if("workflow_id".equals(key)){
            	flag = true;
            }else{
            	flag = compareJson(json1.get(key), json2.get(key), key);
            }
            if(!flag){
            	return flag;
            }
        }
		return flag;
    }
 
    public static boolean compareJson(Object json1, Object json2, String key) {
    	boolean flag = true;
        if (json1 instanceof JSONObject) {
        	flag = compareJson((JSONObject) json1, (JSONObject) json2, key);
        } else if (json1 instanceof JSONArray) {
//            System.out.println("this JSONArray----" + key);
        	flag = compareJson((JSONArray) json1, (JSONArray) json2, key);
        } else if (json1 instanceof String) {
            try {
                String json1ToStr = json1.toString();
                String json2ToStr = json2.toString();
                flag = compareJson(json1ToStr, json2ToStr, key);
            } catch (Exception e) {
                System.out.println("转换发生异常 key:" + key);
                e.printStackTrace();
            }
 
        } else {
//            System.out.println("this other----" + key);
        	flag = compareJson(json1.toString(), json2.toString(), key);
        }
		return flag;
    }
 
    public static boolean compareJson(String str1, String str2, String key) {
    	boolean flag = true;
        if (!str1.equals(str2)) {
        	flag = false;
            System.err.println("不一致key:" + key + ",json1:" + str1 + ",json2:" + str2);
        } else {
        	flag = true;
            System.out.println("一致：key:" + key + ",json1:" + str1 + ",json2:" + str2);
        }
		return flag;
    }
 
    public static boolean compareJson(JSONArray json1, JSONArray json2, String key) {
        boolean flag = true;
        if(json1.size()!=json2.size()){
        	return false;
        }
    	if (json1 != null && json2 != null) {
            Iterator i1 = json1.iterator();
            Iterator i2 = json2.iterator();
            while (i1.hasNext()) {
            	flag = compareJson(i1.next(), i2.next(), key);
            }
        } else {
            if (json1 == null && json2 == null) {
            	flag = false;
                System.err.println("不一致：key:" + key + "  在json1和json2中均不存在");
            } else if (json1 == null) {
            	flag = false;
                System.err.println("不一致：key:" + key + "  在json1中不存在");
            } else if (json2 == null) {
            	flag = false;
                System.err.println("不一致：key:" + key + "  在json2中不存在");
            } else {
            	flag = false;
                System.err.println("不一致：key:" + key + "  未知原因");
            }
 
        }
		return flag;
    }
 
    private final static String st1 = "{\"username\":\"tom\",\"age\":18,\"address\":[{\"province\":\"上海市\"},{\"city\":\"上海市\"},{\"disrtict\":\"静安区\"}]}";
    private final static String st2 = "{username:\"tom\",age:18}";
 
    public static void main(String[] args) {
        System.out.println(st1);
        JSONObject jsonObject1 = JSONObject.parseObject(st1);
        JSONObject jsonObject2 = JSONObject.parseObject(st2);
        boolean flag = compareJson(jsonObject1, jsonObject2, null);
        System.out.println(flag);
    }
 
}

