package cn.com.yeexun.businessTerms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.businessTerms.entity.CodeSet;

public interface ICodeSetDao extends BaseDao<CodeSet>  {
	public List<CodeSet> getAllCodeSet(@Param("isdel") int isdel);
	
	List<CodeSet> getAuditedCodeSet();
	
	public CodeSet getCodeSet(@Param("isdel") int isdel,@Param("id") Long id);
	
	public List<CodeSet> getCodeSetList(
			@Param("name") String name,
			@Param("code") String code,
			@Param("parentId") int parentId,
			@Param("isCodeset") String isCodeset);
	
	public List<CodeSet> getAllCodeSetNum();

	List<CodeSet> getByParentId(Long codeSetId);

	List<CodeSet> getCodesetDir();
}
