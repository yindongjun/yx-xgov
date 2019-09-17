package cn.com.yeexun.documentManagement.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.documentManagement.dao.IDocumentFileDao;
import cn.com.yeexun.documentManagement.entity.DocumentFile;
import cn.com.yeexun.documentManagement.service.IDocumentFileService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.FileUtil;
import cn.com.yeexun.utils.PropertyPlaceholder;

@Service
public class DocumentFileService extends BaseService<DocumentFile> implements IDocumentFileService  {
	
	@Autowired
	public IDocumentFileDao documentFileDao;
	
	String documentContentUrl = PropertyPlaceholder
			.getContextProperty("documentContent.url");

	@Override
	public void deleteFile(Long id) throws Exception {
		DocumentFile dfEntity = this.getById(id);
		dfEntity.setIsdel(1);
		this.update(dfEntity);
	}

	@Override
	public Map<String, Object> updateFile(Long id, String name,
			Map<String, Object> map, HttpServletRequest request)
			throws Exception {
		
		List<Map<String, String>> TabList = new ArrayList<Map<String, String>>();
		String fileFormat = PropertyPlaceholder
				.getContextProperty("fileFormat");
		File newfile = null;
		try {
			// 将当前上下文初始化给 CommonsMutipartResolver（多部分解析器）
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			// 检查form中是否有enctype="multipart/form-data"
			if (multipartResolver.isMultipart(request)) {
				map = CodeUtil.getSuccessMap();
				// 将request变成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 获取multiRequest 中所有的文件名
				String delimiter = multiRequest.getParameter("delimiter");
				Iterator iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					Map<String, String> TabMap = new HashMap<String, String>();
					// 一次遍历所有文件
					MultipartFile file = multiRequest.getFile(iter.next()
							.toString());
					if (!fileFormat.contains(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1,
											file.getOriginalFilename().length()))) {
						map = CodeUtil.getErrorMap(file.getOriginalFilename()
								+ "文件格式不正确！");
						return map;
					}
					String fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
					DocumentFile oldFile = this.getById(id);
					if(!oldFile.getName().equals(fileName)){
						throw new CommonException("更新的文件名称需和原文件保持一致！");
					}
					String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1,file.getOriginalFilename().length());
					DocumentFile dfEntity = this.getById(id);
					if (file != null) {
						StringBuffer str = new StringBuffer();
						str.append(documentContentUrl).append(File.separator)
						.append(dfEntity.getFilePath());
						
						String path = FileUtil.getJarRootDir() + str.toString();
						
						// 上传
						System.out.println(path);
						newfile = new File(path, file.getOriginalFilename());
						// 上传前检查文件是否存在，存在就删除
						if (newfile.exists()) {
							newfile.delete();
						}
						file.transferTo(newfile);
						TabMap.put("tableName", file.getOriginalFilename());
						TabMap.put("destinationFileDelimeter", delimiter);
						TabMap.put("destinationAddress",
								path + "/" + file.getOriginalFilename());
						TabMap.put("destinationFileType", fileType);

						TabList.add(TabMap);
					}
					dfEntity.setLastModifyTime(new Date());
					dfEntity.setName(fileName);
					dfEntity.setType(fileType);
					this.save(dfEntity);
					break;
				}
				map.put(CodeUtil.RESULT_DATA, TabList);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
			map = CodeUtil.getSystemErrorMap();
		} catch (IOException e) {
			e.printStackTrace();
			map = CodeUtil.getSystemErrorMap();
		}
		return map;
	}
	
//	public String getPath(StringBuffer str,Long id) throws Exception {
//		//StringBuffer str = new StringBuffer();
//		DocumentContent dcEntity = this.getById(id);
//		if (dcEntity.getParentId() > 0) {
//			str.append(dcEntity.getName()).append(",");
//			getPath(str,dcEntity.getParentId());
//		}else{
//			str.append(dcEntity.getName()).append(",");
//			return str.toString();
//		}
//		return str.toString();
//	}

	@Override
	public void downLoadFile(long id, HttpServletResponse resp)
			throws Exception {
		//String downLoadFilePath = 
		
		DocumentFile dfEntity = this.getById(id);
		StringBuffer str = new StringBuffer();
		str.append(documentContentUrl).append(File.separator).append(dfEntity.getFilePath());
		String downLoadFilePath = FileUtil.getJarRootDir() + str.toString();
		String fileName = dfEntity.getName();
		String fileType = null;
		if(dfEntity.getType().equals("doc")){
			fileType = ".doc";
		}else if(dfEntity.getType().equals("docx")){
			fileType = ".docx";
		}else{
			fileType = ".pdf";
		}
		String filePath = downLoadFilePath + System.getProperty("file.separator") + fileName + fileType;
		FileUtil.downloadFileToBrowser(filePath, fileName + fileType, resp);
	}

	@Override
	public int getAllFileNum() throws Exception {
		int num = 0;
		List<DocumentFile> fileList =  documentFileDao.getAllFileNum();
		if(fileList != null && fileList.size() > 0){
			num = fileList.size();
		}
		return num;
	}

	@Override
	public boolean isExistFile(String name, String type,Long contentId) throws Exception {
		boolean flag = false;
		List<DocumentFile> documentFile = documentFileDao.getFileByNameAndType(name, type,contentId);
		if(documentFile != null && documentFile.size() > 0){
			flag = true;
		}
		return flag;
	}

	@Override
	public List<Integer> getFileNumByNowTime() throws Exception {
		List<Integer> document = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0);
		Date d = calendar.getTime();
		Integer num = documentFileDao.getFileNumByNowTime(d);
		document.add((num+1)*120);
		for(int i=0;i<3;i++){
			calendar.add(Calendar.DATE, -7);
			Date d1 = calendar.getTime();
			Integer num1 = documentFileDao.getFileNumByNowTime(d1);
			document.add((num1+1)*120);
		}
		return document;
	}
	
	

}
