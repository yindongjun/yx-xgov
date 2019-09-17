package cn.com.yeexun.utils;

import org.apache.commons.lang.StringUtils;

public enum FkRuleEnum {
	
	CASCADE("Cascade","0"),

	NOACTION("no action","3"),
	
	SETNULL("set null","2"),
	
	SETDEFAULT("set default","1");
	
	private String code;
	
	private String name;

	public static String getName(String code){
		for (FkRuleEnum result : FkRuleEnum.values()) {
			if (result.getCode().equals(code)) {
				return result.name();
			}
		}
		return null;
	}
	
	public static String getDbCode(String name){
		for (FkRuleEnum result : FkRuleEnum.values()) {
			if (result.getName().equals(name)) {
				return result.code;
			}
		}
		return null;
	}
	
	private FkRuleEnum(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static FkRuleEnum getFkRuleEnumByName(String name){
	    for(FkRuleEnum dbEnum : FkRuleEnum.values()){
	      if(StringUtils.equals(name, dbEnum.getName())){
	        return dbEnum;
	      }
	    }
	    return null;
  }
	
}
