package cn.com.yeexun.meta_data.entity;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_metadata_attr")
public class MetadataAttrEntity extends Unique implements OperationTimeAware {
	
	private static final long serialVersionUID = 1L;

	private Long metadataId;
	
	private Long modelAttrId;
	
	private String attrValue;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastModifyTime;
	
	private Long createUserId;
	
	@Transient
	private String attrKey;
	
	public String getAttrKey() {
		return attrKey;
	}

	public void setAttrKey(String attrKey) {
		this.attrKey = attrKey;
	}

	@Transient
	private String name;
	
	@Transient
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(Long metadataId) {
		this.metadataId = metadataId;
	}

	public Long getModelAttrId() {
		return modelAttrId;
	}

	public void setModelAttrId(Long modelAttrId) {
		this.modelAttrId = modelAttrId;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	@Override
	public String toString() {
		return "MetadataAttrEntity [id=" + id + ", metadataId=" + metadataId + ", modelAttrId=" + modelAttrId
				+ ", attrValue=" + attrValue + ", createTime=" + createTime + ", createUserId=" + createUserId
				+ ", attrKey=" + attrKey + ", name=" + name + ", code=" + code + "]";
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}


	
}
