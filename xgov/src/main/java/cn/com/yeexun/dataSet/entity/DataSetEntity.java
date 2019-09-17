package cn.com.yeexun.dataSet.entity;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_data_set")
public class DataSetEntity extends Unique implements OperationTimeAware{

	private static final long serialVersionUID = 1L;
	
	public static final int STATUS_DRAFT = 0;
	public static final int STATUS_UNAUDIT = 1;
	public static final int STATUS_AUDITED = 2;
	public static final int STATUS_CHANGE = 3;
	public static final int STATUS_REJECT = 4;
	
	private Long parentId;
	
	private String name;
	
	private String code;
	
	private String isMenu;
	
	private Long createUserId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastModifyTime;
	
	private String descrption;
	
	private Integer status;
	
	private String changeInfo;
	
	private Integer frontStatus;
	
	private String isdel;
	
	@Transient
	private String pname;

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
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

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
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


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getChangeInfo() {
		return changeInfo;
	}

	public void setChangeInfo(String changeInfo) {
		this.changeInfo = changeInfo;
	}

	public Integer getFrontStatus() {
		return frontStatus;
	}

	public void setFrontStatus(Integer frontStatus) {
		this.frontStatus = frontStatus;
	}

	public String getDescrption() {
		return descrption;
	}

	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}





	
}
