package cn.com.yeexun.dataSource.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSON;

public class MetadataImpalaSource implements Serializable {
	
	private static final long serialVersionUID = 8313644536157429035L;
	
	private Long id;

	private Long dataSourceId;

	private String driver;

	private String userName;

	private String password;

	private String port;

	private String url;

	private Date createTime;

	private Date lastModifyTime;

	private int isdel;

	private Integer haveKerberos;

	private String keytabFilepath;

	private String principal;

	private String javaSecurityKrb5Conf;
	
	private String defaultFs;
	
	private String warehouseDir;
	
	private String hadoopConfig;
	
	private String kuduMasterAddress;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKuduMasterAddress() {
		return kuduMasterAddress;
	}

	public void setKuduMasterAddress(String kuduMasterAddress) {
		this.kuduMasterAddress = kuduMasterAddress;
	}

	public String getHadoopConfig() {
		return hadoopConfig;
	}

	public void setHadoopConfig(String hadoopConfig) {
		this.hadoopConfig = hadoopConfig;
	}

	public Long getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(Long dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
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

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

	public Integer getHaveKerberos() {
		return haveKerberos;
	}

	public void setHaveKerberos(Integer haveKerberos) {
		this.haveKerberos = haveKerberos;
	}

	public String getKeytabFilepath() {
		return keytabFilepath;
	}

	public void setKeytabFilepath(String keytabFilepath) {
		this.keytabFilepath = keytabFilepath;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getJavaSecurityKrb5Conf() {
		return javaSecurityKrb5Conf;
	}

	public void setJavaSecurityKrb5Conf(String javaSecurityKrb5Conf) {
		this.javaSecurityKrb5Conf = javaSecurityKrb5Conf;
	}

	public String getDefaultFs() {
		return defaultFs;
	}

	public void setDefaultFs(String defaultFs) {
		this.defaultFs = defaultFs;
	}

	public String getWarehouseDir() {
		return warehouseDir;
	}

	public void setWarehouseDir(String warehouseDir) {
		this.warehouseDir = warehouseDir;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
