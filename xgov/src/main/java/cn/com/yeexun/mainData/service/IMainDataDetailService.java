package cn.com.yeexun.mainData.service;

import java.util.List;
import java.util.Map;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.ExcuteResult;
import cn.com.yeexun.dataSource.entity.Meta;
//import cn.com.yeexun.dataSource.entity.ExcuteResult;
//import cn.com.yeexun.dataSource.entity.Meta;
import cn.com.yeexun.mainData.entity.MainData;
import cn.com.yeexun.mainData.entity.MainDataDetail;
import cn.com.yeexun.meta_data.entity.Metadata;
//import cn.com.yeexun.meta_data.entity.MetadataInfo;

public interface IMainDataDetailService extends IBaseService<MainDataDetail>{

	int deleteTables(Long mainDataId);

	ExcuteResult getShowData(MainDataDetail mdd) throws Exception;

	List<Meta> getAllTables(Long mainDataId) throws Exception;

	List<Map<String, String>> getFields(MainDataDetail mmd) throws Exception;

	void addTables(List<MainDataDetail> tables);

	List<MainDataDetail> getMainTables(Long mainDataId);

	
	List<Map<String, Object>> queryDataBySQL(MainDataDetail mdd, String sql);

	/**
	 * 通过表id或表名获取主数据表
	 * @param mainDataDetailId
	 * @param name
	 * @return
	 */
	MainDataDetail getById(Long mainDataDetailId, String name);

	DataSource getDataSourceById(Long id);
}