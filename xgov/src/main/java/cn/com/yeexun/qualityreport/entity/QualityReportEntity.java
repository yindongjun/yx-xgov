package cn.com.yeexun.qualityreport.entity;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_quality_report")
public class QualityReportEntity extends Unique {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final Integer status_creating = 0;
	public static final Integer statuc_created = 1;
	
	private String name;
	
	private Long templateId;

	private Integer score;

	private Integer rightSum;

	private Integer allSum;

	private String scoreGrade;

	private String radarMap;
	private String rightTop5;
	private String errorDistribution;
	private String errorDataHandle;
	private String scoreDetail;
	
	private Integer deleteFlag;
	private Integer status;

	private Date createTime;
	private Long createBy;
	private Date updateTime;
	private Long updateBy;
	
	@Transient
	private String exportModules;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getRightSum() {
		return rightSum;
	}

	public void setRightSum(Integer rightSum) {
		this.rightSum = rightSum;
	}

	public Integer getAllSum() {
		return allSum;
	}

	public void setAllSum(Integer allSum) {
		this.allSum = allSum;
	}

	public String getScoreGrade() {
		return scoreGrade;
	}

	public void setScoreGrade(String scoreGrade) {
		this.scoreGrade = scoreGrade;
	}

	public String getRadarMap() {
		return radarMap;
	}

	public void setRadarMap(String radarMap) {
		this.radarMap = radarMap;
	}

	public String getRightTop5() {
		return rightTop5;
	}

	public void setRightTop5(String rightTop5) {
		this.rightTop5 = rightTop5;
	}

	public String getErrorDistribution() {
		return errorDistribution;
	}

	public void setErrorDistribution(String errorDistribution) {
		this.errorDistribution = errorDistribution;
	}

	public String getErrorDataHandle() {
		return errorDataHandle;
	}

	public void setErrorDataHandle(String errorDataHandle) {
		this.errorDataHandle = errorDataHandle;
	}
	
	public String getScoreDetail() {
		return scoreDetail;
	}

	public void setScoreDetail(String scoreDetail) {
		this.scoreDetail = scoreDetail;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	
	public String getExportModules() {
		return exportModules;
	}

	public void setExportModules(String exportModules) {
		this.exportModules = exportModules;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
