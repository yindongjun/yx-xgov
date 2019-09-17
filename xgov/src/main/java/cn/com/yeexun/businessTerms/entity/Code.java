package cn.com.yeexun.businessTerms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_code")
public class Code extends Unique implements OperationTimeAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int STATUS_DRAFT = 0;
	public static final int STATUS_UNAUDIT = 1;
	public static final int STATUS_AUDITED = 2;
	public static final int STATUS_CHANGE = 3;
	public static final int STATUS_REJECT = 4;
	 
	//代码
	private String code;
	
	//代码集id
	private long codesetId;
	
	//代码名称
	private String name;
	
	private long parentId;
	
	//所属目录
	private String path;
	
	//审核状态(0:待审核,1:通过,2:退回)
	private String status;
	
	private int isdel;
	
	private String ismenu;
	
	//代码唯一标识码
	private String uniqueCode;
	
	//代码送审前状态值
	private String frontStatus;
	
	//变更原因
	private String changeInfo;
	
	private Date createTime;
	
	private Date lastModifyTime;
	
	@Transient
	private List<Code> children;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	

	public long getCodesetId() {
		return codesetId;
	}

	public void setCodesetId(long codesetId) {
		this.codesetId = codesetId;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Code> getChildren() {
		return children;
	}

	public void setChildren(List<Code> children) {
		this.children = children;
	}

	
	
	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}
	
	

	public String getIsmenu() {
		return ismenu;
	}

	public void setIsmenu(String ismenu) {
		this.ismenu = ismenu;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public String getFrontStatus() {
		return frontStatus;
	}

	public void setFrontStatus(String frontStatus) {
		this.frontStatus = frontStatus;
	}

	public String getChangeInfo() {
		return changeInfo;
	}

	public void setChangeInfo(String changeInfo) {
		this.changeInfo = changeInfo;
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

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

	

}
