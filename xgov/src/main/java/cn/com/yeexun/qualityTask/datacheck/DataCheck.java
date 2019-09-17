package cn.com.yeexun.qualityTask.datacheck;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.com.yeexun.XgovApplication;
import cn.com.yeexun.businessTerms.entity.Code;
import cn.com.yeexun.businessTerms.service.ICodeService;
import cn.com.yeexun.businessTerms.service.impl.CodeService;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataElement.service.IDataElementService;
import cn.com.yeexun.dataElement.service.impl.DataElementService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.meta_data.collector.JdbcHelper;
import cn.com.yeexun.qualityTask.dao.IQualityTaskLogDao;
import cn.com.yeexun.qualityTask.entity.CheckColumn;
import cn.com.yeexun.qualityTask.entity.CheckResult;
import cn.com.yeexun.qualityTask.entity.CheckResultCount;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.EnumVerifyDetail;
import cn.com.yeexun.qualityTask.entity.FormatVerifyDetail;
import cn.com.yeexun.qualityTask.entity.InitNode;
import cn.com.yeexun.qualityTask.entity.IntervalVerifyDetail;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityTask.entity.QualityTaskLog;
import cn.com.yeexun.qualityTask.entity.RegularVerifyDetail;
import cn.com.yeexun.qualityTask.entity.RelationVerifyDetail;
import cn.com.yeexun.qualityTask.service.ICheckResultCountService;
import cn.com.yeexun.qualityTask.service.IDesignTableInfoService;
import cn.com.yeexun.qualityTask.service.IQualityTaskDetailService;
import cn.com.yeexun.qualityTask.service.impl.CheckResultCountService;
import cn.com.yeexun.qualityTask.service.impl.DesignTableInfoService;
import cn.com.yeexun.qualityTask.service.impl.QualityTaskDetailService;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import cn.com.yeexun.utils.DebugException;
import cn.com.yeexun.utils.JedisUtil;
import cn.com.yeexun.utils.SpringContextHelper;

public class DataCheck {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataCheck.class);
	
	private static final String insertSql = "insert into check_result_detail values ";
	
	private static final String REGEX_YYYYMMDD = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]"
			+ "{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]"
			+ "|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|"
			+ "[2468][048]|[3579][26])00))0229)$";
	private static final String REGEX_HHMMSS = "([0-1][0-9]|2[0-3])([0-5][0-9])([0-5][0-9])$";
	private static final String REGEX_YYYYMMDDHHMMSS = "((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9]"
			+ "[0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12]"
			+ "[0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]"
			+ "|[2468][048]|[3579][26])00))0229)) ([0-1][0-9]|2[0-3])([0-5][0-9])([0-5][0-9])$";
	
	private Long designTableInfoId;
	
	private Long taskHisId;
	
	private Long taskId;
	
	private DesignTableInfo designTableInfo;
	
	private int bufferSize = Integer.parseInt(XgovApplication.env.getProperty("xgov.check.insertSize"));
	
	private int redisBatchSize = 1000;
	
	private int debugSize;
	
	private int fetchSize;
	
	private CountDownLatch countDownLatch;
	
	private Map<String, Pattern> regularVerifyPatterns = new HashMap<>();
	
	private Map<String, Pattern> formatVerifyPatterns = new HashMap<>();
	
	private Map<String, List<Code>> enumVerifyCodesets = new HashMap<>();
	
	private Map<String, FormatVerifyDetail> formatVerifyDetails = new HashMap<>();
	
	private Map<String, IntervalVerifyDetail> intervalVerifyDetails = new HashMap<>();
	
	private Map<String, RelationVerifyDetail> relationVerifyDetails = new HashMap<>();
	
	private List<String> redisKeys = new ArrayList<>();
	
	private IDesignTableInfoService designTableInfoService 
			= (IDesignTableInfoService) SpringContextHelper.getBean(DesignTableInfoService.class);
	
	private IQualityTaskDetailService qualityTaskDetailService 
			= (IQualityTaskDetailService) SpringContextHelper.getBean(QualityTaskDetailService.class);
	
//	private IDataSourceService dataSourceService 
//			= (IDataSourceService) SpringContextHelper.getBean(DataSourceService.class);
	
	private DatasourceService2 datasourceService2 
			= (DatasourceService2) SpringContextHelper.getBean(DatasourceService2.class);
	
	private ICheckResultCountService checkResultCountService 
			= (ICheckResultCountService) SpringContextHelper.getBean(CheckResultCountService.class);
	
	private ICodeService codeService 
			= (ICodeService) SpringContextHelper.getBean(CodeService.class);
	
	private IQualityTaskLogDao qualityTaskLogDao 
			= (IQualityTaskLogDao) SpringContextHelper.getBean(IQualityTaskLogDao.class);
	
	private IDataElementService dataElementService 
			= (IDataElementService) SpringContextHelper.getBean(DataElementService.class);
	
	private JedisUtil jedisUtil = (JedisUtil) SpringContextHelper.getBean(JedisUtil.class);
	
	private Connection sysDBConn;
	
	public DataCheck(Long designTableInfoId, Long taskHisId, Long taskId) {
		super();
		this.designTableInfoId = designTableInfoId;
		this.taskHisId = taskHisId;
		this.taskId = taskId;
		String driver = XgovApplication.env.getProperty("spring.datasource.driver-class-name");
		String url = XgovApplication.env.getProperty("spring.datasource.url");
		String username = XgovApplication.env.getProperty("spring.datasource.username");
		String pwd = XgovApplication.env.getProperty("spring.datasource.password");
		this.sysDBConn = JdbcHelper.getConnection(driver, url, username, pwd);
	}
	
	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	
	private Connection getConnection() {
		DataSource dataSource = null;
		try {
			designTableInfo = designTableInfoService.getById(designTableInfoId);
//			dataSource = dataSourceService.getById(designTableInfo.getDatasourceId());
			dataSource = datasourceService2.getDatasourceById(designTableInfo.getDatasourceId());
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		}
		Connection connection = JdbcHelper.getConnection(dataSource.getDriver(), dataSource.getUrl()
				, dataSource.getUserName(), dataSource.getPassword());
		if (Constant.MYSQL.equals(dataSource.getDatabaseType())) {
			fetchSize = Integer.MIN_VALUE;
		} else {
			fetchSize = Integer.parseInt(XgovApplication.env.getProperty("xgov.check.fetchSize"));
		}
		return connection;
	}
	
	/**
	 * 入口方法
	 */
	public void check() {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			LOGGER.info("开始校验数据...");
			qualityTaskLogDao.insert(new QualityTaskLog(taskHisId, designTableInfo.getTableName()
					,QualityTaskLog.INFO, "开始校验数据...", taskId));
			String querySql = buildInitSql();
			stmt = connection.createStatement();
			stmt.setFetchSize(fetchSize);
			rs = stmt.executeQuery(querySql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			String[] columnNames = new String[columnCount];
			// 获取表中的所有字段名
//			DataSource dataSource = dataSourceService.getById(designTableInfo.getDatasourceId());
			DataSource dataSource = datasourceService2.getDatasourceById(designTableInfo.getDatasourceId());
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				if (Constant.HIVE.equals(dataSource.getDatabaseType())) {
					columnName = columnName.substring(columnName.lastIndexOf(".") + 1);
				}
			 	columnNames[i - 1] = columnName;
			}
			LOGGER.info("获取到的该表的所有字段：" + Arrays.toString(columnNames));
			qualityTaskLogDao.insert(new QualityTaskLog(taskHisId, designTableInfo.getTableName()
					,QualityTaskLog.INFO, "获取到的该表的所有字段：" + Arrays.toString(columnNames), taskId));
			// 将校验规则按照列名分组
			LOGGER.info("开始将校验规则按照列名分组。。。");
			qualityTaskLogDao.insert(new QualityTaskLog(taskHisId, designTableInfo.getTableName()
					,QualityTaskLog.INFO, "开始将校验规则按照列名分组...", taskId));
			Map<String, List<QualityTaskDetail>> columnTasksMap = groupTaskByColumn();
			if (columnTasksMap == null || columnTasksMap.size() <= 0) {
				LOGGER.info("该表没有配置任何规则，任务执行结束！");
				qualityTaskLogDao.insert(new QualityTaskLog(taskHisId, designTableInfo.getTableName()
					,QualityTaskLog.WARN, "该表没有配置任何规则，任务执行结束！", taskId));
				if (countDownLatch != null) {
					countDownLatch.countDown();
				}
				return;
			}
			LOGGER.info(designTableInfo.getTableName() + "表上的所有校验规则如下：\n" 
						+ JSON.toJSONString(columnTasksMap));
			LOGGER.info("校验规则分组完成");
			qualityTaskLogDao.insert(new QualityTaskLog(taskHisId, designTableInfo.getTableName()
					, QualityTaskLog.INFO, "验规则分组完成，" + designTableInfo.getTableName() + "表上的所有校验规则如下：\n" 
					+ JSON.toJSONString(columnTasksMap), taskId));
			// 用于封装校验结果统计信息
			CheckResultCount checkResultCount = new CheckResultCount();
			checkResultCount.setDesignTableInfoId(designTableInfoId);
			checkResultCount.setTaskHisId(taskHisId);
			checkResultCount.setTableName(designTableInfo.getTableName());
			checkResultCount.setDatasourceId(designTableInfo.getDatasourceId());
			checkResultCount.setErrorDataNum(0);
			checkResultCount.setColumnNames(JSON.toJSONString(columnNames));
			checkResultCount.setOwner("admin");
			checkResultCount.setStatus(0);
			checkResultCount.setCreateTime(new Date());
			checkResultCount.setUpdateTime(new Date());
			checkResultCount.setDeleteFlag(0);
			checkResultCountService.save(checkResultCount);
			// 校验的总行数
			int totalRows = 0;
			List<CheckResult> errorDatas = new ArrayList<>(bufferSize);
			while (rs.next()) {
				totalRows++;
				if (debugSize > 0) {
					if (totalRows > debugSize) {
						break;
					}
				}
				Map<String, CheckColumn> aRow = new HashMap<>();
				Set<String> errorVerifyCodes = new HashSet<>();
				boolean thisRowIsOk = true;
				for (int i = 1; i <= columnCount; i++) {
					// 用于封装每一列的校验结果，包括数据和违反的校验规则信息。
					Object data = rs.getObject(i);
					CheckColumn aColumn = new CheckColumn();
					String type = metaData.getColumnTypeName(i);
					if(StringUtils.isNotBlank(type)&&(type.toLowerCase().contains("date")||type.toLowerCase().contains("time"))){
						if(null!=data){
							aColumn.setValue(data.toString());
						}else{
							aColumn.setValue(data);
						}
					}else{
						aColumn.setValue(data);
					}
					List<QualityTaskDetail> thisColumnTasks = columnTasksMap.get(columnNames[i - 1]);
					// 进行校验，并将错误原因跟数据封装到一起，然后放到aRow的map中去。
					if (thisColumnTasks != null && thisColumnTasks.size() > 0) {
						for (QualityTaskDetail qualityTaskDetail : thisColumnTasks) {
							Map<String, Object> checkResult = null;
							try {
								checkResult = check(data, qualityTaskDetail, columnNames[i - 1]);
							} catch (Exception e) {
								throw new DebugException(e, designTableInfo.getTableName()
										, columnNames[i - 1], qualityTaskDetail.getVerifyDetail()
										, qualityTaskDetail.getVerifyType());
							}
							if (checkResult != null && !(boolean) checkResult.get("isOk")) {
								// 封装错误信息
								thisRowIsOk = thisRowIsOk ? false : thisRowIsOk;
								List<String> errorMsgs = aColumn.getErrorMsgs();
								if (errorMsgs == null) {
									errorMsgs = new ArrayList<>();
									errorMsgs.add((String) checkResult.get("errorMsg"));
									aColumn.setErrorMsgs(errorMsgs);
								} else {
									errorMsgs.add((String) checkResult.get("errorMsg"));
								}
								errorVerifyCodes.add(qualityTaskDetail.getVerifyType());
							}
						}
					}
					aRow.put(columnNames[i - 1], aColumn);
				}
				if (!thisRowIsOk) {
					// 封装结果
					checkResultCount.setErrorDataNum(checkResultCount.getErrorDataNum() + 1);
					CheckResult checkResult = new CheckResult();
					checkResult.setDataDetail(JSON.toJSONString(aRow));
					checkResult.setResultCountId(checkResultCount.getId());
					StringBuilder errorTypeSb = new StringBuilder(",");
					for (String errorVerifyCode : errorVerifyCodes) {
						errorTypeSb.append(errorVerifyCode).append(",");
					}
					checkResult.setErrorType(errorTypeSb.toString());
					errorDatas.add(checkResult);
				}
				// 判断list是否到达批量写入数据库的条件，写入数据库
				if (errorDatas.size() >= bufferSize) {
					long start = System.currentTimeMillis();
					insertErrorDatas(errorDatas);
					errorDatas.clear();
					LOGGER.debug("入库1000条问题数据完成，耗时：" + (System.currentTimeMillis() - start) + "ms.");
				}
			}
			if (errorDatas.size() > 0) {
				insertErrorDatas(errorDatas);
				errorDatas.clear();
//				LOGGER.info("问题数据入库完成：" + System.currentTimeMillis());
			}
			LOGGER.info("问题数据入库完成：" + System.currentTimeMillis());
			// 更新错误总条数和校验数据的总条数
			/*if (checkResultCount.getErrorDataNum() > 0) {
				checkResultCount.setTotalRows(totalRows);
				checkResultCountService.update(checkResultCount);
			}*/
			checkResultCount.setTotalRows(totalRows);
			checkResultCountService.update(checkResultCount);
		} catch (Exception e) {
			LOGGER.error("质量校验发生错误：", e);
			qualityTaskLogDao.insert(new QualityTaskLog(taskHisId, designTableInfo.getTableName()
					,QualityTaskLog.ERROR, e.getMessage(), taskId));
			throw new CommonException(e.getMessage(), e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {}
			try {
				stmt.close();
			} catch (Exception e) {}
			try {
				connection.close();
			} catch (Exception e) {}
			try {
				sysDBConn.close();
			} catch (Exception e) {}
			if (countDownLatch != null) {
				countDownLatch.countDown();
			}
			// 清理本次任务中在redis中产生的所有key
			if (redisKeys.size() > 0) {
				for (String key : redisKeys) {
					jedisUtil.del(key);
				}
			}
		}
		LOGGER.info("数据校验完成。");
		qualityTaskLogDao.insert(new QualityTaskLog(taskHisId, designTableInfo.getTableName()
				,QualityTaskLog.INFO, "数据校验完成。", taskId));
	}

	private void insertErrorDatas(List<CheckResult> errorDatas) throws Exception {
		StringBuilder sql = new StringBuilder(insertSql);
		for (CheckResult errorData : errorDatas) {
			sql.append("(").append(errorData.getId()).append(",").append(errorData.getResultCountId())
					.append(",'").append(StringEscapeUtils.escapeSql(errorData.getDataDetail())).append("','")
					.append(errorData.getErrorType()).append("'),");
		}
		String sqlString = sql.substring(0, sql.length() - 1);
//		sqlString = StringEscapeUtils.escapeSql(sqlString);
//		LOGGER.info("生成的SQL：" + sqlString);
//		checkResultDetailService.executeUpdate(sqlString);
		Statement stmt = sysDBConn.createStatement();
		stmt.execute(sqlString);
		stmt.close();
	}
	
	/**
	 * 将该表上的所有质量规则按照列名分类。
	 * @return
	 */
	private Map<String, List<QualityTaskDetail>> groupTaskByColumn() {
		
		String tableInfoStatus = designTableInfo.getStatus();
		if (debugSize <= 0 && !DesignTableInfo.STATUS_COMMITED.equals(tableInfoStatus)) {
			LOGGER.warn("该表的规则设计状态不是已提交，designTableInfo:\n" 
					+ JSON.toJSONString(designTableInfo));
			qualityTaskLogDao.insert(new QualityTaskLog(taskHisId, designTableInfo.getTableName()
					,QualityTaskLog.ERROR, "该表的规则设计状态不是已提交，designTableInfo:\n" 
					+ JSON.toJSONString(designTableInfo), taskId));
//			return null;
			throw new CommonException("该表的规则设计状态不是已提交");
		}
		List<QualityTaskDetail> qualityTaskDetails = qualityTaskDetailService.findByDesignTableId(
				designTableInfo.getDatasourceId(), designTableInfo.getTableName());
		// 校验是否绑定了非审核通过状态的数据元。
		Set<Long> set = new HashSet<>();
		for (QualityTaskDetail taskDetail : qualityTaskDetails) {
			if (taskDetail.getElementId() != null) {
				set.add(taskDetail.getElementId());
			}
		}
		for (Long elementId : set) {
			DataElementEntity element;
			try {
				element = dataElementService.getById(elementId);
			} catch (Exception e) {
				throw new CommonException(e.getMessage());
			}
			if (!Integer.valueOf(DataElementEntity.STATUS_AUDITED).equals(element.getStatus())) {
				LOGGER.warn("该表配置了未通过审核的数据元，请先审核数据元！");
				qualityTaskLogDao.insert(new QualityTaskLog(taskHisId, designTableInfo.getTableName()
						,QualityTaskLog.ERROR, "该表配置了未通过审核的数据元，请先审核数据元！", taskId));
//				return null;
				throw new CommonException("该表配置了未通过审核的数据元，请先审核数据元");
			}
		}
		
		Map<String, List<QualityTaskDetail>> qualityTaskMap = new HashMap<>();
		for (QualityTaskDetail qualityTaskDetail : qualityTaskDetails) {
			String columnName = qualityTaskDetail.getColumnName();
			if (!qualityTaskMap.containsKey(columnName)) {
				List<QualityTaskDetail> eachColumnQualityTaskDetails = new ArrayList<>();
				eachColumnQualityTaskDetails.add(qualityTaskDetail);
				qualityTaskMap.put(columnName, eachColumnQualityTaskDetails);
			} else {
				qualityTaskMap.get(columnName).add(qualityTaskDetail);
			}
			if (QualityTaskDetail.REGULAR_VERIFY.equals(qualityTaskDetail.getVerifyType())) {
				RegularVerifyDetail regularVerifyDetail 
						= JSON.parseObject(qualityTaskDetail.getVerifyDetail(), RegularVerifyDetail.class);
				regularVerifyPatterns.put(columnName, Pattern.compile(regularVerifyDetail.getRegularValue()));
			}
			if (QualityTaskDetail.FORMAT_VERIFY.equals(qualityTaskDetail.getVerifyType())) {
				FormatVerifyDetail formatVerifyDetail = 
						JSON.parseObject(qualityTaskDetail.getVerifyDetail(), FormatVerifyDetail.class);
				formatVerifyDetails.put(columnName, formatVerifyDetail);
				String regex = buildRegex(formatVerifyDetail.getFormatType()
						, formatVerifyDetail.getFormatLength());
				formatVerifyPatterns.put(columnName, Pattern.compile(regex));
			}
			if (QualityTaskDetail.ENUM_VERIFY.equals(qualityTaskDetail.getVerifyType())) {
				EnumVerifyDetail enumVerifyDetail 
						= JSON.parseObject(qualityTaskDetail.getVerifyDetail(), EnumVerifyDetail.class);
				Long codeSetId = enumVerifyDetail.getCodeSetId();
				List<Code> codes = null;
				try {
					codes = codeService.getCodesFromSet(codeSetId);
				} catch (Exception e) {
					throw new CommonException(e.getMessage(), e);
				}
				if (codes == null || codes.size() <= 0) {
					LOGGER.warn("代码集为空，codesetId = " + codeSetId);
					qualityTaskLogDao.insert(new QualityTaskLog(taskHisId, designTableInfo.getTableName()
							,QualityTaskLog.WARN, "代码集为空，codesetId = " + codeSetId, taskId));
				}
				enumVerifyCodesets.put(columnName, codes);
			}
			if (QualityTaskDetail.INTERVAL_VERIFY.equals(qualityTaskDetail.getVerifyType())) {
				IntervalVerifyDetail intervalVerifyDetail 
						= JSON.parseObject(qualityTaskDetail.getVerifyDetail(), IntervalVerifyDetail.class);
				intervalVerifyDetails.put(columnName, intervalVerifyDetail);
			}
			if (QualityTaskDetail.RELATION_VERIFY.equals(qualityTaskDetail.getVerifyType())) {
				RelationVerifyDetail relationVerifyDetail 
						= JSON.parseObject(qualityTaskDetail.getVerifyDetail(), RelationVerifyDetail.class);
				relationVerifyDetails.put(columnName, relationVerifyDetail);
				readDataToRedis();
			}
		}
		return qualityTaskMap;
	}
	
	private void readDataToRedis() {
		// 先筛选出所有的关联目标表的表名及其对应的字段名
		Map<String, List<String>> targetTabCols = new HashMap<>();
		for (RelationVerifyDetail relationDetail : relationVerifyDetails.values()) {
			String targetTable = relationDetail.getTargetTable();
			String targetColumn = relationDetail.getTargetColumn();
			List<String> cols = targetTabCols.get(targetTable);
			if (cols != null && cols.size() > 0) {
				cols.add(targetColumn);
			} else {
				List<String> list = new ArrayList<>();
				list.add(targetColumn);
				targetTabCols.put(targetTable, list);
			}
		}
		// 从关联目标表中读出相关字段的数据hash后写入redis。
		Connection connection = getConnection();
		ResultSet rs = null;
		Statement stmt = null;
		Map<String, String> redisSet = new HashMap<>();
		DataSource source;
		try {
//			source = dataSourceService.getById(designTableInfo.getDatasourceId());
			source = datasourceService2.getDatasourceById(designTableInfo.getDatasourceId());
		} catch (Exception e) {
			throw new CommonException(e.getMessage());
		}
		String escapeChar = null;
		if (Constant.MYSQL.equals(source.getDatabaseType()) 
				|| Constant.IMPALA.equals(source.getDatabaseType())
				|| Constant.HIVE.equals(source.getDatabaseType())) {
			escapeChar = "`";
		} else {
			escapeChar = "\"";
		}
		try {
			for (Map.Entry<String, List<String>> eachEntry : targetTabCols.entrySet()) {
				String redisKeyPrefix = "relationTarget&" + taskHisId + "&" + designTableInfo.getDatasourceId() + "&" 
						+ eachEntry.getKey() + "&";
				List<String> cols = eachEntry.getValue();
				StringBuilder selectColumns = new StringBuilder();
				for (String col : cols) {
					selectColumns.append(escapeChar).append(col).append(escapeChar).append(",");
					redisKeys.add(redisKeyPrefix + col);
				}
				
				String tableName = escapeChar + eachEntry.getKey() + escapeChar;
				if (Constant.ORACLE.equals(source.getDatabaseType())) {
					tableName = escapeChar + source.getDbname() + escapeChar + "." + tableName;
				} else if (Constant.DB2.equals(source.getDatabaseType()) || Constant.POSTGRESQL.equals(source.getDatabaseType())) {
					tableName = escapeChar + source.getSchemasName() + escapeChar + "." + tableName;
				}
				String sql = "select " + selectColumns.substring(0, selectColumns.length() - 1) + " from " 
						+ tableName;
				LOGGER.info("SQL：" + sql);
				stmt = connection.createStatement();
				rs = stmt.executeQuery(sql);
				int rowCount = 0;
				while (rs.next()) {
					rowCount++;
					if (debugSize > 0) {
						if (rowCount > debugSize) {
							break;
						}
					}
					for (String col : cols) {
						Object data = rs.getObject(col);
						if (data == null) {
							// redis中set的key规则：relationTarget&taskHisId&数据源ID&表名&列名
//							jedisUtil.sadd(redisKeyPrefix + col, "NULL");
							redisSet.put("NULL", redisKeyPrefix + col);
						} else {
//							jedisUtil.sadd(redisKeyPrefix + col, String.valueOf(data.hashCode()));
//							jedisUtil.sadd(redisKeyPrefix + col, String.valueOf(data));
							redisSet.put(String.valueOf(data), redisKeyPrefix + col);
						}
						if (redisSet.size() >= redisBatchSize) {
							jedisUtil.saddPipeline(redisSet);
						}
					}
				}
			}
			// 最后再次确认提交；
			if (redisSet.size() > 0) {
				jedisUtil.saddPipeline(redisSet);
			}
			
		} catch (Exception e) {
			LOGGER.error("校验数据，获取关联关系目标数据出错：", e);
			throw new CommonException("校验数据，获取关联关系目标数据出错：" + e.getMessage(), e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {}
			try {
				stmt.close();
			} catch (Exception e) {}
			try {
				connection.close();
			} catch (Exception e) {}
		}
	}

	private String buildRegex(String formatType, String formatLength) {
		if (formatType != null) {
			formatType = formatType.trim();
		}
		if (formatLength != null) {
			formatLength = formatLength.trim();
		}
		String regex = null;
		switch (formatType) {
		case "c":
		case "c..":
			regex = "^[a-zA-Z0-9\u4e00-\u9fa5()（）_-]+$";
			break;
		case "a":
			if (formatLength != null && formatLength.length() > 0) {
				regex = "[a-zA-Z]{" + formatLength + "}";
			} else {
				regex = "[a-zA-Z]+";
			}
			break;
		case "a..":
			regex = "[a-zA-Z]{0," + formatLength + "}";
			break;
		case "n":
			if (formatLength != null && formatLength.length() > 0) {
				regex = "[0-9]{" + formatLength + "}";
			} else {
				regex = "[0-9]+";
			}
			break;
		case "n..":
			regex = "[0-9]{0," + formatLength + "}";
			break;
		case "n..()":
			String[] split = formatLength.split(",");
			int allLength = Integer.parseInt(split[0].trim());
			int afterPointLength = Integer.parseInt(split[1].trim());
			regex = "[0-9]{1," + (allLength - afterPointLength) + "}.[0-9]{" + afterPointLength + "}";
			break;
		case "an":
			if (formatLength != null && formatLength.length() > 0) {
				regex = "[0-9A-Za-z]{" + formatLength + "}";
			} else {
				regex = "[0-9A-Za-z]+";
			}
			break;
		case "an..":
			regex = "[0-9A-Za-z]{0," + formatLength + "}";
			break;
		case "d":
			regex = REGEX_YYYYMMDD;
			break;
		case "t":
			regex = REGEX_HHMMSS;
			break;
		case "dt":
			regex = REGEX_YYYYMMDDHHMMSS;
			break;
		default:
			throw new CommonException("暂不支持的格式类型！");
		}
		return regex;
	}
	
	private Map<String, Object> check(Object data, QualityTaskDetail qualityTaskDetail, String columnName) {
		boolean isOk = false;
		Map<String, Object> resultMap = new HashMap<>();
		String verifyType = qualityTaskDetail.getVerifyType();
//		String verifyDetail = qualityTaskDetail.getVerifyDetail();
		switch (verifyType) {
		case QualityTaskDetail.DEFECT_VERIFY:
			if (data instanceof String) {
				isOk = ((String)data).trim().length() > 0;
			} else {
				isOk = data != null;
			}
			resultMap.put("isOk", isOk);
			if (!isOk) {
				resultMap.put("errorMsg", "空值校验");
			}
			break;
		case QualityTaskDetail.ENUM_VERIFY:
			List<Code> codes = enumVerifyCodesets.get(columnName);
			if (data == null) {
				isOk = false;
			} else if(codes != null && codes.size() > 0) {
				for (Code code : codes) {
					if (code.getCode().equals(String.valueOf(data))) {
						isOk = true;
						break;
					}
				}
			}
			resultMap.put("isOk", isOk);
			if (!isOk) {
				resultMap.put("errorMsg", "值域校验");
			}
			break;
		case QualityTaskDetail.FORMAT_VERIFY:
			// 枚举
			// 格式校验
			FormatVerifyDetail formatVerifyDetail = formatVerifyDetails.get(columnName);
			String formatType = formatVerifyDetail.getFormatType();
			if ("c".equals(formatType) || "c..".equals(formatType)) {
				Pattern pattern = formatVerifyPatterns.get(formatVerifyDetail.getColumnName());
				String dataStr = String.valueOf(data);
				boolean matches = pattern.matcher(dataStr).matches();
				/*byte[] bytes = null;
				try {
					bytes = dataStr.getBytes("GB2312");
				} catch (UnsupportedEncodingException e) {
					throw new CommonException("字符集错误！");
				}*/
				int stringLength = countStringlength(dataStr);
				if ("c..".equals(formatType)) {
					isOk = matches && stringLength <= Integer.parseInt(formatVerifyDetail.getFormatLength());
				} else if ("c".equals(formatType) && StringUtils.isNotBlank(formatVerifyDetail.getFormatLength())) {
					isOk = matches && stringLength == Integer.parseInt(formatVerifyDetail.getFormatLength());
				} else {
					isOk = matches;
				}
				
			} else {
				Pattern pattern = formatVerifyPatterns.get(formatVerifyDetail.getColumnName());
				isOk = pattern.matcher(String.valueOf(data)).matches();
			}
			resultMap.put("isOk", isOk);
			if (!isOk) {
				resultMap.put("errorMsg", "格式校验");
			}
			break;
		case QualityTaskDetail.INTERVAL_VERIFY:
			IntervalVerifyDetail intervalVerifyDetail = intervalVerifyDetails.get(columnName);
			isOk = checkNumberInterval(data, intervalVerifyDetail);
			resultMap.put("isOk", isOk);
			if (!isOk) {
				resultMap.put("errorMsg", "数值范围校验");
			}
			break;
		case QualityTaskDetail.REGULAR_VERIFY:
			if (data == null) {
				isOk = false;
			} else {
				Pattern pattern = regularVerifyPatterns.get(qualityTaskDetail.getColumnName());
				isOk = pattern.matcher(String.valueOf(data)).matches();
			}
			resultMap.put("isOk", isOk);
			if (!isOk) {
				resultMap.put("errorMsg", "正则校验");
			}
			break;
		case QualityTaskDetail.UNIQUE_VERIFY:
			String uniqueRedisKey = "unique&" + taskHisId + "&" + designTableInfo.getDatasourceId() + "&" 
					+ designTableInfo.getTableName();
			redisKeys.add(uniqueRedisKey);
			if (data == null) {
				Long count = jedisUtil.sadd(uniqueRedisKey, "NULL");
				if (count > 0) {
					isOk = true;
				} else {
					isOk = false;
					resultMap.put("errorMsg", "唯一性校验");
				}
			} else {
				/*Boolean isExist = jedisUtil.sismember(designTableInfo.getDatasourceId() 
						+ designTableInfo.getTableName() + taskHisId, String.valueOf(data.hashCode()));
				if (isExist) {
					isOk = false;
					resultMap.put("errorMsg", "唯一性校验");
				} else {
					isOk = true;
					jedisUtil.sadd(designTableInfo.getDatasourceId() 
							+ designTableInfo.getTableName() + taskHisId, String.valueOf(data.hashCode()));
				}*/
//				Long count = jedisUtil.sadd(uniqueRedisKey, String.valueOf(data.hashCode()));
				Long count = jedisUtil.sadd(uniqueRedisKey, String.valueOf(data));
				if (count > 0) {
					isOk = true;
				} else {
					isOk = false;
					resultMap.put("errorMsg", "唯一性校验");
				}
			}
			resultMap.put("isOk", isOk);
			break;
		case QualityTaskDetail.RELATION_VERIFY:
			RelationVerifyDetail relationVerifyDetail = relationVerifyDetails.get(qualityTaskDetail.getColumnName());
			String relationRedisKey = "relationTarget&" + taskHisId + "&" + designTableInfo.getDatasourceId() + "&" 
					+ relationVerifyDetail.getTargetTable() + "&" + relationVerifyDetail.getTargetColumn();
			redisKeys.add(relationRedisKey);
//			String value = data == null ? "NULL" : String.valueOf(data.hashCode());
			String value = data == null ? "NULL" : String.valueOf(data);
			isOk = jedisUtil.sismember(relationRedisKey, value);
			resultMap.put("isOk", isOk);
			if (!isOk) {
				resultMap.put("errorMsg", "字段关联比对校验");
			}
			break;
		default:
			new CommonException("不支持的校验规则！");
		}
		return resultMap;
		
	}
	
	/**
	 * 校验数值范围
	 * @param data
	 * @param intervalVerifyDetail
	 * @return
	 */
	private boolean checkNumberInterval(Object data, IntervalVerifyDetail intervalVerifyDetail) {
		boolean isOk = true;
		String min = intervalVerifyDetail.getMinValue();
		String max = intervalVerifyDetail.getMaxValue();
		String dataType = data == null ? "null" : data.getClass().getName();
//		LOGGER.debug("数据类型：" + dataType + ",数值：" + data + ",数值范围：" + min + "~" + max);
		if (data == null) {
			isOk = false;
		} else if (data instanceof Byte) {
			long longValue = ((Byte) data).longValue();
			isOk = longValue >= Long.parseLong(min) && longValue <= Long.parseLong(max);
		} else if (data instanceof Short) {
			long longValue = ((Short) data).longValue();
			isOk = longValue >= Long.parseLong(min) && longValue <= Long.parseLong(max);
		} else if (data instanceof Integer) {
			long longValue = ((Integer) data).longValue();
			isOk = longValue >= Long.parseLong(min) && longValue <= Long.parseLong(max);
		} else if (data instanceof Long) {
			long longValue = ((Long) data).longValue();
			isOk = longValue >= Long.parseLong(min) && longValue <= Long.parseLong(max);
		} else if (data instanceof Float) {
			double doubleValue = ((Float) data).doubleValue();
			isOk = doubleValue >= Double.parseDouble(min) && doubleValue <= Double.parseDouble(max);
		} else if (data instanceof Double) {
			double doubleValue = ((Double) data).doubleValue();
			isOk = doubleValue >= Double.parseDouble(min) && doubleValue <= Double.parseDouble(max);
		} else if (data instanceof BigDecimal) {
			BigDecimal dataValue = (BigDecimal) data;
			BigDecimal minValue = new BigDecimal(min);
			BigDecimal maxValue = new BigDecimal(max);
			isOk = dataValue.compareTo(minValue) >= 0 && dataValue.compareTo(maxValue) <= 0;
		} else {
			throw new CommonException("暂不支持的数据类型：" + dataType + ",字段：" 
					+ intervalVerifyDetail.getColumnName() + ",规则：" + "数据范围校验");
		}
		return isOk;
	}
	
	private String buildInitSql() {
		QualityTaskDetail initNode = qualityTaskDetailService.findInitNode(designTableInfo.getDatasourceId()
				, designTableInfo.getTableName());
		InitNode initTask = JSON.parseObject(initNode.getVerifyDetail(), InitNode.class);
		String sql = initTask.getSql();
		LOGGER.info("生成的SQL：" + sql);
		qualityTaskLogDao.insert(new QualityTaskLog(taskHisId, designTableInfo.getTableName()
				,QualityTaskLog.INFO, "生成的SQL：" + sql, taskId));
		return sql;
	}
	

	public void setDebugSize(int debugSize) {
		this.debugSize = debugSize;
	}
	
	/**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    private static int countStringlength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
    
	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}
	
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9\u4e00-\u9fa5()（）_-]+$");
		String str = "你好啊（中国）(苏州)-_";
		Matcher matcher = pattern.matcher(str);
		System.out.println(matcher.matches());
		System.out.println(countStringlength(str));
	}
	
	

}
