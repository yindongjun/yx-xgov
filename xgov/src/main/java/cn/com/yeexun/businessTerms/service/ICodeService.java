package cn.com.yeexun.businessTerms.service;

import java.util.List;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.businessTerms.entity.Code;

public interface ICodeService extends IBaseService<Code> {
	
	public void addCode(Code codeEntity,Long pid) throws Exception;
	
	public boolean isDuplicate(Code codeEntity) throws Exception;
	
	public void deleteCode(Long id) throws Exception;

	public List<Code> getCodesFromSetPage(Long codeSetId, String status, Page<Code> page) throws Exception;
	
	public List<Code> getCodesFromSet(Long codeSetId) throws Exception;

	public List<Code> getCodeByPid(Long pid) throws Exception;

	public boolean sendAudit(List<Long> idslist)throws Exception;

	public List<Code> getCode(Long id) throws Exception;

	public void submit(List<Long> ids,String status)throws Exception;
	
	public boolean isExistCodeSet(Long id)throws Exception;
	
	public void deleteCodeMetadata(List<Code> codeList) throws Exception;
	
	public int getAllCodeNum() throws Exception;
	
	public List<Code> getCodeLastFive() throws Exception;
	
	public List<Integer> getCodeNumByNowTime() throws Exception;
}
