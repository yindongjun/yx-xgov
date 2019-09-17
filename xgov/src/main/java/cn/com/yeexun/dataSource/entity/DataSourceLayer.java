package cn.com.yeexun.dataSource.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_source_layer")
public class DataSourceLayer extends Unique implements OperationTimeAware {
	
	
	private static final long serialVersionUID = -856695212343336394L;

	public static final Long ROOT_ID = 1L;
	public static final Long ROOT_PARENT_ID = -1L;
	//private int id;
	
	private String name;
	
	private Long parentId;
	
	private String remark;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastModifyTime;
	
	private String uniqueCode;
	
	@Transient
	private List<DataSourceLayer> children;
	
	@Transient
	private String superiorLayer;
	
	private String buildin;
	
	private String deleteFlag;
	
	

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
	

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public List<DataSourceLayer> getChildren() {
		return children;
	}

	public void setChildren(List<DataSourceLayer> children) {
		this.children = children;
	}

	public String getSuperiorLayer() {
		return superiorLayer;
	}

	public void setSuperiorLayer(String superiorLayer) {
		this.superiorLayer = superiorLayer;
	}

	public String getBuildin() {
		return buildin;
	}

	public void setBuildin(String buildin) {
		this.buildin = buildin;
	}
	
	
	
	
}
