package cn.com.yeexun.dataSource.entity;

import java.util.List;

import com.alibaba.fastjson.JSONArray;


public class ExcuteResult {
	private int rowNum;
	private int colNum;
	private List<String> colList;
	private JSONArray datamap;
	
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public int getColNum() {
		return colNum;
	}
	public void setColNum(int colNum) {
		this.colNum = colNum;
	}
	public List<String> getColList() {
		return colList;
	}
	public void setColList(List<String> colList) {
		this.colList = colList;
	}
	public JSONArray getDatamap() {
		return datamap;
	}
	public void setDatamap(JSONArray datamap) {
		this.datamap = datamap;
	}
	@Override
	public String toString() {
		return "ExcuteResult [rowNum=" + rowNum + ", colNum=" + colNum
				+ ", colList=" + colList + ", datamap=" + datamap + "]";
	}

}
