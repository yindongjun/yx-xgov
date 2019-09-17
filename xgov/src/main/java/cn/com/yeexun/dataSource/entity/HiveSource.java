package cn.com.yeexun.dataSource.entity;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_hive_source")
public class HiveSource extends Unique implements OperationTimeAware {
	private static final long serialVersionUID = 8313644536157429035L;
	
	private Long dataSourceId;

	private String serverType;

	private String driver;

	private String hiveMetaDataUserName;

	private String hiveMetaDataPassword;

	private String hiveMetaDataPort;
	
	private String hiveMetaDataIp;
	
	private String hiveMetaDataDbName;
	
	@Transient
	private String hiveMetaDataDbType;

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
	
	private String hadoopConfig;

	public Long getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(Long dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	

	

	public String getHiveMetaDataUserName() {
		return hiveMetaDataUserName;
	}

	public void setHiveMetaDataUserName(String hiveMetaDataUserName) {
		this.hiveMetaDataUserName = hiveMetaDataUserName;
	}

	public String getHiveMetaDataPassword() {
		return hiveMetaDataPassword;
	}

	public void setHiveMetaDataPassword(String hiveMetaDataPassword) {
		this.hiveMetaDataPassword = hiveMetaDataPassword;
	}

	public String getHiveMetaDataPort() {
		return hiveMetaDataPort;
	}

	public void setHiveMetaDataPort(String hiveMetaDataPort) {
		this.hiveMetaDataPort = hiveMetaDataPort;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	public String getHiveMetaDataIp() {
		return hiveMetaDataIp;
	}

	public void setHiveMetaDataIp(String hiveMetaDataIp) {
		this.hiveMetaDataIp = hiveMetaDataIp;
	}

	public String getHiveMetaDataDbName() {
		return hiveMetaDataDbName;
	}

	public void setHiveMetaDataDbName(String hiveMetaDataDbName) {
		this.hiveMetaDataDbName = hiveMetaDataDbName;
	}

	public String getHiveMetaDataDbType() {
		return hiveMetaDataDbType;
	}

	public void setHiveMetaDataDbType(String hiveMetaDataDbType) {
		this.hiveMetaDataDbType = hiveMetaDataDbType;
	}

	
	
	
	
	
}
