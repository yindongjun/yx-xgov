package cn.com.yeexun.datamap.service;

import cn.com.yeexun.dataSource.entity.DataSource;

import com.alibaba.fastjson.JSONObject;

public interface IDataMapService {

	JSONObject getFirstMap() throws Exception;

	Float getDataSize(DataSource dataSource)throws Exception;
	
	public JSONObject getDetailInfo() throws Exception;

}
