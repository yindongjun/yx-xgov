package cn.com.yeexun.qualityreport.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.qualityTask.dao.ICheckResultCountDao;
import cn.com.yeexun.qualityTask.entity.CheckResultCount;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityreport.dao.QualityGradeDao;
import cn.com.yeexun.qualityreport.dao.QualityMeasureDao;
import cn.com.yeexun.qualityreport.dao.QualityReportDao;
import cn.com.yeexun.qualityreport.dao.QualityReportTempalteDao;
import cn.com.yeexun.qualityreport.entity.ErrorDataCount;
import cn.com.yeexun.qualityreport.entity.QualityGradeEntity;
import cn.com.yeexun.qualityreport.entity.QualityMeasureEntity;
import cn.com.yeexun.qualityreport.entity.QualityReportEntity;
import cn.com.yeexun.qualityreport.entity.QualityReportTemplateEntity;
import cn.com.yeexun.qualityreport.service.IQualityReportService;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import tk.mybatis.mapper.entity.Example;

@Service
public class QualityReportService extends BaseService<QualityReportEntity> 
		implements IQualityReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(QualityReportService.class);
	
	@Autowired
	private QualityReportTempalteDao reportTemplateDao;
	
	@Autowired
	private QualityGradeDao gradeDao;
	
	@Autowired
	private QualityMeasureDao measureDao;
	
	@Autowired
	private QualityReportDao reportDao;
	
	@Autowired
	private ICheckResultCountDao checkResultCountDao;
	
	@Autowired
	private DatasourceService2 datasourceService2;

//	private String[] split;
	
	@Override
	public QualityReportTemplateEntity showReportTemplte(Long templateId) {
		
		Example measureExample = new Example(QualityMeasureEntity.class);
		measureExample.createCriteria().andEqualTo("templateId", templateId)
				.andEqualTo("deleteFlag", 0).andEqualTo("parentId", -1);
		measureExample.orderBy("id");
		List<QualityMeasureEntity> rootMeasures = measureDao.selectByExample(measureExample);
		measureExample.clear();
		measureExample.createCriteria().andEqualTo("templateId", templateId)
				.andEqualTo("deleteFlag", 0).andNotEqualTo("parentId", -1);
		measureExample.orderBy("id");
		List<QualityMeasureEntity> childrenMesures = measureDao.selectByExample(measureExample);
		for (QualityMeasureEntity root : rootMeasures) {
			for (QualityMeasureEntity child : childrenMesures) {
				if (child.getParentId() == root.getId()) {
					if (root.getChildren() == null) {
						root.setChildren(new ArrayList<QualityMeasureEntity>());
					}
					root.getChildren().add(child);
				}
			}
		}
		Example selectGradesExample = new Example(QualityGradeEntity.class);
		selectGradesExample.createCriteria().andEqualTo("templateId", templateId).andEqualTo("deleteFlag", 0);
		selectGradesExample.orderBy("gradeOrder").desc();
		List<QualityGradeEntity> grades = gradeDao.selectByExample(selectGradesExample);
		QualityReportTemplateEntity reportTemplate = reportTemplateDao.selectByPrimaryKey(templateId);
		reportTemplate.setGrades(grades);
		reportTemplate.setMeasureIndexs(rootMeasures);
		return reportTemplate;
	}
	
	@Override
	public QualityReportEntity createQualityReport(final String designTableInfoIds, final String name) {
	
		Example reportExample = new Example(QualityReportEntity.class);
		reportExample.createCriteria().andEqualTo("name", name).andEqualTo("deleteFlag", 0);
		List<QualityReportEntity> existReport = reportDao.selectByExample(reportExample);
		if (existReport != null && existReport.size() > 0) {
			throw new CommonException("名为" + name + "的报告已经存在！");
		}
		final QualityReportEntity report = new QualityReportEntity();
		report.setDeleteFlag(Integer.valueOf(Constant.NOT_DELETE));
		report.setCreateTime(new Date());
		report.setName(name);
		report.setTemplateId(1L);
		report.setStatus(QualityReportEntity.status_creating);
		reportDao.insert(report);
		
		new Thread() {
			@Override
			public void run() {
				try {
					createQualityReportData(designTableInfoIds, name, report);
				} catch (Exception e) {
					logger.error("生成质量报告异常：", e);
				}
				
			};
		}.start();
		return null;
	}
	
//	@Override
//	@Transactional
	private QualityReportEntity createQualityReportData(String designTableInfoIds, String name, QualityReportEntity report) {
		
		/*QualityReportEntity report = new QualityReportEntity();
		report.setDeleteFlag(Integer.valueOf(Constant.NOT_DELETE));
		report.setCreateTime(new Date());
		report.setName(name);
		report.setTemplateId(1L);
		reportDao.insert(report);*/
		
		int tableInfoNum = designTableInfoIds.split(",", -1).length;
		List<ErrorDataCount> errorDataCounts = reportDao.countErrorDataByVerifyType(designTableInfoIds);
		
		Example measureExample = new Example(QualityMeasureEntity.class);
		measureExample.createCriteria().andEqualTo("templateId", 1L)
				.andEqualTo("deleteFlag", 0).andEqualTo("parentId", -1);
		List<QualityMeasureEntity> rootMeasures = measureDao.selectByExample(measureExample);
		measureExample.clear();
		measureExample.createCriteria().andEqualTo("templateId", 1L)
				.andEqualTo("deleteFlag", 0).andNotEqualTo("parentId", -1);
		List<QualityMeasureEntity> childrenMesures = measureDao.selectByExample(measureExample);
		for (QualityMeasureEntity root : rootMeasures) {
			for (QualityMeasureEntity child : childrenMesures) {
				if (child.getParentId() == root.getId()) {
					if (root.getChildren() == null) {
						root.setChildren(new ArrayList<QualityMeasureEntity>());
					}
					root.getChildren().add(child);
				}
			}
		}
		Set<String> verifyTypeCodes = new HashSet<>();
		for (QualityMeasureEntity child : childrenMesures) {
			verifyTypeCodes.add(child.getVerifyCode());
		}
		// 分别计算每张表每个类型下问题数据占总数据的百分比
		caculateErrorPercentage(errorDataCounts, verifyTypeCodes);
		// 计算每张表的综合得分
		caculateSumScoerForEachTable(errorDataCounts, rootMeasures);		
//System.out.println(JSON.toJSON(errorDataCounts));

		// 各表评分明细
		List<JSONObject> scoreMaps = new ArrayList<>();
		for (ErrorDataCount dataCount : errorDataCounts) {
			String scoreStr = JSON.toJSONString(dataCount.getScoreMap());
			JSONObject scoreMap = JSON.parseObject(scoreStr);
			scoreMap.put("表名", dataCount.getTableName());
			scoreMaps.add(scoreMap);
		}
System.out.println("各表评分明细：" + JSON.toJSONString(scoreMaps));
		// 计算数据质量评分雷达图
		List<ErrorDataCount> allErrorCounts = reportDao.countAllErrorDataByVerifyType(designTableInfoIds);
		ErrorDataCount allErrorAndTotalCount = reportDao.contAllErrorAndTotal(designTableInfoIds);
		
		/*int totalRows = 0;
		for (ErrorDataCount dataCount : errorDataCounts) {
			totalRows += dataCount.getTotalRows();
		}*/
		allErrorCounts.get(0).setTotalRows(allErrorAndTotalCount.getTotalRows());
		allErrorCounts.get(0).setErrorDataNum(allErrorAndTotalCount.getErrorDataNum());
		report.setAllSum(allErrorAndTotalCount.getTotalRows());
//		report.set
//System.out.println("allErrorCounts:" + JSON.toJSONString(allErrorCounts));
		caculateErrorPercentage(allErrorCounts, verifyTypeCodes);
		caculateSumScoerForEachTable(allErrorCounts, rootMeasures);
//System.out.println("雷达图：" + allErrorCounts);
		Map<String, Integer> scoreMap = allErrorCounts.get(0).getScoreMap();
		Integer allScore = allErrorCounts.get(0).getScoreMap().get("总得分");
		List<QualityGradeEntity> grades = gradeDao.selectAll();
		if (allScore <= 0) {
			report.setScoreGrade("不合格");
		} else {
			for (QualityGradeEntity grade : grades) {
				if (allScore > grade.getLowerLimit() && allScore <= grade.getUpperLimit()) {
					report.setScoreGrade(grade.getGradeName());
				}
			}
		}
		
		scoreMap.put("name", -1);
		Map<String, Integer> fullMarks = new HashMap<>();
		for (QualityMeasureEntity rootMeasure : rootMeasures) {
			String measureIndex = rootMeasure.getMeasureIndex();
			Integer weight = rootMeasure.getWeight();
			fullMarks.put(measureIndex, weight);
		}
		fullMarks.put("name", -2);
		fullMarks.put("总得分", 100);
		List<Map<String, Integer>> radarMap = new ArrayList<>();
		radarMap.add(fullMarks);
		radarMap.add(scoreMap);
System.out.println("radarMap:" + JSON.toJSONString(radarMap));
		// 问题数据分布图
		Map<String, Object> errorDataOfVerify = new HashMap<>();
		ErrorDataCount allErrorCount = allErrorCounts.get(0);
		errorDataOfVerify.put("格式校验", allErrorCount.getFormatError());
		errorDataOfVerify.put("正则校验", allErrorCount.getRegularError());
		errorDataOfVerify.put("空值校验", allErrorCount.getNullError());
//		errorDataOfVerify.put("精度校验", allErrorCount.getFormatError());
		errorDataOfVerify.put("值域校验", allErrorCount.getEnumError());
		errorDataOfVerify.put("数据范围", allErrorCount.getIntervalError());
		errorDataOfVerify.put("数据唯一性", allErrorCount.getUniqueError());
		errorDataOfVerify.put("字段值比对", allErrorCount.getRelationError());
System.out.println("问题数据分布图" + JSON.toJSONString(errorDataOfVerify));
		// top5数据合规率
		Collections.sort(errorDataCounts, new Comparator<ErrorDataCount>() {

			@Override
			public int compare(ErrorDataCount o1, ErrorDataCount o2) {
				return (int) (o2.getRightPercentage() - o1.getRightPercentage());
			}
			
		});
		List<ErrorDataCount> rightTop5 = errorDataCounts.subList(0
				, errorDataCounts.size() >= 5 ? 5 : errorDataCounts.size());
		
		String sourceIds = rightTop5.stream().map(ErrorDataCount :: getDatasourceId).distinct()
				.map(sourceId -> String.valueOf(sourceId)).collect(Collectors.joining(","));
		List<MetadataDatasource> sources = datasourceService2.getSourceByIds(sourceIds);
		for (ErrorDataCount errorCount : rightTop5) {
			for (MetadataDatasource source : sources) {
				if (errorCount.getDatasourceId() == source.getId()) {
					errorCount.setDatasourceName(source.getDatasourceName());
				}
			}
		}
		
		Map<String, Object> pieMapData = new HashMap<>();
		List<Map<String, Object>>  top5List = new ArrayList<>();
		for (ErrorDataCount errorDataCount : rightTop5) {
			Map<String, Object> pieMap = new HashMap<>();
			pieMap.put("表名", errorDataCount.getTableName());
			pieMap.put("所属数据源", errorDataCount.getDatasourceName());
			pieMap.put("原始数据", errorDataCount.getTotalRows());
			pieMap.put("标准数据", errorDataCount.getTotalRows() - errorDataCount.getErrorDataNum());
			pieMap.put("合规率", errorDataCount.getRightPercentage());
			top5List.add(pieMap);
		}
		pieMapData.put("percentage", allErrorCounts.get(0).getRightPercentage());
		pieMapData.put("tableNum", tableInfoNum);
		pieMapData.put("top5List", top5List);
System.out.println("top5:" + JSON.toJSONString(pieMapData));
		// 获取没有处理的问题数据数量前五的记录
		List<CheckResultCount> undealTop5 = checkResultCountDao.selectUndealTop5(designTableInfoIds);
		String undealTop5SourceIds = undealTop5.stream().map(CheckResultCount :: getDatasourceId).distinct()
				.map(sourceId -> String.valueOf(sourceId)).collect(Collectors.joining(","));
		List<MetadataDatasource> undealTop5Sources = datasourceService2.getSourceByIds(undealTop5SourceIds);
		for (CheckResultCount checkCount : undealTop5) {
			for (MetadataDatasource source : undealTop5Sources) {
				if (checkCount.getDatasourceId() == source.getId()) {
					checkCount.setDataSourceName(source.getDatasourceName());
				}
			}
		}
		
		int count = checkResultCountDao.selectCountRows(designTableInfoIds, 0);
		int totalCount = checkResultCountDao.selectCountRotalRows(designTableInfoIds);
		Map<String, Object> errorDataTop5 = new HashMap<>();
		errorDataTop5.put("percentage", (double)Math.round((count * 100.0 / totalCount) * 100) / 100);
		errorDataTop5.put("top5List", undealTop5);
System.out.println("问题数据处理情况：" + JSON.toJSONString(errorDataTop5));
		// 将计算的结果数据存到数据库中
//		QualityReportEntity report = new QualityReportEntity();
		
//		Map<String, Object> resultMap = new HashMap<>();
//		resultMap.put("", value)
		report.setScoreDetail(JSON.toJSONString(scoreMaps));
		report.setRadarMap(JSON.toJSONString(radarMap));
		report.setErrorDistribution(JSON.toJSONString(errorDataOfVerify));
		report.setRightTop5(JSON.toJSONString(pieMapData));
		report.setErrorDataHandle(JSON.toJSONString(errorDataTop5));
		report.setStatus(QualityReportEntity.statuc_created);
		reportDao.updateByPrimaryKeySelective(report);
		return null;
	}

	private void caculateErrorPercentage(List<ErrorDataCount> errorDataCounts, Set<String> verifyTypeCodes) {
		
		Example selectMeasureExample = new Example(QualityMeasureEntity.class);
		selectMeasureExample.createCriteria().andEqualTo("deleteFlag", 0).andNotEqualTo("parentId", -1);
		List<QualityMeasureEntity> measures = measureDao.selectByExample(selectMeasureExample);
		Map<String, String> measureCodeName = new HashMap<>();
		for (QualityMeasureEntity measure : measures) {
			measureCodeName.put(measure.getVerifyCode(), measure.getMeasureIndex());
		}
		for (ErrorDataCount dataCount : errorDataCounts) {
			Map<String, Long> percentageOfVerifyType = new HashMap<>();
			for (String verifyType : verifyTypeCodes) {
				Long percentage = null;
				switch (verifyType) {
				case QualityTaskDetail.DEFECT_VERIFY:
					percentage = Math.round(dataCount.getNullError() * 100.0 / dataCount.getTotalRows());
					break;
				case QualityTaskDetail.ENUM_VERIFY:
					percentage = Math.round(dataCount.getEnumError() * 100.0 / dataCount.getTotalRows());
					break;
				case QualityTaskDetail.FORMAT_VERIFY:
					percentage = Math.round(dataCount.getFormatError() * 100.0 / dataCount.getTotalRows());
					break;
				case QualityTaskDetail.INTERVAL_VERIFY:
					percentage = Math.round(dataCount.getIntervalError() * 100.0 / dataCount.getTotalRows());
					break;
				case QualityTaskDetail.REGULAR_VERIFY:
					percentage = Math.round(dataCount.getRegularError() * 100.0 / dataCount.getTotalRows());
					break;
				case QualityTaskDetail.RELATION_VERIFY:
					percentage = Math.round(dataCount.getRelationError() * 100.0 / dataCount.getTotalRows());
					break;
				case QualityTaskDetail.UNIQUE_VERIFY:
					percentage = Math.round(dataCount.getUniqueError() * 100.0 / dataCount.getTotalRows());
					break;
				default:
					break;
				}
				percentageOfVerifyType.put(measureCodeName.get(verifyType), percentage);
				
			}
			// 每张表的合规率
//			(double)Math.round(3.123 * 100) / 100;
			dataCount.setRightPercentage((double)Math.round((
					100 - (dataCount.getErrorDataNum() * 100.0 / dataCount.getTotalRows())) * 100) / 100);
			dataCount.setPercentageOfVerifyType(percentageOfVerifyType);
		}
	}

	private void caculateSumScoerForEachTable(List<ErrorDataCount> errorDataCounts,
			List<QualityMeasureEntity> rootMeasures) {
		Example selectMeasureExample = new Example(QualityMeasureEntity.class);
		selectMeasureExample.createCriteria().andEqualTo("deleteFlag", 0).andNotEqualTo("parentId", -1);
		List<QualityMeasureEntity> measures = measureDao.selectByExample(selectMeasureExample);
		Map<String, String> measureCodeName = new HashMap<>();
		for (QualityMeasureEntity measure : measures) {
			measureCodeName.put(measure.getMeasureIndex(), measure.getVerifyCode());
		}
		for (ErrorDataCount dataCount : errorDataCounts) {
			Map<String, Integer> scoreMap = new HashMap<>();
			Integer sumScore = 0;
			for (QualityMeasureEntity rootMeasure : rootMeasures) {
				int scoreOfRootMeasure = 0;
				List<QualityMeasureEntity> children = rootMeasure.getChildren();
				for (QualityMeasureEntity childMeasure : children) {
					Map<String, Long> percentageOfVerifyType = dataCount.getPercentageOfVerifyType();
					Long percentage = percentageOfVerifyType.get(childMeasure.getMeasureIndex());
					percentage = percentage == null ? 0 : percentage;
					int mark = 0;
					if (percentage < 10) {
						mark = 0;
					} else if (percentage >= 10 && percentage < 25) {
						mark = -1;
					} else if (percentage >= 25 && percentage < 40) {
						mark = -3;
					} else if (percentage >= 40) {
						mark = 0 - childMeasure.getWeight();
					}
					int score = childMeasure.getWeight() + mark;
					// 最低0分，防止出现方负分；
					score = score <= 0 ? 0 : score;
					scoreOfRootMeasure += score;
				}
				scoreMap.put(rootMeasure.getMeasureIndex(), scoreOfRootMeasure);
				sumScore += scoreOfRootMeasure;
			}
			scoreMap.put("总得分", sumScore);
			dataCount.setScoreMap(scoreMap);
		}
	}


	@Override
	public void addReportTemplte(List<QualityGradeEntity> grades
			, List<QualityMeasureEntity> measureIndexs,
			String exportModules) {
		Date now = new Date();
		for (QualityGradeEntity grade : grades) {
			grade.setUpdateTime(now);
			gradeDao.updateByPrimaryKeySelective(grade);
		}
		for (QualityMeasureEntity measure : measureIndexs) {
			measure.setUpdateTime(now);
			measureDao.updateByPrimaryKeySelective(measure);
		}
		QualityReportTemplateEntity template = new QualityReportTemplateEntity();
		template.setId(1L);
		template.setExportModules(exportModules);
		reportTemplateDao.updateByPrimaryKeySelective(template);
	}

	@Override
	public List<QualityReportEntity> listReports(Page<DesignTableInfo> page, Date startDate, Date endDate) {
		List<QualityReportEntity> reports  = reportDao.listReportsPage(page, startDate, endDate);
		return reports;
	}

	@Override
	public QualityReportEntity showReport(Long reportId) {
		QualityReportEntity report = reportDao.showReport(reportId);
		return report;
	}

	@Override
	public void deleteReport(String reportIds) {
		
		reportDao.deleteReport(reportIds);
		
	}

	

}
