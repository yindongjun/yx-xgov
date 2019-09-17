package cn.com.yeexun.log.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.log.dao.IOperationDao;
import cn.com.yeexun.log.entity.OperationLog;

import org.springframework.stereotype.Service;

@Service
public class OperationService extends BaseService<OperationLog> implements IOperationService {

	@Autowired
	private IOperationDao operationDao;
	
	/**
	 * 查询操作日志
	 * @param personnel 用户名
	 * @param operationMode 操作模块
	 * @param startTime 开始时间(操作时间)
	 * @param endTime 结束时间(操作时间)
	 * @param page 
	 * @return
	 */
	public List<OperationLog> searchOperationLogPage(String personnel, 
			String operationMode, String startTime,
			String endTime, Page<OperationLog> page) {
		return operationDao
				.searchOperationLogPage(personnel, operationMode ,startTime, endTime, page);
		
	}
	
	/**
	 * url中文映射
	 * @param url 操作url
	 * @return
	 */
	public String geturlChinese(String url) {
		return operationDao.geturlChinese(url);
	}

	@Override
	public List<OperationLog> getOperationLogByNum(int i) throws Exception {
		return operationDao.getOperationLogByNum(i);
	}
	
}
