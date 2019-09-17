package cn.com.yeexun.businessTerms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_codeset")
public class CodeSet extends Unique implements OperationTimeAware {

	
	private static final long serialVersionUID = 8313644536157429035L;
	
	//代码集序号
	private String code;
	
	//名称
	private String name;
	
	private int parentId;
	
	
	private Date createTime;
	
	private Date lastModifyTime;
	
	//是否是代码集(0:是，1:否)
	private String isCodeset;
	
	//说明
	private String explanation;
	
	//表示
	private String express;
	
	//编码规则
	private String codeRule;
	
	private int isdel;
	
	private String uniqueCode;
	
	private String createPolicy;
	
	
	public String getCreatePolicy() {
		return createPolicy;
	}

	public void setCreatePolicy(String createPolicy) {
		this.createPolicy = createPolicy;
	}

	@Transient
	private List<CodeSet> children;

	

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

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
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


	public String getIsCodeset() {
		return isCodeset;
	}

	public void setIsCodeset(String isCodeset) {
		this.isCodeset = isCodeset;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getCodeRule() {
		return codeRule;
	}

	public void setCodeRule(String codeRule) {
		this.codeRule = codeRule;
	}

	public List<CodeSet> getChildren() {
		return children;
	}

	public void setChildren(List<CodeSet> children) {
		this.children = children;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodeSet other = (CodeSet) obj;
		if (name != other.name)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	
	
	
}
