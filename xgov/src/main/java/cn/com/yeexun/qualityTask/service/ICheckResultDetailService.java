package cn.com.yeexun.qualityTask.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.qualityTask.entity.CheckResult;

public interface ICheckResultDetailService extends IBaseService<CheckResult>  {
	List<CheckResult> getCheckResultDetail(Long id, Page<CheckResult> page);

	int count(Long id);

//	File exportToExcel(Long resultCountId); 
	String exportToExcel(Long resultCountId, HttpServletRequest request); 
}
