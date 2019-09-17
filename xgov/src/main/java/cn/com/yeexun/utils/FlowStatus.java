package cn.com.yeexun.utils;

public enum FlowStatus {
	
	START("0", "启动"),
	
	COMPLETED("1", "完成"),
	
	EXCEPTION("2", "异常");
	
	private String code;
	private String desc;

	private FlowStatus(String code, String desc) {
		this.desc = desc;
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}
	
	public String getCode(){
		return code;
	}
	
	public static FlowStatus findByCode(String code) {
		for (FlowStatus result : FlowStatus.values()) {
			if (result.getCode().equals(code)) {
				return result;
			}
		}
		return null;
	}

}
