package cn.com.yeexun.dataElement.entity;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSON;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "tb_data_element")
public class DataElementEntity extends Unique implements OperationTimeAware{

	private static final long serialVersionUID = 1L;
	
	public static final int STATUS_DRAFT = 0;
	public static final int STATUS_UNAUDIT = 1;
	public static final int STATUS_AUDITED = 2;
	public static final int STATUS_CHANGE = 3;
	public static final int STATUS_REJECT = 4;
	
	private String dataElementName;
	
	private String dataElementCode;
	
	private String dataElementType;
	
	private String dataElementAttr;

	/**
	 * 数据元状态 0：草稿 1：待审核 2：已审核 3：变更中 4：退回
	 */
	private Integer status;
	
	private String dataElementFlag;
	
	private String definition;
	
	private String codeset;
	
	private String regularExpression;
	
	private String remark;
	
	private String dataRangeFront;
	
	private String dataRangeEnd;
	
	private Integer frontStatus;
	
	@Transient
	private String codesetName;
	
	@Transient
	private String dataType;
	
	@Transient
	private Integer count;
	
	private String isdel;
	
	private String codeList;
	/**
	 * 标准类型
	 */
	private String standardType;
	/**
	 * 标准别名
	 */
	private String standardAlias;
	
	@Transient
	private String tableName;
	
	@Transient
	private String fieldName;
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getCodesetName() {
		return codesetName;
	}

	public void setCodesetName(String codesetName) {
		this.codesetName = codesetName;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastModifyTime;
	
	private String changeInfo;
	
	private String uniqueCode;

	public String getDataElementName() {
		return dataElementName;
	}

	public void setDataElementName(String dataElementName) {
		this.dataElementName = dataElementName;
	}

	public String getDataElementCode() {
		return dataElementCode;
	}

	public void setDataElementCode(String dataElementCode) {
		this.dataElementCode = dataElementCode;
	}

	public String getDataElementType() {
		return dataElementType;
	}

	public void setDataElementType(String dataElementType) {
		this.dataElementType = dataElementType;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDataElementFlag() {
		return dataElementFlag;
	}

	public void setDataElementFlag(String dataElementFlag) {
		this.dataElementFlag = dataElementFlag;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getCodeset() {
		return codeset;
	}

	public void setCodeset(String codeset) {
		this.codeset = codeset;
	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
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

	public String getChangeInfo() {
		return changeInfo;
	}

	public void setChangeInfo(String changeInfo) {
		this.changeInfo = changeInfo;
	}

	public String getDataElementAttr() {
		return dataElementAttr;
	}

	public void setDataElementAttr(String dataElementAttr) {
		this.dataElementAttr = dataElementAttr;
	}

	public String getDataRangeFront() {
		return dataRangeFront;
	}

	public void setDataRangeFront(String dataRangeFront) {
		this.dataRangeFront = dataRangeFront;
	}

	public String getDataRangeEnd() {
		return dataRangeEnd;
	}

	public void setDataRangeEnd(String dataRangeEnd) {
		this.dataRangeEnd = dataRangeEnd;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public Integer getFrontStatus() {
		return frontStatus;
	}

	public void setFrontStatus(Integer frontStatus) {
		this.frontStatus = frontStatus;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public String getCodeList() {
		return codeList;
	}

	public void setCodeList(String codeList) {
		this.codeList = codeList;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getStandardType() {
		return standardType;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}

	public String getStandardAlias() {
		return standardAlias;
	}

	public void setStandardAlias(String standardAlias) {
		this.standardAlias = standardAlias;
	}

}
