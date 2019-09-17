package cn.com.yeexun.dataSource.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSON;

public class MetadataDatasource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8313644536157429035L;
	
	private Long id;
	
	/*
	 * 数据源名称
	 */
	private String datasourceName;
	
	/*
	 * 数据库类型
	 */
	private String databaseType;
	/*
	 * 数据库字符集
	 */
	private String characterSet;
	/*
	 * 数据源url
	 */
	private String url;
	/*
	 * 数据库用户名
	 */
	private String userName;
	/*
	 * 数据库密码
	 */
	private String password;
	/*
	 * hive数据库名
	 */
	private String dbname;
	
	/*
	 * 数据库名
	 */
	private String mongodbAuthDbname;
	/*
	 * 数据库驱动
	 */
	private String driver;
	
	/*
	 * 数据源添加时间
	 */
	private Date createTime;
	
	/*
	 * oracle SID
	 */
	private String oracleSid;
	
	/*
	 * 修改时间
	 */
	private Date lastModifyTime;
	
	/*
	 * 删除标志位
	 */
	private int isdel;
	
	/*
	 * IP
	 */
	private String ip;
	
	/*
	 * 端口号
	 */
	private String post;
	
	/*
	 * Mysql url库名后面的一些配置
	 */
	private String urlConfig;
	
	private boolean checked;
	
	private String description;
	
	private String sourceAddress;
	
	/**
	 * 数据源模式名
	 */
	private String schemasName;
	
	public String getMongodbAuthDbname() {
		return mongodbAuthDbname;
	}

	public void setMongodbAuthDbname(String mongodbAuthDbname) {
		this.mongodbAuthDbname = mongodbAuthDbname;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getDatasourceName() {
		return datasourceName;
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public String getCharacterSet() {
		return characterSet;
	}

	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;	
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
		
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOracleSid() {
		return oracleSid;
	}

	public void setOracleSid(String oracleSid) {
		this.oracleSid = oracleSid;
	}


	public String getUrlConfig() {
		return urlConfig;
	}

	public void setUrlConfig(String urlConfig) {
		this.urlConfig = urlConfig;
	}

	public String getSchemasName() {
		return schemasName;
	}

	public void setSchemasName(String schemasName) {
		this.schemasName = schemasName;
	}


	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}

