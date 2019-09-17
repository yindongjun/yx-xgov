package cn.com.yeexun.qualityTask.service;

import java.util.List;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.qualityTask.entity.QualityTaskLog;

public interface IQualityTaskLogService extends IBaseService<QualityTaskLog> {
	List<QualityTaskLog> getLogsByTable(Long taskHisId, String tableName);
}
