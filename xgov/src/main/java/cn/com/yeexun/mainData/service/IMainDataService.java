package cn.com.yeexun.mainData.service;

import java.util.List;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.mainData.entity.MainData;

public interface IMainDataService extends IBaseService<MainData> {

	List<DataSource> getDatasource();

	List<MainData> getDataSByDSIds(List<Long> DSId);

	List<MainData> getMainDataByName(String type, String maindataName,
			Page<MainData> page);

	int delete(Long mainDataId);

}
