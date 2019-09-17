package cn.com.yeexun.documentManagement.service.impl;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.documentManagement.dao.IDocumentContentDao;
import cn.com.yeexun.documentManagement.dao.IDocumentFileDao;
import cn.com.yeexun.documentManagement.entity.DocumentContent;
import cn.com.yeexun.documentManagement.entity.DocumentFile;
import cn.com.yeexun.documentManagement.service.IDocumentContentService;
import cn.com.yeexun.documentManagement.service.IDocumentFileService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.FileUtil;
import cn.com.yeexun.utils.PropertyPlaceholder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class DocumentContentService extends BaseService<DocumentContent> implements IDocumentContentService {

	
	public static Map<String,Object> mapArray = new LinkedHashMap<String, Object>();
	public List<DocumentContent> menuCommon; 
	
	@Autowired
	public IDocumentFileService documentFileService;
	@Autowired
	public IDocumentContentDao documentContentDao;
	@Autowired
	public IDocumentFileDao documentFileDao;
//	String documentContentUrl = PropertyPlaceholder
//			.getContextProperty("documentContent.url");
	
	
	@Override
	public List<Object> getMenuDocumentContent() throws Exception {
		List<Object> lists = new ArrayList<Object>();
		lists = menuList(documentContentDao.getAllContent(0));
		return lists;
	}
	
	public List<Object> menuList(List<DocumentContent> menu){  
		List<Object> list = new ArrayList<Object>();
		this.menuCommon = menu;
	    for (DocumentContent x : menu) {   
	      if(x.getParentId() == -1){ 
	    	x.setChildren(menuChild(x.getId()));
	        list.add(x); 
	      } 
	    }   
	    return list; 
	  }
	
	public List<DocumentContent> menuChild(long id){ 
	    List<DocumentContent> lists = new ArrayList<DocumentContent>(); 
	    for(DocumentContent a:menuCommon){ 
	      if(a.getParentId() == id){ 
	    	a.setChildren(menuChild(a.getId()));
	    	//a.getChildren()
	    	DocumentContent dcEnyity = new DocumentContent();
	    	dcEnyity.setId(a.getId());
	    	dcEnyity.setIsdel(a.getIsdel());
	    	dcEnyity.setCreateTime(a.getCreateTime());
	    	dcEnyity.setLastModifyTime(a.getLastModifyTime());
	    	dcEnyity.setName(a.getName());
	    	dcEnyity.setParentId(a.getParentId());
	    	if(a.getChildren().size() > 0){
	    		dcEnyity.setChildren(a.getChildren());
	    	}
	        lists.add(dcEnyity); 
	      } 
	    } 
	    return lists; 
	  }
	
	@Override
	public void addDocumentContent(Long id, Long parentId, String name,
			Long userId) throws Exception {
		
		StringBuffer url = new StringBuffer();
		StringBuffer url1 = new StringBuffer();
		String documentContentUrl = FileUtil.getJarRootDir() 
				+ PropertyPlaceholder.getContextProperty("documentContent.url");
		if(parentId>0){
			url.append(documentContentUrl).append(File.separator);
			String[] item = getPath(url1,parentId).split(",");
			//StringBuffer str = new StringBuffer();
			for(int i=item.length-1;i>=0;i--){
				url.append(item[i]).append(File.separator);
			}
			url.append(name);
			//url.deleteCharAt(url.length()-1);
		}else{
			url.append(documentContentUrl).append(File.separator)
			.append(name);
		}
		File writeFile = new File(url.toString());
		if(!writeFile.exists() && !writeFile.isDirectory()){
			writeFile.mkdirs();
		}

		if(null==id){
			DocumentContent dcEntity = new DocumentContent();
			dcEntity.setParentId(parentId);
			dcEntity.setIsdel(0);
			dcEntity.setName(name);
			dcEntity.setCreateTime(new Date());
			//dcEntity.setUserId(userId);
			this.insert(dcEntity);
		}else{
			DocumentContent dcEntity = getById(id);
			dcEntity.setName(name);
			//dcEntity.setUserId(userId);
			this.update(dcEntity);
		}
		/*List<DocumentContent> lists = documentContentDao.getAllContentByPid(1,parentId);
		  if(lists != null && lists.size()>0){
			for(DocumentContent dc:lists){
				if(name.equals(dc.getName()) && parentId == dc.getParentId()){
					dc.setIsdel(0);
					this.update(dc);
				}
			}
		} else {
			DocumentContent dcEntity = new DocumentContent();
			dcEntity.setParentId(parentId);
			dcEntity.setIsdel(0);
			dcEntity.setName(name);
			dcEntity.setCreateTime(new Date());
			//dcEntity.setUserId(userId);
			this.insert(dcEntity);
		}*/
			
	}
	
	
	//向上找所有的路径
	@Override
	public String getFullPath(Long id) throws Exception {
//		//String[] item = getPath(id).split(",");
//		StringBuffer str = new StringBuffer();
//		for(int i=item.length-1;i>=0;i--){
//			str.append(item[i]);
//		}
		return null;
	}

	public String getPath(StringBuffer str,Long id) throws Exception {
		//StringBuffer str = new StringBuffer();
		DocumentContent dcEntity = this.getById(id);
		if (dcEntity.getParentId() > 0) {
			str.append(dcEntity.getName()).append(",");
			getPath(str,dcEntity.getParentId());
		}else{
			str.append(dcEntity.getName()).append(",");
			return str.toString();
		}
		return str.toString();
	}
	
	
	@Override
	public Map<String, Object> upload(Long id,Map<String, Object> map,
			HttpServletRequest request) throws Exception {
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
				Iterator iter1 = multiRequest.getFileNames();
				while (iter1.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter1.next()
							.toString());
					if ( ! fileFormat.contains(
							file.getOriginalFilename().substring(
											file.getOriginalFilename().lastIndexOf(".") + 1,
											file.getOriginalFilename().length()) ) ) {
						map = CodeUtil.getErrorMap(file.getOriginalFilename()
								+ "文件格式不正确！");
						return map;
					}
					String fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf("."));
					String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1,file.getOriginalFilename().length());
					boolean b = documentFileService.isExistFile(fileName, fileType,id);
					if(b){
						map = CodeUtil.getErrorMap(file.getOriginalFilename()+"文件库里已存在，请重新选择文件上传！");
						return map;
					}
				}
				
				
				
				while (iter.hasNext()) {
					StringBuffer str1 = new StringBuffer();
					Map<String, String> TabMap = new HashMap<String, String>();
					// 一次遍历所有文件
					MultipartFile file = multiRequest.getFile(iter.next()
							.toString());
//					if (!fileFormat.contains(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1,
//											file.getOriginalFilename().length()))) {
//						map = CodeUtil.getErrorMap(file.getOriginalFilename()
//								+ "文件格式不正确！");
//						return map;
//					}
					//根据最后一个点分割文件，作为文件名和文件类型
					String fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
					String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1,file.getOriginalFilename().length());
						if (file != null) {
							StringBuffer str = new StringBuffer();
							StringBuffer str2 = new StringBuffer();
							DocumentContent dcEntity = this.getById(id);
							long parentId = dcEntity.getParentId();
							String documentContentUrl = FileUtil.getJarRootDir() 
									+ PropertyPlaceholder.getContextProperty("documentContent.url");
							File importDir = new File(documentContentUrl);
							if (importDir.exists()) {
								if (!importDir.isDirectory()) {
									importDir.delete();
									importDir.mkdirs();
								}
							} else {
								importDir.mkdirs();
							}
							
							if(parentId>0){
								str.append(documentContentUrl).append(File.separator);
								String[] item = getPath(str2,parentId).split(",");
								//StringBuffer str = new StringBuffer();
								for(int i=item.length-1;i>=0;i--){
									str.append(item[i]).append(File.separator);
									str1.append(item[i]).append(File.separator);
								}
								str.append(dcEntity.getName());
								str1.append(dcEntity.getName());
							}else{
								str.append(documentContentUrl).append(File.separator)
								.append(dcEntity.getName());
								str1.append(dcEntity.getName());
							}
							String path = str.toString();
							
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
							DocumentFile dfEntity = new DocumentFile();
							dfEntity.setName(fileName);
							dfEntity.setType(fileType);
							dfEntity.setIsdel(0);
							dfEntity.setContentId(id);
							dfEntity.setFilePath(str1.toString());
							dfEntity.setCreateTime(new Date());
							documentFileService.insert(dfEntity);
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
	
	List<DocumentContent> childMenu = new ArrayList<DocumentContent>();
	
	public List<DocumentContent> treeMenuList(List<DocumentContent> menuList,Long id){
		for(DocumentContent dc:menuList){
			if(dc.getParentId() == id){
				treeMenuList(menuList,dc.getId());
				childMenu.add(dc);
				//menuList.remove(dc);
			}
		}
		return childMenu;
	}

	@Override
	public List<DocumentContent> getLowerMenu(Long id) throws Exception {
		List<DocumentContent> lists = new ArrayList<DocumentContent>();
		List<DocumentContent> lowerList = new ArrayList<DocumentContent>();
		lists = documentContentDao.getAllContent(0);
		lowerList = treeMenuList(lists,id);
		lowerList.add(this.getById(id));
		return lowerList;
	}

	@Override
	public void deleteMenu(Long id) throws Exception {
		List<DocumentContent> lists = getLowerMenu(id);
		Set<Long> idsSet = new HashSet<Long>();
		for(DocumentContent dc:lists){
			idsSet.add(dc.getId());
		}
		List<Long> ids = new ArrayList<Long>(idsSet);
		//this.delete(ids);
	}

	@Override
	public void deleteFolder(Long id) throws Exception {
		List<DocumentContent> lists = getLowerMenu(id);
		List<DocumentFile> listFile = documentFileDao.getAllFileByIsdel(0);
		Set<Long> idsSet = new HashSet<Long>();
		for(DocumentContent dc:lists){
			idsSet.add(dc.getId());
			dc.setIsdel(1);
			this.update(dc);
		}
		List<Long> idList = new ArrayList<Long>(idsSet);
		//逻辑删除文件夹下的文件
		for(Long ids:idList){
			for(DocumentFile df:listFile){
				if(ids == df.getContentId()){
					df.setIsdel(1);
					documentFileService.update(df);
				}
			}
		}
	}

	@Override
	public List<DocumentFile> getDocumentFile(Long contentId, Page<DocumentFile> page) {
		return documentFileDao.getAllFilePage(0, contentId, page);
	}

	@Override
	public boolean isExistContent(Long parentId, String name) throws Exception {
		boolean flag = false;
		DocumentContent documentContent =   documentContentDao.getAllContentByName(parentId,name);
		if(documentContent != null && StringUtils.isNotBlank(documentContent.getName())){
			flag = true;
		}
		return flag;
	}
	
	
	
}
