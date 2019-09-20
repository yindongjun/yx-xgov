package cn.com.yeexun.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;


public class FileUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	  * 保存命令到指定文件夹下
	 * @param cmd 		命令
	 * @param path 		路径
	 */
	public static void saveFile(String cmd,String path){
		
		if(StringUtils.isBlank(cmd) || StringUtils.isBlank(path)){
			throw new CommonException("参数异常");
		}
		
		//若文件夹不存在，则创建文件夹
		File writeFile = new File(path);
		if(!writeFile.getParentFile().exists()){
			writeFile.getParentFile().mkdirs();
		}
		
		FileOutputStream fos = null;
		FileChannel  write = null;
		try {
			fos = new FileOutputStream(writeFile);
			write = fos.getChannel();
			ByteBuffer src = Charset.forName("UTF-8").encode(cmd); 
			write.write(src);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException("文件保存异常");
		} finally{
			try {
				if(null != fos){
					fos.close();
				}
				if(null != write){
					write.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new CommonException("通道关闭异常");
			}
			
		}
	}
	
	
	/**
	 * 
	 * 复制并删除原文件
	 * @param path		原文件path
	 * @param copyPath	复制路径
	 */
	public static void copyAndDel(String path,String copyPath){
		
		if(StringUtils.isBlank(path) || StringUtils.isBlank(copyPath)){
			throw new CommonException("参数异常");
		}
		
		
		//若文件夹不存在，则创建文件夹
		File writeFile = new File(copyPath);
		if(!writeFile.getParentFile().exists()){
			writeFile.getParentFile().mkdirs();
		}
		
		FileChannel inputChannel = null;    
	    FileChannel outputChannel = null;
		File file = null;
		try {
			inputChannel = new FileInputStream(path).getChannel();
			outputChannel = new FileOutputStream(copyPath).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException("复制文件异常");
		} finally{
			try {
				if(inputChannel != null){
					inputChannel.close();
				}
				if(outputChannel != null){
					outputChannel.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new CommonException("关闭通道异常");
			}
		}
		
		//删除文件
		/*file = new File(path);
		file.delete();*/
		
	}
	
	
	/**
	 * 读取指定路径文件并返回字符串
	 * @param filePath
	 * @return
	 */
	public static String loadFile(String filePath){
		
		if(StringUtils.isBlank(filePath)){
			throw new CommonException("参数异常");
		}
		
		StringBuilder sb = new StringBuilder();
		
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
			if(null == lines || lines.size() <1){
				return "";
			}else{
				for(String line : lines){
					sb.append(line+"\n");
				}
				return sb.toString();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException("读取文件异常");
		}
	}
	
	/**
	 * 追加内容到文件种
	 * @param content
	 * @param path
	 */
	public static void saveFileAppend(String content, String path){
		
		if(StringUtils.isBlank(content) || StringUtils.isBlank(path)){
			throw new CommonException("文件内容或文件路径为空，请核查");
		}
		
		//若文件夹不存在，则创建文件夹
		File writeFile = new File(path);
		if(!writeFile.getParentFile().exists()){
			writeFile.getParentFile().mkdirs();
		}
		
		BufferedWriter out = null;     
	    try {     
	         out = new BufferedWriter(new OutputStreamWriter(     
	                  new FileOutputStream(path, true)));     
	                 out.write(content);     
	        } catch (Exception e) {     
	            e.printStackTrace();     
	        } finally {     
	            try {     
	                out.close();     
	            } catch (IOException e) {     
	                e.printStackTrace();     
	            }     
	        }     
//		FileOutputStream fos = null;
//		FileChannel  write = null;
//		try {
//			fos = new FileOutputStream(writeFile);
//			write = fos.getChannel();
//			ByteBuffer src = Charset.forName("UTF-8").encode(cmd); 
//			write.write(src);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new CommonException("文件保存异常");
//		} finally{
//			try {
//				if(null != fos){
//					fos.close();
//				}
//				if(null != write){
//					write.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new CommonException("通道关闭异常");
//			}
//			
//		}
	}
	
	
	public static void main(String[] args){
		/*saveFile("哈哈哈","G:\\dataToolsFiles\\shell\\buff\\3.txt");
		
		String a = loadFile("G:\\dataToolsFiles\\shell\\buff\\3.txt");
		System.out.println(a);*/
		
		
		
		//copyAndDel("G:\\1.txt","G:\\文档\\test.sh");
		
		
		/*String a = "{\"id\":\"1\",\"name\":\"ds\",\"etlList\":[{\"id\":\"1\",\"step\":\"22\"},{\"id\":\"2\",\"step\":\"33\"}]}";
		FinalWorkFlowVO vo = JSON.parseObject(a, FinalWorkFlowVO.class);
		System.out.println(vo.getName());*/
		
		
//		saveFileAppend("abc", "D:\\logs\\77.log");
		
	}
	
	/**
	 * 读取指定路径文件并返回字符串
	 * @param filePath 文件路径 包含文件名称
	 * @param fileName 下载下来的文件名称
	 * @return
	 * @throws Exception 
	 */
	public static void downloadFileToBrowser(String filePath, String fileName, HttpServletResponse response) throws Exception{
		
		FileInputStream in = null;
		BufferedOutputStream out = null;
		
		try {
			response.setContentType("application/force-download");// 应用程序强制下载
			if (StringUtils.isNotBlank(filePath)) {
//				fileName = new String(fileName.getBytes("iso8859-1"), "UTF-8");
				// 通过文件名找出文件的所在目录
				// 得到要下载的文件
				File file = new File(filePath);
				// 如果文件不存在
				if (!file.exists()) {
					throw new Exception("文件不存在，请重试！");
				}
				// 设置响应头，控制浏览器下载该文件
				response.setHeader(
						"content-disposition",
						"attachment;filename="
								+ URLEncoder.encode(fileName, "UTF-8"));
				// 读取要下载的文件，保存到文件输入流
				in = new FileInputStream(filePath);
				// 创建输出流
				out = new BufferedOutputStream(response.getOutputStream());
				// 创建缓冲区
				byte buffer[] = new byte[1024];
				int len = 0;
				// 循环将输入流中的内容读取到缓冲区当中
				while ((len = in.read(buffer)) > 0) {
					// 输出缓冲区的内容到浏览器，实现文件下载
					out.write(buffer, 0, len);
				}
				out.flush(); // 不可少
				response.flushBuffer();// 不可少
			}else{
				throw new CommonException("路径错误！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("下载失败，请重试！");
		} finally {
			try {
				in.close();
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getJarRootDir() {
		ApplicationHome applicationHome = new ApplicationHome();
		File dir = applicationHome.getDir();
		LOGGER.debug("jar所在目录为：" + dir.getAbsolutePath());
		return dir.getAbsolutePath();
//		return "/opt/xgov-0.0.1-SNAPSHOT/";
	}
	
	

}
