package cn.com.yeexun.utils;

import org.apache.commons.lang.StringUtils;

public enum DbTypeEnum {

	MYSQL("MySQL","-2"),
	
	MONGODB("MongoDB","-3"),
	
	ORACLE("Oracle","-4"),
	
	SQLSERVER("SqlServer","-5"), 
	
	HIVE("Hive","-6"),
	
	HDFS("HDFS","-7"),
	
	DB2("DB2","-8"),
	
	TXTFILE("TxtFile", "-9"),
	
	FTP("Ftp","-10"),
	
	HBASE("Hbase","-11"),
	
	POSTGRESQL("PostgreSQL","-12"),
	
	IMPALA("Impala","-13"),
	
	TERADATA("Teradata","-14"),
	
	SYBASE("Sybase","-15");
	
	
	private String dbcode;
	private String dbname;

	public static String getDbName(String dbcode){
		for (DbTypeEnum result : DbTypeEnum.values()) {
			if (result.getDbcode().equals(dbcode)) {
				return result.name();
			}
		}
		return null;
	}
	
	public static String getDbCode(String dbname){
		for (DbTypeEnum result : DbTypeEnum.values()) {
			if (result.getDbname().equalsIgnoreCase(dbname)) {
				return result.dbcode;
			}
		}
		return null;
	}
	
	private DbTypeEnum(String dbname, String dbcode) {
		this.dbname = dbname;
		this.dbcode = dbcode;
	}
	
	public String getDbcode() {
		return dbcode;
	}

	public void setDbcode(String dbcode) {
		this.dbcode = dbcode;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	
	public static DbTypeEnum getDbEnumByName(String dbname){
	    for(DbTypeEnum dbEnum : DbTypeEnum.values()){
	      if(StringUtils.equalsIgnoreCase(dbname, dbEnum.getDbname())){
	        return dbEnum;
	      }
	    }
	    return null;
  }
	
}
