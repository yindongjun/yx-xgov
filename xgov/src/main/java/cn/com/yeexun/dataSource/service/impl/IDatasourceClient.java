package cn.com.yeexun.dataSource.service.impl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name= "${xgov.serviceName.metadata}", fallback = DatasourceClientHystric.class)
public interface IDatasourceClient {
	
	@RequestMapping(value = "/data_source/get_by_id",method = RequestMethod.GET)
	String getDatasourceById(@RequestParam(value = "datasourceId") Long datasourceId);
	
	@RequestMapping(value = "/data_source/get_special_by_id",method = RequestMethod.GET)
	String getSpecialSourceById(@RequestParam(value = "datasourceId") Long datasourceId);
	
	@RequestMapping(value = "/data_source/xgovGetDataourceByTypes",method = RequestMethod.GET)
	String getDataSourceByTypes(@RequestParam(value = "types") String types
			, @RequestParam(value = "sourceName") String sourceName
			, @RequestParam(value = "start") Integer start
			, @RequestParam(value = "length") Integer length);
	
	@RequestMapping(value = "/data_source/getAllDataSource",method = RequestMethod.GET)
	String getAllDataSource(@RequestParam(value = "sourceName") String sourceName);

	@RequestMapping(value = "/data_source/getSourceByIds",method = RequestMethod.GET)
	String getSourceByIds(@RequestParam(value = "sourceIds") String sourceIds);
	
	@RequestMapping(value = "/metadata_info/searchColumn",method = RequestMethod.POST)
	String searchColumn(@RequestParam(value = "columnNames") String columnNames
			, @RequestParam(value = "sourceId") Long sourceId);

}
