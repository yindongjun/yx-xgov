package cn.com.yeexun.documentManagement.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.documentManagement.entity.DocumentFile;

public interface IDocumentFileService extends IBaseService<DocumentFile> {

	public void deleteFile(Long id) throws Exception;

	public Map<String, Object> updateFile(Long id, String name, Map<String, Object> map,
			HttpServletRequest request) throws Exception;

	public void downLoadFile(long id, HttpServletResponse resp) throws Exception;
	
	public int getAllFileNum() throws Exception;
	
	public boolean isExistFile(String name,String type,Long contentId) throws Exception;
	
	List<Integer> getFileNumByNowTime() throws Exception;

}
