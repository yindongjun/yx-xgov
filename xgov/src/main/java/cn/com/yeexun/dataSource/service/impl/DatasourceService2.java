package cn.com.yeexun.dataSource.service.impl;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;

import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.HbaseSource;
import cn.com.yeexun.dataSource.entity.HdfsSource;
import cn.com.yeexun.dataSource.entity.HiveSource;
import cn.com.yeexun.dataSource.entity.ImpalaSource;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.dataSource.entity.MetadataImpalaSource;
import cn.com.yeexun.dataSource.entity.MetaddataHiveSource;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import cn.com.yeexun.utils.FeignException;
import cn.com.yeexun.utils.ResponseUtil;

@Service
public class DatasourceService2 {
	
	private static final Logger LOG = LoggerFactory.getLogger(DatasourceService2.class);
	
	@Autowired
	private IDatasourceClient datasourceClient;
	
	public DataSource getDatasourceById(Long id) {
		
		String datasource = datasourceClient.getDatasourceById(id);
		if (null == datasource) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
		}
		if (ResponseUtil.isError(datasource)) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
		}
		MetadataDatasource metaSource = ResponseUtil.getObjectData(datasource, MetadataDatasource.class);
		DataSource source = new DataSource(); 
		try {
			BeanUtils.copyProperties(source, metaSource);
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		}
		
		String dbType = source.getDatabaseType();
		if (Constant.HIVE.equalsIgnoreCase(dbType)) {
			String hivesourceStr = datasourceClient.getSpecialSourceById(id);
			if (null == hivesourceStr) {
				throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
			}
			if (ResponseUtil.isError(hivesourceStr)) {
				throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
			}
			MetaddataHiveSource hiveSource = ResponseUtil.getObjectData(hivesourceStr, MetaddataHiveSource.class);
			source.setDriver(hiveSource.getDriver());
			source.setUrl(hiveSource.getUrl());
			source.setUserName(hiveSource.getUserName());
			source.setPassword(hiveSource.getPassword());
		} else if (Constant.IMPALA.equalsIgnoreCase(dbType)) {
			String impalaSourceStr = datasourceClient.getSpecialSourceById(id);
			if (null == impalaSourceStr) {
				throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
			}
			if (ResponseUtil.isError(impalaSourceStr)) {
				throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
			}
			MetadataImpalaSource impalaSource = ResponseUtil.getObjectData(impalaSourceStr, MetadataImpalaSource.class);
			source.setDriver(impalaSource.getDriver());
			source.setUrl(impalaSource.getUrl());
			source.setUserName(impalaSource.getUserName());
			source.setPassword(impalaSource.getPassword());
		} else if (Constant.ORACLE.equalsIgnoreCase(dbType)) {
			source.setDbname(source.getSchemasName());
		}
		
		LOG.info("获取到的datasource对象：" + JSON.toJSONString(source));
		return source;
	}
	
	public MetadataDatasource getMetaDatasourceById(Long id) {
		
		String datasource = datasourceClient.getDatasourceById(id);
		if (null == datasource) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
		}
		if (ResponseUtil.isError(datasource)) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
		}
		MetadataDatasource metaSource = ResponseUtil.getObjectData(datasource, MetadataDatasource.class);
		LOG.info("获取到的metadataSource对象：" + JSON.toJSONString(metaSource));
		return metaSource;
	}
	
	public HiveSource getHiveSource(Long sourceId) {
		String hivesourceStr = datasourceClient.getSpecialSourceById(sourceId);
		if (null == hivesourceStr) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
		}
		if (ResponseUtil.isError(hivesourceStr)) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
		}
		HiveSource hiveSource = ResponseUtil.getObjectData(hivesourceStr, HiveSource.class);
		MetadataDatasource metaDatasource = this.getMetaDatasourceById(sourceId);
		hiveSource.setDriver(metaDatasource.getDriver());
		hiveSource.setUrl(metaDatasource.getUrl());
		hiveSource.setHiveMetaDataUserName(metaDatasource.getUserName());
		hiveSource.setHiveMetaDataPassword(metaDatasource.getPassword());
		LOG.info("获取到的hivesource对象：" + JSON.toJSONString(hiveSource));
		return hiveSource;
	}
	
	public ImpalaSource getImpalaSource(Long sourceId) {
		String impalaSourceStr = datasourceClient.getSpecialSourceById(sourceId);
		if (null == impalaSourceStr) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
		}
		if (ResponseUtil.isError(impalaSourceStr)) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
		}
		ImpalaSource impalaSource = ResponseUtil.getObjectData(impalaSourceStr, ImpalaSource.class);
		MetadataDatasource metaDatasource = this.getMetaDatasourceById(sourceId);
		impalaSource.setDriver(metaDatasource.getDriver());
		impalaSource.setUrl(metaDatasource.getUrl());
		impalaSource.setUserName(metaDatasource.getUserName());
		impalaSource.setPassword(metaDatasource.getPassword());
		LOG.info("获取到的impalaSource对象：" + JSON.toJSONString(impalaSource));
		return impalaSource;
		
	}
	
	public HbaseSource getHbaseSource(Long sourceId) {
		String hbaseSourceStr = datasourceClient.getSpecialSourceById(sourceId);
		if (null == hbaseSourceStr) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
		}
		if (ResponseUtil.isError(hbaseSourceStr)) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
		}
		HbaseSource hbaseSource = ResponseUtil.getObjectData(hbaseSourceStr, HbaseSource.class);
		LOG.info("获取到的hbaseSource对象：" + JSON.toJSONString(hbaseSource));
		return hbaseSource;
	}
	
	public HdfsSource getHdfsSource(Long sourceId) {
		
		String hdfsSourceStr = datasourceClient.getSpecialSourceById(sourceId);
		if (null == hdfsSourceStr) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
		}
		if (ResponseUtil.isError(hdfsSourceStr)) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
		}
		HdfsSource dsHdfs = ResponseUtil.getObjectData(hdfsSourceStr, HdfsSource.class);
		LOG.info("获取到的hdfsSource对象：" + JSON.toJSONString(dsHdfs));
		return dsHdfs;
		
	}
	
	public String getDataSourceByTypes(String types, String sourceName, Integer start
			, Integer length) {
		String sourceStr = datasourceClient.getDataSourceByTypes(types, sourceName, start, length);
		if (null == sourceStr) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
		}
		if (ResponseUtil.isError(sourceStr)) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
		}
		return sourceStr;
//		List<DataSource> sources = ResponseUtil.getListData(sourceStr, DataSource.class);
//		return sources;
	}
	
	public List<DataSource> getAllDataSource(String sourceName) {
		String sourceStr = datasourceClient.getAllDataSource(sourceName);
		if (null == sourceStr) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
		}
		if (ResponseUtil.isError(sourceStr)) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
		}
		List<DataSource> dataSources = ResponseUtil.getListData(sourceStr, DataSource.class);
		return dataSources;
	}
	
	public List<MetadataDatasource> getSourceByIds(String sourceIds) {
		if (sourceIds == null || sourceIds.trim().length() <= 0) {
			return null;
		}
		String sourceStr = datasourceClient.getSourceByIds(sourceIds);
		if (null == sourceStr) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
		}
		if (ResponseUtil.isError(sourceStr)) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
		}
		List<MetadataDatasource> dataSources = ResponseUtil.getListData(sourceStr, MetadataDatasource.class);
		return dataSources;
	}

	public String searchColumn(String columnNames, Long sourceId) {
		String resp = datasourceClient.searchColumn(columnNames, sourceId);
		if (null == resp) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_DISCONTECTED);
		}
		if (ResponseUtil.isError(resp)) {
			throw new FeignException(CodeUtil.FEIGN_CLIENT_ERROR);
		}
		return resp;
	}
}
