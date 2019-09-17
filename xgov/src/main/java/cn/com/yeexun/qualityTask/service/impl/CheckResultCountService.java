package cn.com.yeexun.qualityTask.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.qualityTask.dao.ICheckResultCountDao;
import cn.com.yeexun.qualityTask.entity.CheckResultCount;
import cn.com.yeexun.qualityTask.service.ICheckResultCountService;
import cn.com.yeexun.utils.VerifyException;

@Service
public class CheckResultCountService 
		extends BaseService<CheckResultCount> 
		implements ICheckResultCountService {

	@Autowired
	private ICheckResultCountDao checkResultCountDao;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	@Override
	public List<CheckResultCount> queryProblemDataPage(Integer status,
			Long sourceId, Long ownerId, String tableName, String taskName, Page page) throws Exception {
		//List<DesignSourceInfo> dsiList = new ArrayList<DesignSourceInfo>();
		List<CheckResultCount> crcList = new ArrayList<CheckResultCount>();
		crcList = checkResultCountDao.queryProblemDataListPage(status, sourceId, ownerId, tableName
				, taskName, page);
		String sourceIds = crcList.stream().mapToLong(CheckResultCount :: getDatasourceId).distinct()
				.mapToObj(dataSourceId -> String.valueOf(dataSourceId)).collect(Collectors.joining(","));
		List<MetadataDatasource> sources = datasourceService2.getSourceByIds(sourceIds);
		for (CheckResultCount checkResult : crcList) {
			for (MetadataDatasource source : sources) {
				if (checkResult.getDatasourceId() == source.getId()) {
					checkResult.setDataSourceName(source.getDatasourceName());
				}
			}
		}
		
		return crcList;
	}

	@Override
	public List<DataSource> getAllDBList() throws Exception {
		/*List<CheckResultCount> checkResultList = this.listAll();
		Set<Long> idSet = new HashSet<Long>();
		List<DataSource> dbList = new ArrayList<DataSource>();
		List<DesignSourceInfo> dssList = new ArrayList<DesignSourceInfo>();
		if(checkResultList != null && checkResultList.size()>0){
			for(CheckResultCount cr:checkResultList){
				dssList.add(designSourceInfoService.getById(cr.getDesignSourceId()));
			}
		}
		//因为DesignSourceInfo对象中SourceId很多重复，所有用set去重
		if(dssList != null && dssList.size() > 0){
			for(DesignSourceInfo dsi:dssList){
				if(dsi != null){
					idSet.add(dsi.getSourceId());
				}
			}
		}
		
		List<Long> idList = new ArrayList<Long>(idSet);
		for(Long id:idList){
			dbList.add(dataSourceService.getById(id));
		}
		return dbList;*/
		return null;
	}

	@Override
	public void dealProblemData(String ids, String dealPeople, String dealComment) throws Exception {
		String[] item = ids.split(",");
		for (String idStr : item) {
			long id = Long.parseLong(idStr);
			CheckResultCount checkResultCount = this.getById(id);
			if(checkResultCount != null){
				if(checkResultCount.getStatus() != CheckResultCount.STATUS_SOLVED){
					checkResultCount.setDealComment(dealComment);
					checkResultCount.setDealPeople(dealPeople);
					checkResultCount.setStatus(CheckResultCount.STATUS_SOLVED);
					checkResultCount.setDealTime(new Date());
					checkResultCount.setUpdateTime(new Date());
					save(checkResultCount);
				}else {
					throw new VerifyException("已经处理过的不能再次处理！");
				}
			}
		}
	}

	@Override
	public CheckResultCount operationRecord(Long id) throws Exception {
		return this.getById(id);
	}

}
