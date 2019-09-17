package cn.com.yeexun.standardAudit.service;

import java.util.List;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.standardAudit.entity.AuditDetail;

public interface IAuditDetailService extends IBaseService<AuditDetail> {
	public List<AuditDetail> listAuditDetailPage(Long standardAuditId,Page<AuditDetail> page) throws Exception;
}
