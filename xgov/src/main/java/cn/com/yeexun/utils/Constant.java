package cn.com.yeexun.utils;

public class Constant {
	
	public final static String MYSQL = "MySQL";
	
	public final static String ORACLE = "Oracle";
	
	public final static String SQLSERVER = "SqlServer";
	
	public final static String HDFS = "HDFS";
	
	public final static String MONGODB = "MongoDB";
	
	public final static String HIVE = "Hive";
	
	public final static String IMPALA = "Impala";
	
	public final static String TXTFILE = "TxtFile";
	
	public final static String FTP = "Ftp";
	
	public final static String HBASE = "Hbase";
	
	public final static String DB2 = "DB2";
	
	public final static String POSTGRESQL = "PostgreSQL";
	
	public final static String SYBASE = "Sybase";
	
	public final static String TERADATA = "Teradata";
	
	
	//-------以下常量表示tb_task_type表中的flag字段---------------
	public final static String R_RDB = "1";
	public final static String W_RDB = "2";
	public final static String R_MONGODB = "3";
	public final static String W_MONGODB = "4";
	public final static String W_HDFS = "8";
	public final static String R_HDFS = "7";
	public final static String R_HIVE = "9";
	public final static String W_HIVE = "10";
	//------------------END---------------------------------
	
	/*=============transform===================*/
	public final static int is_parent = 1;
	public final static int not_parent = 0;
	
	/*=================符号 ===========*/
	public final static String SPOT = ".";
	public final static String UNDERLINE = "__";
	public final static String SPRIT = "/";
	
	/*============工作流==============*/
	public final static String TABLE_NEW_SUFFIX = "(new)";
	public final static String TABLE_NEW_STATUS = "1";
	public final static String TABLE_OLD_STATUS = "0";
	public final static String PLACE_CHOICE_TABLE = "请选择表";
	
	
	/*==========BIG工作流 ===========*/
	
	public final static String SHEEL_BUFF_PATH = "shellBuffFilePath";
	public final static String SQL_BUFF_PATH = "sqlBuffFilePath";
	public final static String SHEEL_FILE_SUFFIX = ".sh";
	public final static String SQL_FILE_SUFFIX = ".sql";
	public final static String SHEEL_FLIE_PATH = "shellFilePath";
	public final static String SQL_FLIE_PATH = "sqlFilePath";
	public final static String FILE_CHANGED = "1";
	
	public final static int ETL = 0;
	public final static int SHELL = 1;
	public final static int SQL = 2;
	// SQL语句分隔符；
	public final static String DEFAULT_SQL_DELIMITER = ";";
	public final static String DEFAULT_SQLSERVER_DELIMITER = "GO";
	public final static String DEFAULT_HIVESQL_DELIMITER = ";";
	public final static String DEFAULT_ENCODING = "UTF-8";
	
	public final static String IS_DELETE = "1";
	public final static String NOT_DELETE = "0";
	//调度类型
	public final static String DISPATCH_TYPE_COLLECT = "0";
	public final static String DISPATCH_TYPE_QUALITY = "1";
	
	//hive表文件存储类型
	public final static String TEXT_FILE = "TextFile";
	public final static String ORC_FILE = "OrcFile";
	public final static String RC_FILE = "RcFile";
	public final static String SEQUENCE_FILE = "SequenceFile";
	
	//元数据类型
	public final static String BUSSINESS_METADATA = "0";
	public final static String TECHNOLOGY_METADATA = "1";
	public final static String MANAGEMENT_METADATA = "2";
	
	//元数据中是否是已经发布的元数据
	public final static int IS_RELEASE = 1;
	public final static int NOT_RELEASE = 0; 
	
	//调度状态
//	public final static String DISPATCH_STATUS_ON = "0";
//	public final static String DISPATCH_STATUS_SUCCEED = "1";
//	public final static String DISPATCH_STATUS_FILED = "2";
	
	//任务提交状态
	public final static String TASK_STATUS_DRAFT = "0";//草稿
	public final static String TASK_STATUS_SUBMITED = "1";//已提交
	public final static String TASK_STATUS_CHANGE = "2";//变更中
	public final static String TASK_STATUS_NOT_CONFIGURED = "3";//未配置
	
	
	/**changeType:1-审核通过，要将关联关系加到已经生成规则的表中
	 *            2-解除关系（已审核的数据元），要将关联关系从对应的规则中删除
	 *            3-变更操作，要将关联关系从对应的规则中删除
	 *            4-编辑关联关系（已审核的数据元），将关联关系加到已经生成规则的表中
	 */
	public final static String CHANGE_SUBMIT = "1";
	public final static String CHANGE_REMOVE = "2";
	public final static String CHANGE_ALTER = "3";
	public final static String CHANGE_EDIT = "4";  
	
	//数据抽取方式
	public final static String extractType_full = "0";
	public final static String extractType_increment = "1"; 
}
