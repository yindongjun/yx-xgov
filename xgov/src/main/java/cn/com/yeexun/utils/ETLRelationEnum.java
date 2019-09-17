package cn.com.yeexun.utils;


/**
 * @author yang.li
 * @version 1.0.0,2017年11月16日 下午1:38:52
 * @since yeexun
 */

public enum ETLRelationEnum {
	
	R2R("R2R","0"),
	
	R2HDFS("R2Hdfs","1"),
	
	HDFS2R("Hdfs2R","2"),
	
	HDFS2HDFS("Hdfs2Hdfs","3"),
	
	R2MONGODB("R2Mongodb","4"),
	
	MONGODB2R("Mongodb2R","5"),
	
	MONGODB2MONGODB("Mongodb2Mongodb","6"),
	
	R2HIVE("R2Hive","7"),
	
	HIVE2R("Hive2R","8"),
	
	HIVE2HIVE("Hive2Hive","9"),
	
	MONGODB2HIVE("Mongodb2Hive","10"),
	
	HIVE2MONGODB("Hive2Mongodb","11"),
	
	HDFS2MONGODB("Hdfs2Mongodb","12"),
	
	MONGODB2HDFS("Mongodb2Hdfs","13"),
	
	HDFS2HIVE("Hdfs2Hive","14"),
	
	HIVE2HDFS("Hive2Hdfs","15"),
	
	TXTFILE2R("TxtFile2R", "16"),
	
	TXTFILE2HIVE("TxtFile2Hive", "17"),
	
	TXTFILE2MONGODB("TxtFile2Mongodb", "18"),
	
	TXTFILE2FTP("TxtFile2Ftp", "19"),
	
	TXTFILE2HDFS("TxtFile2Hdfs", "20"),
	
	FTP2R("Ftp2R", "21"),
	
	FTP2HDFS("Ftp2Hdfs", "22"),
	
	FTP2MONGODB("Ftp2Mongodb", "23"),
	
	FTP2HIVE("Ftp2Hive", "24"),
	
	R2FTP("R2Ftp", "25"),
	
	HDFS2FTP("Hdfs2Ftp", "26"),
	
	MONGODB2FTP("Mongodb2Ftp", "27"),
	
	HIVE2FTP("Hive2Ftp", "28"),
	
	FTP2FTP("Ftp2Ftp", "29"),
	
	HBASE2HDFS("Hbase2Hdfs","30"),
	
	HBASE2HIVE("Hbase2Hive","31"),
	
	HBASE2FTP("Hbase2Ftp","32"),
	
	HBASE2MONGODB("Hbase2Mongodb","33"),
	
	HBASE2R("Hbase2R","34"),
	
	HBASE2HBASE("Hbase2Hbase","35"),
	
	HDFS2HBASE("Hdfs2Hbase","36"),
	
	HIVE2HBASE("Hive2Hbase","37"),
	
	FTP2HBASE("Ftp2Hbase","38"),
	
	MONGODB2HBASE("Mongodb2Hbase","39"),
	
	TXT2HBASE("TxtFile2Hbase","40"),
	
	R2HBASE("R2Hbase","41"),
	
	R2IMPALA("R2Impala","42"),
	TXT2IMPALA("TxtFile2Impala","43"),
	FTP2IMPALA("Ftp2Impala","44"),
	HDFS2IMPALA("Hdfs2Impala","45"),
	MONGODB2IMPALA("Mongodb2Impala","46"),
	HIVE2IMPALA("Hive2Impala","47"),
	HBASE2IMPALA("Hbase2Impala","48"),
	IMPALA2IMPALA("Impala2Impala","49"),
	
	IMPALA2R("Impala2R","50"),
	IMPALA2TXT("Impala2TxtFile","51"),
	IMPALA2FTP("Impala2Ftp","52"),
	IMPALA2HDFS("Impala2Hdfs","53"),
	IMPALA2MONGODB("Impala2Mongodb","54"),
	IMPALA2HIVE("Impala2Hive","55"),
	IMPALA2HBASE("Impala2Hbase","56");
	
	

	private String code;
	
	private String name;
	
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static String getName(String code){
		for (ETLRelationEnum result : ETLRelationEnum.values()) {
			if (result.getCode().equals(code)) {
				return result.name();
			}
		}
		return null;
	}
	
	public static String getCode(String name){
		for (ETLRelationEnum result : ETLRelationEnum.values()) {
			if (result.getName().equals(name)) {
				return result.code;
			}
		}
		return null;
	}
	
	public static String getNamebyCode(String code){
		for (ETLRelationEnum result : ETLRelationEnum.values()) {
			if (result.getCode().equals(code)) {
				return result.getName();
			}
		}
		return null;
	}
	
	public static ETLRelationEnum getByCode(String code){
		for (ETLRelationEnum result : ETLRelationEnum.values()) {
			if (result.getCode().equals(code)) {
				return result;
			}
		}
		return null;
	}
	
	private ETLRelationEnum(String name, String code) {
		this.name = name;
		this.code = code;
	}

}
