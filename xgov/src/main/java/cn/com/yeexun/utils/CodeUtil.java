package cn.com.yeexun.utils;

import java.util.HashMap;
import java.util.Map;

public class CodeUtil {
	public final static String CODE_TEXT = "code";
	public final static String MESSAGE_TEXT = "message";
	
	public final static String CODE_SUCCESS = "200";
	public final static String CODE_SUCCESS_MESSAGE = "Success";
	
	public final static String CODE_NOT_DELETE = "405";
	public final static String CODE_NOT_DELETE_MESSAGE = "The task is running! Can not delate!";
	
	public final static String CODE_BAD_REQUEST = "400";
	public final static String CODE_BAD_REQUEST_MESSAGE = "Bad request,Request parameter is null or error";
	
	public final static String CODE_UNAUTHORIZED = "401";
	public final static String CODE_UNAUTHORIZED_MESSAGE = "Permission denied";
	
	public final static String CODE_NOT_FOUND = "404";
	public final static String CODE_NOT_FOUND_MESSAGE = "Request or page is not found";
	
	public final static String CODE_REQUEST_ERROR = "506";
	public final static String CODE_REQUEST_ERROR_MESSAGE = "System Error!";
	
	public final static String CODE_INVENTORY_SHORTAGE = "508";
	public final static String CODE_INVENTORY_SHORTAGE_MESSAGE = "Inventory shortage!";
	
	public final static String CODE_TIMEOUT = "502";
	public final static String CODE_TIMEOUT_MESSAGE = "time out!";
	
	public final static String CODE_TABLE_NOT_FOUND = "000";
	public final static String CODE_TABLE_NOT_FOUND_MESSAGE = "The table is not exist!";
	
	public final static String CODE_SAVE_ERROR = "600";
	// 当验证表不存在时，返回601；
	public final static String CODE_TABLE_NOT_EXIST = "601";
	// 在数据转换时，当判断不需要展示数据时，返回224
	public final static String CODE_NOT_SHOW = "224";
	//
	public final static String CODE_TOKEN_ERROR = "10010";
	
	public final static String RESULT_DATA = "data";
	public final static String RESULT_DATA_COUNT = "recordsFiltered"; //datatable返回的必要参数
	public final static String RESULT_DRAW = "draw"; 	//datatable返回的必要参数
	public final static String RESULT_HEADS = "heads";  //获取动态表格的表头
	public final static String RESULT_ROWCOUNT = "rowCount"; //动态表格的行数
	public final static String RESULT_COLCOUNT = "colCount"; //动态表格的列数
	public final static String RESULT_SQLCOUNT = "sqlCount"; //动态表格的列数
	
	public final static String FEIGN_CLIENT_ERROR = "微服务组件接口返回异常";
	public final static String FEIGN_CLIENT_DISCONTECTED = "微服务组件接口连接异常";
	
	//获取动态表格的表头
	
	
	public static Map<String, Object> getBadRequestMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CODE_TEXT, CODE_BAD_REQUEST);
		map.put(MESSAGE_TEXT, CODE_BAD_REQUEST_MESSAGE);
		return map;
	}
	
	public static Map<String, Object> getErrorRequestMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CODE_TEXT, CODE_REQUEST_ERROR);
		map.put(MESSAGE_TEXT, CODE_REQUEST_ERROR_MESSAGE);
		return map;
	}

	public static Map<String, Object> getUnauthorizedMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CODE_TEXT, CODE_UNAUTHORIZED);
		map.put(MESSAGE_TEXT, CODE_UNAUTHORIZED_MESSAGE);
		return map;
	}
	
	public static Map<String, Object> getSuccessMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CODE_TEXT, CODE_SUCCESS);
		map.put(MESSAGE_TEXT, CODE_SUCCESS_MESSAGE);
		return map;
	}
	
	public static Map<String, Object> getSystemErrorMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CODE_TEXT, CODE_REQUEST_ERROR);
		map.put(MESSAGE_TEXT, CODE_REQUEST_ERROR_MESSAGE);
		return map;
	}
	
	public static Map<String, Object> getSaveErrorMap(String e){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CODE_TEXT, CODE_SAVE_ERROR);
		map.put(MESSAGE_TEXT, e);
		return map;
	}
	
	public static Map<String, Object> getSqlErrorMap(String e){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CODE_TEXT, CODE_TABLE_NOT_FOUND);
		map.put(MESSAGE_TEXT, e);
		return map;
	}
	
	public static Map<String, Object> getErrorMap(String msg){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CODE_TEXT, CODE_REQUEST_ERROR);
		map.put(MESSAGE_TEXT, msg);
		return map;
	}
	
	public static Map<String, Object> getDeleteError(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CODE_TEXT, CODE_NOT_DELETE);
		map.put(MESSAGE_TEXT, CODE_NOT_DELETE_MESSAGE);
		return map;
	}
}
