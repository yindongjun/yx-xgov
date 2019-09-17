package cn.com.yeexun.metaModel.entity;

import javax.persistence.Table;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_metamodel_attr")
public class MetamodelAttrEntity extends Unique {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long modelId;

    private String code;

    private String name;

    private String type;

    private Integer length;

    private String readable;

    private String required;

    private String buildin;

    private String controlsType;

    private Integer sortKey;

    private String createTime;

    private String updateTime;

    private String createUser;

    private String updateUser;

    public MetamodelAttrEntity(Long id, Long modelId, String code, String name, String type, Integer length, String readable, String required, String buildin, String controlsType, Integer sortKey, String createTime, String updateTime, String createUser, String updateUser) {
        this.id = id;
        this.modelId = modelId;
        this.code = code;
        this.name = name;
        this.type = type;
        this.length = length;
        this.readable = readable;
        this.required = required;
        this.buildin = buildin;
        this.controlsType = controlsType;
        this.sortKey = sortKey;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.createUser = createUser;
        this.updateUser = updateUser;
    }

    public MetamodelAttrEntity() {
        super();
    }

    public Long getModelId() {
        return modelId;
    }

    public MetamodelAttrEntity setModelId(Long modelId) {
        this.modelId = modelId;
        return this;
    }

    public String getCode() {
        return code;
    }

    public MetamodelAttrEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public MetamodelAttrEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public MetamodelAttrEntity setType(String type) {
        this.type = type == null ? null : type.trim();
        return this;
    }

    public Integer getLength() {
        return length;
    }

    public MetamodelAttrEntity setLength(Integer length) {
        this.length = length;
        return this;
    }

    public String getReadable() {
        return readable;
    }

    public MetamodelAttrEntity setReadable(String readable) {
        this.readable = readable == null ? null : readable.trim();
        return this;
    }

    public String getRequired() {
        return required;
    }

    public MetamodelAttrEntity setRequired(String required) {
        this.required = required == null ? null : required.trim();
        return this;
    }

    public String getBuildin() {
        return buildin;
    }

    public MetamodelAttrEntity setBuildin(String buildin) {
        this.buildin = buildin == null ? null : buildin.trim();
        return this;
    }

    public String getControlsType() {
        return controlsType;
    }

    public MetamodelAttrEntity setControlsType(String controlsType) {
        this.controlsType = controlsType == null ? null : controlsType.trim();
        return this;
    }

    public Integer getSortKey() {
        return sortKey;
    }

    public MetamodelAttrEntity setSortKey(Integer sortKey) {
        this.sortKey = sortKey;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public MetamodelAttrEntity setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public MetamodelAttrEntity setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public MetamodelAttrEntity setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public MetamodelAttrEntity setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

	@Override
	public String toString() {
		return "MetamodelAttrEntity [modelId=" + modelId + ", code=" + code + ", name=" + name + ", type=" + type
				+ ", length=" + length + ", readable=" + readable + ", required=" + required + ", buildin=" + buildin
				+ ", controlsType=" + controlsType + ", sortKey=" + sortKey + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", createUser=" + createUser + ", updateUser=" + updateUser + "]";
	}
    
    
}