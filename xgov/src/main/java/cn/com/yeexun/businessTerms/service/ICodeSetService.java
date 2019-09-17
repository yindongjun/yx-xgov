package cn.com.yeexun.businessTerms.service;

import java.io.File;
import java.util.List;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.businessTerms.entity.CodeSet;

public interface ICodeSetService extends IBaseService<CodeSet> {
	public List<CodeSet>  getCodeSet() throws Exception;

	public void updateAndAddCodeSet(CodeSet codeSetEntity) throws Exception;
	
	public List<CodeSet> getLowerMenu(Long id) throws Exception;

	public void deleteCodeSets(Long id) throws Exception;

	public CodeSet echoCodeSet(Long id) throws Exception;
	
	public boolean isDuplicate(CodeSet codeSetEntity) throws Exception;
	
	public int getAllCodeSetNum() throws Exception;
	
	void importFromExcel(File file, Integer parentId);


	void getAllParent(Long codesetId, StringBuilder parentIds);

	List<CodeSet> getAuditedCodeSet();

	void changeCode(Long id, String changeInfo);

	void getAllParentName(Long codesetId, StringBuilder parentNames);

	String exportToExcel(Long parentId);

}
