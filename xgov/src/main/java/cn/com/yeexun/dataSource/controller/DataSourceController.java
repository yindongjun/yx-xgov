package cn.com.yeexun.dataSource.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.collectTask.service.ICollectTaskService;
import cn.com.yeexun.common.controller.BaseController;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataElement.service.IDataElementService;
import cn.com.yeexun.dataElement.service.IRelationShipService;
import cn.com.yeexun.dataSource.entity.DataSource;
//import cn.com.yeexun.dataSource.entity.DataSourceLayer;
import cn.com.yeexun.dataSource.entity.ExcuteResult;
import cn.com.yeexun.dataSource.entity.HbaseSource;
import cn.com.yeexun.dataSource.entity.HdfsSource;
import cn.com.yeexun.dataSource.entity.HiveSource;
import cn.com.yeexun.dataSource.entity.ImpalaSource;
import cn.com.yeexun.dataSource.entity.Meta;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
//import cn.com.yeexun.dataSource.service.IDataSourceLayerService;
//import cn.com.yeexun.dataSource.service.IDataSourceService;
//import cn.com.yeexun.dataSource.service.IHbaseSourceService;
//import cn.com.yeexun.dataSource.service.IHiveSourceService;
//import cn.com.yeexun.dataSource.service.IImpalaSourceService;
import cn.com.yeexun.dataSource.service.impl.IDatasourceClient;
import cn.com.yeexun.meta_data.collector.HiveMetadataCollector;
import cn.com.yeexun.meta_data.collector.JdbcHelper;
import cn.com.yeexun.meta_data.collector.MetadataCollector;
import cn.com.yeexun.meta_data.entity.HiveMetadataInfo;
import cn.com.yeexun.meta_data.service.IMetadataService;
import cn.com.yeexun.qualityTask.dao.IQualityTaskDetailDao;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityTask.service.IDesignTableInfoService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import cn.com.yeexun.utils.FeignException;
import cn.com.yeexun.utils.ParameterUtil;
import cn.com.yeexun.utils.ResponseUtil;
import cn.com.yeexun.utils.VerifyException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Controller
@RequestMapping("data_source")
@Api("数据源管理")
public class DataSourceController /*extends BaseController<DataSource> */{

	private Logger logger = Logger.getLogger(DataSourceController.class);

//	@Autowired
//	private IDataSourceService dataSourceService;
//
//	@Autowired
//	private IDataSourceLayerService dataSourceLayerService;
	
//	@Autowired
//	private IHdfsSourceService hdfsSourceService;
	
//	@Autowired
//	private IHiveSourceService hiveSourceService;
//	
//	@Autowired
//	private IImpalaSourceService impalaSourceService;
//	
//	@Autowired
//	private IHbaseSourceService hbaseSourceService;
	
//	DataSource dataSource = new DataSource();
//	HiveSource hiveSource = new HiveSource();
//	HbaseSource hbaseSource = new HbaseSource();
//	HdfsSource hdfsSource = new HdfsSource();

	@Autowired
	private ICollectTaskService collectTaskService;
	
	@Autowired
	private IRelationShipService relationShipService;
	
	@Autowired
	private IDataElementService dataElementService;
	
	@Autowired
	private IMetadataService metadataService;
	
	@Autowired
	private IDesignTableInfoService tableInfoService;
	
	@Autowired
	private IQualityTaskDetailDao qualityTaskDetailDao;
	
	@Autowired
	private IDatasourceClient datasourceClient;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	
	/*@Override
	public IBaseService<DataSource> service() {
//		return dataSourceService;
		return null;
	}*/

	/**
	 * 数据源校验
	 * 
	 * @param datasource
	 * @param request
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "/check_datasource", method = RequestMethod.POST)
	@ApiOperation("数据源校验")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "数据源id", required = true, dataType = "Long"),
			@ApiImplicitParam(name = "datasourceName", value = "数据源名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "databaseType", value = "数据源类型", required = true, dataType = "String"),
			@ApiImplicitParam(name = "characterSet", value = "数据源编码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "userName", value = "数据库用户名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "password", value = "数据库密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "dbname", value = "数据库名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "oracleSid", value = "oracle实例名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "ip", value = "IP地址", required = true, dataType = "String"),
			@ApiImplicitParam(name = "port", value = "端口号", required = true, dataType = "String"),
			@ApiImplicitParam(name = "sourceLayerId", value = "数据分层id", required = true, dataType = "int"),
			@ApiImplicitParam(name = "departmentId", value = "部门id", required = true, dataType = "int"),
			@ApiImplicitParam(name = "schemasName", value = "模式名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataDbType", value = "hive元数据库类型", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataIp", value = "hive元数据库ip", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataPort", value = "hive元数据库端口号", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataDbName", value = "hive元数据库名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataUserName", value = "hive元数据库用户名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataPassword", value = "hive元数据库密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "zkQuorum", value = "hbaseIP地址", required = true, dataType = "String"),
			@ApiImplicitParam(name = "rootdir", value = "hbaseURL", required = true, dataType = "String"),
			@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest") })
	public String checkDataSource(Long id, String datasourceName,
			String databaseType, String characterSet, String userName,
			String password, String dbname, String oracleSid, String ip,
			String port, Long layerId,String layerList, Long departmentId,
			String schemasName, String hiveMetaDataDbType,
			String hiveMetaDataIp, String hiveMetaDataPort,
			String hiveMetaDataDbName, String hiveMetaDataUserName,
			String hiveMetaDataPassword, Long hbaseId,Long hiveId,Long hdfsId,String zkQuorum, String rootdir,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		DataSource dsEntity = getDataSource(id, datasourceName, databaseType,
				characterSet, userName, password, dbname, oracleSid, ip, port,
				layerId, layerList,departmentId, schemasName);

		HiveSource dsHive = getHiveSource(hiveId,hiveMetaDataDbType, hiveMetaDataIp,
				hiveMetaDataPort, hiveMetaDataDbName, hiveMetaDataUserName,
				hiveMetaDataPassword);

		HbaseSource dsHbase = getHbaseSource(hbaseId,zkQuorum, rootdir);

		HdfsSource dsHdfs = new HdfsSource();
		dsHdfs.setKeytabFilepath("");
		dsHdfs.setPrincipal("");
		dsHdfs.setHadoopConfig("[]");
		dsHdfs.setHaveKerberos(0);
		try {
			map = dataSourceService.dataSourceCheck(dsEntity, dsHive, dsHbase,
					dsHdfs);
		} catch (SQLException e2) {
			logger.error("error", e2);
			map = CodeUtil.getErrorMap(ParameterUtil.CONN_PARAMS_ERROR);
		} catch (TimeoutException e3) {
			logger.error("error", e3);
			map = CodeUtil.getErrorMap(ParameterUtil.HDFS_CONN_TIMEOUT);
		} catch (VerifyException verifyException){
			logger.error("error", verifyException);
			map = CodeUtil.getErrorMap(verifyException.getMessage());
		} catch (Exception e4) {
			logger.error("error", e4);
			map = CodeUtil.getErrorMap(ParameterUtil.MSG_ERROR);
		}
		return JSON.toJSONString(map);
	}

	public DataSource getDataSource(Long id, String datasourceName,
			String databaseType, String characterSet, String userName,
			String password, String dbname, String oracleSid, String ip,
			String port, Long sourceLayerId,String layerList, Long departmentId, String schemasName) {
		DataSource dataSource = new DataSource();
		if (id != null) {
			dataSource.setId(id);
		} else {
			dataSource.setId(0);
		}
		dataSource.setDatasourceName(datasourceName);
		dataSource.setDatabaseType(databaseType);
		dataSource.setCharacterSet(characterSet);
		dataSource.setUserName(userName);
		
		if (id != null && id > 0 && StringUtils.isNotEmpty(password)) {
			try {
				DataSource source = dataSourceService.getById(id);
				dataSource.setPassword(source.getPassword());
			} catch (Exception e) {
				throw new CommonException("编辑数据源异常！", e);
			}
		} else {
			dataSource.setPassword(password);
		}
		dataSource.setPassword(password);
		
		dataSource.setDbname(dbname);
		dataSource.setOracleSid(oracleSid);
		dataSource.setIp(ip);
		dataSource.setPort(port);
//		dataSource.setSourceLayerId(1);
		//2018/12/20 设置数据源目录分层 dyc
		dataSource.setSourceLayerId(sourceLayerId.intValue());
		dataSource.setLayerList(layerList);
		dataSource.setDepartmentId(1);
		dataSource.setSchemasName(schemasName);
		return dataSource;
	}

	public HiveSource getHiveSource(Long hiveId,String hiveMetaDataDbType,
			String hiveMetaDataIp, String hiveMetaDataPort,
			String hiveMetaDataDbName, String hiveMetaDataUserName,
			String hiveMetaDataPassword) {
		HiveSource hiveSource = new HiveSource();
		if (hiveId != null) {
			hiveSource.setId(hiveId);
		} else {
			hiveSource.setId(0);
		}
		hiveSource.setHiveMetaDataIp(hiveMetaDataIp);
		hiveSource.setHiveMetaDataPort(hiveMetaDataPort);
		hiveSource.setHiveMetaDataDbName(hiveMetaDataDbName);
		hiveSource.setHiveMetaDataUserName(hiveMetaDataUserName);
		hiveSource.setHiveMetaDataPassword(hiveMetaDataPassword);
		return hiveSource;
	}

	public HbaseSource getHbaseSource(Long hbaseId,String zkQuorum, String rootdir) {
		HbaseSource hbaseSource = new HbaseSource();
		if (hbaseId != null) {
			hbaseSource.setId(hbaseId);
		} else {
			hbaseSource.setId(0);
		}
		hbaseSource.setZkQuorum(zkQuorum);
		hbaseSource.setRootdir(rootdir);
		return hbaseSource;
	}
	
	public HdfsSource getHdfsSource(Long hdfsId){
		HdfsSource hdfsSource = new HdfsSource();
		if (hdfsId != null) {
			hdfsSource.setId(hdfsId);
		} else {
			hdfsSource.setId(0);
		}
		hdfsSource.setHaveKerberos(0);
		hdfsSource.setKeytabFilepath("");
		hdfsSource.setPrincipal("");
		hdfsSource.setHadoopConfig("[]");
		return hdfsSource;
	}

	@ResponseBody
	@RequestMapping(value = "/update_add_action", method = RequestMethod.POST)
	@ApiOperation("保存数据源")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "数据源id", required = true, dataType = "Long"),
			@ApiImplicitParam(name = "datasourceName", value = "数据源名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "databaseType", value = "数据源类型", required = true, dataType = "String"),
			@ApiImplicitParam(name = "characterSet", value = "数据源编码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "userName", value = "数据库用户名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "password", value = "数据库密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "dbname", value = "数据库名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "oracleSid", value = "oracle实例名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "ip", value = "IP地址", required = true, dataType = "String"),
			@ApiImplicitParam(name = "port", value = "端口号", required = true, dataType = "String"),
			@ApiImplicitParam(name = "sourceLayerId", value = "数据分层id", required = true, dataType = "int"),
			@ApiImplicitParam(name = "departmentId", value = "部门id", required = true, dataType = "int"),
			@ApiImplicitParam(name = "schemasName", value = "模式名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataDbType", value = "hive元数据库类型", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataIp", value = "hive元数据库ip", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataPort", value = "hive元数据库端口号", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataDbName", value = "hive元数据库名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataUserName", value = "hive元数据库用户名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "hiveMetaDataPassword", value = "hive元数据库密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "zkQuorum", value = "hbaseIP地址", required = true, dataType = "String"),
			@ApiImplicitParam(name = "rootdir", value = "hbaseURL", required = true, dataType = "String"),
			@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest") })
	public String updateDataSource(Long id, String datasourceName,
			String databaseType, String characterSet, String userName,
			String password, String dbname, String oracleSid, String ip,
			String port, Long layerId, String layerList,Long departmentId,
			String schemasName, String hiveMetaDataDbType,
			String hiveMetaDataIp, String hiveMetaDataPort,
			String hiveMetaDataDbName, String hiveMetaDataUserName,
			String hiveMetaDataPassword,Long hbaseId,Long hiveId,Long hdfsId, String zkQuorum, String rootdir,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		DataSource dsEntity = getDataSource(id, datasourceName, databaseType,
				characterSet, userName, password, dbname, oracleSid, ip, port,
				layerId,layerList, departmentId, schemasName);

		HiveSource dsHive = getHiveSource(hiveId,hiveMetaDataDbType, hiveMetaDataIp,
				hiveMetaDataPort, hiveMetaDataDbName, hiveMetaDataUserName,
				hiveMetaDataPassword);

		HbaseSource dsHbase = getHbaseSource(hbaseId,zkQuorum, rootdir);

		HdfsSource dsHdfs = getHdfsSource(hdfsId);
//		dsHdfs.setHaveKerberos(0);
//		dsHdfs.setKeytabFilepath("");
//		dsHdfs.setPrincipal("");
//		dsHdfs.setHadoopConfig("[]");
		try {
			// long userId = userUtil.currentUser(request);
			dataSourceService.updateAndAddDataSource(dsEntity, dsHive, dsHbase,
					dsHdfs);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("error", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}*/

	/**
	 * 数据源显示
	 * 
	 * @param request
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "/list_action", method = RequestMethod.POST)
	@ApiOperation("显示数据源")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "start", value = "分页起始记录", required = true, dataType = "int"),
		@ApiImplicitParam(name = "length", value = "每页显示条数", required = true, dataType = "int"),
		@ApiImplicitParam(name = "dataSourceType", value = "数据源类型", required = true, dataType = "String"),
		@ApiImplicitParam(name = "dataSourceName", value = "数据源名称", required = true, dataType = "String"),
		@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest") 
		})
	public String listDataSource(int start,int length,Long layerId,String dataSourceType,String dataSourceName, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = transferRequestParamToMap(request);
			Page<DataSource> page = new Page<DataSource>();
//			String dataType = MapUtils.getString(paramMap, "dataType");
//			String dsName = MapUtils.getString(paramMap, "dsName");
			page.setStart(MapUtils.getIntValue(paramMap, "start", start));
			page.setLength(MapUtils.getIntValue(paramMap, "length", length));
			// 根据角色显示数据源 管理员：全查 其它：对应用户ID查询
			Long userId = (long) 2;
			// boolean roleBl = iRoleServiceImpl.byUserIdCompareRole(userId);
			// if(roleBl) {
			// userId = null;
			// }
			List<DataSource> list = dataSourceService.listDataSourcePage(
					userId, dataSourceName, dataSourceType,layerId, page);
			if(list != null && list.size() > 0){
				//生成数据库的服务地址，形如：数据库名@地址   hdfs只显示地址  hive显示
				for( DataSource ds:list ){
					if(StringUtils.isNotBlank(ds.getDbname()) && collectTaskService.isRDBMS(ds))
						ds.setSourceAddress( ds.getDbname() + "@" + ds.getIp() );
					else if( ds.getDatabaseType().equalsIgnoreCase(Constant.HBASE) ){
						HbaseSource hbaseSource = hbaseSourceService.getByDataSourceId( ds.getId() );
						ds.setSourceAddress( ds.getDbname() + "@" + hbaseSource.getZkQuorum() );
					}else if( ds.getDatabaseType().equalsIgnoreCase(Constant.HIVE) ){
						ds.setSourceAddress( ds.getDbname() + "@" + ds.getIp() );
					}else {
						ds.setSourceAddress( ds.getIp() );
					}



				}
			}
			page.setData(list);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
			map.put(CodeUtil.RESULT_DRAW, request.getParameter("draw"));
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} 
		catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		} 
		return JSON.toJSONString(map);
	}

	@ResponseBody
	@RequestMapping(value = "layerMenu", method = RequestMethod.GET)
	@ApiOperation("显示分层表菜单")
	@ApiImplicitParams({ @ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest") })
	public String layerMenu(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DataSourceLayer> fristMenu = dataSourceLayerService
					.getMenuLayer();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, fristMenu);
		} catch (Exception e) {
			logger.error("error", e);
			return JSON.toJSONString(CodeUtil.getErrorRequestMap());
		}
		return JSON.toJSONString(map,
				SerializerFeature.DisableCircularReferenceDetect);
	}*/

	/**
	 * 数据源删除 逻辑删除
	 * 
	 * @param ids
	 *            数据源id 单个或多个
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	/*@ResponseBody
	@RequestMapping(value = "/deletes_action", method = RequestMethod.GET)
	@ApiOperation("删除数据源")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "需要删除的数据源id", required = true, dataType = "String"),
			@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest") })
	public String deleteDataSource(Long id, HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = CodeUtil.getSuccessMap();
		try {
			boolean elementRelation = relationShipService.elementRelation(id);
			if(elementRelation){
				throw new CommonException("该数据源已关联的数据元标准，如需删除，请前往数据标准-数据元解除关联关系，再进行删除操作。");
			}
			Example example = new Example(QualityTaskDetail.class);
			example.createCriteria().andEqualTo("datasourceId", id).andEqualTo("deleteFlag", "0")
					.andNotEqualTo("verifyType", "1");
			List<QualityTaskDetail> qualityTaskDetails = qualityTaskDetailDao.selectByExample(example);
			if (qualityTaskDetails != null && qualityTaskDetails.size() > 0) {
				throw new CommonException("该数据源已关联规则设计，无法删除");
			}
			//修改该数据源的采集任务
			collectTaskService.delDateSource(id);
			DataSource dataSources = service().getById(id);
			dataSources.setIsdel(1);
			service().save(dataSources);
			// 删除校验任务
			List<QualityTaskDetail> taskDetails = tableInfoService.getQualityTaskIdBySourceId(id);
			if (taskDetails != null && taskDetails.size() > 0) {
				String taskDetailIds = "";
				for (QualityTaskDetail qualityTaskDetail : taskDetails) {
					taskDetailIds = taskDetailIds + qualityTaskDetail.getId() + ",";
				}
				tableInfoService.deleteQualityTasks(taskDetailIds.substring(0, taskDetailIds.length() - 1));
			}
			
			if(Constant.HIVE.equals(dataSources.getDatabaseType())){
				HiveSource hiveSource = hiveSourceService.getByDataSourceId(dataSources.getId());
				hiveSource.setIsdel(1);
				hiveSourceService.save(hiveSource);
			} else if (Constant.IMPALA.equals(dataSources.getDatabaseType())) {
				ImpalaSource impalaSource = impalaSourceService.getByDataSourceId(dataSources.getId());
				impalaSource.setIsdel(Integer.valueOf(Constant.IS_DELETE));
				impalaSourceService.save(impalaSource);
			} else if(Constant.HBASE.equals(dataSources.getDatabaseType())){
				HbaseSource hbaseSource = hbaseSourceService.getByDataSourceId(dataSources.getId());
				hbaseSource.setIsdel(1);
				hbaseSourceService.save(hbaseSource);
			}
			//删除数据源之后删除采集的元数据信息
//				metadataService.getBySourceId(id);
			metadataService.deleteBySourceId(id);
			
			
			//List<DataSource> dataSources = service().listByIds(list);
//			if (dataSources == null || dataSources.isEmpty()) {
//				return JSON.toJSONString(CodeUtil.getBadRequestMap());
//			}
//			for (DataSource dataSource : dataSources) {
//				dataSource.setIsdel(1);
//				service().save(dataSource);
//			}
		} catch (CommonException e){
			logger.warn(e);
	    	map = CodeUtil.getErrorMap(e.getMessage());
		} catch (Exception e) {
			map = CodeUtil.getErrorMap(e.getMessage());
			logger.error("error:", e);
		}
		return JSON.toJSONString(map);
	}

	@ResponseBody
	@RequestMapping(value = "/updateAddLayer", method = RequestMethod.POST)
	@ApiOperation("添加修改分层")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "数据分层id", required = true, dataType = "long"),
			@ApiImplicitParam(name = "name", value = "数据分层名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "parentId", value = "数据分层父id", required = true, dataType = "String"),
			@ApiImplicitParam(name = "remark", value = "数据分层描述", required = true, dataType = "String"),
			@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest") })
	public String updateAddLayer(Long id,String name,long parentId,String remark,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		DataSourceLayer dslEntity = new DataSourceLayer();
		if (id != null) {
			dslEntity.setId(id);
		} else {
			dslEntity.setId(0);
			dslEntity.setBuildin("N");
		}
		dslEntity.setName(name);
		dslEntity.setParentId(parentId);
		dslEntity.setRemark(remark);
		try {
			if(!dataSourceLayerService.isExistLayer(id,name)){
				List<DataSourceLayer> layerList = dataSourceLayerService
						.updateAddLayer(dslEntity);
				map = CodeUtil.getSuccessMap();
				map.put(CodeUtil.RESULT_DATA, layerList);
			} else {
				map.put(CodeUtil.MESSAGE_TEXT,"该分层名称已存在！");
				map.put(CodeUtil.CODE_TEXT, CodeUtil.CODE_REQUEST_ERROR);
			}
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}

	@ResponseBody
	@RequestMapping(value = "/deleteLayer", method = RequestMethod.GET)
	@ApiOperation("删除分层")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "删除层的id", required = true, dataType = "Long"),
			@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest") })
	public String deleteLayer(Long id, HttpServletRequest request) {
		Map<String, Object> map = CodeUtil.getSuccessMap();
		if (id == null) {
			return JSON.toJSONString(CodeUtil.getBadRequestMap());
		}
		try {
			dataSourceLayerService.delLayerByPid(id);
			if(dataSourceService.getDataSourceByLayerId(id)){
				map.put(CodeUtil.MESSAGE_TEXT, "已关联数据源，不能删除！");
				map.put(CodeUtil.CODE_TEXT, CodeUtil.CODE_REQUEST_ERROR);
			}else{
				DataSourceLayer dsl = dataSourceLayerService.getById(id);
				if(dsl.getBuildin().equals("N")){
					dataSourceLayerService.delete(id);
					dataSourceLayerService.deleteDataSourceLayerMetadata(dsl);
					map = CodeUtil.getSuccessMap();
				} else {
					map.put(CodeUtil.MESSAGE_TEXT, "该分层为内置分层，不能删除！");
					map.put(CodeUtil.CODE_TEXT, CodeUtil.CODE_REQUEST_ERROR);
				}
			}
		} catch (CommonException e){
	    	map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}*/

	/**
	 * 数据源回显
	 * 
	 * @param id
	 *            数据源id
	 * @param request
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "/get_source_id", method = RequestMethod.GET)
	@ApiOperation("数据源回显")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "数据源id", required = true, dataType = "Long"),
			@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest") })
	public String getSourceById(long id, HttpServletRequest request) {
		Map<String, Object> map = CodeUtil.getSuccessMap();
		try {
			map = dataSourceService.echoDataSource(id);
			// map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}

	@ResponseBody
	@RequestMapping(value = "/get_layer_id", method = RequestMethod.GET)
	@ApiOperation("数据分层回显")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "数据分层id", required = true, dataType = "Long"),
			@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest") })
	public String getLayerById(long id, HttpServletRequest request) {
		Map<String, Object> map = CodeUtil.getSuccessMap();
		try {
			map = dataSourceLayerService.echoDataLayer(id);
			// map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}*/
	
	@ResponseBody
	@RequestMapping(value = "/get_datatree", method = RequestMethod.POST)
	@ApiOperation("获取字段信息-数据元关联关系处展示")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "数据源id", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "pid", value = "父id", required = true, dataType = "HttpServletRequest"),
		@ApiImplicitParam(name = "name", value = "表名", required = true, dataType = "HttpServletRequest") })
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		/*
		 * 获取传进来的参数，id，pid，name
		 */
        String id = request.getParameter("id");
        String pid = request.getParameter("pid");
        String name = request.getParameter("name");
        String searchName = request.getParameter("searchName");
        String sourceName = request.getParameter("sourceName");
        long metadataid = 0;
        if(!StringUtil.isEmpty(id)){
        	metadataid = Integer.parseInt(id);
        }
       	long metadatapid = Integer.parseInt(pid);
        String type = null;
        Map<String, Object> map = new HashMap<String, Object>();
    	List<Meta> list = new ArrayList<Meta>();
    	/*
    	 * 对id，pid进行判断
    	 * 当pid为-1时，为第一次加载树，获取数据源,
    	 * 当pid小于-1，为加载第二级数据，即获取表
    	 * 当id不为0，pid>0，为加载第三级，即数据列
    	 */
    	try{
    		if( pid.equals("-1")){
//            	List<DataSource> listSource= dataSourceService.getAllDataSource(sourceName);
    			List<DataSource> listSource= datasourceService2.getAllDataSource(sourceName);
            	// 将数据源对象转化为metadata对象，add到list中
    			for (int i = 0; i < listSource.size(); i++) {
    				String sourName = listSource.get(i).getDatabaseType();
    				//判断数据源类型是否为关系型数据源
    				if(collectTaskService.isRDBMS(listSource.get(i)) 
    						|| Constant.IMPALA.equals(listSource.get(i).getDatabaseType())
    						|| Constant.HIVE.equals(listSource.get(i).getDatabaseType())) {
    					Meta meta = new Meta();
	    				meta.setId(listSource.get(i).getId());
	    				meta.setName(listSource.get(i).getDatasourceName()+"("+listSource.get(i).getDbname()
	    						+")");
	    				meta.setTypename(listSource.get(i).getDatasourceName()+"("+listSource.get(i).getDbname()
	    						+")");
	    				meta.setPid(-1l);
	    				//DB2将模式名放入pname
	    				if(Constant.DB2.equals(listSource.get(i).getDatabaseType())){
	    					meta.setPname(listSource.get(i).getSchemasName());
	    				}else{
	    					meta.setPname(listSource.get(i).getDbname());
	    				}
	    				
	    				meta.setIsParent("true");
	    				meta.setType(listSource.get(i).getDatabaseType());
	    			    list.add(meta);
    				}
    			}
            }else if ( metadatapid < -1 && 0!=metadataid && -3!=metadataid) {
//            	DataSource source1 = dataSourceService.getById(metadataid);//查找该数据源
            	DataSource source1 = datasourceService2.getDatasourceById(metadataid);
            	
            	
            	List<Meta> tableList = new ArrayList<Meta>();
            	if(collectTaskService.isRDBMS(source1)) {
            		MetadataCollector collector = collectTaskService.getMetadataCollector(source1.getDatabaseType());
                	// 调用service的获取数据表的方法，add到list中
//            		List<Meta> tableList = new ArrayList<Meta>();
            		tableList = collector.getTables(source1);
            	} else if (Constant.IMPALA.equals(source1.getDatabaseType()) 
            			|| Constant.HIVE.equals(source1.getDatabaseType())) {
					HiveMetadataCollector hiveMetadataCollector = new HiveMetadataCollector();
					Connection conn = JdbcHelper.getConnection(source1.getDriver(), source1.getUrl(), source1.getUserName(), source1.getPassword());
					Connection conn1 = null;
					if (Constant.HIVE.equals(source1.getDatabaseType())) {
//						HiveSource hiveSource = hiveSourceService.getByDataSourceId(source1.getId());
						HiveSource hiveSource = datasourceService2.getHiveSource(source1.getId());
						
						conn1 = JdbcHelper.getConnection(hiveSource.getDriver()
								, hiveSource.getUrl(), hiveSource.getHiveMetaDataUserName()
								, hiveSource.getHiveMetaDataPassword());
					} else {
//						ImpalaSource impalaSource = impalaSourceService.getByDataSourceId(source1.getId());
						ImpalaSource impalaSource = datasourceService2.getImpalaSource(source1.getId());
						
						conn1 = JdbcHelper.getConnection(impalaSource.getDriver()
								, impalaSource.getUrl(), impalaSource.getUserName(), impalaSource.getPassword());
					}
					hiveMetadataCollector.setMetaConnection(conn1);
					hiveMetadataCollector.setConnection(conn);
					List<HiveMetadataInfo> tables = hiveMetadataCollector.getTables(source1);
					for (int i = 0; i < tables.size(); i++) {
						HiveMetadataInfo hiveMetadataInfo = tables.get(i);
						Meta meta = new Meta(); 
						meta.setId(Long.valueOf(i));
						meta.setPid(source1.getId());
						meta.setPname(source1.getDbname());
						meta.setName(hiveMetadataInfo.getName());
						meta.setType(hiveMetadataInfo.getTableType());
						meta.setTypename(hiveMetadataInfo.getName());
						meta.setIsParent("true");
						tableList.add(meta);
					}
				}
        		// 依据表名进行模糊查询
        		if(StringUtil.isNotEmpty(searchName)){
        			 Pattern pattern = Pattern.compile(searchName,Pattern.CASE_INSENSITIVE);
	        		   for(int i=0; i < tableList.size(); i++){
	        		      Matcher matcher = pattern.matcher(tableList.get(i).getName());
	        		      if(matcher.find()){
	        		    	  list.add(tableList.get(i));
	        		      }
	        		   }
        		}else{
        			list.addAll(tableList);
        		}
        		List<DataElementEntity> elements = relationShipService.getElementByDatasourceId(
        				source1.getId());
        		for (Meta meta : list) {
					for (DataElementEntity element : elements) {
						if (meta.getName().equals(element.getTableName())) {
							meta.setBind(true);
							break;
						}
					}
				}
            }else if(id != null && metadatapid>0) {
//            	DataSource source = dataSourceService.getById(metadatapid);
            	DataSource source = datasourceService2.getDatasourceById(metadatapid);
            	
            	List<DataElementEntity> elements = relationShipService.getElementByDatasourceId(source.getId());
            	List<Meta> columnList = new ArrayList<Meta>();
            	if (collectTaskService.isRDBMS(source)) {
            		MetadataCollector collector = collectTaskService.getMetadataCollector(source.getDatabaseType());
                	// 调用service中的获取列的方法，add到list中
                	columnList = collector.getColumns(name,source);
				} else if (Constant.IMPALA.equals(source.getDatabaseType()) 
						|| Constant.HIVE.equals(source.getDatabaseType())) {
					HiveMetadataCollector hiveMetadataCollector = new HiveMetadataCollector();
					Connection conn = JdbcHelper.getConnection(source.getDriver(), source.getUrl()
							, source.getUserName(), source.getPassword());
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
					columnList = hiveMetadataCollector.getColumn(name, source);
				}
            	
    			for(Meta column:columnList){
    				column.setId(Long.valueOf(metadataid + "" + column.getId()));
    				column.setPid(metadataid);
    				column.setPname(name);
    				column.setSourceName(source.getDatasourceName());
    				column.setSourceId(source.getId());
    				column.setBind(false);
    				column.setTypename(column.getName()+"("+column.getType()+")");
    				DataElementEntity dataElementEntity = null;
					for (DataElementEntity element : elements) {
						if (element.getTableName().equals(column.getPname()) 
								&& element.getFieldName().equals(column.getName())) {
							dataElementEntity = element;
						}
					}
    				if(null!=dataElementEntity){
    					StringBuffer name2 = new StringBuffer();
    					name2.append(column.getName()).append("(").append(column.getType())
		    					.append(")").append("——").append(dataElementEntity.getDataElementCode()).append("(")
		    					.append(dataElementEntity.getDataElementName()).append(" ").append(dataElementEntity.getDataElementType())
		    					.append(")");
    					column.setTypename(name2.toString());
    					column.setBind(true);
    				}
    			}
    			list.addAll(columnList);
            }
            // 将list转为json，返回页面
            map = CodeUtil.getSuccessMap();
    		map.put(CodeUtil.RESULT_DATA, list);
    		map.put(CodeUtil.RESULT_DRAW, request.getParameter("draw"));
    	} catch (CommonException e){
	    	map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();	
    	}catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
    }
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/searchByColumn", method = RequestMethod.POST)
	@ApiOperation("数据元关联关系依据列名搜索")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "数据源id", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "tableName", value = "表名", required = false, dataType = "String"),
		@ApiImplicitParam(name = "columnName", value = "列名", required = true, dataType = "String"),
		@ApiImplicitParam(name = "enableFuzzy", value = "是否使用模糊搜索", required = true, dataType = "boolean")
	})
	public String findColumn(Long id, String tableName, String columnName, Boolean enableFuzzy) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DataElementEntity> elements = relationShipService.getElementByDatasourceId(id);
			List<Map<String, Object>> list = new ArrayList<>();
//			DataSource datasource = dataSourceService.getById(id);
			DataSource datasource = datasourceService2.getDatasourceById(id);
			
			Map<String, List<Meta>> tabColsMap = null;
			if (collectTaskService.isRDBMS(datasource)) {
				MetadataCollector collector = collectTaskService.getMetadataCollector(datasource.getDatabaseType());
				tabColsMap = collector.findColumn(datasource, tableName, columnName, enableFuzzy);
			} else if (Constant.IMPALA.equals(datasource.getDatabaseType())) {
				HiveMetadataCollector hiveMetadataCollector = new HiveMetadataCollector();
//				ImpalaSource impalaSource = impalaSourceService.getByDataSourceId(datasource.getId());
				ImpalaSource impalaSource = datasourceService2.getImpalaSource(datasource.getId());
				
				Connection connection = JdbcHelper.getConnection(impalaSource.getDriver(), impalaSource.getUrl()
						, impalaSource.getUserName(), impalaSource.getPassword());
				hiveMetadataCollector.setMetaConnection(connection);
				tabColsMap = hiveMetadataCollector.findColumn(datasource, tableName, columnName, enableFuzzy);
			} else if (Constant.HIVE.equals(datasource.getDatabaseType())) {
				HiveMetadataCollector hiveMetadataCollector = new HiveMetadataCollector();
//				HiveSource hiveSource = hiveSourceService.getByDataSourceId(datasource.getId());
				HiveSource hiveSource = datasourceService2.getHiveSource(datasource.getId());
				
				
				Connection connection = JdbcHelper.getConnection(hiveSource.getDriver(), hiveSource.getUrl()
						, hiveSource.getHiveMetaDataUserName(), hiveSource.getHiveMetaDataPassword());
				hiveMetadataCollector.setMetaConnection(connection);
				tabColsMap = hiveMetadataCollector.findColumn(datasource, tableName, columnName, enableFuzzy);
			}
			int i = 1;
			if (tabColsMap != null && tabColsMap.size() > 0) {
				for (Map.Entry<String, List<Meta>> entry : tabColsMap.entrySet()) {
					Map<String, Object> table = new HashMap<>();
					table.put("id", i);
					table.put("pid", 1);
					table.put("bind", false);
					table.put("isParent", true);
					table.put("name", entry.getKey());
					table.put("pname", datasource.getDatasourceName());
					table.put("type", "TABLE");
					table.put("typename", entry.getKey());
					table.put("cols", new ArrayList<Map<String, Object>>());
					List<Meta> cols = entry.getValue();
					for (int j = 0; j < cols.size(); j++) {
						Meta meta = cols.get(j);
						Map<String , Object> col = new HashMap<>();
						col.put("id", i * 1000 + j);
						col.put("pid", i);
						col.put("bind", false);
						col.put("isParent", false);
						col.put("name", meta.getName());
						col.put("pname", entry.getKey());
						col.put("type", meta.getType());
						col.put("typename", meta.getName()+"("+meta.getType()+")");
						col.put("sourceId", datasource.getId());
						col.put("sourceName", datasource.getDatasourceName());
						DataElementEntity dataElementEntity = null;
						for (DataElementEntity element : elements) {
							if (element.getTableName().equals(entry.getKey()) 
									&& element.getFieldName().equals(meta.getName())) {
								dataElementEntity = element;
							}
						}
	    				if(null!=dataElementEntity){
	    					StringBuffer name2 = new StringBuffer();
	    					name2.append(meta.getName()).append("(").append(meta.getType())
			    					.append(")").append("——").append(dataElementEntity.getDataElementCode()).append("(")
			    					.append(dataElementEntity.getDataElementName()).append(" ").append(dataElementEntity.getDataElementType())
			    					.append(")");
	    					col.put("typename", name2.toString());
	    					col.put("bind", true);
	    				}
	    				Object object = table.get("cols");
	    				((ArrayList<Map<String, Object>>) object).add(col);
	    				
					}
					list.add(table);
					i += 1;
				}
			}
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
		} catch (CommonException e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
		
	@ResponseBody
	@RequestMapping(value = "/select_action", method = RequestMethod.POST)
	@ApiOperation("数据元关联关系依据表名查看表中数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "数据源id", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "tableName", value = "所选表名", required = true, dataType = "String")
	})
	public String selectFromDB(Long id, String tableName, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		ExcuteResult result = new ExcuteResult();
		String errorMsg = "";
		try {
//			DataSource source = dataSourceService.getById(id);
			DataSource source = datasourceService2.getDatasourceById(id);
			
			
			String sql = "select * from " + tableName;
			if (source != null) {
				MetadataCollector collector = collectTaskService.getMetadataCollector(source.getDatabaseType());
				result = collector.excute(source, sql);
				map = CodeUtil.getSuccessMap();
				map.put(CodeUtil.RESULT_DATA, result.getDatamap());
				map.put(CodeUtil.RESULT_HEADS, result.getColList());
				map.put(CodeUtil.RESULT_ROWCOUNT, result.getRowNum());
				map.put(CodeUtil.RESULT_COLCOUNT, result.getColNum());
			}
		} catch (Exception e) {
			logger.error("error:", e);
			errorMsg = e.getMessage();
			if (errorMsg.contains("\n")) {
				map = CodeUtil.getSqlErrorMap(errorMsg.substring(0,
						errorMsg.indexOf("\n")));
			} else {
				map = CodeUtil.getSqlErrorMap(errorMsg);
			}

		}

		return JSON.toJSONString(map);
	}
	
	/*@ResponseBody
	@RequestMapping(value = "/get_all_dataSource", method = RequestMethod.GET)
	@ApiOperation("查找所有数据源")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "sourceName", value = "数据源名称", required = false, dataType = "String")})
	public String getAllSource(String sourceName, HttpServletRequest request) {
		Map<String, Object> map = CodeUtil.getSuccessMap();
		try {
			List<DataSource> sourceList = dataSourceService.getAllDataSource(sourceName);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, sourceList);
		} catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}*/
	
/*	@ApiOperation("查询未设置采集任务的数据源")
	@RequestMapping(value = "/uncollect-ds", method = RequestMethod.GET)
	@ResponseBody
	public String getUncollectDatasources() {
		Map<String, Object> map = CodeUtil.getSuccessMap();
		try {
			List<DataSource> uncollectDatasuorce = dataSourceService.getUncollectDatasuorce();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, uncollectDatasuorce);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}*/
	
	@ApiOperation("为共享系统提供提供数据源执行")
	@RequestMapping(value = "/forShareExcute", method = RequestMethod.POST)
	@ResponseBody
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "数据源id", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "sql", value = "执行sql", required = true, dataType = "String")
	})
	public String forShareExcute(Long id,String sql) {
		Map<String, Object> map = CodeUtil.getSuccessMap();
		ExcuteResult result = new ExcuteResult();
		String errorMsg = "";
		try {
//			DataSource source = dataSourceService.getById(id);
			DataSource source = datasourceService2.getDatasourceById(id);
			
			
			if (source != null) {
				if("impala".equals(source.getDatabaseType().toLowerCase()) || "hive".equals(source.getDatabaseType().toLowerCase())){
					HiveMetadataCollector hiveCollector = new HiveMetadataCollector();
					Connection conn = JdbcHelper.getConnection(source.getDriver(), source.getUrl(),
							source.getUserName(), source.getPassword());
					hiveCollector.setConnection(conn);
					result = hiveCollector.excute(source, sql);
				}else{
					MetadataCollector collector = collectTaskService.getMetadataCollector(source.getDatabaseType());
					result = collector.excute(source, sql);
				}
				map = CodeUtil.getSuccessMap();
				map.put(CodeUtil.RESULT_DATA, result.getDatamap());
				map.put(CodeUtil.RESULT_HEADS, result.getColList());
				map.put(CodeUtil.RESULT_ROWCOUNT, result.getRowNum());
				map.put(CodeUtil.RESULT_COLCOUNT, result.getColNum());
			}
		} catch (Exception e) {
			logger.error("error:", e);
			errorMsg = e.getMessage();
			if (errorMsg.contains("\n")) {
				map = CodeUtil.getSqlErrorMap(errorMsg.substring(0,
						errorMsg.indexOf("\n")));
			} else {
				map = CodeUtil.getSqlErrorMap(errorMsg);
			}

		}
		return JSON.toJSONString(map);
	}
	
	
	
	
}
