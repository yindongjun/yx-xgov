package cn.com.yeexun.metaModel.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_metamodel")
public class MetamodelEntity extends Unique {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Long parentId;

    private String code;

    private String name;

    private String icoId;

    private String discription;

    private String createTime;

    private String updateTime;

    private String createUser;

    private String updateUser;

    private String isMenu;

    private String buildin;

    public MetamodelEntity(Long id, Long parentId, String code, String name, String icoId, String discription, String createTime, String updateTime, String createUser, String updateUser, String isMenu, String buildin) {
        this.id = id;
        this.parentId = parentId;
        this.code = code;
        this.name = name;
        this.icoId = icoId;
        this.discription = discription;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.isMenu = isMenu;
        this.buildin = buildin;
    }

    public MetamodelEntity() {
        super();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public String getIcoId() {
        return icoId;
    }

    public void setIcoId(String icoId) {
        this.icoId = icoId;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
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
    @Transient
   	private List<MetamodelEntity> children = new ArrayList<MetamodelEntity>();

   	public List<MetamodelEntity> getChildren() {
   		return children;
   	}

   	public void setChildren(List<MetamodelEntity> children) {
   		this.children = children;
   	}
       
}