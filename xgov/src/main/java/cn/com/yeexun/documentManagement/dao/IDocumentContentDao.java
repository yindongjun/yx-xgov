package cn.com.yeexun.documentManagement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.documentManagement.entity.DocumentContent;

public interface IDocumentContentDao extends BaseDao<DocumentContent> {
	
	public List<DocumentContent> getAllContentByPid(@Param("isdel") long isdel,@Param("parentId") long parentId);
	
	public List<DocumentContent> getAllContent(@Param("isdel") long isdel);
	
	public DocumentContent getAllContentByName(@Param("parentId") long parentId,@Param("name") String name);
}
