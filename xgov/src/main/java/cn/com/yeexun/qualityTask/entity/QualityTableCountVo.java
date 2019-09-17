package cn.com.yeexun.qualityTask.entity;

public class QualityTableCountVo {
	
	private Long datasourceId;
	
	private int tableNum;

	public Long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(Long datasourceId) {
		this.datasourceId = datasourceId;
	}

	public int getTableNum() {
		return tableNum;
	}

	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
	}

	@Override
	public String toString() {
		return "QualityTableCountVo [datasourceId=" + datasourceId + ", tableNum=" + tableNum + "]";
	}
	
	

}
