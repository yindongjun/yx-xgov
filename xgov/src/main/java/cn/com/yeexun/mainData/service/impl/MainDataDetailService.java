package cn.com.yeexun.mainData.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.collectTask.service.ICollectTaskService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.ExcuteResult;
import cn.com.yeexun.dataSource.entity.Meta;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.mainData.dao.MainDataDao;
//import cn.com.yeexun.dataSource.entity.ExcuteResult;
//import cn.com.yeexun.dataSource.entity.Meta;
import cn.com.yeexun.mainData.dao.MainDataDetailDao;
import cn.com.yeexun.mainData.entity.MainData;
import cn.com.yeexun.mainData.entity.MainDataDetail;
import cn.com.yeexun.mainData.service.IMainDataDetailService;
import cn.com.yeexun.meta_data.collector.JdbcHelper;
import cn.com.yeexun.meta_data.collector.MetadataCollector;
import cn.com.yeexun.utils.CommonException;

@Service
public class MainDataDetailService extends BaseService<MainDataDetail> implements IMainDataDetailService {

	Connection con ;
	
	ResultSet resultSet;
	
	DatabaseMetaData metadata;
	
	@Autowired
	private MainDataDetailDao mainDataDetailDao;
	
//	@Autowired
//	private IDataSourceService dataSourceService;
	
	@Autowired
	private ICollectTaskService collectTaskService;
	
	@Autowired
	private MainDataDao mainDataDao;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	
	@Override
	public MainDataDetail getById(Long mainDataDetailId,String name) {
		MainDataDetail mainDataDetail = mainDataDetailDao.getById(mainDataDetailId,name);
		return mainDataDetail;
	}
	
	/**
	 * 获取数据源下的所有表
	 * @param dataSourceId
	 * @return
	 */
	@Override
	public List<Meta> getAllTables(Long mainDataId) throws Exception{
		
		MainData maindata = mainDataDao.selectByPrimaryKey(mainDataId);
		DataSource dataSource = datasourceService2.getDatasourceById(maindata.getDataSourceId());
//		DataSource dataSource = mainDataDetailDao.getDataSourceByMainDataId(mainDataId);
		//获取主数据源下的表
		MetadataCollector colllect = collectTaskService.getMetadataCollector(dataSource.getDatabaseType());
		List<Meta> tables = colllect.getTables(dataSource);
		
		List<MainDataDetail> mainTables = mainDataDetailDao.getMainTables(mainDataId);
		List<String> exitTables = new ArrayList<String>();
 		for (MainDataDetail mainDataDetail : mainTables) {
			exitTables.add(mainDataDetail.getTableName());
		}
		//如果表不再主数据源中则添加表
 		List<Meta> result = new ArrayList<Meta>();
		for (Meta table : tables) {
			if (!exitTables.contains(table.getName())) {
				result.add(table);
			}
		}
		return result;
	}

	
	/**
	 * 给主数据添加表
	 * @param tables
	 */
	@Override
	public void addTables(List<MainDataDetail> tables) {
		mainDataDetailDao.insertList(tables);
	}

	/**
	 * 获取主数据下的所有表
	 * @param mainDataId
	 * @return
	 */
	@Override
	public List<MainDataDetail> getMainTables(Long mainDataId){
		List<MainDataDetail> tables = mainDataDetailDao.getMainTables(mainDataId);
		return tables;
	}
	
	
	/**
	 * 获取表里的字段
	 * @param mmd
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String,String>> getFields(MainDataDetail mmd) throws Exception{
		List<Map<String,String>> fields = new ArrayList<Map<String,String>>();
		
//		MainData maindata = mainDataDao.selectByPrimaryKey(mmd.getId());
		Long dataSourceId = mainDataDetailDao.getDataSourceId(mmd.getId());
		DataSource dataSource = datasourceService2.getDatasourceById(dataSourceId);
		MetadataCollector colllect = collectTaskService.getMetadataCollector(dataSource.getDatabaseType());
		List<Meta> columns = colllect.getColumns(mmd.getTableName(), dataSource);
		for (Meta meta : columns) {
			Map<String, String> field = new HashMap<String, String>();
			field.put("field_name", meta.getName());
			field.put("data_type",meta.getDataType());
			field.put("field_type",meta.getType());
			//匹配数据库类型返回查询条件
			String queryCondition = new String();
			if (StringUtils.isNotBlank(meta.getDataType())) {
				switch (Integer.parseInt(meta.getDataType())) {
				case Types.BIT: 
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;

				case Types.TINYINT: 
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;
					
				case Types.SMALLINT: 
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;

				case Types.INTEGER: 
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;
					
				case Types.BIGINT: 
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;

				case Types.FLOAT: 
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;

				case Types.REAL:
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;

				case Types.DOUBLE: 
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;
					
				case Types.NUMERIC: 
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;
					
				case Types.DECIMAL: 
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;

				case Types.CHAR: 
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;
					
				case Types.VARCHAR: 
					queryCondition="=,!=,IS NOT NULL,IN,NOT IN";
					break;

				case Types.LONGVARCHAR: 
					queryCondition="=,!=,IS NOT NULL,IN,NOT IN";
					break;

				case Types.DATE:
					queryCondition="=,!=,>,<,>=,<=";
					break;

				case Types.TIME: 
					queryCondition="=,!=,>,<,>=,<=";
					break;
					
				case Types.TIMESTAMP: 
					queryCondition="=,!=,>,<,>=,<=";
					break;
					
				case Types.BINARY: 
					queryCondition="";
					break;
					
				case Types.VARBINARY: 
					queryCondition="";
					break;
					
				case Types.LONGVARBINARY: 
					queryCondition="";
					break;
					
				case Types.NULL: 
					queryCondition="";
					break;
					
				case Types.OTHER: 
					queryCondition="";
					break;
					
				case Types.BOOLEAN: 
					queryCondition="=,!=";
					break;
					
				case Types.ROWID: 
					queryCondition="=,!=,>,<,>=,<=,IS NOT NULL,IN,NOT IN";
					break;
					
				case Types.NCHAR: 
					queryCondition="=,!=,IS NOT NULL,IN,NOT IN";
					break;
					
				case Types.NVARCHAR: 
					queryCondition="=,!=,IS NOT NULL,IN,NOT IN";
					break;
					
				case Types.LONGNVARCHAR: 
					queryCondition="=,!=,IS NOT NULL,IN,NOT IN";
					break;
				
				default:
					queryCondition="";
					break;
				}
			}
			field.put("queryCondition",queryCondition);
			fields.add(field);
		}
		return fields;
	}


	/**
	 * 获取表里的数据
	 * @param mdd
	 * @return
	 */
	@Override
	public ExcuteResult getShowData(MainDataDetail mdd) throws Exception{
		Long dataSourceId = mainDataDetailDao.getDataSourceId(mdd.getId());
		DataSource dataSource = datasourceService2.getDatasourceById(dataSourceId);
//		DataSource dataSource = mainDataDetailDao.getDataSource(mdd.getId());
		MetadataCollector colllect = collectTaskService.getMetadataCollector(dataSource.getDatabaseType());
		// 先到数据源中判断该表是否存在
		boolean tableIsExist = colllect.validateTableExist(dataSource, mdd.getTableName());
		if (!tableIsExist) {
			throw new CommonException(mdd.getTableName() + "表在数据库" + dataSource.getDbname() + "中不存在！");
		}
		ExcuteResult result = colllect.showDataPage(dataSource, mdd.getTableName(),"0","10");
		return result;
	}

	

	/**
	 * 
	 * 通过查询语句查找表中数据  返回List<Map<field, data>>
	 * @param mdd 
	 * @param sql
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryDataBySQL(MainDataDetail mdd,String sql) {
//		DataSource dataSource = mainDataDetailDao.getDataSource(mdd.getId());
		Long dataSourceId = mainDataDetailDao.getDataSourceId(mdd.getId());
		DataSource dataSource = datasourceService2.getDatasourceById(dataSourceId);
		List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
		try {
			con = JdbcHelper.getConnection(dataSource.getDriver(), dataSource.getUrl(), dataSource.getUserName(), dataSource.getPassword());
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData  rsmd = rs.getMetaData();
			//获取表的字段数
			int columCount =  rsmd.getColumnCount();
			//遍历表中字段和数据
			while(rs.next()) {
				Map<String, Object> data = new HashMap<String, Object>();
				for(int i=1 ; i <= columCount ; i++ ) {
					data.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
				}
				dataMap.add(data);
			}
			
			ps.close();
			rs.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(con != null) {
				con=null;
			}
			if(resultSet != null) {
				resultSet = null;
			}
		}
		return dataMap;
	}
	
	
	@Override
	public int deleteTables(Long mainDataId){
		int result = mainDataDetailDao.deleteTables(mainDataId);
		return result;
	}

	@Override
	public DataSource getDataSourceById(Long id) {
//		DataSource datasoutce = mainDataDetailDao.getDataSource(id);
		Long dataSourceId = mainDataDetailDao.getDataSourceId(id);
		DataSource dataSource = datasourceService2.getDatasourceById(dataSourceId);
		return dataSource;
	}

}
