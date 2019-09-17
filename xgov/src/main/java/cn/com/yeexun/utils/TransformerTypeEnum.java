package cn.com.yeexun.utils;

public enum TransformerTypeEnum {
	
	DX_KV_ENUM("dx_kv_enum","0"),
	
	DX_SUBSTR("dx_substr","1"),
	
	DX_PAD("dx_pad","2"),
	
	DX_REPLACE("dx_replace","3"),
	
	DX_FILTER("dx_filter","4"),
	
	DX_FILTER_TWO("dx_filter","5"),
	
	DX_FILTER_THREE("dx_filter","6");
	
	private String typeName;
	
	private String type;
	
	public String getTypeName() {
		return typeName;
	}
	
	public String getType() {
		return type;
	}

	private TransformerTypeEnum(String typeName, String type) {  
		this.typeName = typeName;
		this.type = type;
	}  
	
	public static String getTransformerTypeName(String type){
		for(TransformerTypeEnum transformerEnum : TransformerTypeEnum.values()){
			if(type.equals(transformerEnum.getType())){
				return transformerEnum.getTypeName();
			}
	    }
	    return null;
	}
	
	public static String getTransformerName(String type){
		for(TransformerTypeEnum transformerEnum : TransformerTypeEnum.values()){
			if(type.equals(transformerEnum.getType())){
				return transformerEnum.name();
			}
	    }
	    return null;
	}
	
	public static String getTransformerEnumType(String name){
		for(TransformerTypeEnum transformerEnum : TransformerTypeEnum.values()){
			if(name.equals(transformerEnum.name())){
				return transformerEnum.getTypeName();
			}
	    }
	    return null;
	}	
	
	public static String getTransformerEnumTypeName(String name){
		for(TransformerTypeEnum transformerEnum : TransformerTypeEnum.values()){
			if(name.equals(transformerEnum.name())){
				return transformerEnum.getTypeName();
			}
	    }
	    return null;
	}	
	
}
