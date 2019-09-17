package cn.com.yeexun.qualityreport.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityreport.entity.ErrorDataCount;
import cn.com.yeexun.qualityreport.entity.QualityReportEntity;

public interface QualityReportDao extends BaseDao<QualityReportEntity> {
	
	List<ErrorDataCount> countErrorDataByVerifyType(@Param("tableInfoIds") String tableInfoIds);
	
	List<ErrorDataCount> countAllErrorDataByVerifyType(@Param("tableInfoIds") String tableInfoIds);

	List<QualityReportEntity> listReportsPage(@Param("page") Page<DesignTableInfo> page
			, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	QualityReportEntity showReport(@Param("reportId") Long reportId);

	ErrorDataCount contAllErrorAndTotal(@Param("tableInfoIds") String tableInfoIds);

	void deleteReport(@Param("reportIds") String reportIds);
	
	

}
