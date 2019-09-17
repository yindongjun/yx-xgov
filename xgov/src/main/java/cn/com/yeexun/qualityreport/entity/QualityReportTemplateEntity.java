package cn.com.yeexun.qualityreport.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_quality_report_template")
public class QualityReportTemplateEntity extends Unique {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String exportModules;

	private String description;

	private Integer deleteFlag;

	private Date createTime;
	private Long createBy;
	private Date updateTime;
	private Long updateBy;
	
	@Transient
	private List<QualityGradeEntity> grades;
	@Transient
	private List<QualityMeasureEntity> measureIndexs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getExportModules() {
		return exportModules;
	}

	public void setExportModules(String exportModules) {
		this.exportModules = exportModules;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
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
	
	public List<QualityGradeEntity> getGrades() {
		return grades;
	}

	public void setGrades(List<QualityGradeEntity> grades) {
		this.grades = grades;
	}

	public List<QualityMeasureEntity> getMeasureIndexs() {
		return measureIndexs;
	}

	public void setMeasureIndexs(List<QualityMeasureEntity> measureIndexs) {
		this.measureIndexs = measureIndexs;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
