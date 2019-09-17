package cn.com.yeexun.utils;

public class ParameterUtil {
	//flag
	public final static String LOCK = "1";
	public final static String UN_LOCK = "0";
	public final static String DELETEED = "1";
	public final static String UN_DEL = "0";
	public final static String SUCCESS = "success";
	
	//node
	public final static int ZERO_NODE = 0;
	public final static int FIRST_NODE = 1;
	public final static int SECOND_NODE = 2;
	public final static int THREE_NODE = 3;
	public final static int FOUR_NODE = 4;
	public final static int FIVE_NODE = 5;
	public final static int SIX_NODE = 6;
	public final static int SEVEN_NODE = 7;
	
	//flow
	public final static String EXE_FLOW_INFO = "exeFlowInfo";
	public final static String FLOW_INSTANCE = "flowInstance";
	
	//datasource add/update error msg
	public final static String CONN_PARAMS_ERROR = "连接参数有误";
	public final static String HDFS_CONN_TIMEOUT= "连接超时";
	public final static String MSG_ERROR = "连接有误";
	
	//repeat name
	public final static String SOURCE_NAME_REPEAT = "数据源名称已存在";
	public final static String SOURCE_DBNAME_WRONG = "url中填写的数据库与DBname名称不一致";
	public final static String WEREHOUSE_DBNAME_WRONG = "werehouse.dir中填写的数据库与DBname名称不一致";
	public final static String WEREHOUSE_START_WRONG = "werehouse.dir必须以斜杠开头";
	public final static String SOURCE_DATA_REPEAT = "该数据源信息已存在，不能重复添加！";
	public final static String USER_NAME_REPEAT = "用户名已存在";
	public final static String ROLE_NAME_REPEAT = "角色名已存在";
	public final static String TEL_NAME_REPEAT = "号码已存在";
	public final static String EMAIL_NAME_REPEAT = "邮箱已存在";
	
	//login operation log msg
	public final static String LOGIN_MODE = "登录模块";
	public final static String MENU_CHINESE = "登录用户菜单权限";
	public final static String LOGIN = "登入";
	public final static String LOGIN_OUT = "登出";
	
	//login error msg
	public final static String LOGIN_USER_LOCK_DELETE= "用户已锁定或已删除";
	public final static String LOGIN_PWD_ERROR = "登录密码有误";
	public final static String LOGIN_NAME_NOT_EXIST = "用户名不存在";
	public final static String LOGIN_ERROR_FREQUENCY = "登录失败次数过多, 请联系管理员";
	public final static String USER_NOT_PRIVILEGE = "用户未分配权限";
	
		//columns check
	public final static String COLUMNS_NUM = "数据源和目的源中列个数不匹配！";
	public final static String COLUMNS_TYPE = "数据源和目的源中列类型不匹配！";
	public final static String COLUMNS_NAME = "数据源和目的源中列名称不匹配！";
	public final static String TABLE_NOT_EXIST = "该表在目的源中不存在！";
	public final static String FILE_EXIST = "该文件已经存在！";
	
	/*=============工作流是否可执行的状态===================*/
	public final static String EXECUTABLE = "1";
	public final static String NOT_EXECUTABLE = "0";
	
	public final static String MONGODB_NULLDATA_EXCEPTION = "MONGODB中第一行数据包含null值，无法获取数据类型！";
	
}
