package cn.com.yeexun.qualityTask.vo;

public class ColumnDiffDetail {

	/**
	 * 校验类型
	 */
	private String checkRuleType;
	/**
	 * 是否改变
	 */
	private String changeNotice;
	/**
	 * 新版本的校验值
	 */
	private String newRuleValue;
	/**
	 * 老版本的校验值
	 */
	private String oldRuleValue;

	public String getCheckRuleType() {
		return checkRuleType;
	}

	public String getChangeNotice() {
		return changeNotice;
	}

	public String getNewRuleValue() {
		return newRuleValue;
	}

	public String getOldRuleValue() {
		return oldRuleValue;
	}

	public void setCheckRuleType(String checkRuleType) {
		this.checkRuleType = checkRuleType;
	}

	public void setChangeNotice(String changeNotice) {
		this.changeNotice = changeNotice;
	}

	public void setNewRuleValue(String newRuleValue) {
		this.newRuleValue = newRuleValue;
	}

	public void setOldRuleValue(String oldRuleValue) {
		this.oldRuleValue = oldRuleValue;
	}
	
}
