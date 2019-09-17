package cn.com.yeexun.mainData.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.mainData.entity.MainDataDetail;

public interface MainDataDetailDao extends BaseDao<MainDataDetail> {

	

	/**
	 * 获取一个主数据源下的所有表
	 * @param mainDataId
	 * @return
	 */
	public List<MainDataDetail> getMainTables(@Param(value = "mainDataId")Long mainDataId);
	
	/**
	 * 通过表Id获取到数据源
	 * @param tableId
	 * @return
	 */
	public Long getDataSourceId(@Param(value="tableId")Long tableId);
	
	/**
	 * 通过主数据Id获取到数据源
	 * @param mainDataId
	 * @return
	 */
	public DataSource getDataSourceByMainDataId(@Param(value="mainDataId")Long mainDataId);
	
	public MainDataDetail getById(@Param(value = "mainDataDetailId") Long mainDataDetailId ,@Param(value = "name") String name);
	
	/**
	 * 删除主数据下的所有表
	 * @param mainDataId
	 * @return
	 */
	public int deleteTables(@Param(value = "mainDataId")Long mainDataId);
	
	public int deleteTables(@Param(value = "tableIds")List<Long> tableIds);
}
