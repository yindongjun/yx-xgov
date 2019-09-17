package cn.com.yeexun.meta_data.collector;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;

//import cn.com.yeexun.collectTask.entity.CollectTask;
import cn.com.yeexun.collectTask.service.ICollectTaskService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.ExcuteResult;
import cn.com.yeexun.dataSource.entity.Meta;
//import cn.com.yeexun.meta_data.entity.MetadataInfo;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.VerifyException;

public class Db2MetadataCollecctor implements MetadataCollector {
	
	private static Logger logger = LoggerFactory.getLogger(Db2MetadataCollecctor.class);
	
	private Connection connection;
	
	private DataSource datasource;
	
	// 采集历史
//	private CollectTask collectTask;
	
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
	private SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss.SS");
	
	@Autowired
	private ICollectTaskService collectTaskService;
	
//	@Autowired
//	private IDataSourceService datasourceService;
	
	private Long collectTaskId;
	
	/*public Db2MetadataCollecctor() {
		super();
	}*/
	
	/*public Db2MetadataCollecctor(Long collectTaskId) {
		super();
		this.collectTaskId = collectTaskId;
	}*/

	/*public void init() {
		try {
			this.collectTask = collectTaskService.getById(this.collectTaskId);
			this.datasource = datasourceService.getById(this.collectTask.getDatasourceId());
			String driver = datasource.getDriver();
			String url = datasource.getUrl();
			String username = datasource.getUserName();
			String password = datasource.getPassword();
			this.connection = JdbcHelper.getConnection(driver, url, username, password);
		} catch (Exception e) {
			throw new CommonException("初始化MysqlMetadataCollector出错：", e);
		}
	}*/


	/*@Override
	public void setConnection(Connection connection) {
		this.connection = connection;
	}*/
	
	

	/*@Override
	public List<MetadataInfo> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) {
		ResultSet rs = null;
		List<MetadataInfo> tables = new ArrayList<MetadataInfo>();
		try {
			DatabaseMetaData metaData = connection.getMetaData();
			rs = metaData.getTables(null, schemaPattern, tableNamePattern, types);
			while (rs.next()) {
				MetadataInfo table = new MetadataInfo();
				table.setPname(catalog);
				table.setName(rs.getString("TABLE_NAME"));
				table.setTableSpaceName(rs.getString("TABLE_NAME"));
				table.setDescription(rs.getString("REMARKS"));
				tables.add(table);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage(), e);
			throw new CommonException(e.getMessage(), e);
		}
		return tables;
	}*/

	/*@Override
	public Map<String, List<MetadataInfo>> getColumnMetadata(String catalog, String schema) {
		Map<String,List<MetadataInfo>> result = new HashMap<String, List<MetadataInfo>>();
		ResultSet rs = null;
		try{
			DatabaseMetaData dbMetaData = connection.getMetaData();
			rs = dbMetaData.getColumns(null, schema, "%", "%");
			List<MetadataInfo> columnList = new ArrayList<MetadataInfo>();
			
			while(rs.next()){
				MetadataInfo column = new MetadataInfo();
				column.setPname(rs.getString("TABLE_NAME"));
				column.setName(rs.getString("COLUMN_NAME"));//获取列名
				if (rs.getString("REMARKS") != null){
					column.setComment(rs.getString("REMARKS"));//获取列的comment
				} else {
					column.setComment("");
				}
				column.setNullable(rs.getInt("NULLABLE"));//获得列的是否可为空 ，0表示不可为空，1表示可为空
				column.setColumnSize(rs.getInt("COLUMN_SIZE"));	// 获取字段长度；
				column.setDecimalDigits(rs.getString("DECIMAL_DIGITS"));//获取字段精度
				column.setInitialValue(rs.getString("COLUMN_DEF"));//获取字段默认值
				column.setDataType(rs.getString("DATA_TYPE"));
				column.setDataTypeName(rs.getString("TYPE_NAME"));
				columnList.add(column);
			}
			if (!columnList.isEmpty()) {
				for (MetadataInfo column : columnList) {
					String tableName = column.getPname();
					if (result.containsKey(tableName)){
						result.get(tableName).add(column);
					} else {
						List<MetadataInfo> newTabColumn = new ArrayList<MetadataInfo>();
						newTabColumn.add(column);
						result.put(tableName, newTabColumn);
					}
				}
				if(!columnList.isEmpty()){
					for (String tableName : result.keySet()) {
						List<MetadataInfo> fkList = new ArrayList<MetadataInfo>();
						List<MetadataInfo> pkColumnList = new ArrayList<MetadataInfo>();
						List<MetadataInfo> indexList = new ArrayList<MetadataInfo>();
						ResultSet rs1 = null;
						ResultSet rs2 = null;
						ResultSet rs3 = null;
						rs1 = dbMetaData.getPrimaryKeys(null, catalog, tableName);
						while(rs1.next()){
							MetadataInfo pkcolumn = new MetadataInfo();
							pkcolumn.setPname(rs1.getString("TABLE_NAME"));
							pkcolumn.setName(rs1.getString("PK_NAME"));
							pkcolumn.setColumnName(rs1.getString("COLUMN_NAME"));
							pkColumnList.add(pkcolumn); 
						}
						rs2 = dbMetaData.getIndexInfo(null, catalog, tableName, false, true);
						while(rs2.next()){
							MetadataInfo index = new MetadataInfo();
							index.setPname(rs2.getString("TABLE_NAME"));//索引所在的字段名称
							index.setName(rs2.getString("INDEX_NAME"));//索引名称
							index.setIsUniqueIndex(rs2.getString("NON_UNIQUE"));
							index.setIndexType(rs2.getString("TYPE"));//索引类型
							index.setOrdinalPosition(rs2.getString("ORDINAL_POSITION"));
							index.setPages(rs2.getString("PAGES"));
							index.setIndexColumnName(rs2.getString("COLUMN_NAME"));
							if(StringUtils.isNotBlank(index.getPname())){
								indexList.add(index);
							}
						}
						rs3 = dbMetaData.getImportedKeys(null, catalog, tableName);
						while (rs3.next()){  
							MetadataInfo fkcolumn = new MetadataInfo();
							fkcolumn.setName(rs3.getString("FK_NAME"));
							fkcolumn.setFkColumnName(rs3.getString("FKCOLUMN_NAME"));
			                fkcolumn.setReferenceDatabase(rs3.getString("PKTABLE_SCHEM"));
			                fkcolumn.setReferenceTableName(rs3.getString("PKTABLE_NAME"));
			                fkcolumn.setReferenceColumnName(rs3.getString("PKCOLUMN_NAME"));
			                fkList.add(fkcolumn);
			            }
						List<MetadataInfo> columns = result.get(tableName);
						if(!columns.isEmpty()){
							for(MetadataInfo column:columns){
								for( MetadataInfo pk: pkColumnList){
									if(pk.getColumnName().equals(column.getName())){
										column.setIsPK("true");
										column.setKeySeq(pk.getKeySeq());
										column.setPkName(pk.getName());
									}
								}
								for( MetadataInfo fk: fkList){
									if(fk.getFkColumnName().equals(column.getName())){
										column.setIsFk("true");
										column.setFkName(fk.getName());
										column.setReferenceDatabase(fk.getReferenceDatabase());
										column.setReferenceTableName(fk.getReferenceTableName());
										column.setReferenceColumnName(fk.getReferenceColumnName());
									}
								}
								if(indexList != null && indexList.size()>0){
									for(MetadataInfo index : indexList){
//										if(index.getIndexColumnName().equals(column.getName())){
										if(index.getIndexColumnName() != null 
												&& index.getIndexColumnName().equals(column.getName())){
											column.setIsIndex("true");
											column.setIndexName(index.getName());
											column.setIsUnique(index.getIsUniqueIndex());
											column.setOrdinalPosition(index.getOrdinalPosition());
											column.setPages(index.getPages());
										}
									}
								}
							}
						}
						if (rs != null ) {
							rs.close();
						}
						if (rs1 != null ) {
							rs1.close();
						}
						if (rs2 != null ) {
							rs2.close();
						}
						if (rs3 != null ) {
							rs3.close();
						}
					}
				}
			}
		} catch (Exception e){
			throw new CommonException("获取列信息发生异常！", e);
		}
		return result;
	}*/
	
	/*public static void main(String[] args) {
		String driver = "com.ibm.db2.jcc.DB2Driver";
		String url = "jdbc:db2://10.221.121.6:50000/sample";
		String username = "db2inst1";
		String password = "db2inst1";
		Connection connection = JdbcHelper.getConnection(driver, url, username, password);
		Db2MetadataCollecctor collector = new Db2MetadataCollecctor();
		collector.setConnection(connection);
		List<MetadataInfo> tables = collector.getTables(null, null, null, null);
		System.out.println(tables);
		Map<String, List<MetadataInfo>> columnMetadata = collector.getColumnMetadata(null, "DB2INST1");
		System.out.println(columnMetadata);
	}*/

	/*@Override
	public Connection getConnection() {
		return this.connection;
	}*/

	/*@Override
	public MetadataInfo getDatabaseInfo(DataSource source) {
		MetadataInfo dbMetadata = new MetadataInfo();
		dbMetadata.setName(source.getDbname());
		dbMetadata.setIpAddress(source.getIp());
		dbMetadata.setdBPort(source.getPort());
		dbMetadata.setDatabaseName(source.getDbname());
		dbMetadata.setSchema(source.getSchemasName());
		dbMetadata.setUserName(source.getUserName());
		dbMetadata.setPassword(source.getPassword());
		dbMetadata.setDataSourceName(source.getDatasourceName());
		dbMetadata.setDatabaseType(source.getDatabaseType());
		dbMetadata.setCharacterSet(source.getCharacterSet());
		return dbMetadata;
	}*/
	
	
	@Override
	public List<Meta> getTables(DataSource source)
			throws Exception {
		List<Meta> tableList = new ArrayList<>();
		ResultSet rs = null;
		Connection con = null;
		try{
			con = getConnection2(source);
			DatabaseMetaData dbMetaData = con.getMetaData();
			String[] types = { "TABLE" ,"VIEW"};
			rs = dbMetaData.getTables(null, source.getSchemasName(), "%", types);
			long i=source.getId()*100;
			while(rs.next()){
				ResultSet rs1 = null;
				Meta table = new Meta();
				table.setId(i++);
				table.setPid(source.getId());
				table.setPname(source.getDbname());
				table.setName(rs.getString("TABLE_NAME"));
				table.setType(rs.getString("TABLE_TYPE"));
				table.setTypename(rs.getString("TABLE_NAME"));
				table.setIsParent("true");
				tableList.add(table);
			}
		} catch (VerifyException e) {
			throw new CommonException(e.getMessage(), e);
		} catch(Exception e){
			throw e; 
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(con!=null){
				con.close();
			}
		}
		return tableList;
	}

	/**
	 * 获取数据库连接方法。对获取连接设置超时配置，如果在超时时间内没有返回数据库连接就出现连接超时。
	 * @param datasource 数据源信息
	 * @return Connection
	 * @throws Exception
	 */
	private Connection getConnection2(DataSource datasource)throws Exception{
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		final DataSource ds = datasource;
		Connection con = null;
		Callable<Connection> callablel =new Callable<Connection>(){
			String driver = ds.getDriver();
			String url = ds.getUrl();
			String username = ds.getUserName();
			String password = ds.getPassword();
			public Connection call() throws Exception{
				Class.forName(driver);
				Connection connection = DriverManager.getConnection(url, username, password);
				return connection;
			}
		};
		try{
			Future<Connection> future = executorService.submit(callablel);
			//设置超时时间，在时间内才会返回数据库连接
			con = future.get(1000 * 5, TimeUnit.MILLISECONDS);
		}catch(TimeoutException timeout){
			logger.debug("数据源连接超时，请检查连接参数！！ ", timeout);
			throw new VerifyException("数据源连接超时，请检查连接参数！！ ");
		}catch(Exception e){
			logger.debug("数据源连接失败，请检查数据源！！ ", e);
			throw new CommonException("数据源连接失败，请检查数据源！！ ");
		}finally {
			executorService.shutdown();
		}
		return con;
	}

	@Override
	public List<Meta> getColumns(String tablename, DataSource source)
			throws Exception {
		List<Meta> columnList = new ArrayList<Meta>();
		ResultSet rs = null;
		Connection con = null;
		try{
			con = getConnection2(source);
			DatabaseMetaData dbMetaData = con.getMetaData();
			rs = dbMetaData.getColumns( null, source.getSchemasName(), tablename,"%" );
			long i = 1;//从1开始排序
			while(rs.next()){
					Meta column = new Meta();
					column.setId(i++);
					column.setName(rs.getString("COLUMN_NAME"));//获取列名
					column.setType( rs.getString("TYPE_NAME"));//获取列的类型
					String dataType  = rs.getString("DATA_TYPE");//字段数据类型(对应java.sql.Types中的常量)
					column.setDataType(dataType);//获取列的类型
					columnList.add(column);
			}
		}catch(Exception e){
			throw e; 
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(con!=null){
				con.close();
			}
		}
		return columnList;
	}

	@Override
	public ExcuteResult excute(DataSource source, String sql)
			throws Exception {
		ExcuteResult result = new ExcuteResult();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			con = getConnection2(source);
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			List<String> colList = new ArrayList<String>();
			for (int i = 1; i <= colCount; i++) {
				String columnName = rsmd.getColumnName(i);
				colList.add(columnName);
			}
			JSONArray array = new JSONArray();
			while (rs.next()) {
				LinkedHashMap<String, Object> datamap = new LinkedHashMap<String, Object>();
				// 遍历每一列
				for (int i = 1; i <= colCount; i++) {
					Object value = null;
					String columnName = rsmd.getColumnName(i);
					//判断是不是clob类型
					if(2005 == rsmd.getColumnType(i)){
						Clob clob = rs.getClob(columnName);//java.sql.Clob
						String detailinfo = "";
					    if(clob != null){
					    	detailinfo = clob.getSubString((long)1,(int)clob.length());
					    }
					    value = detailinfo;
					}else{
						value = rs.getObject(columnName);
					}
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
			}
			int rowCount = array.size();
			result.setColNum(colCount);
			result.setRowNum(rowCount);
			result.setColList(colList);
			result.setDatamap(array);
		}catch(Exception e){
			throw e; //由于页面上要显示执行sql语句时的异常信息 ，因此在此处对异常并不处理，直接抛出
//			checkErrorMessage(e);
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
	
	@Override
	public String createSelectSql(DataSource source, String tableName)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(source.getDbname()).append(".").append(source.getSchemasName()).append(".").append(tableName);
		return sql.toString();
	}

	@Override
	public ExcuteResult showDataPage(DataSource source, String tableName,
			String pageStart,String pageSize) {
		try {
			Long end=Long.valueOf(pageStart)+Long.valueOf(pageSize);
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM (	SELECT B.*, ROWNUMBER() OVER() AS TN FROM (	SELECT * FROM ")
			.append(source.getDbname()).append(".").append(source.getSchemasName()).append(".\"").append(tableName)
			//.append(source.getSchemasName()).append(".").append(tableName)
			.append("\") AS B) AS A WHERE A.TN BETWEEN "+pageStart+" AND "+end);
			logger.debug("查询数据SQL：" + sql.toString());
			return this.excute(source, sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*public Float getSourceDataSize(DataSource source)
			throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		Float size = null;
		StringBuffer sql = new StringBuffer("select sum(x.DATA_OBJECT_P_SIZE)/(1024*1024) as size from SYSIBMADM.ADMINTABINFO x where TABSCHEMA='");
		sql.append(source.getSchemasName()).append("'");
		connection = getConnection2(source);
		stmt = connection.createStatement();
		rs = stmt.executeQuery(sql.toString());
		while (rs.next()) {
			size = rs.getFloat("size");//单位MB
		}
		return size;
	}*/

	/*@Override
	public boolean canBeConnected(DataSource ds) throws Exception{
		try{
			Connection conn = this.getConnection2(ds);
			DatabaseMetaData databaseMetaData = conn.getMetaData();

			//判断数据库是否存在
			ResultSet resultSet = databaseMetaData.getCatalogs();
			//判断对比目标是否存在
			boolean exit = false;
			System.out.println("存在的数据库：---------");
			while (resultSet.next()){
				//数据库名，模式名，表名大小写不敏感
				String getCatalogName = resultSet.getString("TABLE_CAT");
				System.out.println(getCatalogName);
				if( getCatalogName.equalsIgnoreCase(ds.getDbname()) ) {
					exit = true;
					break;
				}
			}
			if( !exit ){
				throw new VerifyException("请检查数据库名");
			}

			//判断传入的模式是否存在，
			resultSet = databaseMetaData.getSchemas();
			System.out.println("存在的模式：---------");
			while( resultSet.next() ) {
				String schemaName = resultSet.getString("TABLE_SCHEM");
				System.out.println(schemaName);
				if (schemaName.equals(ds.getSchemasName())) {
					return true;
				}
			}
			throw new VerifyException("请检查模式名");

		} catch (VerifyException verifyException) {
			throw verifyException;
		}catch (Exception e) {
			throw e;
		}
	}*/

	@Override
	public Map<String, List<Meta>> findColumn(DataSource dataSource, String tableNameLike
			, String columnNameLike, Boolean enableFuzzy)
			throws SQLException {
		Connection connection = JdbcHelper.getConnection(dataSource.getDriver(), dataSource.getUrl()
				, dataSource.getUserName(), dataSource.getPassword());
		DatabaseMetaData dbMetaData = connection.getMetaData();
		enableFuzzy = enableFuzzy == null ? true : enableFuzzy;
		if (enableFuzzy) {
			if (tableNameLike != null) {
				tableNameLike = "%" + tableNameLike + "%";
			}
			if (columnNameLike != null) {
				columnNameLike = "%" + columnNameLike + "%";
			}
		}
		ResultSet rs = dbMetaData.getColumns(null, dataSource.getSchemasName(), tableNameLike, columnNameLike);
		Map<String, List<Meta>> tabColsMap = new HashMap<>();
		int num = 0;
		while (rs.next()) {
			if (num < 200) {
				String tableName = rs.getString("TABLE_NAME");
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
	}

	@Override
	public boolean validateTableExist(DataSource dataSource, String tableName) {
		boolean flag = false;
		Connection connection = JdbcHelper.getConnection(dataSource.getDriver(), dataSource.getUrl()
				, dataSource.getUserName(), dataSource.getPassword());
		
		ResultSet rs = null;
		try {
			DatabaseMetaData dbMetaData = connection.getMetaData();
			String type [] = {"TABLE"};
			rs = dbMetaData.getTables(null, dataSource.getSchemasName(), tableName, type);
			flag = rs.next();
		} catch (SQLException e) {
			throw new CommonException("校验表是否存在异常：", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
		return flag;
	}
}
