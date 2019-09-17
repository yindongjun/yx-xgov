package cn.com.yeexun.qualityTask.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.qualityTask.dao.IQualityTaskLogDao;
import cn.com.yeexun.qualityTask.entity.QualityTaskLog;
import cn.com.yeexun.qualityTask.service.IQualityTaskLogService;

@Service
public class QualityTaskLogService extends BaseService<QualityTaskLog> implements IQualityTaskLogService {
	
	@Autowired
	private IQualityTaskLogDao qualityTaskLogDao;

	@Override
	public List<QualityTaskLog> getLogsByTable(Long taskHisId, String tableName) {
		return qualityTaskLogDao.getLogsByTable(taskHisId, tableName);
	}

}
