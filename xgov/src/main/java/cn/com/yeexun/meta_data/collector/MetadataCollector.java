package cn.com.yeexun.meta_data.collector;

import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.ExcuteResult;
//import cn.com.yeexun.dataSource.entity.ExcuteResult;
//import cn.com.yeexun.dataSource.entity.Meta;
//import cn.com.yeexun.meta_data.entity.MetadataInfo;
import cn.com.yeexun.dataSource.entity.Meta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MetadataCollector {

//	public Connection getConnection() ;
//	
//	public void setConnection(Connection connection);
//	
//	public MetadataInfo getDatabaseInfo(DataSource source) throws Exception ;
//	
//	public List<MetadataInfo> getTables(String catalog, String schemaPattern, 
//			String tableNamePattern, String[] types) throws Exception;
//	
	public List<Meta> getTables(DataSource source) throws Exception;
//	
	public List<Meta> getColumns(String tablename, DataSource source) throws Exception;
//	
//	public Map<String,List<MetadataInfo>> getColumnMetadata(String catalog, String schema) throws Exception ;
//	
	public ExcuteResult excute(DataSource source,String sql) throws Exception;
//
	public String createSelectSql(DataSource source, String tableName) throws Exception;
//	
	/**
	 * 数据库的分页查询
	 * @param source
	 * @param tableName
	 * @return
	 */
	public ExcuteResult showDataPage(DataSource source, String tableName,String pageStart,String pageSize);
//	public Float getSourceDataSize(DataSource source)throws Exception;

	/**
	 * 判端数据源受否可以被连接，包括数据库传入数据库是否存在，模式是否存在
	 * @param dataSource 数据源信息
	 * @return
	 */
//	public boolean canBeConnected(DataSource dataSource) throws Exception;
//
	Map<String, List<Meta>> findColumn(DataSource dataSource, String tableNameLike, String columnNameLike, Boolean enableFuzzy)
			throws SQLException;
//
	boolean validateTableExist(DataSource dataSource, String tableName);
}
