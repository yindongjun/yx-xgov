package cn.com.yeexun.documentManagement.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_document_contents")
public class DocumentContent extends Unique implements OperationTimeAware {
	private long userId;
	
	private String name;
	
	private long parentId;
	
	private int isdel;
	
	private Date createTime;
	
	private Date lastModifyTime;
	
	@Transient
	private List<DocumentContent> children;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
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

	public List<DocumentContent> getChildren() {
		return children;
	}

	public void setChildren(List<DocumentContent> children) {
		this.children = children;
	}

}
