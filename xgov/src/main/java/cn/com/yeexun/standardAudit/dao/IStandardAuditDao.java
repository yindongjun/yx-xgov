package cn.com.yeexun.standardAudit.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.standardAudit.entity.StandardAudit;

public interface IStandardAuditDao extends BaseDao<StandardAudit> {
	
	public List<StandardAudit> listStandardAuditPage(@Param("status") String status,
			@Param("auditResult") String auditResult,
			@Param("type") String type, 
			@Param("taskName") String taskName,
			@Param("page") Page<StandardAudit> page);
	
	public StandardAudit getByFlowId(@Param("type") String type, @Param("flowId") Long flowId);

	StandardAudit changeDetail(@Param("id") Long id, @Param("type") String type);

}
