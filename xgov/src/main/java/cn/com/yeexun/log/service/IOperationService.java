package cn.com.yeexun.log.service;

import java.util.List;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.log.entity.OperationLog;

public interface IOperationService extends IBaseService<OperationLog> {
	
	List<OperationLog> searchOperationLogPage(String personnel,
			String operationMode, String startTime, 
			String endTime, Page<OperationLog> page) throws Exception;
	
	String geturlChinese(String url)throws Exception;

	List<OperationLog> getOperationLogByNum(int i)throws Exception;
}

