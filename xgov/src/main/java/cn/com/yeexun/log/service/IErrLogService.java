package cn.com.yeexun.log.service;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.log.entity.ExecErrLog;

public interface IErrLogService extends IBaseService<ExecErrLog>{
	
	public ExecErrLog getByEtlFlowId(Long etlFlowId,  String type) throws Exception;
}
