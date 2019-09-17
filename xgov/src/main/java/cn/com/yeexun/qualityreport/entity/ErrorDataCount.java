package cn.com.yeexun.qualityreport.entity;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class ErrorDataCount {

	private Long datasourceId;
	
	private String datasourceName;

	private String tableName;

	private int formatError;
	private int nullError;
	private int intervalError;
	private int enumError;
	private int regularError;
	private int uniqueError;
	private int relationError;
//	private int totalError;
	private int errorDataNum;
	private int totalRows;
	
	private double rightPercentage;
	
	private Map<String, Long> percentageOfVerifyType = new HashMap<>();
	private Map<String, Integer> scoreMap = new HashMap<>(); 

	public Long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(Long datasourceId) {
		this.datasourceId = datasourceId;
	}
	
	public String getDatasourceName() {
		return datasourceName;
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getFormatError() {
		return formatError;
	}

	public void setFormatError(int formatError) {
		this.formatError = formatError;
	}

	public int getNullError() {
		return nullError;
	}

	public void setNullError(int nullError) {
		this.nullError = nullError;
	}

	public int getIntervalError() {
		return intervalError;
	}

	public void setIntervalError(int intervalError) {
		this.intervalError = intervalError;
	}

	public int getEnumError() {
		return enumError;
	}

	public void setEnumError(int enumError) {
		this.enumError = enumError;
	}

	public int getRegularError() {
		return regularError;
	}

	public void setRegularError(int regularError) {
		this.regularError = regularError;
	}

	public int getUniqueError() {
		return uniqueError;
	}

	public void setUniqueError(int uniqueError) {
		this.uniqueError = uniqueError;
	}

	public int getRelationError() {
		return relationError;
	}

	public void setRelationError(int relationError) {
		this.relationError = relationError;
	}
	
	/*public int getTotalError() {
		return totalError;
	}

	public void setTotalError(int totalError) {
		this.totalError = totalError;
	}*/
	
	public int getErrorDataNum() {
		return errorDataNum;
	}

	public void setErrorDataNum(int errorDataNum) {
		this.errorDataNum = errorDataNum;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	
	public Map<String, Long> getPercentageOfVerifyType() {
		return percentageOfVerifyType;
	}

	public void setPercentageOfVerifyType(Map<String, Long> percentageOfVerifyType) {
		this.percentageOfVerifyType = percentageOfVerifyType;
	}
	
	public Map<String, Integer> getScoreMap() {
		return scoreMap;
	}

	public void setScoreMap(Map<String, Integer> scoreMap) {
		this.scoreMap = scoreMap;
	}
	
	public double getRightPercentage() {
		return rightPercentage;
	}

	public void setRightPercentage(double rightPercentage) {
		this.rightPercentage = rightPercentage;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
