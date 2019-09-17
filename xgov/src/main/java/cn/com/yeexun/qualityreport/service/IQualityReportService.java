package cn.com.yeexun.qualityreport.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.qualityreport.entity.QualityMeasureEntity;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityreport.entity.QualityGradeEntity;
import cn.com.yeexun.qualityreport.entity.QualityReportEntity;
import cn.com.yeexun.qualityreport.entity.QualityReportTemplateEntity;

public interface IQualityReportService extends IBaseService<QualityReportEntity> {
	
	QualityReportEntity createQualityReport(String designTableInfoIds, String name);

	void addReportTemplte(List<QualityGradeEntity> grades, List<QualityMeasureEntity> evaluationIndexs,
			String exportModules);

	QualityReportTemplateEntity showReportTemplte(Long templateId);

	List<QualityReportEntity> listReports(@Param("") Page<DesignTableInfo> page, @Param("") Date startDate
			, @Param("") Date endDate);

	QualityReportEntity showReport(Long reportId);

	void deleteReport(String reportIds);

}
