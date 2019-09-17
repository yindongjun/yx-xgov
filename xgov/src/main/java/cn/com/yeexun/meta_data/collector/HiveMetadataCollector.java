package cn.com.yeexun.meta_data.collector;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.And;

import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.ExcuteResult;
import cn.com.yeexun.dataSource.entity.HiveSource;
import cn.com.yeexun.dataSource.entity.Meta;
//import cn.com.yeexun.meta_data.constants.BigDateMetaModelConstants;
//import cn.com.yeexun.meta_data.dao.IMetadataAttrDao;
import cn.com.yeexun.meta_data.dao.IMetadataDao;
import cn.com.yeexun.meta_data.entity.HiveMetadataInfo;
import cn.com.yeexun.meta_data.entity.Metadata;
import cn.com.yeexun.meta_data.entity.MetadataAttrEntity;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import cn.com.yeexun.utils.SpringContextHelper;

public class HiveMetadataCollector {

	private static Logger logger = LoggerFactory.getLogger(HiveMetadataCollector.class);
	
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
	private SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss.SS");
	
	private IMetadataDao metadataDao 
			= (IMetadataDao) SpringContextHelper.getBean(IMetadataDao.class);
	
/*	private IMetadataAttrDao metadataAttrDao 
			= (IMetadataAttrDao) SpringContextHelper.getBean(IMetadataAttrDao.class);*/
	
	private Connection connection;//数据库的链接
	
	private Connection metaConnection;//元数据库的链接
	
	public Connection getConnection() {
		return connection;
	}
 
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public Connection getMetaConnection() {
		return metaConnection;
	}

	public void setMetaConnection(Connection connection) {
		this.metaConnection = connection;
	}
	
	/*public void collect(DataSource source, HiveSource hiveSource, long collectId, long menuId) {
		Metadata dbMetadata = this.saveDbMetadata(collectId, source,menuId);
		//封装库的attr信息
		this.saveDbMetadataAttr(dbMetadata, source);
	    //采集表的信息
		List<HiveMetadataInfo> tables = this.getTables(source);
		List<Metadata> tableMetadata = this.saveTableMetadata(tables, collectId, source, dbMetadata);
		//查询字段信息
		Map<String, List<HiveMetadataInfo>> columnInfo =  this.getColumnMetadata(source);
		List<MetadataAttrEntity> allTableAttr = new ArrayList<MetadataAttrEntity>();
		List<Metadata> allColumnMetadata = new ArrayList<Metadata>();
		for (Metadata table : tableMetadata) {
			//封装表的属性信息
			List<MetadataAttrEntity> tableAttr = saveTableMetadataAttr(table, tables);
			allTableAttr.addAll(tableAttr);
			//封装字段主键外键索引信息
			List<Metadata> columnMetadatas = saveColumnMetadata(collectId, table, columnInfo.get(table.getName()));
			allColumnMetadata.addAll(columnMetadatas);
		}
		metadataAttrDao.insertList(allTableAttr);
		metadataDao.insertList(allColumnMetadata);
		//封装字段属性
		List<MetadataAttrEntity> allColumnAttr = new ArrayList<MetadataAttrEntity>();
		for (Metadata columnMetadata : allColumnMetadata) {
			Metadata table = metadataDao.selectByPrimaryKey(columnMetadata.getParentId());
			List<HiveMetadataInfo> columns = columnInfo.get(table.getName());
			List<MetadataAttrEntity> atrr = new ArrayList<MetadataAttrEntity>();
			if(BigDateMetaModelConstants.HiveColumns == columnMetadata.getMetamodelId() ){
				for (HiveMetadataInfo metadataInfo : columns) {
					if(metadataInfo.getName().equals(columnMetadata.getName())){
						atrr = saveColumnAttr(columnMetadata, metadataInfo);
					}
				}
			}
			allColumnAttr.addAll(atrr);
		}
		metadataAttrDao.insertList(allColumnAttr);
		
	}*/

	/*private List<MetadataAttrEntity> saveColumnAttr(Metadata columnMetadata,
			HiveMetadataInfo metadataInfo) {
		List<MetadataAttrEntity> attrs = new ArrayList<MetadataAttrEntity>();
		MetadataAttrEntity attr1 = new MetadataAttrEntity();
		attr1.setAttrKey("ColumnSeq");
		attr1.setAttrValue(metadataInfo.getColumnSeq());
		attr1.setModelAttrId(BigDateMetaModelConstants.ColumnSeq);
		attrs.add(attr1);
		MetadataAttrEntity attr2 = new MetadataAttrEntity();
		attr2.setAttrKey("ColmType");
		attr2.setAttrValue(metadataInfo.getColmType());
		attr2.setModelAttrId(BigDateMetaModelConstants.ColmType);
		attrs.add(attr2);
		MetadataAttrEntity attr3 = new MetadataAttrEntity();
		attr3.setAttrKey("ColmSize");
		attr3.setAttrValue(metadataInfo.getColmSize());
		attr3.setModelAttrId(BigDateMetaModelConstants.ColmSize);
		attrs.add(attr3);
		MetadataAttrEntity attr4 = new MetadataAttrEntity();
		attr4.setAttrKey("IsBucketCol");
		attr4.setAttrValue(metadataInfo.getIsBucketCol());
		attr4.setModelAttrId(BigDateMetaModelConstants.IsBucketCol);
		attrs.add(attr4);
		MetadataAttrEntity attr5 = new MetadataAttrEntity();
		attr5.setAttrKey("IsPartCol");
		attr5.setAttrValue(metadataInfo.getIsPartCol());
		attr5.setModelAttrId(BigDateMetaModelConstants.IsPartCol);
		attrs.add(attr5);
		if(attrs != null && attrs.size()>0){
			for (MetadataAttrEntity metadataAttrEntity : attrs) {
				metadataAttrEntity.setCreateTime(new Date());
				metadataAttrEntity.setCreateUserId(0L);//TODO
				metadataAttrEntity.setMetadataId(columnMetadata.getId());
			}
		}
		return attrs;
	}*/

	/*private List<Metadata> saveColumnMetadata(long collectId, Metadata table,
			List<HiveMetadataInfo> columnInfo) {
		List<Metadata> allMetadata = new ArrayList<Metadata>();
		for (HiveMetadataInfo column : columnInfo) {
//			String columnUniqueCode = table.getUniqueCode() + "HiveColumns^" + column.getName();
			Metadata columnMetadata = new Metadata();
			columnMetadata.setName(column.getName());
			columnMetadata.setCode(column.getName());
			columnMetadata.setParentCode(table.getCode());
			columnMetadata.setMetamodelId(BigDateMetaModelConstants.HiveColumns);
//			columnMetadata.setUniqueCode(columnUniqueCode);
			columnMetadata.setBuildin("N");
			columnMetadata.setCollectId(collectId);
			columnMetadata.setCreateTime(new Date());
			columnMetadata.setCreateUserId(0L);//TODO
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

	/*private List<MetadataAttrEntity> saveTableMetadataAttr(Metadata table,
			List<HiveMetadataInfo> tables) {
		List<MetadataAttrEntity> tableAttr = new ArrayList<MetadataAttrEntity>();
		for (HiveMetadataInfo tableInfo : tables) {
			if(table.getName().equals(tableInfo.getName()) && BigDateMetaModelConstants.HiveTable == table.getMetamodelId()){
				MetadataAttrEntity attr1 = new MetadataAttrEntity();
				attr1.setAttrKey("TableType");
				attr1.setAttrValue(tableInfo.getTableType());
				attr1.setModelAttrId(BigDateMetaModelConstants.TableType);
				tableAttr.add(attr1);
				MetadataAttrEntity attr2 = new MetadataAttrEntity();
				attr2.setAttrKey("TableLocation");
				attr2.setAttrValue(tableInfo.getTableLocation());
				attr2.setModelAttrId(BigDateMetaModelConstants.TableLocation);
				tableAttr.add(attr2);
				MetadataAttrEntity attr3 = new MetadataAttrEntity();
				attr3.setAttrKey("TableOwner");
				attr3.setAttrValue(tableInfo.getTableOwner());
				attr3.setModelAttrId(BigDateMetaModelConstants.TableOwner);
				tableAttr.add(attr3);
				MetadataAttrEntity attr4 = new MetadataAttrEntity();
				attr4.setAttrKey("TableCreateTime");
				attr4.setAttrValue(tableInfo.getTableCreateTime());
				attr4.setModelAttrId(BigDateMetaModelConstants.TableCreateTime);
				tableAttr.add(attr4);
				MetadataAttrEntity attr5 = new MetadataAttrEntity();
				attr5.setAttrKey("TableAccessTime");
				attr5.setAttrValue(tableInfo.getTableAccessTime());
				attr5.setModelAttrId(BigDateMetaModelConstants.TableAccessTime);
				tableAttr.add(attr5);
				MetadataAttrEntity attr6 = new MetadataAttrEntity();
				attr6.setAttrKey("Retention");
				attr6.setAttrValue(tableInfo.getRetention());
				attr6.setModelAttrId(BigDateMetaModelConstants.Retention);
				tableAttr.add(attr6);
				MetadataAttrEntity attr7 = new MetadataAttrEntity();
				attr7.setAttrKey("ProtectMode");
				attr7.setAttrValue(tableInfo.getProtectMode());
				attr7.setModelAttrId(BigDateMetaModelConstants.ProtectMode);
				tableAttr.add(attr7);
				MetadataAttrEntity attr8 = new MetadataAttrEntity();
				attr8.setAttrKey("ViewExpandedText");
				attr8.setAttrValue(tableInfo.getViewExpandedText());
				attr8.setModelAttrId(BigDateMetaModelConstants.ViewExpandedText);
				tableAttr.add(attr8);
				MetadataAttrEntity attr9 = new MetadataAttrEntity();
				attr9.setAttrKey("ViewOriginalText");
				attr9.setAttrValue(tableInfo.getViewOriginalText());
				attr9.setModelAttrId(BigDateMetaModelConstants.ViewOriginalText);
				tableAttr.add(attr9);
				MetadataAttrEntity attr10 = new MetadataAttrEntity();
				attr10.setAttrKey("BucketsNum");
				attr10.setAttrValue(tableInfo.getBucketsNum());
				attr10.setModelAttrId(BigDateMetaModelConstants.BucketsNum);
				tableAttr.add(attr10);
				MetadataAttrEntity attr11 = new MetadataAttrEntity();
				attr11.setAttrKey("Parameters");
				attr11.setAttrValue(tableInfo.getParameters());
				attr11.setModelAttrId(BigDateMetaModelConstants.Parameters);
				tableAttr.add(attr11);
				MetadataAttrEntity attr12 = new MetadataAttrEntity();
				attr12.setAttrKey("InputFormat");
				attr12.setAttrValue(tableInfo.getInputFormat());
				attr12.setModelAttrId(BigDateMetaModelConstants.InputFormat);
				tableAttr.add(attr12);
				MetadataAttrEntity attr13 = new MetadataAttrEntity();
				attr13.setAttrKey("IsCompressed");
				attr13.setAttrValue(tableInfo.getIsCompressed());
				attr13.setModelAttrId(BigDateMetaModelConstants.IsCompressed);
				tableAttr.add(attr13);
				MetadataAttrEntity attr14 = new MetadataAttrEntity();
				attr14.setAttrKey("HdfsLocation");
				attr14.setAttrValue(tableInfo.getHdfsLocation());
				attr14.setModelAttrId(BigDateMetaModelConstants.HdfsLocation);
				tableAttr.add(attr14);
				MetadataAttrEntity attr15 = new MetadataAttrEntity();
				attr15.setAttrKey("OutputFormat");
				attr15.setAttrValue(tableInfo.getOutputFormat());
				attr15.setModelAttrId(BigDateMetaModelConstants.OutputFormat);
				tableAttr.add(attr15);
			}
		}
		if(tableAttr != null && tableAttr.size()>0){
			for (MetadataAttrEntity metadataAttrEntity : tableAttr) {
				metadataAttrEntity.setCreateTime(new Date());
				metadataAttrEntity.setCreateUserId(0L);//TODO
				metadataAttrEntity.setMetadataId(table.getId());
			}
		}
		return tableAttr;
	}*/
	
	public List<Meta> getColumn(String tableName, DataSource dataSource) {
		ResultSet rs = null;
		ResultSet rs1 = null;
		Statement stmt = null;
		List<Meta> allColumns = new ArrayList<>();
		try {
			String sql = "select t.TBL_NAME ,t.TBL_ID,c2.INTEGER_IDX,c2.COLUMN_NAME,c2.TYPE_NAME,c2.COMMENT as col_COMMENT " 
					+ "from TBLS t "
					+ "left join SDS s on t.SD_ID=s.SD_ID "
					+ "left join CDS c on s.CD_ID=c.CD_ID "
					+ "left join COLUMNS_V2 c2 on c.CD_ID=c2.CD_ID "
					+ "left join DBS d on d.DB_ID = t.DB_ID " 
					+ "where  d.NAME ='"
					+ dataSource.getDbname() 
					+ "' and t.TBL_NAME = '" 
					+ tableName 
					+ "' order by t.TBL_NAME ASC , c2.INTEGER_IDX ASC";
			stmt = metaConnection.createStatement();
			rs = stmt.executeQuery(sql);
			Long id = 0L;
			while(rs.next()){
				Meta column = new Meta();
				column.setId(id);
				column.setName(rs.getString("COLUMN_NAME"));// 获取列名
				column.setType(rs.getString("TYPE_NAME"));
				allColumns.add(column);
			}
			id += 1;
		} catch (SQLException e) {
			throw new CommonException("获取impala列信息异常：", e);
		}
		return allColumns;
		
	}

	/*private Map<String, List<HiveMetadataInfo>> getColumnMetadata(
			DataSource source) {
		Map<String, List<HiveMetadataInfo>> result = new HashMap<String, List<HiveMetadataInfo>>();
		ResultSet rs = null;
		ResultSet rs1 = null;
		Statement stmt = null;
		try {
			List<HiveMetadataInfo> allColumns = new ArrayList<HiveMetadataInfo>();
			String sql = "select t.TBL_NAME ,t.TBL_ID,c2.INTEGER_IDX,c2.COLUMN_NAME,c2.TYPE_NAME,c2.COMMENT as col_COMMENT " 
					+ "from TBLS t "
					+ "left join SDS s on t.SD_ID=s.SD_ID "
					+ "left join CDS c on s.CD_ID=c.CD_ID "
					+ "left join COLUMNS_V2 c2 on c.CD_ID=c2.CD_ID "
					+ "left join DBS d on d.DB_ID = t.DB_ID " 
					+ "where  d.NAME ='"
					+ source.getDbname()
					+ "' order by t.TBL_NAME ASC , c2.INTEGER_IDX ASC";
			stmt = metaConnection.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				HiveMetadataInfo column = new HiveMetadataInfo();
				column.setName(rs.getString("COLUMN_NAME"));// 获取列名
				column.setPname(rs.getString("TBL_NAME"));
				column.setColmType(rs.getString("TYPE_NAME"));// 获取列的类型
				column.setColumnSeq(rs.getString("INTEGER_IDX"));
				column.setIsBucketCol("N");
				column.setIsPartCol("N");
				allColumns.add(column);
			}
			//获取一个库的分区字段
			String sql1 = "SELECT "
					+ "T.TBL_NAME, "
					+ "PK.PKEY_NAME ,"
					+ "PK.PKEY_TYPE, "
					+ "PK.INTEGER_IDX, "
					+ "PK.PKEY_COMMENT "
					+ "FROM PARTITION_KEYS PK "
					+ "LEFT JOIN TBLS T ON T.TBL_ID =  PK.TBL_ID "
					+ "LEFT JOIN DBS d on d.DB_ID = T.DB_ID  "
					+ "where  d.NAME = '"
					+ source.getDbname()
					+ "' order by T.TBL_NAME ASC , PK.INTEGER_IDX ASC";
			
			rs1 = stmt.executeQuery(sql1);
			while(rs1.next()){
				HiveMetadataInfo column = new HiveMetadataInfo();
				column.setName(rs1.getString("PKEY_NAME"));// 获取列名
				column.setColmType(rs1.getString("PKEY_TYPE"));// 获取列的类型
				column.setPname(rs1.getString("TBL_NAME"));
				column.setColumnSeq(rs1.getString("INTEGER_IDX"));
				column.setIsPartCol("Y");
				allColumns.add(column);
			}
			//获取分桶信息
			Map<String, List<HiveMetadataInfo>> bucketInfo = getBucketInfo(source.getDbname());
			//将所有的字段信息封装成一个map
			if(!allColumns.isEmpty()){
				for(HiveMetadataInfo column : allColumns){
					String tableName = column.getPname();
					if(result.containsKey(tableName)){
						result.get(tableName).add(column);
					} else {
						List<HiveMetadataInfo> newTabColumn = new ArrayList<HiveMetadataInfo>();
						newTabColumn.add(column);
						result.put(tableName, newTabColumn);
					}
				}
			}
			//将分桶信息和sorted信息封装到字段信息中去
			if(!bucketInfo.isEmpty()){
				for( String tableName : bucketInfo.keySet() ) {
					List<HiveMetadataInfo> columns = result.get(tableName);
					List<HiveMetadataInfo> bucketColumn = bucketInfo.get(tableName);
					for (HiveMetadataInfo column : columns) {
						for(HiveMetadataInfo bucket : bucketColumn){
							if(bucket.getName().equals(column.getName())){
								column.setIsBucketCol("Y");
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.debug("", e);
			throw new CommonException(e.getMessage(), e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				logger.debug("", e);
				throw new CommonException(e.getMessage(), e);
			}
		}
		return result;
	}*/

	/*private List<Metadata> saveTableMetadata(List<HiveMetadataInfo> tables, long collectId, DataSource source,
			Metadata dbMetadata) {
		List<Metadata> tableMetadat = new ArrayList<Metadata>();
		for (HiveMetadataInfo table : tables) {
//			String uniqueCode = dbMetadata.getUniqueCode() + "HiveTable^" + table.getName();
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
			tableInfo.setMetamodelId(BigDateMetaModelConstants.HiveTable);
			tableInfo.setParentId(dbMetadata.getId());
			tableInfo.setSourceId(source.getId());
//			tableInfo.setUniqueCode(uniqueCode);
			tableInfo.setDeleteFlag("0");
			tableMetadat.add(tableInfo);
		}
		metadataDao.insertList(tableMetadat);
		return tableMetadat;
	}*/

	public List<HiveMetadataInfo> getTables(DataSource source){
		List<HiveMetadataInfo> tableList = new ArrayList<HiveMetadataInfo>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select t.*, s.LOCATION, s.INPUT_FORMAT, s.IS_COMPRESSED,s.OUTPUT_FORMAT, s.NUM_BUCKETS from TBLS t left join DBS d on t.DB_ID = d.DB_ID left join SDS s on s.SD_ID = t.SD_ID where d.NAME='"
					+ source.getDbname() + "'" + "order by t.TBL_NAME";
			stmt = metaConnection.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Statement stmt2 = null;
				ResultSet rs2 = null;
				String sql2 = "SELECT * from TABLE_PARAMS p left join TBLS t on t.tbl_id = p.tbl_id left join DBS d on t.DB_ID = d.DB_ID"
						+ " where t.TBL_NAME = '"
						+ rs.getString("TBL_NAME")
						+ "' and d.NAME = '"
						+ source.getDbname() + "'" ;
				stmt2 = metaConnection.createStatement();
				rs2 = stmt2.executeQuery(sql2);
				JSONObject parametar = new JSONObject();
				while (rs2.next()) {
					parametar.put(rs2.getString("PARAM_KEY"), rs2.getString("PARAM_VALUE"));
				}
				rs2.close();
				stmt2.close();
				HiveMetadataInfo table = new HiveMetadataInfo();
				table.setPname(source.getDbname());
				table.setName(rs.getString("TBL_NAME"));
				table.setTableType(rs.getString("TBL_TYPE"));
				table.setBucketsNum(rs.getString("NUM_BUCKETS"));
				table.setHdfsLocation(rs.getString("LOCATION"));
				table.setInputFormat(rs.getString("INPUT_FORMAT"));
				table.setOutputFormat(rs.getString("OUTPUT_FORMAT"));
				table.setTableOwner(rs.getString("OWNER"));
				table.setIsCompressed(rs.getString("IS_COMPRESSED"));
				table.setRetention(rs.getString("RETENTION"));
				table.setTablePrivacy("");
				table.setParameters(parametar.toJSONString());
				table.setPricipalName("");
				table.setViewExpandedText(rs.getString("VIEW_EXPANDED_TEXT"));
				table.setViewOriginalText(rs.getString("VIEW_ORIGINAL_TEXT"));
				table.setProtectMode("");
				tableList.add(table);
			}
		} catch (Exception e) {
			logger.debug("", e);
			throw new CommonException("", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				logger.debug("", e);
				throw new CommonException("", e);
			}
		}
		return tableList;
	}

	/*private void saveDbMetadataAttr(Metadata dbMetadata, DataSource source) {
		List<MetadataAttrEntity> attrs = new ArrayList<MetadataAttrEntity>();
		MetadataAttrEntity attr1 = new MetadataAttrEntity();
		attr1.setAttrKey("HiveDataBaseIP");
		attr1.setModelAttrId(BigDateMetaModelConstants.HiveDataBaseIP);
		attr1.setAttrValue(source.getIp());
		attrs.add(attr1);
		MetadataAttrEntity attr2 = new MetadataAttrEntity();
		attr2.setAttrKey("HiveDataBasePort");
		attr2.setModelAttrId(BigDateMetaModelConstants.HiveDataBasePort);
		attr2.setAttrValue(source.getPort());
		attrs.add(attr2);
		MetadataAttrEntity attr3 = new MetadataAttrEntity();
		attr3.setAttrKey("HiveDataBaseName");
		attr3.setModelAttrId(BigDateMetaModelConstants.HiveDataBaseName);
		attr3.setAttrValue(source.getDbname());
		attrs.add(attr3);
		MetadataAttrEntity attr4 = new MetadataAttrEntity();
		attr4.setAttrKey("HiveDataBaseOwner");
		attr4.setModelAttrId(BigDateMetaModelConstants.HiveDataBaseOwner);
		attr4.setAttrValue(source.getUserName());
		attrs.add(attr4);
		MetadataAttrEntity attr5 = new MetadataAttrEntity();
		attr5.setAttrKey("HiveDataSourceName");
		attr5.setModelAttrId(BigDateMetaModelConstants.HiveDataSourceName);
		attr5.setAttrValue(source.getDatasourceName());
		attrs.add(attr5);
		for (MetadataAttrEntity attr : attrs) {
			attr.setMetadataId(dbMetadata.getId());
			attr.setCreateTime(new Date());
			attr.setCreateUserId(1L);//TODO
		}
		metadataAttrDao.insertList(attrs);
	}*/

	/*private Metadata saveDbMetadata(long collectId, DataSource source,
			long menuId) {
//		String uniqueCode = "HIVE_" +
//				source.getIp() + "_" + source.getPort() + "_" + source.getSchemasName() +
//				"HiveDatabase^" + source.getDbname();
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
		dbMetadata.setMetamodelId(BigDateMetaModelConstants.HiveDatabase);
		dbMetadata.setParentId(menuId);
		dbMetadata.setSourceId(source.getId());
//		dbMetadata.setUniqueCode(uniqueCode);
		dbMetadata.setDeleteFlag("0");
		metadataDao.insert(dbMetadata);
		return dbMetadata;
	}*/
	
	/*private Map<String, List<HiveMetadataInfo>> getBucketInfo(
			String dbname) throws SQLException {
		Map<String, List<HiveMetadataInfo>> result = new HashMap<String, List<HiveMetadataInfo>>();
		List<HiveMetadataInfo> columnList = new ArrayList<HiveMetadataInfo>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = metaConnection.createStatement();
			String sql = "select t.TBL_NAME, b.BUCKET_COL_NAME, b.INTEGER_IDX "
					+ "from BUCKETING_COLS b "
					+ "left join TBLS t on b.sd_id  = t.sd_id "
					+ "left join DBS d on d.db_id = t.db_id "
					+ "where d.name = '"
					+ dbname
					+ "' order by t.tbl_name, b.integer_idx";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				HiveMetadataInfo column = new HiveMetadataInfo();
				column.setName(rs.getString("BUCKET_COL_NAME"));
				column.setPname(rs.getString("TBL_NAME"));
				columnList.add(column);
			}
			if (columnList != null && columnList.size() > 0) {
				for(HiveMetadataInfo column : columnList){
					String tableName = column.getPname();
					if(result.containsKey(tableName)){
						result.get(tableName).add(column);
					} else {
						List<HiveMetadataInfo> newTabColumn = new ArrayList<HiveMetadataInfo>();
						newTabColumn.add(column);
						result.put(tableName, newTabColumn);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}finally{
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		
		return result;
	}*/

	public ExcuteResult showDataPage(DataSource dataSource, String tableName
			, String pageStart, String pageSize) {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(this.createSelectSql(dataSource, tableName)).append(" "+" limit "+pageSize);
			return this.excute(dataSource, sql.toString());
		} catch (Exception e) {
			throw new CommonException("查询数据异常:", e);
		}
	}
	
	public String createSelectSql(DataSource source, String tableName) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(tableName);
		
		return sql.toString();
	}
	
	public ExcuteResult excute(DataSource source,String sql) throws Exception{
	    	
		ExcuteResult result = new ExcuteResult();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			List<String> colList = new ArrayList<String>();
			for (int i = 1; i <= colCount; i++) {
				String columnName = rsmd.getColumnName(i);
				colList.add(columnName);
			}
			JSONArray array = new JSONArray();
			int num = 0;
			while (rs.next()) {
				LinkedHashMap<String, Object> datamap = new LinkedHashMap<String, Object>();
				// 遍历每一列
				for (int i = 1; i <= colCount; i++) {
					String columnName = rsmd.getColumnName(i);
					columnName = columnName.substring(columnName.lastIndexOf(".") + 1);
					Object value = rs.getObject(i);
					if(value != null){
						if(value.getClass()==Date.class){
							String msg = sdf1.format(value);
							value = msg;
						}
						if(value.getClass()==Timestamp.class){
							String msg = sdf2.format(value);
							value = msg;
						}
						if(value.getClass()==Time.class){
							String msg = sdf3.format(value);
							value = msg;
						}
					}
					datamap.put(columnName, value);
				}
				array.add(datamap);
				num += 1;
			}
//			rs.last();
//			int rowCount = rs.getRow();
			int rowCount = num;
			result.setColNum(colCount);
			result.setRowNum(rowCount);
			result.setColList(colList);
			result.setDatamap(array);
		}catch(Exception e){
			throw e;
		}finally{
			if(rs!=null){
				rs.close();
			} 
			if(stmt!=null){
				stmt.close();
			}
			if(con!=null){
				con.close();
			}
		}
		return result;
    }
	
	public Map<String, List<Meta>> findColumn(DataSource dataSource, String tableNameLike
			, String columnNameLike, Boolean enableFuzzy) throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;
		enableFuzzy = enableFuzzy == null ? true : enableFuzzy;
		if (enableFuzzy) {
			if (tableNameLike != null) {
				tableNameLike = "%" + tableNameLike + "%";
			}
			if (columnNameLike != null) {
				columnNameLike = "%" + columnNameLike + "%";
			}
		}
		try {
			String sql = "select t.TBL_NAME ,t.TBL_ID,c2.INTEGER_IDX,c2.COLUMN_NAME,c2.TYPE_NAME,c2.COMMENT as col_COMMENT " 
					+ "from TBLS t "
					+ "left join SDS s on t.SD_ID=s.SD_ID "
					+ "left join CDS c on s.CD_ID=c.CD_ID "
					+ "left join COLUMNS_V2 c2 on c.CD_ID=c2.CD_ID "
					+ "left join DBS d on d.DB_ID = t.DB_ID " 
					+ "where  d.NAME ='"
					+ dataSource.getDbname() + "'";
			if (tableNameLike != null && tableNameLike.trim().length() > 0) {
				sql = sql + " and t.TBL_NAME like '" + tableNameLike + "'";
			}
			if (columnNameLike != null && columnNameLike.trim().length() > 0) {
				sql = sql + " and c2.COLUMN_NAME like '" + columnNameLike + "'";
			}
			sql = sql + " order by t.TBL_NAME ASC , c2.INTEGER_IDX ASC";
			stmt = metaConnection.createStatement();
			rs = stmt.executeQuery(sql);
			Map<String, List<Meta>> tabColsMap = new HashMap<>();
			int num = 0;
			while (rs.next()) {
				if (num < 200) {
					String tableName = rs.getString("TBL_NAME");
					String columnName = rs.getString("COLUMN_NAME");
					List<Meta> cols = tabColsMap.get(tableName);
					Meta meta = new Meta();
					meta.setName(columnName);
					meta.setPname(tableName);
					meta.setType(rs.getString("TYPE_NAME"));
					if (cols == null) {
						cols = new ArrayList<>();
						cols.add(meta);
						tabColsMap.put(tableName, cols);
					} else {
						cols.add(meta);
					}
					num += 1;
				} else {
					break;
				}
			}
			return tabColsMap;
		} catch (SQLException e) {
			throw new CommonException("获取impala列信息异常：", e);
		}
	}
	
	
	
}
