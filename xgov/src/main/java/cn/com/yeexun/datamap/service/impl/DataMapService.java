package cn.com.yeexun.datamap.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.yeexun.businessTerms.entity.Code;
import cn.com.yeexun.businessTerms.service.ICodeService;
import cn.com.yeexun.businessTerms.service.ICodeSetService;
import cn.com.yeexun.collectTask.service.ICollectTaskService;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataElement.service.IDataElementService;
import cn.com.yeexun.dataSet.service.IDataSetService;
import cn.com.yeexun.dataSource.entity.DataSource;
//import cn.com.yeexun.dataSource.service.impl.IDataSourceService;
import cn.com.yeexun.datamap.entity.RecentDate;
import cn.com.yeexun.datamap.service.IDataMapService;
import cn.com.yeexun.documentManagement.service.IDocumentFileService;
import cn.com.yeexun.meta_data.collector.MetadataCollector;
@Service
public class DataMapService implements IDataMapService{
	
	@Autowired
	private IDataElementService dataElementService;
	
	@Autowired
	private IDataSetService dataSetService;
	
	@Autowired
	private IDocumentFileService documentFileService;
	
//	@Autowired
//	private IDataSourceService dataSourceService;
	
	@Autowired
	private ICodeSetService codeSetService;
	
	@Autowired
	private ICodeService codeService;
	
	@Autowired
	private ICollectTaskService collectTaskService;
	

	@Override
	public JSONObject getFirstMap() throws Exception{
		/*JSONObject index0 = new JSONObject();
		//获取接入区的的所有数据源 
		List<DataSource> sources = dataSourceService.getDataSourceByLayer("接入层");
		index0.put("AccessArea", sources);
		//获取汇总区的所有数据源
		List<DataSource> source2 = dataSourceService.getDataSourceByLayer("汇总层");
		Float allDataSize = (float) 0.0;
		Float increment = (float) 0.0;
		for (DataSource dataSource : source2) {
			if (collectTaskService.isRDBMS(dataSource)) {
				MetadataCollector collect = collectTaskService.getMetadataCollector(dataSource.getDatabaseType());
				Float currentSize = collect.getSourceDataSize(dataSource);
				if (currentSize != null) {
					allDataSize = allDataSize + currentSize;
					if (dataSource.getDataSize() != null) {
						increment = increment + currentSize - dataSource.getDataSize();
					}
				}
			}
		}
		JSONObject summaryArea = new JSONObject();
		summaryArea.put("sourceNum", source2.size());
		summaryArea.put("dataSize", allDataSize);
		summaryArea.put("increment", increment);
		index0.put("summaryArea", summaryArea);
		//获取标准区的汇总数据 数据集总个数   文档总个数   业务术语总个数   代码总个数
		JSONObject standardArea = new JSONObject();
		
		int dataSetNum = dataSetService.getAllDataSetNum();
//		
//		  map.put("sum", sum);
//		   map.put("relCount", relCount);
//		   map.put("unRelCount",unRelCount);
//		
//		Map<String, Integer> dataEleNum = dataElementService.getElementCountInfo();
//		int allEleNum = dataEleNum.get("sum");
		int codeSetNum = codeSetService.getAllCodeSetNum();
		int documentNum = documentFileService.getAllFileNum();
		standardArea.put("dataSetNum", dataSetNum);
		standardArea.put("codeSetNum", codeSetNum);
		standardArea.put("documentNum", documentNum);
		index0.put("standardArea", standardArea);
		//显示问题数据和正确数据
		JSONObject dataArea = new JSONObject();
		dataArea.put("DirtyRead", 10);
		dataArea.put("CorrectData", 80);
		index0.put("dataArea", dataArea);
		
		return index0;*/
		return null;
	}

	@Override
	public Float getDataSize(DataSource dataSource) throws Exception {
//		MetadataCollector collector = collectTaskService.getMetadataCollector(dataSource.getDatabaseType());
//		return collector.getSourceDataSize(dataSource);
		return null;
	}
	
	@Override
	public JSONObject getDetailInfo() throws Exception{
		JSONObject index1 = new JSONObject();
		//数据集总数
		int dataSetNum = dataSetService.getAllDataSetNum()*128;
		//数据元总数
		Map<String, Integer> elementInfo = dataElementService.getElementCountInfo();
		int dataElementNum = elementInfo.get("sum")*128;
		//业务术语总数
		int codeSetNum = codeSetService.getAllCodeSetNum()*128;
		//代码总数
		int codeNum = codeService.getAllCodeNum()*128;
		//文档总数
		int documentNum = documentFileService.getAllFileNum()*128;
		
		//标准概况信息
		JSONObject generalInfo = new JSONObject();
		generalInfo.put("dataSetNum", dataSetNum);
		generalInfo.put("dataElementNum", dataElementNum);
		generalInfo.put("codeSetNum", codeSetNum);
		generalInfo.put("documentNum", documentNum);
		generalInfo.put("codeNum", codeNum);
		
		index1.put("generalInfo", generalInfo);
		
		//数据稽核率 有关联关系数据元除以数据元总数
		JSONObject relationNum = new JSONObject();
		relationNum.put("relCount", elementInfo.get("relCount")*120);
		relationNum.put("unRelCount", elementInfo.get("unRelCount")*120);
		index1.put("dateAuditRate", relationNum);
		
		//数据标准趋势分析
		JSONObject dataStandardsTrends = new JSONObject();
		//1.数据元
		JSONObject dataElement = dataElementService.getCountByTime();
		//2.代码集
		List<Integer> code = codeService.getCodeNumByNowTime();
		//3.文档管理
		List<Integer> document = documentFileService.getFileNumByNowTime();
		dataStandardsTrends.put("time", dataElement.get("time"));
		dataStandardsTrends.put("element", dataElement.get("element"));
		dataStandardsTrends.put("code", code);
		dataStandardsTrends.put("document", document);
		
		index1.put("dataStandardsTrends", dataStandardsTrends);
		
		//常用数据标准
		List<DataElementEntity> commonElement = dataElementService.getRelEleTop20();
		index1.put("commonElement", commonElement);
		
		//近期更新标准recent update 数据元及业务术语
		List<DataElementEntity> elementList =  dataElementService.getElementTop5();
		List<Code> codeList = codeService.getCodeLastFive();
		List<RecentDate> recentDate = new ArrayList<>();
		
		for(int i = 0; i<elementList.size()&&i<2; i++){
			RecentDate recentdate = new RecentDate();
			recentdate.setName(elementList.get(i).getDataElementName());
			recentdate.setStatus(elementList.get(i).getStatus().toString());
			recentdate.setType("数据元");
			recentDate.add(recentdate);
		}
		for(int i = 0; i<codeList.size()&&i<2; i++){
			RecentDate recentdate = new RecentDate();
			recentdate.setName(codeList.get(i).getName());
			recentdate.setStatus(codeList.get(i).getStatus());
			recentdate.setType("业务术语");
			recentDate.add(recentdate);
		}
//		JSONObject recentUpdate = new JSONObject();
//		recentUpdate.put("elementList", elementList);
//		recentUpdate.put("codeList", codeList);
		
		index1.put("recentUpdate", recentDate);
		
		return index1;
	}

}
