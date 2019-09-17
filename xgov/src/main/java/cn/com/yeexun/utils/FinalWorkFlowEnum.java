package cn.com.yeexun.utils;

public enum FinalWorkFlowEnum {
	
	ETL("etl","0"),
	
	SHELL("shell","1"),
	
	SQL("sql","2");
	
	private String typeName;
	
	private String type;
	
	public String getTypeName() {
		return typeName;
	}
	
	public String getType() {
		return type;
	}

	private FinalWorkFlowEnum(String typeName, String type) {  
		this.typeName = typeName;
		this.type = type;
	}  
	
	public static String getTransformerTypeName(String type){
		for(FinalWorkFlowEnum finalWorkFlowEnum : FinalWorkFlowEnum.values()){
			if(type.equals(finalWorkFlowEnum.getType())){
				return finalWorkFlowEnum.getTypeName();
			}
	    }
	    return null;
	}
	
	public static String getTransformerName(String type){
		for(FinalWorkFlowEnum finalWorkFlowEnum : FinalWorkFlowEnum.values()){
			if(type.equals(finalWorkFlowEnum.getType())){
				return finalWorkFlowEnum.name();
			}
	    }
	    return null;
	}
	
	public static String getfinalWorkFlowEnumType(String name){
		for(FinalWorkFlowEnum finalWorkFlowEnum : FinalWorkFlowEnum.values()){
			if(name.equals(finalWorkFlowEnum.name())){
				return finalWorkFlowEnum.getTypeName();
			}
	    }
	    return null;
	}	
	
	public static String getfinalWorkFlowEnumTypeName(String name){
		for(FinalWorkFlowEnum finalWorkFlowEnum : FinalWorkFlowEnum.values()){
			if(name.equals(finalWorkFlowEnum.name())){
				return finalWorkFlowEnum.getTypeName();
			}
	    }
	    return null;
	}	
	
}
