package cn.com.yeexun.dataElement.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * @author yx-hj
 *
 */
public class DataElementVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String dataElementName;
	
	private String dataElementCode;
	
	private String dataElementType;
	
	private String dataElementAttr;
	
	private Integer status;
	
	private String dataElementFlag;
	
	private String definition;
	
	private Long codeset;
	
	private String regularExpression;
	
	private String remark;
	
	private Integer dataRangeFront;
	
	private Integer dataRangeEnd;
	
	private Integer frontStatus;
	
	private String codesetName;
	
	private String dataType;
	
	private Integer count;
	
	private String isdel;
	
	private String codeList;
	
	private String changeInfo;
	
	private String uniqueCode;
	
	public String getChangeInfo() {
		return changeInfo;
	}

	public void setChangeInfo(String changeInfo) {
		this.changeInfo = changeInfo;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getDataElementAttr() {
		return dataElementAttr;
	}

	public void setDataElementAttr(String dataElementAttr) {
		this.dataElementAttr = dataElementAttr;
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

	public Long getCodeset() {
		return codeset;
	}

	public void setCodeset(Long codeset) {
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

	public Integer getDataRangeFront() {
		return dataRangeFront;
	}

	public void setDataRangeFront(Integer dataRangeFront) {
		this.dataRangeFront = dataRangeFront;
	}

	public Integer getDataRangeEnd() {
		return dataRangeEnd;
	}

	public void setDataRangeEnd(Integer dataRangeEnd) {
		this.dataRangeEnd = dataRangeEnd;
	}

	public Integer getFrontStatus() {
		return frontStatus;
	}

	public void setFrontStatus(Integer frontStatus) {
		this.frontStatus = frontStatus;
	}

	public String getCodesetName() {
		return codesetName;
	}

	public void setCodesetName(String codesetName) {
		this.codesetName = codesetName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
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

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	
	

}
