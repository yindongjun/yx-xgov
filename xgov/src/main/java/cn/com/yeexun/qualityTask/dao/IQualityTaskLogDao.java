package cn.com.yeexun.qualityTask.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.qualityTask.entity.QualityTaskLog;

public interface IQualityTaskLogDao extends BaseDao<QualityTaskLog> {
	
	List<QualityTaskLog> getLogsByTable(@Param("taskHisId") Long taskHisId
			, @Param("tableName") String tableName);

}
