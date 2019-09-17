package cn.com.yeexun.dataSet.entity;

import java.util.Date;

import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_element_set")
public class ElementSetEntity extends Unique implements OperationTimeAware {
	
	private static final long serialVersionUID = 1L;
	
	private Long setId;
	
	private Long elementId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastModifyTime;
	
	public Long getElementId() {
		return elementId;
	}

	public Date getCreateTime() {
		return createTime;
	}


	public void setElementId(Long elementId) {
		this.elementId = elementId;
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

	public Long getSetId() {
		return setId;
	}

	public void setSetId(Long setId) {
		this.setId = setId;
	}



	

}
