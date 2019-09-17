package cn.com.yeexun.standardAudit.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.standardAudit.entity.AuditDetail;

public interface IAuditDetailDao extends BaseDao<AuditDetail> {
	public List<AuditDetail> listAuditDetailPage(@Param("standardAuditId") Long standardAuditId,
			@Param("page") Page<AuditDetail> page
			);
}
