package cn.com.yeexun.collectTask.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
//import cn.com.yeexun.collectTask.dao.ICollectTaskDao;
//import cn.com.yeexun.collectTask.entity.CollectTask;
import cn.com.yeexun.collectTask.service.ICollectTaskService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.HbaseSource;
import cn.com.yeexun.dataSource.entity.HdfsSource;
import cn.com.yeexun.dataSource.entity.HiveSource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.dispatch.dao.IDispatchTaskDao;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.quzrtz.IQuartzService;
import cn.com.yeexun.dispatch.quzrtz.job.MetadataCollectQuartzJob;
import cn.com.yeexun.dispatch.service.IDispatchTaskService;
import cn.com.yeexun.meta_data.collector.Db2MetadataCollecctor;
//import cn.com.yeexun.meta_data.collector.HDFSMetadataCollector;
import cn.com.yeexun.meta_data.collector.HbaseMetadataCollector;
import cn.com.yeexun.meta_data.collector.HiveMetadataCollector;
import cn.com.yeexun.meta_data.collector.JdbcHelper;
import cn.com.yeexun.meta_data.collector.MetadataCollector;
import cn.com.yeexun.meta_data.collector.MysqlMetadataCollector;
import cn.com.yeexun.meta_data.collector.OracleMetadataCollector;
import cn.com.yeexun.meta_data.collector.PostgresqlMetadataCollector;
import cn.com.yeexun.meta_data.collector.SqlServerMetadataCollector;
import cn.com.yeexun.meta_data.collector.SybaseMetadataCollector;
import cn.com.yeexun.meta_data.collector.TeradataMetadataCollector;
//import cn.com.yeexun.meta_data.constants.RdbmsMetaModelConstants;
import cn.com.yeexun.meta_data.dao.IMetadataAttrDao;
import cn.com.yeexun.meta_data.dao.IMetadataDao;
import cn.com.yeexun.meta_data.entity.Metadata;
import cn.com.yeexun.meta_data.entity.MetadataAttrEntity;
//import cn.com.yeexun.meta_data.entity.MetadataInfo;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;

@Service
public class CollectTaskService /*extends BaseService<CollectTask>*/ implements ICollectTaskService{
	
	private static final Logger logger = LoggerFactory.getLogger(CollectTaskService.class);
	
//	@Autowired
//	private ICollectTaskDao collectTaskDao;
//	
//	@Autowired
//	private IDispatchTaskDao dispatchTaskDao;
//	
//	@Autowired
//	private IMetadataDao metadataDao;
//	
//	@Autowired
//	private IMetadataAttrDao metadataAttrDao;
	
//	@Autowired
//	private IDataSourceService dataSourceService;
//	
//	@Autowired
//	private IHiveSourceService hiveSourceService;
//	
//	@Autowired
//	private IHdfsSourceService hdfsSourceService;
	
//	@Autowired
//	private IQuartzService quartzService;
//	
//	@Autowired
//	private IDispatchTaskService dispatchTaskService;
	
//	@Autowired
//	private IHbaseSourceService hbaseSourceService;
	
//	@Autowired
//	private IDatasourceClient datasourceClient;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	
	public MetadataCollector getMetadataCollector(String databaseType) {
		MetadataCollector collector = null;
		switch (databaseType.toLowerCase()) {
		case "mysql":
			collector = new MysqlMetadataCollector();
			break;
		case "sqlserver":
			collector = new SqlServerMetadataCollector();
			break;
		case "oracle":
			collector = new OracleMetadataCollector();
			break;
		case "db2":
			collector = new Db2MetadataCollecctor();
			break;
		case "postgresql":
			collector = new PostgresqlMetadataCollector();
			break;
		case "teradata":
			collector = new TeradataMetadataCollector();
			break;
		case "sybase":
			collector = new SybaseMetadataCollector();
			break;
		default:
			break;
		}
		return collector;
	}

	public  Boolean isRDBMS(DataSource source) {
		String type = source.getDatabaseType();
		if(Constant.MYSQL.equalsIgnoreCase(type) ||
				Constant.ORACLE.equalsIgnoreCase(type) ||
				Constant.SQLSERVER.equalsIgnoreCase(type) ||
				Constant.TERADATA.equalsIgnoreCase(type) ||
				Constant.SYBASE.equalsIgnoreCase(type) ||
				Constant.DB2.equalsIgnoreCase(type) ||
				Constant.POSTGRESQL.equalsIgnoreCase(type)){
			return true;
		}
		return false;
	}
	


}
