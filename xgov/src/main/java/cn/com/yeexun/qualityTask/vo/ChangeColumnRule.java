package cn.com.yeexun.qualityTask.vo;

public class ChangeColumnRule {
	/**
	 * 字段名称
	 */
	private String columnName;
	/**
	 * 是否有改变
	 */
	private boolean isChange;
	/**
	 * 现在对应的的QualityRuleDetail的id
	 */
	private long currentDetailId;
	/**
	 * 之前对应的QualityRuleDetail的id
	 */
	private long beforeDetailId;

	public String getColumnName() {
		return columnName;
	}

	public boolean isChange() {
		return isChange;
	}

	public long getCurrentDetailId() {
		return currentDetailId;
	}

	public long getBeforeDetailId() {
		return beforeDetailId;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}

	public void setCurrentDetailId(long currentDetailId) {
		this.currentDetailId = currentDetailId;
	}

	public void setBeforeDetailId(long beforeDetailId) {
		this.beforeDetailId = beforeDetailId;
	}
	


}
