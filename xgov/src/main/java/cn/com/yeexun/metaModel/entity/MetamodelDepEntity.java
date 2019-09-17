package cn.com.yeexun.metaModel.entity;

import javax.persistence.Table;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_metamodel_dep")
public class MetamodelDepEntity extends Unique {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private String modelId;

    private String modelCode;

    private String modelName;

    private String depModelId;

    private String depModelCode;

    private String depModelName;

    public MetamodelDepEntity(Long id, String code, String name, String modelId, String modelCode, String modelName, String depModelId, String depModelCode, String depModelName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.modelId = modelId;
        this.modelCode = modelCode;
        this.modelName = modelName;
        this.depModelId = depModelId;
        this.depModelCode = depModelCode;
        this.depModelName = depModelName;
    }

    public MetamodelDepEntity() {
        super();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId == null ? null : modelId.trim();
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDepModelId() {
        return depModelId;
    }

    public void setDepModelId(String depModelId) {
        this.depModelId = depModelId;
    }

    public String getDepModelCode() {
        return depModelCode;
    }

    public void setDepModelCode(String depModelCode) {
        this.depModelCode = depModelCode;
    }

    public String getDepModelName() {
        return depModelName;
    }

    public void setDepModelName(String depModelName) {
        this.depModelName = depModelName;
    }

	@Override
	public String toString() {
		return "MetamodelDepEntity [code=" + code + ", name=" + name + ", modelId=" + modelId + ", modelCode="
				+ modelCode + ", modelName=" + modelName + ", depModelId=" + depModelId + ", depModelCode="
				+ depModelCode + ", depModelName=" + depModelName + "]";
	}
    
    
    
}