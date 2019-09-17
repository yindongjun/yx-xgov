package cn.com.yeexun.utils;

import org.apache.commons.lang.StringUtils;

public enum CheckTypeEnum {

	DEFAULT_TYPE("default_type","1"),
	
	// 格式校验
	FORMAT_VERIFY("format_verify","2"),
	
	// 空值校验
	DEFECT_VERIFY("defect_verify","3"),
	
	// 数值范围校验
	INTERVAL_VERIFY("interval_verify","4"), 
	
	// 值域校验
	ENUM_VERIFY("enum_verify","5"),
	
	// 正则校验
	REGULAR_VERIFY("regular_verify","6");
	
	private String checkType;
	
	private String code;
	
	public String getCheckType() {
		return checkType;
	}

	public String getCode() {
		return code;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	private CheckTypeEnum(String checkType, String code) {
		this.checkType = checkType;
		this.code = code;
	}
	
	public static String getCheckType(String code){
		for (CheckTypeEnum result : CheckTypeEnum.values()) {
			if (result.getCode().equals(code)) {
				return result.name();
			}
		}
		return null;
	}
	
	public static String getDbCode(String checkType){
		for (CheckTypeEnum result : CheckTypeEnum.values()) {
			if (result.getCheckType().equalsIgnoreCase(checkType)) {
				return result.code;
			}
		}
		return null;
	}
	
	public static CheckTypeEnum getDbEnumByName(String checkType){
	    for(CheckTypeEnum dbEnum : CheckTypeEnum.values()){
	      if(StringUtils.equalsIgnoreCase(checkType, dbEnum.getCheckType())){
	        return dbEnum;
	      }
	    }
	    return null;
	}
}
