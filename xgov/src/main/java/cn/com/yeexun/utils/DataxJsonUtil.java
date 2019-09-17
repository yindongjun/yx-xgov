package cn.com.yeexun.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DataxJsonUtil {
	
	public static JSONObject putStrKV(JSONObject jsonObj,String key,Object value){
		jsonObj.put(key, value);
		return jsonObj;
	}
		  
	public static JSONArray putObj2Array(JSONArray arrayObj,Object element){
		arrayObj.add(element);
		return arrayObj;
	}
		  
	public static JSONArray putStrArray2JsonArray(JSONArray arrayObj,String[] strArrays){
		if(strArrays!=null && strArrays.length>0){
			for(String str : strArrays){
				arrayObj.add(str);
			}
		}
		return arrayObj;
	}
		 
}
