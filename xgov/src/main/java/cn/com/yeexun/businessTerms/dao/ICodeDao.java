package cn.com.yeexun.businessTerms.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.businessTerms.entity.Code;

public interface ICodeDao extends BaseDao<Code> {
	public List<Code> getCodeList(
			@Param("id") long id,
			@Param("name") String name,
			@Param("code") String code,
			@Param("codesetId") long codesetId);
	
	public List<Code> getAllCodeBycodesetIdPage(@Param("isdel") int isdel
			, @Param("codesetId") long codesetId, @Param("status") String status
			, @Param("page") Page<Code> page);
	
	public List<Code> getAllCodeBycodesetId(@Param("isdel") int isdel
			, @Param("codesetId") long codesetId);
	
	public List<Code> getAllCodeByPid(@Param("isdel") int isdel,@Param("parentId") long parentId);
	
	public List<Code> getAllCode(@Param("isdel") int isdel);
	
	public List<Code> getCodeById(@Param("isdel") int isdel,@Param("id") Long id);
	
	public List<Code> getCodeLastFive();
	
	public Integer getCodeNumByNowTime(@Param("createTime") Date createTime);
}
