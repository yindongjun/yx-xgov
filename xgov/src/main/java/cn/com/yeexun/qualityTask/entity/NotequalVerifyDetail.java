package cn.com.yeexun.qualityTask.entity;

import com.alibaba.fastjson.JSON;

public class NotequalVerifyDetail {
	
	private String delimiter;
	
	private String notequalValue;

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

}
