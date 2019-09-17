package cn.com.yeexun.documentManagement.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.documentManagement.entity.DocumentFile;

public interface IDocumentFileDao extends BaseDao<DocumentFile> {
	
	public List<DocumentFile> getAllFilePage(@Param("isdel") long isdel
			, @Param("contentId") long contentId
			, @Param("page") Page<DocumentFile> page);
	
	public List<DocumentFile> getAllFileByIsdel(@Param("isdel") long isdel);
	
	
	public List<DocumentFile> getAllFileNum();
	
	public List<DocumentFile> getFileByNameAndType(@Param("name") String name,@Param("type") String type,@Param("contentId") Long contentId);
	
	public Integer getFileNumByNowTime(@Param("createTime") Date createTime);
	
}
