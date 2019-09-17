package cn.com.yeexun.dataSource.entity;

import javax.persistence.Table;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_hbase_source")
public class HbaseSource extends Unique {
	// 数据源Id
	private Long datasourceId;
	
	// zookeeper集群地址
	private String zkQuorum;
	
	// Hbase在HDFS中路径
	private String rootdir;
	
	// Hbase是否为集群模式
	private Integer distributed;
	
	//删除标志位
	private int isdel;
	
	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

	public Long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(Long datasourceId) {
		this.datasourceId = datasourceId;
	}

	public String getZkQuorum() {
		return zkQuorum;
	}

	public void setZkQuorum(String zkQuorum) {
		this.zkQuorum = zkQuorum;
	}

	public String getRootdir() {
		return rootdir;
	}

	public void setRootdir(String rootdir) {
		this.rootdir = rootdir;
	}

	public Integer getDistributed() {
		return distributed;
	}

	public void setDistributed(Integer distributed) {
		this.distributed = distributed;
	}
	
	
}
