package cn.com.yeexun.meta_data.collector;

//import cn.com.yeexun.dataSource.dao.IHbaseSourceDao;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.ExcuteResult;
import cn.com.yeexun.dataSource.entity.HbaseSource;
import cn.com.yeexun.dataSource.entity.Meta;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
//import cn.com.yeexun.meta_data.constants.BigDateMetaModelConstants;
//import cn.com.yeexun.meta_data.dao.IMetadataAttrDao;
import cn.com.yeexun.meta_data.dao.IMetadataDao;
import cn.com.yeexun.meta_data.entity.HbaseMetadataInfo;
import cn.com.yeexun.meta_data.entity.Metadata;
import cn.com.yeexun.meta_data.entity.MetadataAttrEntity;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import cn.com.yeexun.utils.SpringContextHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.IOException;
import java.util.*;

public class HbaseMetadataCollector {
	
	private static Logger logger = LoggerFactory.getLogger(HbaseMetadataCollector.class);
	
	private IMetadataDao metadataDao 
			= (IMetadataDao) SpringContextHelper.getBean(IMetadataDao.class);
	
//	private IMetadataAttrDao metadataAttrDao 
//			= (IMetadataAttrDao) SpringContextHelper.getBean(IMetadataAttrDao.class);
//	
//	private IHbaseSourceDao hbaseSourceDao 
//			= (IHbaseSourceDao) SpringContextHelper.getBean(IHbaseSourceDao.class);
	
	private DatasourceService2 datasourceService2 
			= (DatasourceService2) SpringContextHelper.getBean(DatasourceService2.class);
	
	
	/**
	 * 获取hbase数据源连接的方法
	 * @param source
	 * @return
	 * @throws Exception
	 */
	private Connection getConnection(DataSource source) throws Exception{
		Connection conn = null;
		Configuration conf = HBaseConfiguration.create();
//		HbaseSource hbaseSource = hbaseSourceDao.getByDataSourceId(source.getId());
		HbaseSource hbaseSource = datasourceService2.getHbaseSource(source.getId());
//	    conf.set("hbase.zookeeper.property.clientPort", "2181");
//	    conf.set("hbase.zookeeper.quorum", "xian-yeexun2,xian-yeexun3,xian-yeexun4");
//		conf.set("hadoop.home.dir", " hdfs://10.221.121.2:9000/hbase");  
//	    conf.set("hbase.master", "xian-yeexun5:");
//	    conf.set("hadoop.home.dir", "hdfs://10.221.121.5:8020/hbase");  
        conf.set("hbase.zookeeper.property.clientPort", source.getPort());
        conf.set("hbase.zookeeper.quorum", hbaseSource.getZkQuorum());
        conn = ConnectionFactory.createConnection(conf);
//        conn = HbaseUtils.getConnection(
//        		hbaseSource.getZkQuorum(), source.getPost());
		return conn;
	}
	
	public List<HbaseMetadataInfo> getTables(DataSource source) {
		List<HbaseMetadataInfo> tableList = new ArrayList<>();
		Connection con = null;
		try{
			con = getConnection(source);
			TableName[] tableArr = con.getAdmin().listTableNamesByNamespace(source.getDbname());
			for (TableName table : tableArr) {
				HbaseMetadataInfo metadataInfo = new HbaseMetadataInfo();
				metadataInfo.setName(table.getNameAsString());
				metadataInfo.setPname(source.getDbname());
				tableList.add(metadataInfo);
			}
			Admin admin = con.getAdmin();
			HTableDescriptor[] tableDescriptors = admin.listTableDescriptorsByNamespace(source.getDbname());
			for (HTableDescriptor td : tableDescriptors) {
				HbaseMetadataInfo tableINfo = new HbaseMetadataInfo();
				tableINfo.setName(td.getNameAsString());
				tableINfo.setPname(source.getDbname());
				tableINfo.setRegition(String.valueOf(td.getRegionReplication()));
				tableList.add(tableINfo);
			}
		}catch(Exception e){
			throw new CommonException(e.getMessage(), e); 
		}finally{
			if(con!=null){
				try {
					con.close();
				} catch (IOException e) {
				}
			}
		}
		return tableList;
	}
	
	/*private Map<String, List<HbaseMetadataInfo>> getColumnFamilyMetadata(DataSource source) {
		
		Map<String, List<HbaseMetadataInfo>> result = new HashMap<>();
//		List<HbaseMetadataInfo> cfs = new ArrayList<>();
		Connection connection = null;
		try {
			connection = getConnection(source);
			Admin admin = connection.getAdmin();
			HTableDescriptor[] tables = admin.listTableDescriptorsByNamespace(source.getDbname());
			for (HTableDescriptor table : tables) {
				String tableName = table.getTableName().getNameAsString();
				HColumnDescriptor[] columnFamilies = table.getColumnFamilies();
				List<HbaseMetadataInfo> cfs = new ArrayList<>();
				for (HColumnDescriptor cf : columnFamilies) {
					HbaseMetadataInfo columnFamily = new HbaseMetadataInfo();
					columnFamily.setName(cf.getNameAsString());
					columnFamily.setPname(tableName);
					cfs.add(columnFamily);
				}
				result.put(tableName, cfs);
			}
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					
				}
			}
		}
		return result;
	}*/
	
	/*private List<Metadata> saveTableMetadata(List<HbaseMetadataInfo> tables, long collectId, DataSource source,
			Metadata dbMetadata) {
		List<Metadata> tableMetadat = new ArrayList<Metadata>();
		for (HbaseMetadataInfo table : tables) {
//			String uniqueCode = dbMetadata.getUniqueCode() + "HbaseTable^" + table.getName();
			Metadata tableInfo = new Metadata();
			tableInfo.setName(table.getName());
			tableInfo.setCode(table.getName());
			tableInfo.setParentCode(dbMetadata.getCode());
			tableInfo.setBuildin("N");
			tableInfo.setCollectId(collectId);
			tableInfo.setCreateTime(new Date());
			tableInfo.setIsMenu("N");
			tableInfo.setIsRelease(0);
			tableInfo.setLastModifyTime(new Date());
			tableInfo.setMetadataType(Constant.TECHNOLOGY_METADATA);
			tableInfo.setMetamodelId(BigDateMetaModelConstants.HbaseTable);
			tableInfo.setParentId(dbMetadata.getId());
			tableInfo.setSourceId(source.getId());
//			tableInfo.setUniqueCode(uniqueCode);
			tableInfo.setDeleteFlag("0");
			tableMetadat.add(tableInfo);
		}
		metadataDao.insertList(tableMetadat);
		return tableMetadat;
	}*/
	
	/*private List<MetadataAttrEntity> saveTableMetadataAttr(Metadata table,
			List<HbaseMetadataInfo> tables) {
		List<MetadataAttrEntity> tableAttr = new ArrayList<MetadataAttrEntity>();
		for (HbaseMetadataInfo tableInfo : tables) {
			if(table.getName().equals(tableInfo.getName()) 
					&& BigDateMetaModelConstants.HbaseTable == table.getMetamodelId()){
				MetadataAttrEntity attr1 = new MetadataAttrEntity();
				attr1.setAttrKey("Regition");
				attr1.setAttrValue(tableInfo.getRegition());
				attr1.setModelAttrId(BigDateMetaModelConstants.Regition);
				tableAttr.add(attr1);
			}
		}
		if(tableAttr != null && tableAttr.size()>0){
			for (MetadataAttrEntity metadataAttrEntity : tableAttr) {
				metadataAttrEntity.setCreateTime(new Date());
				metadataAttrEntity.setCreateUserId(0L);
				metadataAttrEntity.setMetadataId(table.getId());
			}
		}
		return tableAttr;
	}
	*/
	
	/*private List<Metadata> saveColumnFamilyMetadata(long collectId, Metadata table,
			List<HbaseMetadataInfo> columnInfo) {
		List<Metadata> allMetadata = new ArrayList<>();
		for (HbaseMetadataInfo column : columnInfo) {
//			String columnUniqueCode = table.getUniqueCode() + "HbaseColumnFamily^" + column.getName();
			Metadata columnMetadata = new Metadata();
			columnMetadata.setName(column.getName());
			columnMetadata.setCode(column.getName());
			columnMetadata.setParentCode(table.getCode());
			columnMetadata.setMetamodelId(BigDateMetaModelConstants.HbaseFamily);
//			columnMetadata.setUniqueCode(columnUniqueCode);
			columnMetadata.setBuildin("N");
			columnMetadata.setCollectId(collectId);
			columnMetadata.setCreateTime(new Date());
			columnMetadata.setCreateUserId(0L);
			columnMetadata.setIsMenu("N");
			columnMetadata.setIsRelease(0);
			columnMetadata.setLastModifyTime(new Date());
			columnMetadata.setMetadataType(Constant.TECHNOLOGY_METADATA);
			columnMetadata.setParentId(table.getId());
			columnMetadata.setSourceId(table.getSourceId());
			columnMetadata.setDeleteFlag("0");
			allMetadata.add(columnMetadata);
		}
		return allMetadata;
	}*/
	
	
	/*private List<MetadataAttrEntity> saveColumnfamilyAttr(Metadata columnMetadata,
			HbaseMetadataInfo metadataInfo) {
		List<MetadataAttrEntity> attrs = new ArrayList<>();
		// TODO
		return attrs;
	}*/
	
	
	/*private void saveDbMetadataAttr(Metadata dbMetadata, DataSource source) {
		List<MetadataAttrEntity> attrs = new ArrayList<MetadataAttrEntity>();
		MetadataAttrEntity attr1 = new MetadataAttrEntity();
		attr1.setAttrKey("Admin");
		attr1.setModelAttrId(BigDateMetaModelConstants.Admin);
		attr1.setAttrValue(source.getUserName());
		attrs.add(attr1);
		MetadataAttrEntity attr2 = new MetadataAttrEntity();
		attr2.setAttrKey("DataSourceName");
		attr2.setModelAttrId(BigDateMetaModelConstants.DataSourceName);
		attr2.setAttrValue(source.getDatasourceName());
		attrs.add(attr2);
		MetadataAttrEntity attr3 = new MetadataAttrEntity();
		attr3.setAttrKey("Master");
		attr3.setModelAttrId(BigDateMetaModelConstants.Master);
		attr3.setAttrValue(source.getIp());
		attrs.add(attr3);
		MetadataAttrEntity attr4 = new MetadataAttrEntity();
		attr4.setAttrKey("MasterPort");
		attr4.setModelAttrId(BigDateMetaModelConstants.MasterPort);
		attr4.setAttrValue(source.getPort());
		attrs.add(attr4);
		MetadataAttrEntity attr5 = new MetadataAttrEntity();
		HbaseSource hbaseSource = null;
		try {
			hbaseSource = hbaseSourceDao.getByDataSourceId(source.getId());
		} catch (Exception e) {
			throw new CommonException("", e);
		}
		attr5.setAttrKey("ZookeeperClientPort");
		attr5.setModelAttrId(BigDateMetaModelConstants.ZookeeperClientPort);
		attr5.setAttrValue(source.getPort());
		attrs.add(attr5);
		MetadataAttrEntity attr6 = new MetadataAttrEntity();
		attr6.setAttrKey("ZookeeperQuorum");
		attr6.setModelAttrId(BigDateMetaModelConstants.ZookeeperQuorum);
		attr6.setAttrValue(hbaseSource.getZkQuorum());
		attrs.add(attr6);
		for (MetadataAttrEntity attr : attrs) {
			attr.setMetadataId(dbMetadata.getId());
			attr.setCreateTime(new Date());
			attr.setCreateUserId(1L);
		}
		metadataAttrDao.insertList(attrs);
	}*/

	/*private Metadata saveDbMetadata(long collectId, DataSource source,
			long menuId) {
//		String uniqueCode = "HBASE_" +
//				source.getIp() + "_" + source.getPort() + "_" + source.getSchemasName() +
//				"HbaseNamespace^" + source.getDbname();
		Metadata dbMetadata = new Metadata();
		dbMetadata.setName(source.getDbname());
		dbMetadata.setCode(source.getDbname());
		dbMetadata.setBuildin("N");
		dbMetadata.setCollectId(collectId);
		dbMetadata.setCreateTime(new Date());
		dbMetadata.setIsMenu("N");
		dbMetadata.setIsRelease(0);
		dbMetadata.setLastModifyTime(new Date());
		dbMetadata.setMetadataType(Constant.TECHNOLOGY_METADATA);
		dbMetadata.setMetamodelId(BigDateMetaModelConstants.HbaseNameSpace);
		dbMetadata.setParentId(menuId);
		dbMetadata.setSourceId(source.getId());
		dbMetadata.setDeleteFlag("0");
		metadataDao.insert(dbMetadata);
		return dbMetadata;
	}*/
	
	
	/*public void collect(DataSource source, HbaseSource hbaseSource, long collectId, long menuId) {
		Metadata dbMetadata = this.saveDbMetadata(collectId, source,menuId);
		//封装库的attr信息
		this.saveDbMetadataAttr(dbMetadata, source);
	    //采集表的信息
		List<HbaseMetadataInfo> tables = this.getTables(source);
		//20190227 ywz 如果采集到的表空间中没有表则不往库中存数据
		if(tables.size() > 0){
			List<Metadata> tableMetadata = this.saveTableMetadata(tables, collectId, source, dbMetadata);
			//查询字段信息
			Map<String, List<HbaseMetadataInfo>> columnInfo =  this.getColumnFamilyMetadata(source);
			List<MetadataAttrEntity> allTableAttr = new ArrayList<MetadataAttrEntity>();
			List<Metadata> allColumnMetadata = new ArrayList<Metadata>();
			for (Metadata table : tableMetadata) {
				//封装表的属性信息
				List<MetadataAttrEntity> tableAttr = saveTableMetadataAttr(table, tables);
				allTableAttr.addAll(tableAttr);
				List<Metadata> columnMetadatas = saveColumnFamilyMetadata(collectId, table, columnInfo.get(table.getName()));
				allColumnMetadata.addAll(columnMetadatas);
			}
			metadataAttrDao.insertList(allTableAttr);
			metadataDao.insertList(allColumnMetadata);
			//封装字段属性
			List<MetadataAttrEntity> allColumnAttr = new ArrayList<>();
			for (Metadata columnMetadata : allColumnMetadata) {
				Metadata table = metadataDao.selectByPrimaryKey(columnMetadata.getParentId());
				List<HbaseMetadataInfo> columns = columnInfo.get(table.getName());
				List<MetadataAttrEntity> atrr = new ArrayList<MetadataAttrEntity>();
				if(BigDateMetaModelConstants.HbaseFamily == columnMetadata.getMetamodelId() ){
					for (HbaseMetadataInfo metadataInfo : columns) {
						if(metadataInfo.getName().equals(columnMetadata.getName())){
							atrr = saveColumnfamilyAttr(columnMetadata, metadataInfo);
						}
					}
				}
				allColumnAttr.addAll(atrr);
			}
			if (allColumnAttr.size() > 0) {
				metadataAttrDao.insertList(allColumnAttr);
			}
		}

	}*/
	
	
	public ExcuteResult getHbaseData(String tablename, DataSource source, String startRowKey, String endRowKey,String column,String columnFimaly,Long rowNum) throws Exception{
		ExcuteResult result = new ExcuteResult();
		ResultScanner rs = null;
		Connection con = null;
		Scan scan = new Scan();
		FilterList f = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		try{
			con = getConnection(source);
			TableName table = TableName.valueOf(tablename);
			Table hTable = con.getTable(table); 
			f.addFilter(new PageFilter(rowNum));	//由前端传过来的参数确定读取多少行数据
			if(StringUtils.isNotEmpty(startRowKey)){
	    		scan.setStartRow(startRowKey.getBytes());
	    	}
			if(StringUtils.isNotEmpty(endRowKey)){
	    		scan.setStopRow(endRowKey.getBytes());
	    	}
			if(StringUtils.isNotEmpty(column)){
	    		f.addFilter(new QualifierFilter(CompareFilter.CompareOp.EQUAL,
	    	            new SubstringComparator(column)));
	    	}
			if(StringUtils.isNotEmpty(columnFimaly)){
	    		f.addFilter(new FamilyFilter(CompareFilter.CompareOp.EQUAL, 
	    				new BinaryComparator(Bytes.toBytes(columnFimaly))));
	    		
	    	}
	    	scan.setReversed(true);
	    	scan.setFilter(f);
		    rs = hTable.getScanner(scan);
		    JSONArray array = new JSONArray();
		    List<String> columns = new ArrayList<>();
		    boolean isFirstRow = true;
		    for (Result r : rs) {
		    	LinkedHashMap<String, Object> datamap = new LinkedHashMap<String, Object>();
		    	for (Cell cell : r.rawCells()) {
		    		String columnName  = Bytes.toString(CellUtil.cloneFamily(cell)) + ":" 
		    				+ Bytes.toString(CellUtil.cloneQualifier(cell));
		    		if (isFirstRow) {
		    			columns.add(columnName);
					}
		    		byte[] valueBytes = CellUtil.cloneValue(cell);
		    		String valueString = new String(valueBytes);
		    		datamap.put(columnName, valueString);
		    	}
		    	array.add(datamap);
		    	if (isFirstRow) {
					isFirstRow = false;
				}
		    }
			result.setColList(columns);
			result.setDatamap(array);
		}catch(Exception e){
			throw e; 
		}finally{
			if(con!=null){
				con.close();
			}
		}
		return result;
	}
	
	
	

}
