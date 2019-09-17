package cn.com.yeexun.qualityTask.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.qualityTask.entity.CheckResult;

public interface ICheckResultDetailDao extends BaseDao<CheckResult> {
	
//	List<CheckResult> getCheckResultByResultCountIdPage(@Param("resultCountId") Long resultCountId
//			, @Param("page") Page<CheckResult> page);
	
	List<CheckResult> getCheckResultByResultCountId(@Param("resultCountId") Long resultCountId
			, @Param("page") Page<CheckResult> page);
	
	int count(Long resultCountId);
	
}
