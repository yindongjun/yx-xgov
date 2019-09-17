package cn.com.yeexun.mainData.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.mainData.dao.MainDataDao;
import cn.com.yeexun.mainData.entity.MainData;
import cn.com.yeexun.mainData.service.IMainDataService;
import cn.com.yeexun.utils.ResponseUtil;

@Service
public class MainDataService extends BaseService<MainData> implements IMainDataService{

	@Autowired
	MainDataDao maindataDao;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	@Override
	public List<DataSource> getDatasource() {
		
		String types = "'MySQL','Oracle','SqlServer','PostgreSQL','DB2','Sybase','Teradata'";
		String datasources = datasourceService2.getDataSourceByTypes(types, null, 0, Integer.MAX_VALUE);
		List<DataSource> allSources = ResponseUtil.getListData(datasources, DataSource.class);
		
		List<Long> mainDataSourceIds = maindataDao.getMainDataSourceId();
		List<DataSource> sources = allSources.stream().filter(
				source -> !mainDataSourceIds.contains(source.getId())).collect(Collectors.toList());
		return sources;
	}
	/**
	 * 查询传入的数据源是否存在
	 * @param DSId
	 * @return
	 */
	@Override
	public List<MainData> getDataSByDSIds(List<Long> DSId) {
		List<MainData> list=maindataDao.getDataSByDSIds(DSId);
		return list;
	}

	@Override
	public List<MainData> getMainDataByName(String type, String maindataName, Page<MainData> page) {
		return maindataDao.getMainDataByNamePage(type, maindataName,page);
	}
	
	@Override
	public int delete(Long mainDataId){
		int result = maindataDao.deleteTables(mainDataId);
		return result;
	}
}
