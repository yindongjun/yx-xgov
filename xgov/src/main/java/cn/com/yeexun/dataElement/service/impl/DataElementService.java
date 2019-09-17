package cn.com.yeexun.dataElement.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.businessTerms.dao.ICodeDao;
import cn.com.yeexun.businessTerms.entity.Code;
import cn.com.yeexun.businessTerms.entity.CodeSet;
import cn.com.yeexun.businessTerms.service.ICodeSetService;
import cn.com.yeexun.collectTask.service.ICollectTaskService;
import cn.com.yeexun.dataElement.dao.IDataElementDao;
import cn.com.yeexun.dataElement.dao.IRelationShipDao;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataElement.entity.RelationShipEntity;
import cn.com.yeexun.dataElement.service.IDataElementService;
import cn.com.yeexun.dataElement.service.IRelationShipService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.HiveSource;
import cn.com.yeexun.dataSource.entity.ImpalaSource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.dispatch.service.IDispatchTaskService;
import cn.com.yeexun.meta_data.collector.HiveMetadataCollector;
import cn.com.yeexun.meta_data.collector.JdbcHelper;
import cn.com.yeexun.meta_data.collector.MetadataCollector;
import cn.com.yeexun.meta_data.entity.Metadata;
import cn.com.yeexun.meta_data.entity.MetadataAttrEntity;
import cn.com.yeexun.meta_data.entity.MetadataInfo;
import cn.com.yeexun.meta_data.service.IMetadataAttrService;
import cn.com.yeexun.meta_data.service.IMetadataService;
import cn.com.yeexun.qualityTask.entity.DefectVerifyDetail;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.EnumVerifyDetail;
import cn.com.yeexun.qualityTask.entity.FormatVerifyDetail;
import cn.com.yeexun.qualityTask.entity.InitNode;
import cn.com.yeexun.qualityTask.entity.IntervalVerifyDetail;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityTask.entity.RegularVerifyDetail;
import cn.com.yeexun.qualityTask.service.IDesignTableInfoService;
import cn.com.yeexun.qualityTask.service.IQualityTaskDetailService;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import cn.com.yeexun.utils.ResponseUtil;
import tk.mybatis.mapper.entity.Example;

@Service
public class DataElementService extends BaseService<DataElementEntity> implements IDataElementService{
	
	private static final int EXCEL_READ_START_ROW = 8;

	@Autowired
	private IDataElementDao dataElementDao;
	
	@Autowired
	private IRelationShipDao relationShipDao;
	
	@Autowired
	private IMetadataService metadataService;
	
	@Autowired
	private IMetadataAttrService metadataAttrService;
	
	@Autowired
	private ICodeSetService codeSetService;
	
	@Autowired
	private IRelationShipService relationShipService;
	
//	@Autowired
//	private IDesignSourceInfoService designSourceService;
	
	@Autowired
	private IDesignTableInfoService designTableService;
	
	@Autowired
	private IQualityTaskDetailService qualityTaskDetailService;
	
	@Autowired
	private ICollectTaskService collectTaskService;
	
//	@Autowired
//	private IDataSourceService dataSourceService;
	
	@Autowired
	private IDispatchTaskService dispatchService;
	
//	@Autowired
//	private IImpalaSourceService impalaSourceService;
//	
//	@Autowired
//	private IHiveSourceService hiveSourceService;
	
	@Autowired
	private ICodeDao codeDao;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	
	@Override
	public DataElementEntity getByUniqueCode(String uniqueCode)
			throws Exception {
		return dataElementDao.getByUniqueCode(uniqueCode);
	}

	@Override
	public List<DataElementEntity> showListByStatusPage(Long dataSourceId,
			String tableName, Integer status, 
			String dataElementType, String dataElementName,
			Page<DataElementEntity> page)throws Exception {
		return dataElementDao.showListByStatusPage(dataSourceId, tableName, status, dataElementType, dataElementName, page);
	}
	/**
	 * 标准审核修改数据元状态值
	 */
	@Override
	public void submit(List<Long> ids, String status) throws Exception {
		for (long id : ids) {
			DataElementEntity element = getById(id);
			if("1".equals(status)){
				// 修改数据元的状态值为已审核状态
				element.setStatus(2);
				save(element);
				// 查看数据元之前状态，为草稿时新增一条元数据，变更状态时更新版本信息
				Metadata meta = new Metadata();
				meta.setParentId(2L);
				meta.setName(element.getDataElementName());
				meta.setCode(element.getDataElementCode());//数据元中用code来表示唯一性
				meta.setIsMenu("N");
				meta.setBuildin("N");
				meta.setMetamodelId(21L);
//				meta.setCreateUserId(createUserId);
				meta.setCreateTime(new Date());
				meta.setLastModifyTime(new Date());
				meta.setMetadataType(Constant.BUSSINESS_METADATA);
				meta.setIsRelease(Constant.IS_RELEASE);
//				meta.setUniqueCode(element.getUniqueCode());
				meta.setDeleteFlag(Constant.NOT_DELETE);
				
				Metadata meta1 = new Metadata();
				meta1.setCode(element.getDataElementCode());
				meta1.setMetamodelId(21L);
				List<Metadata> models = metadataService.getMetaByUniqueCode(meta1);
				if(null != models && models.size() > 0){
					List<Long> ids2 = new ArrayList<>();
					for(Metadata model:models){
						ids2.add(model.getId());
					}
					metadataService.deleteList(ids2);
					meta.setVersion(models.get(models.size()-1).getVersion()+1);
					if(StringUtils.isNotEmpty(element.getChangeInfo())){
						meta.setVersionDescription(element.getChangeInfo());
					}else{
						meta.setVersionDescription("修改");
					}
				}else{
					meta.setVersion(1);
					meta.setVersionDescription("新增");
				}
				
				/*if(0 == element.getFrontStatus()){
					meta.setVersion(1);
					meta.setVersionDescription("新增");
				}else{
					Metadata meta1 = new Metadata();
					meta1.setCode(element.getDataElementCode());
					meta1.setMetamodelId(21L);
					List<Metadata> models = metadataService.getMetaByUniqueCode(meta1);
					if(null!=models && models.size()>0){
						List<Long> ids2 = new ArrayList<>();
						for(Metadata model:models){
							ids2.add(model.getId());
						}
						metadataService.deleteList(ids2);
					}
					meta.setVersion(models.get(0).getVersion()+1);
					if(StringUtils.isNotEmpty(element.getChangeInfo())){
						meta.setVersionDescription(element.getChangeInfo());
					}else{
						meta.setVersionDescription("修改");
					}
				}*/
				metadataService.save(meta);
				//保存元数据属性值
				for(int i = 1; i < 14; i++){
					MetadataAttrEntity attr = new MetadataAttrEntity();
					attr.setMetadataId(meta.getId());
					attr.setModelAttrId((long)i);
					switch(i){
						case 1:
							attr.setAttrValue(element.getDataElementName());
							break;
						case 2:
							attr.setAttrValue(element.getDataElementCode());
							break;
						case 3:
							attr.setAttrValue(element.getDataElementType());
							break;
						case 4:
							attr.setAttrValue(element.getDataElementAttr());
							break;
						case 5:
							attr.setAttrValue(element.getDefinition());
							break;
						case 6:
							//需要修改为值域名称
//							attr.setAttrValue(element.getCodeset().toString());
							if(StringUtils.isNotBlank(element.getCodeset())){
								Long codeSet = Long.parseLong(element.getCodeset());
								CodeSet code = codeSetService.echoCodeSet(codeSet);
								if(null!=code){
									attr.setAttrValue(code.getName());
								}
							}
							break;
						case 7:
							if(null != element.getDataRangeFront() && null != element.getDataRangeEnd()){
								String range = element.getDataRangeFront() + "~" + element.getDataRangeEnd();
								attr.setAttrValue(range);
							}
							break;
						case 8:
							attr.setAttrValue(element.getRegularExpression());
							break;
						case 9:
							attr.setAttrValue(element.getRemark());
							break;
						case 10:
							//数据元分类
//							attr.setAttrValue(element.getDataElementFlag());
							break;
						case 11:
							attr.setAttrValue(element.getCreateTime().toString());
							break;
						case 12:
							attr.setAttrValue(element.getLastModifyTime().toString());
							break;
						case 13:
							attr.setAttrValue(meta.getVersion().toString());
							break;
						default:
						    //...;
						    break;
					}
					metadataAttrService.save(attr);
				}
				// 判断该数据元是否绑定了关联关系，如果绑定了就把原来quality_task_detail删掉.
				QualityTaskDetail example = new QualityTaskDetail();
				example.setElementId(id);
				example.setDeleteFlag("0");
				List<QualityTaskDetail> qualityTaskDetails = qualityTaskDetailService.query(example);
				if (qualityTaskDetails != null && qualityTaskDetails.size() > 0) {
					/*for (QualityTaskDetail qualityTaskDetail : qualityTaskDetails) {
						qualityTaskDetail.setDeleteFlag(Constant.IS_DELETE);
						qualityTaskDetailService.update(qualityTaskDetail);
					}*/
					qualityTaskDetailService.deleteByElementId(id);
					// 重新生成
					List<RelationShipEntity> relations = relationShipDao.getByElementId(id);
					createQualityTaskDetail(relations);
				}
			}else{
				//修改数据元的状态值    退回
//				element.setStatus(element.getFrontStatus());
				element.setStatus(DataElementEntity.STATUS_REJECT);
				save(element);
			}
		}
	}

	private void createQualityTaskDetail(List<RelationShipEntity> relations) throws Exception {
		// 判断是否绑定了数据规则
//		List<RelationShipEntity> relations = relationShipService.getByElementId(element.getId());
		
		for (RelationShipEntity relation : relations) {
			if (relation != null) {
				List<QualityTaskDetail> qualityTaskDetails = new ArrayList<>();
				DataElementEntity element= dataElementDao.selectByPrimaryKey(relation.getDataElementId());
				// 更新quality_task_detail
				// 生成quality_task_detail
				// 生成初始化节点
//				DataSource source = dataSourceService.getById(relation.getSourceId());
				DataSource source = datasourceService2.getDatasourceById(relation.getSourceId());

				
				String sql = null;
				if (collectTaskService.isRDBMS(source)) {
					MetadataCollector collector = collectTaskService.getMetadataCollector(
							source.getDatabaseType());
					sql = collector.createSelectSql(source, relation.getTablename());
				} else if (Constant.IMPALA.equals(source.getDatabaseType()) 
						|| Constant.HIVE.equals(source.getDatabaseType())) {
					Connection conn = JdbcHelper.getConnection(source.getDriver(), source.getUrl()
							, source.getUserName(), source.getPassword());
					HiveMetadataCollector hiveMetadataCollector = new HiveMetadataCollector();
					Connection conn1 = null;
					if (Constant.HIVE.equals(source.getDatabaseType())) {
//						HiveSource hiveSource = hiveSourceService.getByDataSourceId(source.getId());
						
						HiveSource hiveSource = datasourceService2.getHiveSource(source.getId());
						
						conn1 = JdbcHelper.getConnection(hiveSource.getDriver()
								, hiveSource.getUrl(), hiveSource.getHiveMetaDataUserName()
								, hiveSource.getHiveMetaDataPassword());
					} else {
//						ImpalaSource impalaSource = impalaSourceService.getByDataSourceId(source.getId());
						
						ImpalaSource impalaSource = datasourceService2.getImpalaSource(source.getId());
						
						conn1 = JdbcHelper.getConnection(impalaSource.getDriver()
								, impalaSource.getUrl(), impalaSource.getUserName(), impalaSource.getPassword());
					}
					hiveMetadataCollector.setMetaConnection(conn1);
					hiveMetadataCollector.setConnection(conn);
					sql = hiveMetadataCollector.createSelectSql(source, relation.getTablename());
				}
				if (sql == null || sql.trim().length() <= 0) {
					throw new CommonException("生成查询SQL异常：SQL=" + sql);
				}
				InitNode initNode = new InitNode();
				initNode.setSql(sql);
				initNode.setExtractType(Constant.extractType_full);
				QualityTaskDetail initDetail = new QualityTaskDetail();
				initDetail.setName("初始化");
//				initDetail.setDesignTableId(tableInfo.getId());
				initDetail.setVerifyType("1");
				initDetail.setVerifyDetail(JSON.toJSONString(initNode));
				initDetail.setDeleteFlag(Constant.NOT_DELETE);
				initDetail.setColumnName(relation.getFildname());
				initDetail.setDatasourceId(relation.getSourceId());
				initDetail.setTableName(relation.getTablename());
				initDetail.setElementId(relation.getDataElementId());
				initDetail.setRelationId(relation.getId());
				initDetail.setEnable(QualityTaskDetail.ENABLE);
				qualityTaskDetails.add(initDetail);
				
				// 1.格式校验
				FormatVerifyDetail formatVerifyDetail = new FormatVerifyDetail();
				formatVerifyDetail.setColumnName(relation.getFildname());
				formatVerifyDetail.setFormatLength(element.getDataElementAttr());
				formatVerifyDetail.setFormatType(element.getDataElementType());
				formatVerifyDetail.setIsStandard("Y");
				QualityTaskDetail formatDetail = new QualityTaskDetail();
				formatDetail.setName("格式校验");
//				formatDetail.setDesignTableId(tableInfo.getId());
				formatDetail.setVerifyType("2");
				formatDetail.setVerifyDetail(JSON.toJSONString(formatVerifyDetail));
				formatDetail.setDeleteFlag(Constant.NOT_DELETE);
				formatDetail.setColumnName(relation.getFildname());
				formatDetail.setDatasourceId(relation.getSourceId());
				formatDetail.setTableName(relation.getTablename());
				formatDetail.setElementId(relation.getDataElementId());
				formatDetail.setRelationId(relation.getId());
				formatDetail.setEnable(QualityTaskDetail.ENABLE);
				qualityTaskDetails.add(formatDetail);
				// 空值校验
				DefectVerifyDetail defectVerifyDetail = new DefectVerifyDetail();
				defectVerifyDetail.setColumnName(relation.getFildname());
				defectVerifyDetail.setIsStandard("Y");
				QualityTaskDetail defectDetail = new QualityTaskDetail();
				defectDetail.setName("空值校验");
//				defectDetail.setDesignTableId(tableInfo.getId());
				defectDetail.setVerifyType("3");
				defectDetail.setVerifyDetail(JSON.toJSONString(defectVerifyDetail));
				defectDetail.setDeleteFlag(Constant.NOT_DELETE);
				defectDetail.setColumnName(relation.getFildname());
				defectDetail.setDatasourceId(relation.getSourceId());
				defectDetail.setTableName(relation.getTablename());
				defectDetail.setElementId(relation.getDataElementId());
				defectDetail.setRelationId(relation.getId());
				defectDetail.setEnable(QualityTaskDetail.ENABLE);
				qualityTaskDetails.add(defectDetail);
				// 2.数据范围检验
				if (StringUtils.isNotEmpty(element.getDataRangeEnd())) {
					IntervalVerifyDetail intervalVerifyDetail = new IntervalVerifyDetail();
					intervalVerifyDetail.setColumnName(relation.getFildname());
					intervalVerifyDetail.setIsStandard("Y");
					intervalVerifyDetail.setMaxValue(element.getDataRangeEnd());
					intervalVerifyDetail.setMinValue(element.getDataRangeFront());
					QualityTaskDetail intervalDetail = new QualityTaskDetail();
					intervalDetail.setName("数据范围校验");
//					intervalDetail.setDesignTableId(tableInfo.getId());
					intervalDetail.setVerifyType("4");
					intervalDetail.setVerifyDetail(JSON.toJSONString(intervalVerifyDetail));
					intervalDetail.setDeleteFlag(Constant.NOT_DELETE);
					intervalDetail.setColumnName(relation.getFildname());
					intervalDetail.setDatasourceId(relation.getSourceId());
					intervalDetail.setTableName(relation.getTablename());
					intervalDetail.setElementId(relation.getDataElementId());
					intervalDetail.setRelationId(relation.getId());
					intervalDetail.setEnable(QualityTaskDetail.ENABLE);
					qualityTaskDetails.add(intervalDetail);
				}
				// 3.枚举校验
				String codesetId = element.getCodeset();
				if (StringUtils.isNotEmpty(codesetId)) {
					CodeSet codeset = codeSetService.getById(Long.valueOf(codesetId));
					EnumVerifyDetail enumVerifyDetail = new EnumVerifyDetail();
					enumVerifyDetail.setCodeSetId(Long.valueOf(codesetId));
					enumVerifyDetail.setCodeSetName(codeset.getName());
					enumVerifyDetail.setColumnName(relation.getFildname());
					enumVerifyDetail.setIsStandard("Y");
					QualityTaskDetail enumDetail = new QualityTaskDetail();
					enumDetail.setName("值域校验");
//					enumDetail.setDesignTableId(tableInfo.getId());
					enumDetail.setVerifyType("5");
					enumDetail.setVerifyDetail(JSON.toJSONString(enumVerifyDetail));
					enumDetail.setDeleteFlag(Constant.NOT_DELETE);
					enumDetail.setColumnName(relation.getFildname());
					enumDetail.setDatasourceId(relation.getSourceId());
					enumDetail.setTableName(relation.getTablename());
					enumDetail.setElementId(relation.getDataElementId());
					enumDetail.setRelationId(relation.getId());
					enumDetail.setEnable(QualityTaskDetail.ENABLE);
					qualityTaskDetails.add(enumDetail);
				}
				// 4.正则校验
				String regex = element.getRegularExpression();
				if (StringUtils.isNotEmpty(regex)) {
					RegularVerifyDetail regularVerifyDetail = new RegularVerifyDetail();
					regularVerifyDetail.setColumnName(relation.getFildname());
					regularVerifyDetail.setIsStandard("Y");
					regularVerifyDetail.setRegularValue(element.getRegularExpression());
					QualityTaskDetail regexDetail = new QualityTaskDetail();
					regexDetail.setName("正则校验");
//					regexDetail.setDesignTableId(tableInfo.getId());
					regexDetail.setVerifyType("6");
					regexDetail.setVerifyDetail(JSON.toJSONString(regularVerifyDetail));
					regexDetail.setDeleteFlag(Constant.NOT_DELETE);
					regexDetail.setColumnName(relation.getFildname());
					regexDetail.setDatasourceId(relation.getSourceId());
					regexDetail.setTableName(relation.getTablename());
					regexDetail.setElementId(relation.getDataElementId());
					regexDetail.setRelationId(relation.getId());
					regexDetail.setEnable(QualityTaskDetail.ENABLE);
					qualityTaskDetails.add(regexDetail);
				}
				qualityTaskDetailService.insertList(qualityTaskDetails);
				// 生成design_table_info表
				// 先根据datasource_id,table_name,判断design_table_info中是否已经存在相应的记录；
				DesignTableInfo example = new DesignTableInfo();
				example.setDatasourceId(relation.getSourceId());
				example.setTableName(relation.getTablename());
				List<DesignTableInfo> tableInfos = designTableService.query(example);
				if (tableInfos != null && tableInfos.size() == 1) {
					DesignTableInfo tableInfo = tableInfos.get(0);
					List<QualityTaskDetail> taskDetails = qualityTaskDetailService.findByDesignTableId(
							tableInfo.getDatasourceId(), tableInfo.getTableName());
					if (taskDetails != null && taskDetails.size() > 0) {
						tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
						designTableService.update(tableInfo);
					}
					
				} else if (tableInfos == null || tableInfos.size() <= 0) {
					DesignTableInfo tableInfo = new DesignTableInfo();
					tableInfo.setDatasourceId(relation.getSourceId());
					tableInfo.setTaskName(relation.getTablename() + "_QualityTask");
					tableInfo.setTableName(relation.getTablename());
					tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
					tableInfo.setCreateTime(new Date());
//					tableInfo.setCreateUser("admin");
					tableInfo.setUpdateTime(new Date());
					tableInfo.setDeleteFlag(Constant.NOT_DELETE);
					tableInfo.setIsDispatch(0);
					designTableService.insert(tableInfo);
				} else {
					throw new CommonException("根据datasourceId和tableName查询tableDesignInfo异常，查询结果：" 
							+ tableInfos);
				}
				
				
			}
		}
	}

	@Override
	public List<DataElementEntity> getByIds(String ids) throws Exception {
		// TODO Auto-generated method stub
		return dataElementDao.getByIds(ids);
	}

	@Override
	public List<DataElementEntity> getAllElement(Long dataSourceId,
			String tableName, String dataElementType, String dataElementName)
			throws Exception {
		return dataElementDao.getAllElement(dataSourceId, tableName, dataElementType, dataElementName);
	}

	@Override
	public Map<String, Integer> getElementCountInfo() throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Integer sum = dataElementDao.getElementSum();
		Integer relCount = relationShipDao.getRelEleCount();
		Integer unRelCount = sum -relCount;
		map.put("sum", sum);
		map.put("relCount", relCount);
		map.put("unRelCount",unRelCount);
		return map;
	}

	@Override
	public List<DataElementEntity> getElementTop5() throws Exception {
		return dataElementDao.getElementTop5();
	}
	
	public List<DataElementEntity> getRelEleTop20() throws Exception {
		/*List<RelationShipEntity> relList = relationShipDao.getRelTop10();
		List<Long> ids = new ArrayList<>();
		for(RelationShipEntity tmp:relList){
			ids.add(tmp.getDataElementId());
		}
		List<DataElementEntity> result = listByIds(ids);*/
		List<DataElementEntity> result = dataElementDao.getRelElementTop20();
		return result;
	}

	@Override
	public JSONObject getCountByTime() throws Exception {
		JSONObject result = new JSONObject();
		List<String> time = new ArrayList<>();
		List<Integer> element = new ArrayList<>();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd");
		Date date = new Date();
		for(int i = 0; i < 4; i++){
			Date date2 = getlastWeakTime(date, i);
			time.add(df.format(date2));
			element.add((dataElementDao.getCountByTime(date2)+1)*120);
		}
		result.put("time", time);
		result.put("element", element);
		return result;
	} 
	
    /**
	 * 获取系统一周前的时间
	 * @return
	 */
	public static Date getlastWeakTime(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, -i);
		date = calendar.getTime();
		return date;
	}

	@Override
	public boolean checkRelation(Long sourceId,
			String tableName, String column) throws Exception {
		// TODO Auto-generated method stub
//		int count = dataElementDao.checkRelation(sourceId, tableName, column);
//		if(count>0){
//			return true;
//		}else{
//			return false;
//		}
		return false;
	}

	public void saveRelation(Long dataElementId, String relationJson) {
		List<RelationShipEntity> list;
		try {
			list = relationShipService.getByElementId(dataElementId);
		} catch (Exception e) {
			throw new CommonException("根据数据元id查询数据元关联关系异常，dataelementID=" + dataElementId, e);
		}
		List<Long> ids = new ArrayList<>();
		if(null != list && list.size() > 0){
			for(RelationShipEntity relation : list){
				ids.add(relation.getId());
			}
		}
		if(ids.size()>0){
			// 表示该数据元之前已经关联过表，需要先删除之前绑定表
			try {
				// 先将与这些关联关系相关的调度停掉，并且将design_table_info中的记录状态设置为草稿
				for (RelationShipEntity relation : list) {
//					List<QualityTaskDetail> taskDetails = qualityTaskDetailService.findByRelationId(
//							relation.getId());
					List<Long> deleteIds = new ArrayList<>();
					deleteIds.add(relation.getId());
					qualityTaskDetailService.deleteByRelationIds(deleteIds);
					DesignTableInfo tableInfo = designTableService.getBySourceAndTable(
							relation.getSourceId(), relation.getTablename());
					List<QualityTaskDetail> qualityTaskDetails = qualityTaskDetailService
							.findByDesignTableId(relation.getSourceId(), relation.getTablename());
					if (tableInfo != null) {
						if (qualityTaskDetails != null && qualityTaskDetails.size() > 0) {
							tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
						} else {
							tableInfo.setStatus(DesignTableInfo.STATUS_UNCOMMIT);
						}
						designTableService.update(tableInfo);
						/*DispatchTask dispatchTask = dispatchService.findByTaskIdAndType(tableInfo.getId(), "1");
						if (dispatchTask != null) {
							dispatchService.pause(dispatchTask.getId());
						}*/
					}
					
					/*if (taskDetails != null && taskDetails.size() > 0) {
						for (QualityTaskDetail detail : taskDetails) {
							DesignTableInfo tableInfo = designTableService.getBySourceAndTable(
									detail.getDatasourceId(), detail.getTableName());
							if (tableInfo != null) {
								List<QualityTaskDetail> details = qualityTaskDetailService
										.findByDesignTableId(tableInfo.getDatasourceId()
										, tableInfo.getTableName());
								if (details == null || details.size() <= 0) {
									tableInfo.setStatus(DesignTableInfo.STATUS_UNCOMMIT);
								} else {
									tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
								}
								designTableService.update(tableInfo);
								DispatchTask dispatchTask = dispatchService.findByTaskIdAndType(tableInfo.getId(), "1");
								if (dispatchTask != null) {
									dispatchService.pause(dispatchTask.getId());
								}
							}
						}
					}*/
				}
				// 先要同步删除原来创建的quality_task_detail记录 
//				qualityTaskDetailService.deleteByRelationIds(ids);
				relationShipService.delete(ids);
				
			} catch (Exception e) {
				throw new CommonException("根据id批量删除数据元关联关系异常，relationids=" + ids, e);
			}
		}
		List<RelationShipEntity> relations = JSONArray.parseArray(relationJson, RelationShipEntity.class);
//		if(StringUtils.isNotEmpty(relationJson)){
		if(relations != null && relations.size() > 0) {
//			List<RelationShipEntity> relations = JSONArray.parseArray(relationJson, RelationShipEntity.class);
			for(RelationShipEntity relation : relations){
				if (relation.getId() > 0) {
					// 之前就存在的，这次就将id重置后，再插入
					relation.setId(0);
					relation.setLastModifyTime(new Date());
				} else {
					relation.setDataElementId(dataElementId);
					relation.setIsdel(Constant.NOT_DELETE);
					relation.setCreateTime(new Date());
					relation.setLastModifyTime(new Date());
				}
			}
			try {
				relationShipService.insertList(relations);
			} catch (Exception e) {
				throw new CommonException("批量插入数据元关联关系异常：", e);
			}
			// 判断数据元是否绑定了表，如果绑定了表就生成quality_task_detail记录
			// 生成质量规则，入quality_task_detail表。
			try {
				createQualityTaskDetail(relations);
			} catch (Exception e) {
				throw new CommonException("生成质量规则并入quality_task_detail表时异常：", e);
			}
			
			
		}
	}
	

	@Override
	@Transactional
	public void importFromExcel(File file) {
//		List<DataElementEntity> dataElements = new ArrayList<>();
		XSSFWorkbook xssfwb = null;
		try {
			try {
				xssfwb = new XSSFWorkbook(file);
			} catch (InvalidFormatException | IOException e) {
				throw new CommonException("读取数据元excel文件异常：", e);
			}
			XSSFSheet sheet = xssfwb.getSheetAt(0);
			String valueOfA2 = sheet.getRow(1).getCell(0).getStringCellValue();
			if (!"数据元导入模板".equals(valueOfA2.trim())) {
				throw new CommonException("Excel文件格式异常，请下载正确的模板填写后重新上传！");
			}
			int lastRowNum = sheet.getLastRowNum();
			for (int i = EXCEL_READ_START_ROW; i <= lastRowNum; i++) {
				DataElementEntity dataElement = new DataElementEntity();
				XSSFRow row = sheet.getRow(i);
				if (row != null) {
					XSSFCell cell = row.getCell(0);
					if (cell != null) {
						cell.setCellType(CellType.STRING);
						dataElement.setDataElementName(cell.getStringCellValue());
					}
					cell = row.getCell(1);
					if (cell != null) {
						cell.setCellType(CellType.STRING);
						dataElement.setDataElementCode(cell.getStringCellValue());
					}
					cell = row.getCell(2);
					if (cell != null) {
						cell.setCellType(CellType.STRING);
						dataElement.setDataElementType(cell.getStringCellValue());
					}
					cell = row.getCell(3);
					if (cell != null) {
						cell.setCellType(CellType.STRING);
						dataElement.setDataElementAttr(cell.getStringCellValue());
					} else {
						dataElement.setDataElementAttr("");
					}
					
					cell = row.getCell(4);
					if (cell != null) {
						cell.setCellType(CellType.STRING);
						String codesetId = cell.getStringCellValue();
						if (StringUtils.isNotEmpty(codesetId)) {
							String[] split = codesetId.split("\\^");
							dataElement.setCodeset(split[1]);
							// 根据codesetid获取上面的所有上级id，并拼接成逗号分隔的字符串。
							// 先判断这个codeSet是否存在
							CodeSet codeset = codeSetService.getById(Long.valueOf(split[1]));
							if (codeset == null 
									|| codeset.getIsdel() == Integer.parseInt(Constant.IS_DELETE)) {
								throw new CommonException(codesetId + "对应的代码集不存在，请修改后重新导入！");
							}
							// 判断该代码集中的所有代码是否都是已审核状态
							Example codeExample = new Example(Code.class);
							codeExample.createCriteria().andEqualTo("isdel", 0)
									.andNotEqualTo("status", Code.STATUS_AUDITED)
									.andEqualTo("codesetId", codeset.getId());
							List<Code> unauditCodes = codeDao.selectByExample(codeExample);
							if (unauditCodes != null && unauditCodes.size() > 0) {
								throw new CommonException(codesetId + "对应的代码集下存在非审核状态的代码，请修改后重新导入！");
							}
							StringBuilder allParentIds = new StringBuilder();
							codeSetService.getAllParent(Long.valueOf(split[1]), allParentIds);
							dataElement.setCodeList(allParentIds.substring(0, allParentIds.length() - 1));
						}
					}
					cell = row.getCell(5);
					if (cell != null) {
						cell.setCellType(CellType.STRING);
						dataElement.setDataRangeFront(cell.getStringCellValue());
					} else {
						dataElement.setDataRangeFront("");
					}
					cell = row.getCell(6);
					if (cell != null) {
						cell.setCellType(CellType.STRING);
						dataElement.setDataRangeEnd(cell.getStringCellValue());
					} else {
						dataElement.setDataRangeEnd("");
					}
					cell = row.getCell(7);
					if (cell != null) {
						cell.setCellType(CellType.STRING);
						switch(cell.getStringCellValue()){
							case "身份证号码验证":
								dataElement.setRegularExpression("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X))$");
								break;
							case "手机号码验证":
								dataElement.setRegularExpression("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
								break;
							case "IP地址验证":
								dataElement.setRegularExpression("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))");
								break;
							case "邮箱地址验证":
								dataElement.setRegularExpression("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
								break;
							default:
								break;
						}
					}
					cell = row.getCell(8);
					if (cell != null) {
						cell.setCellType(CellType.STRING);
						dataElement.setRemark(cell.getStringCellValue());
					}
					dataElement.setStatus(DataElementEntity.STATUS_DRAFT);
					dataElement.setCreateTime(new Date());
					dataElement.setLastModifyTime(new Date());
					dataElement.setUniqueCode(dataElement.getDataElementCode() + dataElement.getDataElementType());
					dataElement.setIsdel(Constant.NOT_DELETE);
				}
//				dataElements.add(dataElement);
				// 校验各个参数是否符合要求
//				validate(dataElement);
				// 根据唯一标识 查看数据库中是否已经存在该数据元，然后再插入
				DataElementEntity existElement = null;
				try {
					existElement = dataElementDao.getByUniqueCode(dataElement.getUniqueCode());
				} catch (Exception e) {
					throw new CommonException("根据uniqueCode查找数据元异常，uniqueCode=" 
							+ dataElement.getUniqueCode());
				}
				if (existElement != null) {
					throw new CommonException("第" + (i + 1) + "行" + dataElement.getDataElementCode() 
							+ "数据元已存在，导入失败，请修改后重新导入！");
				}
				dataElementDao.insert(dataElement);
				
			}
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		} finally {
			try {
				xssfwb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
//		dataElementDao.insertList(dataElements);
		
	}

	private void validate(DataElementEntity dataElement) {
		
		if (!(dataElement.getDataElementName().length() > 0 
				&& dataElement.getDataElementName().length() <= 20)) {
			throw new CommonException("数据源名称长度必须大于0且小于20！");
		}
		if (!dataElement.getDataElementCode().matches("[a-zA-Z0-9_]+")) {
			throw new CommonException("数据源英文名称只能包含英文字母、数字、下划线！");
		}
		String type = dataElement.getDataElementType();
		if ("c".equals(type) || "c..".equals(type) 
				|| "a".equals(type) || "a..".equals(type) 
				|| "n".equals(type)|| "n..".equals(type)
				|| "an".equals(type)|| "an..".equals(type)) {
			if (!dataElement.getDataElementAttr().matches("[0-9]+")) {
				throw new CommonException("参数错误，检查数据长度是否为数字！");
			}
		} else if ("n..()".equals(type)) {
			if (!dataElement.getDataElementAttr().matches("\\d+(,\\d+)")) {
				throw new CommonException("参数错误，检查数据长度是否为英文逗号分隔的两个数字！");
			}
		}
	}

	@Override
	public String searchColumn(Long sourceId, Long elementId) {
		DataElementEntity element = dataElementDao.selectByPrimaryKey(elementId);
		String alias = element.getStandardAlias();
		String dataElementCode = element.getDataElementCode();
		String columnNames;
		if (alias != null && alias.trim().length() > 0) {
			columnNames = dataElementCode + "," + alias;
		} else {
			columnNames = dataElementCode;
		}
		String metadatas = datasourceService2.searchColumn(columnNames, sourceId);
//		String metadataInfos = ResponseUtil.getStringData(metadatas);
		List<MetadataInfo> infos = ResponseUtil.getListData(metadatas, MetadataInfo.class);
		List<DataElementEntity> elements = relationShipService.getElementByDatasourceId(sourceId);
		if (infos != null && infos.size() > 0) {
			for (MetadataInfo metadataInfo : infos) {
				List<MetadataInfo> children = metadataInfo.getChildren();
				for (MetadataInfo column : children) {
					for (DataElementEntity dataElement : elements) {
						if (column.getTableName().equals(dataElement.getTableName()) 
								&& column.getMetadataName().equals(dataElement.getFieldName())) {
							column.setIsBind(true);
//							StringBuffer name2 = new StringBuffer();
//	    					name2.append(column.getMetadataName()).append("(").append(column.getType())
//			    					.append(")").append("——").append(dataElementEntity.getDataElementCode()).append("(")
//			    					.append(dataElementEntity.getDataElementName()).append(" ").append(dataElementEntity.getDataElementType())
//			    					.append(")");
//	    					column.setTypename(name2.toString());
						}
					}
				}
				
			}
		}
		
		return JSON.toJSONString(infos);
//		return metadatas;
	}
	
	
//	public static void main(String[] args) throws InvalidFormatException, IOException {
//		importFromExcel1(new File("C:\\Users\\yx-hj\\Desktop\\test.xlsx"));
//	}

}
