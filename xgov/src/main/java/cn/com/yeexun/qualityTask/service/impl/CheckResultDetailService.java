package cn.com.yeexun.qualityTask.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.dispatch.entity.DispatchHistory;
import cn.com.yeexun.dispatch.service.impl.DispatchHistoryService;
import cn.com.yeexun.qualityTask.dao.ICheckResultCountDao;
import cn.com.yeexun.qualityTask.dao.ICheckResultDetailDao;
import cn.com.yeexun.qualityTask.entity.CheckResult;
import cn.com.yeexun.qualityTask.entity.CheckResultCount;
import cn.com.yeexun.qualityTask.service.ICheckResultDetailService;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.FileUtil;
import cn.com.yeexun.utils.PropertyPlaceholder;

@Service
public class CheckResultDetailService extends BaseService<CheckResult> implements ICheckResultDetailService {
	
	private static final String ERROR_DATA_SELECT_SQL 
			= "SELECT * FROM check_result_detail where result_count_id = ?";
	
	private static final int START_WRITE_ROW = 9;
	
	private static final int MAX_ROW_NUM = 1048575;
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private ICheckResultDetailDao checkResultDetailDao;
	
	@Autowired
	private ICheckResultCountDao checkResultCountDao;
	
//	@Autowired
//	private DataSourceService datasourceService;
	
	@Autowired
	private DispatchHistoryService dispatchHistoryService;
	
	@Autowired
	private javax.sql.DataSource sysDbDataSource;
	
	@Autowired
	private DatasourceService2 datasourceService2;

	@Override
	public List<CheckResult> getCheckResultDetail(Long id, Page<CheckResult> page) {
		return checkResultDetailDao.getCheckResultByResultCountId(id, page);
	}
	
	@Override
	public int count(Long id) {
		return checkResultDetailDao.count(id);
	}
	
	@Override
//	public File exportToExcel(Long resultCountId) {
	public String exportToExcel(Long resultCountId, HttpServletRequest request) {
		
		CheckResultCount checkResultCount;
//		DesignSourceInfo designSourceInfo;
		DataSource dataSource;
		DispatchHistory dispatchHistory;
		try {
			checkResultCount = checkResultCountDao.selectByPrimaryKey(resultCountId);
//			designSourceInfo = designSourceInfoService.getById(checkResultCount.getDesignSourceId());
//			dataSource = datasourceService.getById(checkResultCount.getDatasourceId());
			dataSource = datasourceService2.getDatasourceById(checkResultCount.getDatasourceId());
			dispatchHistory = dispatchHistoryService.getById(checkResultCount.getTaskHisId());
		} catch (Exception e) {
			throw new CommonException(e.getMessage(), e);
		}
		int sheetNum = 1;
		SXSSFWorkbook wb = new SXSSFWorkbook();
		SXSSFSheet sheet = wb.createSheet(checkResultCount.getTableName() + "_" + sheetNum);
		sheetNum += 1;
		SXSSFRow row1 = sheet.createRow(1);
		SXSSFCell cell11 = row1.createCell(0);
		SXSSFCell cell12 = row1.createCell(1);
		cell11.setCellValue("所属数据源");
		cell12.setCellValue(dataSource.getDatasourceName());
		
		SXSSFRow row2 = sheet.createRow(2);
		SXSSFCell cell21 = row2.createCell(0);
		SXSSFCell cell22 = row2.createCell(1);
		cell21.setCellValue("数据表");
		cell22.setCellValue(checkResultCount.getTableName());
		
		SXSSFRow row3 = sheet.createRow(3);
		SXSSFCell cell31 = row3.createCell(0);
		SXSSFCell cell32 = row3.createCell(1);
		cell31.setCellValue("问题责任人");
		cell32.setCellValue(checkResultCount.getOwner());
		
		SXSSFRow row4 = sheet.createRow(4);
		SXSSFCell cell41 = row4.createCell(0);
		SXSSFCell cell42 = row4.createCell(1);
		cell41.setCellValue("稽核任务执行时间");
		cell42.setCellValue(SDF.format(dispatchHistory.getStartTime()));
		
		SXSSFRow row5 = sheet.createRow(5);
		SXSSFCell cell51 = row5.createCell(0);
		SXSSFCell cell52 = row5.createCell(1);
		cell51.setCellValue("疑问数据条数");
		cell52.setCellValue(checkResultCount.getErrorDataNum());
		
		SXSSFRow row7 = sheet.createRow(6);
		SXSSFCell cell71 = row7.createCell(0);
		cell71.setCellValue("问题数据详情：");
		
		// 写表头
		String columnNamesStr = checkResultCount.getColumnNames();
		List<String> columnNames = JSON.parseArray(columnNamesStr, String.class);
		SXSSFRow row8 = sheet.createRow(8);
		for (int i = 0; i < columnNames.size(); i++) {
			row8.createCell(i + 2).setCellValue(columnNames.get(i));
		}
		// 写数据
		Connection sysDBConn = null;
		try {
			sysDBConn = sysDbDataSource.getConnection();
		} catch (SQLException e) {
			try {
				wb.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw new CommonException("从数据库连接池中获取连接异常：", e);
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = sysDBConn.prepareStatement(ERROR_DATA_SELECT_SQL);
			ps.setFetchSize(Integer.MIN_VALUE);
			ps.setLong(1, resultCountId);
			rs = ps.executeQuery();
			int errorDataCount = 0;
			int i = 0;
			int startRow = START_WRITE_ROW;
			int currentRow = startRow;
			while (rs.next()) {
				JSONObject dataDetail = JSON.parseObject(rs.getString("data_detail"));
				currentRow = startRow + i * 2;
				SXSSFRow dataRow = sheet.createRow(currentRow);
				SXSSFRow errorMsgRow = sheet.createRow(currentRow + 1);
				dataRow.createCell(0).setCellValue(++errorDataCount);
				dataRow.createCell(1).setCellValue("原始数据");
				errorMsgRow.createCell(1).setCellValue("触发规则");
				for (int k = 0; k < columnNames.size(); k++) {
					dataRow.createCell(k + 2).setCellValue(
							dataDetail.getJSONObject(columnNames.get(k)).getString("value"));
					errorMsgRow.createCell(k + 2).setCellValue(
							dataDetail.getJSONObject(columnNames.get(k)).getString("errorMsgs"));
				}
				if (currentRow  + 1 >= MAX_ROW_NUM - 2) {
					// 新起一个sheet，写表头
					sheet = wb.createSheet(checkResultCount.getTableName() + "_" + sheetNum);
					sheetNum += 1;
					SXSSFRow headRow = sheet.createRow(0);
					for (int k = 0; k < columnNames.size(); k++) {
						headRow.createCell(k + 2).setCellValue(columnNames.get(k));
					}
					startRow = 1;
					i = 0;
				} else {
					i += 1;
				}
				
			}
		} catch (SQLException e) {
			try {
				wb.close();
			} catch (IOException e1) {
			}
			throw new CommonException("导出问题数据异常：", e);
		} finally {
			try {
				sysDBConn.close();
				ps.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		FileOutputStream fos = null;
		String tempFileName = "问题数据" + resultCountId + System.currentTimeMillis() + ".xlsx";
		String tempFileDir = FileUtil.getJarRootDir()
				+ PropertyPlaceholder.getContextProperty("errorDataTempDir");
		File tempDir = new File(tempFileDir);
		if (tempDir.exists()) {
			if (!tempDir.isDirectory()) {
				tempDir.delete();
				tempDir.mkdirs();
			}
		} else {
			tempDir.mkdirs();
		}
		
//	System.out.println("===========" + tempFileDir + tempFileName);	
		try {
			File file = new File(tempFileDir + tempFileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
		} catch (IOException e) {
			try {
				wb.close();
			} catch (IOException e1) {
			}
			throw new CommonException("创建问题数据临时文件异常：", e);
		}
		try {
			wb.write(fos);
			wb.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempFileName;
		// 下载文件
//		FileUtil.downloadFileToBrowser(tempFileDir + tempFileName, tempFileName, response);
		
	}
	
	public static void main(String[] args) {
//		/WEB-INF/errorData_tmp/问题数据511553758147289.xlsx
		File file = new File("/WEB-INF/问题数据511553758147289.xlsx");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
