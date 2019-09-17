package cn.com.yeexun.dataSource.entity;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSON;

import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "tb_data_source")
public class DataSource extends Unique implements OperationTimeAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	 * 数据源添加时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	/*
	 * 删除标志位
	 */
	private int isdel;
	
	/*
	 * 数据库名
	 */
	private String dbname;
	
	/*
	 * hive元数据库库名
	 */
	@Transient
	private String dbHive;
	
	/*
	 * 数据库驱动
	 */
	private String driver;
	
	/*
	 * 修改时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@OrderBy("desc")
	private Date lastModifyTime;
	
	/*
	 * oracle SID
	 */
	@Transient
	private String oracleSid;
	
	/*
	 * IP
	 */
	private String ip;
	
	/*
	 * 端口号
	 */
	private String port;
	
	
	
	/*
	 * Mysql url库名后面的一些配置
	 */
	@Transient
	private String urlConfig;
	
	@Transient
	private String db2DataBase;
	
	/*
	 * 数据源分层id
	 */
	private int sourceLayerId;
	
	
	private String layerList;
	
	/*
	 * 所属部门id
	 */
	private int departmentId;
	
	/*
	 * 模式名
	 */
	private String schemasName;
	/**
	 * 每天0点查询的库的数据总量
	 */
	private Float dataSize;
	
	@Transient
	private String sourceAddress;
	
	
	
	

	public Float getDataSize() {
		return dataSize;
	}

	public void setDataSize(Float dataSize) {
		this.dataSize = dataSize;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getSourceLayerId() {
		return sourceLayerId;
	}

	public void setSourceLayerId(int sourceLayerId) {
		this.sourceLayerId = sourceLayerId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getSchemasName() {
		return schemasName;
	}

	public void setSchemasName(String schemasName) {
		this.schemasName = schemasName;
	}

	public String getDbHive() {
		return dbHive;
	}

	public void setDbHive(String dbHive) {
		this.dbHive = dbHive;
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

	public String getDb2DataBase() {
		return db2DataBase;
	}

	public void setDb2DataBase(String db2DataBase) {
		this.db2DataBase = db2DataBase;
	}

	public String getLayerList() {
		return layerList;
	}

	public void setLayerList(String layerList) {
		this.layerList = layerList;
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
