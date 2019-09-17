package cn.com.yeexun.dataSource.entity;

import java.io.Serializable;

import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_datasource_layer")
public class DatasourceSourceLayerEntity extends Unique implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long datasourceId;
	
	private Long layerId;

	public Long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(Long datasourceId) {
		this.datasourceId = datasourceId;
	}


	
	
	public Long getLayerId() {
		return layerId;
	}

	public void setLayerId(Long layerId) {
		this.layerId = layerId;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

}
