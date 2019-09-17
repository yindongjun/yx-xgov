package cn.com.yeexun.dataSource.entity;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;


@Table(name = "tb_impala_source")
public class ImpalaSource extends Unique implements OperationTimeAware {
	private static final long serialVersionUID = 8313644536157429035L;
	
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
	
	@Transient
	private String defaultFs;
	
	@Transient
	private String warehouseDir;
	
	@Transient
	private String hadoopConfig;
	
	@Transient
	private String kuduMasterAddress;
	
	private String ip;
	
	private String dbname;
	
	@Transient
	private String metadataDbType;
	
	public String getMetadataDbType() {
		return metadataDbType;
	}

	public void setMetadataDbType(String metadataDbType) {
		this.metadataDbType = metadataDbType;
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

	public String getHadoopConfig() {
		return hadoopConfig;
	}

	public void setHadoopConfig(String hadoopConfig) {
		this.hadoopConfig = hadoopConfig;
	}

	public String getKuduMasterAddress() {
		return kuduMasterAddress;
	}

	public void setKuduMasterAddress(String kuduMasterAddress) {
		this.kuduMasterAddress = kuduMasterAddress;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	
	
	
	
}
