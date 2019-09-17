package cn.com.yeexun.documentManagement.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.documentManagement.entity.DocumentContent;
import cn.com.yeexun.documentManagement.entity.DocumentFile;

public interface IDocumentContentService extends IBaseService<DocumentContent> {
	public List<Object> getMenuDocumentContent() throws Exception;
	
	public void addDocumentContent(Long id,Long parentId,String name,Long userId) throws Exception;
	
	public String getFullPath(Long id) throws Exception;
	
	public List<DocumentContent> getLowerMenu(Long id) throws Exception;
	
	public void deleteMenu(Long id) throws Exception;

	public void deleteFolder(Long id) throws Exception;
	
	public Map<String, Object> upload(Long id,Map<String, Object> map,
			HttpServletRequest request) throws Exception;

	public List<DocumentFile> getDocumentFile(Long contentId, Page<DocumentFile> page);
	
	public boolean isExistContent(Long parentId,String name) throws Exception;
}
