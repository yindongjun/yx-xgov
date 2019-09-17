package cn.com.yeexun.documentManagement.entity;

import java.util.Date;

import javax.persistence.Table;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_document_management")
public class DocumentFile extends Unique implements OperationTimeAware   {
	
	private String name;
	
	private String type;
	
	private String filePath;
	
	private Date createTime;
	
	private Date lastModifyTime;
	
	private long userId;
	
	private long isdel;
	
	private long contentId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	

	public long getIsdel() {
		return isdel;
	}

	public void setIsdel(long isdel) {
		this.isdel = isdel;
	}

	public long getContentId() {
		return contentId;
	}

	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	
	
	
}
