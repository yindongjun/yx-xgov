package cn.com.yeexun.dispatch.entity;

public class TaskOverviewVo {
	
	private int totalSum;
	
	private int runningSum;
	
	private int successedSum;
	
	private int failedSum;
	
	private int pausedSum;
	
	private int finalizedSum;

	public int getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(int totalSum) {
		this.totalSum = totalSum;
	}

	public int getRunningSum() {
		return runningSum;
	}

	public void setRunningSum(int runningSum) {
		this.runningSum = runningSum;
	}

	public int getSuccessedSum() {
		return successedSum;
	}

	public void setSuccessedSum(int successedSum) {
		this.successedSum = successedSum;
	}

	public int getFailedSum() {
		return failedSum;
	}

	public void setFailedSum(int failedSum) {
		this.failedSum = failedSum;
	}

	public int getPausedSum() {
		return pausedSum;
	}

	public void setPausedSum(int pausedSum) {
		this.pausedSum = pausedSum;
	}

	public int getFinalizedSum() {
		return finalizedSum;
	}

	public void setFinalizedSum(int finalizedSum) {
		this.finalizedSum = finalizedSum;
	}

	@Override
	public String toString() {
		return "TaskOverviewVo [totalSum=" + totalSum + ", runningSum=" + runningSum + ", successedSum=" + successedSum
				+ ", failedSum=" + failedSum + ", pausedSum=" + pausedSum + ", finalizedSum=" + finalizedSum + "]";
	}
	
	

	
	

}
