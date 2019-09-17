package cn.com.yeexun.meta_data.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

public class MetadataInfo extends Unique {
	
	private static final long serialVersionUID = -2184925494309115364L;
	
	private String metadataName;
	
	private Long metamodelId;
	
	private Long parentId;
	
	private Long sourceId;
	
	private String attrJson;
	
	private Date creatTime;
	
	private Integer isDel;
	
	private String isParent;
	
	private Long collectHistId;

	private String uid;
	
	private List<MetadataInfo> children;
	
	private String datasourceName;
	
	private String tableName;
	
	private Boolean isBind;
	
	public Boolean getIsBind() {
		return isBind;
	}

	public void setIsBind(Boolean isBind) {
		this.isBind = isBind;
	}

	public String getDatasourceName() {
		return datasourceName;
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<MetadataInfo> getChildren() {
		return children;
	}

	public void setChildren(List<MetadataInfo> children) {
		this.children = children;
	}

	public String getMetadataName() {
		return metadataName;
	}

	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}

	public Long getMetamodelId() {
		return metamodelId;
	}

	public void setMetamodelId(Long metamodelId) {
		this.metamodelId = metamodelId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getAttrJson() {
		return attrJson;
	}

	public void setAttrJson(String attrJson) {
		this.attrJson = attrJson;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public Long getCollectHistId() {
		return collectHistId;
	}

	public void setCollectHistId(Long collectHistId) {
		this.collectHistId = collectHistId;
	}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

    @Override
    public boolean equals(Object obj) {
        // 自反性
        if (this == obj) return true;
        // 任何对象不等于null，比较是否为同一类型
        if (!(obj instanceof MetadataInfo)) return false;
        MetadataInfo metadataInfo = (MetadataInfo)obj;
        return this.getId() == metadataInfo.getId() && Objects.equals(this.getMetadataName(), metadataInfo.getMetadataName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMetadataName());
    }

}