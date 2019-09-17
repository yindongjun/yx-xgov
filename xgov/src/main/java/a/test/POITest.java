package a.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONObject;

public class POITest {

	public static void main(String[] args) throws IOException, InvalidFormatException {
//		
//		JSONObject jsonObj = new JSONObject();
//		jsonObj.put("id", 1);
//		jsonObj.put("name", "zhangsan");
//		Set<String> keySet = jsonObj.keySet();
//		System.out.println(keySet);
		
		testReadExcel();
		
	}
	
	public static void testWriteExcel() throws IOException {
		SXSSFWorkbook wb = new SXSSFWorkbook();
		SXSSFSheet sheet = wb.createSheet("sheet-1");
		SXSSFRow row = sheet.createRow(0);
		SXSSFCell cell = row.createCell(0);
		cell.setCellValue("hello,你好！");
		FileOutputStream fos = new FileOutputStream(new File("E:\\testpoi.xlsx"));
		wb.write(fos);
		wb.close();
		fos.close();
	}
	
	public static void testReadExcel() throws InvalidFormatException, IOException {
//		FileInputStream fis = new FileInputStream(new File("E:\\testpoi.xlsx"));
		XSSFWorkbook xssfwb = new XSSFWorkbook(new File("E:\\testpoi.xlsx"));
		XSSFSheet sheet = xssfwb.getSheetAt(0);
		XSSFRow row = sheet.getRow(0);
		XSSFCell cell = row.getCell(0);
		String value = cell.getStringCellValue();
		System.out.println(value);
		xssfwb.close();
	}

}
