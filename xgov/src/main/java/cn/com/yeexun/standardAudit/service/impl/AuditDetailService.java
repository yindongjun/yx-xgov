package cn.com.yeexun.standardAudit.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.standardAudit.dao.IAuditDetailDao;
import cn.com.yeexun.standardAudit.entity.AuditDetail;
import cn.com.yeexun.standardAudit.service.IAuditDetailService;

@Service
public class AuditDetailService extends BaseService<AuditDetail> implements IAuditDetailService {
	
	@Autowired
	private IAuditDetailDao auditDetailDao;
	
	@Override
	public List<AuditDetail> listAuditDetailPage(Long standardAuditId,
			Page<AuditDetail> page) throws Exception {
		return auditDetailDao.listAuditDetailPage(standardAuditId, page);
	}
}
