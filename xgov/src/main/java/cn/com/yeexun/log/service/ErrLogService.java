package cn.com.yeexun.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.log.dao.IErrLogDao;
import cn.com.yeexun.log.entity.ExecErrLog;

@Service
public class ErrLogService extends BaseService<ExecErrLog> implements IErrLogService {
	
	@Autowired
	private IErrLogDao errLogDao;

	@Override
	public ExecErrLog getByEtlFlowId(Long etlFlowId, String type) throws Exception {
		return errLogDao.getByEtlFlowId(etlFlowId, type);
	}

}
