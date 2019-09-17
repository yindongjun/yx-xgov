package cn.com.yeexun.meta_data.entity;

public class HbaseMetadataInfo {
	
	//公共属性
	private String pname;
	
	private String name;
	
	//HbaseTable
	private String MaxRegionSize;
	
	private String MinRegionSize;
	
	private String MemstoreSize;
	
	private String ColumnFamilyCount;
	
	private String Regition;
	
	private String Priority;
	
	//HbaseFamily
	private String DataBlockEncode;
	
	private String BloomFilter;
	
	private String BlockCache;
	
	private String Configuration;
	
	private String ReplicationScope;
	
	private String Versions;
	
	private String Compression;
	
	private String MinVersions;
	
	private String TTL;
	
	private String KeepDeletedCells;
	
	private String BlockSize;
	
	private String InMemory;

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

	public String getMaxRegionSize() {
		return MaxRegionSize;
	}

	public void setMaxRegionSize(String maxRegionSize) {
		MaxRegionSize = maxRegionSize;
	}

	public String getMinRegionSize() {
		return MinRegionSize;
	}

	public void setMinRegionSize(String minRegionSize) {
		MinRegionSize = minRegionSize;
	}

	public String getMemstoreSize() {
		return MemstoreSize;
	}

	public void setMemstoreSize(String memstoreSize) {
		MemstoreSize = memstoreSize;
	}

	public String getColumnFamilyCount() {
		return ColumnFamilyCount;
	}

	public void setColumnFamilyCount(String columnFamilyCount) {
		ColumnFamilyCount = columnFamilyCount;
	}

	public String getRegition() {
		return Regition;
	}

	public void setRegition(String regition) {
		Regition = regition;
	}

	public String getPriority() {
		return Priority;
	}

	public void setPriority(String priority) {
		Priority = priority;
	}

	public String getDataBlockEncode() {
		return DataBlockEncode;
	}

	public void setDataBlockEncode(String dataBlockEncode) {
		DataBlockEncode = dataBlockEncode;
	}

	public String getBloomFilter() {
		return BloomFilter;
	}

	public void setBloomFilter(String bloomFilter) {
		BloomFilter = bloomFilter;
	}

	public String getBlockCache() {
		return BlockCache;
	}

	public void setBlockCache(String blockCache) {
		BlockCache = blockCache;
	}

	public String getConfiguration() {
		return Configuration;
	}

	public void setConfiguration(String configuration) {
		Configuration = configuration;
	}

	public String getReplicationScope() {
		return ReplicationScope;
	}

	public void setReplicationScope(String replicationScope) {
		ReplicationScope = replicationScope;
	}

	public String getVersions() {
		return Versions;
	}

	public void setVersions(String versions) {
		Versions = versions;
	}

	public String getCompression() {
		return Compression;
	}

	public void setCompression(String compression) {
		Compression = compression;
	}

	public String getMinVersions() {
		return MinVersions;
	}

	public void setMinVersions(String minVersions) {
		MinVersions = minVersions;
	}

	public String getTTL() {
		return TTL;
	}

	public void setTTL(String tTL) {
		TTL = tTL;
	}

	public String getKeepDeletedCells() {
		return KeepDeletedCells;
	}

	public void setKeepDeletedCells(String keepDeletedCells) {
		KeepDeletedCells = keepDeletedCells;
	}

	public String getBlockSize() {
		return BlockSize;
	}

	public void setBlockSize(String blockSize) {
		BlockSize = blockSize;
	}

	public String getInMemory() {
		return InMemory;
	}

	public void setInMemory(String inMemory) {
		InMemory = inMemory;
	}

	@Override
	public String toString() {
		return "HbaseMetadataInfo [pname=" + pname + ", name=" + name + ", MaxRegionSize=" + MaxRegionSize
				+ ", MinRegionSize=" + MinRegionSize + ", MemstoreSize=" + MemstoreSize + ", ColumnFamilyCount="
				+ ColumnFamilyCount + ", Regition=" + Regition + ", Priority=" + Priority + ", DataBlockEncode="
				+ DataBlockEncode + ", BloomFilter=" + BloomFilter + ", BlockCache=" + BlockCache + ", Configuration="
				+ Configuration + ", ReplicationScope=" + ReplicationScope + ", Versions=" + Versions + ", Compression="
				+ Compression + ", MinVersions=" + MinVersions + ", TTL=" + TTL + ", KeepDeletedCells="
				+ KeepDeletedCells + ", BlockSize=" + BlockSize + ", InMemory=" + InMemory + "]";
	}
	
}
