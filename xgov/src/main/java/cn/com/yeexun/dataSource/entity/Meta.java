package cn.com.yeexun.dataSource.entity;
//数据库树形展示时使用
public class Meta {
	private Long id;
	
	private Long Pid;
	
	private String pname;
	
	private String name;
	
	private String typename;
	
	private String type;
	
	private String isParent;
	
	private boolean isBind;
	
	private String sourceName;
	
	private Long sourceId;
	
	private String dataType;
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return Pid;
	}

	public void setPid(Long pid) {
		Pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public boolean isBind() {
		return isBind;
	}

	public void setBind(boolean isBind) {
		this.isBind = isBind;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	
}
