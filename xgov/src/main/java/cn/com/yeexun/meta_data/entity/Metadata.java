package cn.com.yeexun.meta_data.entity;
/**
 * 元数据存储主体表
 */
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;
import cn.com.yeexun.metaModel.entity.MetamodelCombEntity;

@Table(name = "tb_metadata")
public class Metadata extends Unique implements OperationTimeAware {

	private static final long serialVersionUID = 1L;

	private Long parentId;
	
	private String parentCode;
	
	private String name;
	
	private String code;
	
	private String isMenu;
	
	private String buildin;
	
	private Long metamodelId;
	
	private Long createUserId;
	
	private String path;
	
	private Long sourceId;
	
	private String metadataType;
	
//	private String uniqueCode;
	
	private Integer version;
	
	@Transient
	private String versionName;
	
	private String deleteFlag;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastModifyTime;
	
	private Integer isRelease;

	@Transient
	private List<Metadata> children;
	
	@Transient
	private List<MetadataAttrEntity> attrs;
	
	@Transient
	private List<MetamodelCombEntity> combins;
	
	@Transient
	private String metaModelName;
	
	private Long collectId;
	
	private String versionDescription;
	
	@Transient
	private Date startTime;
	
	/*@Transient
	private HDFSMetadataInfo hdfsInfo;
	
	
	public HDFSMetadataInfo getHdfsInfo() {
		return hdfsInfo;
	}

	public void setHdfsInfo(HDFSMetadataInfo info) {
		this.hdfsInfo = info;
	}*/

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getVersionDescription() {
		return versionDescription;
	}

	public void setVersionDescription(String versionDescription) {
		this.versionDescription = versionDescription;
	}

	
	public Integer getIsRelease() {
		return isRelease;
	}

	public void setIsRelease(Integer isRelease) {
		this.isRelease = isRelease;
	}

	public Long getCollectId() {
		return collectId;
	}

	public void setCollectId(Long collectId) {
		this.collectId = collectId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

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

	public String getIsMenu() {
		return isMenu;
	}

	public void setIsMenu(String isMenu) {
		this.isMenu = isMenu;
	}

	public String getBuildin() {
		return buildin;
	}

	public void setBuildin(String buildin) {
		this.buildin = buildin;
	}

	public Long getMetamodelId() {
		return metamodelId;
	}

	public void setMetamodelId(Long metamodelId) {
		this.metamodelId = metamodelId;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getMetadataType() {
		return metadataType;
	}

	public void setMetadataType(String metadataType) {
		this.metadataType = metadataType;
	}

//	public String getUniqueCode() {
//		return uniqueCode;
//	}
//
//	public void setUniqueCode(String uniqueCode) {
//		this.uniqueCode = uniqueCode;
//	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public List<Metadata> getChildren() {
		return children;
	}

	public void setChildren(List<Metadata> children) {
		this.children = children;
	}

	public List<MetadataAttrEntity> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<MetadataAttrEntity> attrs) {
		this.attrs = attrs;
	}

	public List<MetamodelCombEntity> getCombins() {
		return combins;
	}

	public void setCombins(List<MetamodelCombEntity> combins) {
		this.combins = combins;
	}

	public String getMetaModelName() {
		return metaModelName;
	}

	public void setMetaModelName(String metaModelName) {
		this.metaModelName = metaModelName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return "Metadata [parentId=" + parentId + ", name=" + name + ", code="
				+ code + ", isMenu=" + isMenu + ", buildin=" + buildin
				+ ", metamodelId=" + metamodelId + ", createUserId="
				+ createUserId + ", path=" + path + ", sourceId=" + sourceId
				+ ", metadataType=" + metadataType + ",  version=" + version + ", createTime="
				+ createTime + ", lastModifyTime=" + lastModifyTime
				+ ", children=" + children + ", attrs=" + attrs + ", combins="
				+ combins + ", metaModelName=" + metaModelName + ", isRelease="
				+ isRelease + ", collectId=" + collectId
				+ ", versionDescription=" + versionDescription + "]";
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
}
