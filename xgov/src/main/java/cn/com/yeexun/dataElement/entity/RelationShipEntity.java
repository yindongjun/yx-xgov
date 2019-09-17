package cn.com.yeexun.dataElement.entity;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_element_relation")
public class RelationShipEntity extends Unique implements OperationTimeAware{

	private static final long serialVersionUID = 1L;
	
	private Long sourceId;
	
	private String tablename;
	
	private String fildname;
	
	private Long dataElementId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastModifyTime;
	
	private String isdel;
	
	@Transient
	private String sourceName;

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getFildname() {
		return fildname;
	}

	public void setFildname(String fildname) {
		this.fildname = fildname;
	}

	public Long getDataElementId() {
		return dataElementId;
	}

	public void setDataElementId(Long dataElementId) {
		this.dataElementId = dataElementId;
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

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

}
