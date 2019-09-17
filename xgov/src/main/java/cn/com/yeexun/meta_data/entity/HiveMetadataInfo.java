package cn.com.yeexun.meta_data.entity;

public class HiveMetadataInfo {
	//公共属性
	private String pname;
	
	private String name;
	
	//hiveTable属性
	private String TableType;
	
	private String TableLocation;
	
	private String TableName;
	
	private String TableOwner;
	
	private String TableCreateTime;
	
	private String TableAccessTime;
	
	private String Retention;
	
	private String ProtectMode;
	
	private String ViewExpandedText;
	
	private String ViewOriginalText;
	
	private String BucketsNum;
	
	private String Parameters;
	
	private String PricipalName;
	
	private String TablePrivacy;
	
	private String InputFormat;
	
	private String IsCompressed;
	
	private String HdfsLocation;
	
	private String OutputFormat;
	
	//hiveColumn属性
	private String ColumnSeq;
	
	private String ColmType;
	
	private String ColmSize;
	
	private String IsBucketCol;
	
	private String IsPartCol;

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTableType() {
		return TableType;
	}

	public void setTableType(String tableType) {
		TableType = tableType;
	}

	public String getTableLocation() {
		return TableLocation;
	}

	public void setTableLocation(String tableLocation) {
		TableLocation = tableLocation;
	}

	public String getTableName() {
		return TableName;
	}

	public void setTableName(String tableName) {
		TableName = tableName;
	}

	public String getTableOwner() {
		return TableOwner;
	}

	public void setTableOwner(String tableOwner) {
		TableOwner = tableOwner;
	}

	public String getTableCreateTime() {
		return TableCreateTime;
	}

	public void setTableCreateTime(String tableCreateTime) {
		TableCreateTime = tableCreateTime;
	}

	public String getTableAccessTime() {
		return TableAccessTime;
	}

	public void setTableAccessTime(String tableAccessTime) {
		TableAccessTime = tableAccessTime;
	}

	public String getRetention() {
		return Retention;
	}

	public void setRetention(String retention) {
		Retention = retention;
	}

	public String getProtectMode() {
		return ProtectMode;
	}

	public void setProtectMode(String protectMode) {
		ProtectMode = protectMode;
	}

	public String getViewExpandedText() {
		return ViewExpandedText;
	}

	public void setViewExpandedText(String viewExpandedText) {
		ViewExpandedText = viewExpandedText;
	}

	public String getViewOriginalText() {
		return ViewOriginalText;
	}

	public void setViewOriginalText(String viewOriginalText) {
		ViewOriginalText = viewOriginalText;
	}

	public String getBucketsNum() {
		return BucketsNum;
	}

	public void setBucketsNum(String bucketsNum) {
		BucketsNum = bucketsNum;
	}

	public String getParameters() {
		return Parameters;
	}

	public void setParameters(String parameters) {
		Parameters = parameters;
	}

	public String getPricipalName() {
		return PricipalName;
	}

	public void setPricipalName(String pricipalName) {
		PricipalName = pricipalName;
	}

	public String getTablePrivacy() {
		return TablePrivacy;
	}

	public void setTablePrivacy(String tablePrivacy) {
		TablePrivacy = tablePrivacy;
	}

	public String getInputFormat() {
		return InputFormat;
	}

	public void setInputFormat(String inputFormat) {
		InputFormat = inputFormat;
	}

	public String getIsCompressed() {
		return IsCompressed;
	}

	public void setIsCompressed(String isCompressed) {
		IsCompressed = isCompressed;
	}

	public String getHdfsLocation() {
		return HdfsLocation;
	}

	public void setHdfsLocation(String hdfsLocation) {
		HdfsLocation = hdfsLocation;
	}

	public String getOutputFormat() {
		return OutputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		OutputFormat = outputFormat;
	}

	public String getColumnSeq() {
		return ColumnSeq;
	}

	public void setColumnSeq(String columnSeq) {
		ColumnSeq = columnSeq;
	}

	public String getColmType() {
		return ColmType;
	}

	public void setColmType(String colmType) {
		ColmType = colmType;
	}

	public String getColmSize() {
		return ColmSize;
	}

	public void setColmSize(String colmSize) {
		ColmSize = colmSize;
	}

	public String getIsBucketCol() {
		return IsBucketCol;
	}

	public void setIsBucketCol(String isBucketCol) {
		IsBucketCol = isBucketCol;
	}

	public String getIsPartCol() {
		return IsPartCol;
	}

	public void setIsPartCol(String isPartCol) {
		IsPartCol = isPartCol;
	}
	
	
	
	
}
