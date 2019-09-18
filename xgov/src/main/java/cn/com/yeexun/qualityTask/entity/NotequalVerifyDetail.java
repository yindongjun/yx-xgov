package cn.com.yeexun.qualityTask.entity;

import com.alibaba.fastjson.JSON;

public class NotequalVerifyDetail {
	
	private String delimiter;
	
	private String notequalValue;
	
	private String targetColumn;

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getNotequalValue() {
		return notequalValue;
	}

	public void setNotequalValue(String notequalValue) {
		this.notequalValue = notequalValue;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);	
	}

	public String getTargetColumn() {
		return targetColumn;
	}

	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}

}
