package cn.com.yeexun.dataSource.entity;

import javax.persistence.Table;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_hdfs_source")
public class HdfsSource extends Unique {
	// 数据源Id
	private Long datasourceId;
	
	//是否有认证：1-有认证 0-没有认证
	private Integer haveKerberos;

	private String keytabFilepath;

	private String principal;

	private String javaSecurityKrb5Conf;
	
	private String hadoopConfig;

	//删除标志位
	private int isdel;

	public Long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(Long datasourceId) {
		this.datasourceId = datasourceId;
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

	public String getHadoopConfig() {
		return hadoopConfig;
	}

	public void setHadoopConfig(String hadoopConfig) {
		this.hadoopConfig = hadoopConfig;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}
	
	
	
}
