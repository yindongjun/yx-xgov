package cn.com.yeexun.dataSource.service.impl;

import org.springframework.stereotype.Component;

@Component
public class DatasourceClientHystric implements IDatasourceClient {

	@Override
	public String getDatasourceById(Long datasourceId) {
		return null;
	}

	@Override
	public String getSpecialSourceById(Long datasourceId) {
		return null;
	}

	@Override
	public String getDataSourceByTypes(String types, String sourceName, Integer start, Integer length) {
		return null;
	}

	@Override
	public String getAllDataSource(String sourceName) {
		return null;
	}

	@Override
	public String getSourceByIds(String sourceIds) {
		return null;
	}

	@Override
	public String searchColumn(String columnNames, Long sourceId) {
		return null;
	}

}
