package cn.com.yeexun.metaModel.entity;

import javax.persistence.Table;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_metamodel_comb")
public class MetamodelCombEntity extends Unique {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

    private String name;

    private String modelCode;

    private String modelName;

    private Long modelId;

    private String combinModelId;

    private String combinModelCode;

    private String combinModelName;

    public MetamodelCombEntity(Long id, String code, String name, String modelCode, String modelName, Long modelId, String combinModelId, String combinModelCode, String combinModelName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.modelCode = modelCode;
        this.modelName = modelName;
        this.modelId = modelId;
        this.combinModelId = combinModelId;
        this.combinModelCode = combinModelCode;
        this.combinModelName = combinModelName;
    }

    public MetamodelCombEntity() {
        super();
    }

    
    public String getCode() {
        return code;
    }

    public MetamodelCombEntity setCode(String code) {
        this.code = code == null ? null : code.trim();
        return this;
    }

    public String getName() {
        return name;
    }

    public MetamodelCombEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getModelCode() {
        return modelCode;
    }

    public MetamodelCombEntity setModelCode(String modelCode) {
        this.modelCode = modelCode;
        return this;
    }

    public String getModelName() {
        return modelName;
    }

    public MetamodelCombEntity setModelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public Long getModelId() {
        return modelId;
    }

    public MetamodelCombEntity setModelId(Long modelId) {
        this.modelId = modelId;
        return this;
    }

    public String getCombinModelId() {
        return combinModelId;
    }

    public MetamodelCombEntity setCombinModelId(String combinModelId) {
        this.combinModelId = combinModelId;
        return this;
    }

    public String getCombinModelCode() {
        return combinModelCode;
    }

    public MetamodelCombEntity setCombinModelCode(String combinModelCode) {
        this.combinModelCode = combinModelCode;
        return this;
    }

    public String getCombinModelName() {
        return combinModelName;
    }

    public MetamodelCombEntity setCombinModelName(String combinModelName) {
        this.combinModelName = combinModelName;
        return this;
    }

	@Override
	public String toString() {
		return "MetamodelCombEntity [code=" + code + ", name=" + name + ", modelCode=" + modelCode + ", modelName="
				+ modelName + ", modelId=" + modelId + ", combinModelId=" + combinModelId + ", combinModelCode="
				+ combinModelCode + ", combinModelName=" + combinModelName + "]";
	}
    
    
}