package cn.com.yeexun.mainData.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.mainData.entity.MainData;

public interface MainDataDao extends BaseDao<MainData> {
	/**
	 * 获取不在主数据源里面的数据源
	 * @return List<DataSource>
	 */
	public List<DataSource> getDatasource();

	public List<MainData> getDataSByDSIds(@Param(value = "DSId")List<Long> DSId);

	public List<MainData> getMainDataByNamePage(@Param("type")String type, @Param(value = "maindataName")String maindataName, @Param("page")Page<MainData> page);
	
	public int deleteTables(@Param(value="mainDataId")Long mainDataId);
	
	List<Long> getMainDataSourceId();
}
