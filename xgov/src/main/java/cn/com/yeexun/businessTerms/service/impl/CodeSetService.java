package cn.com.yeexun.businessTerms.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.businessTerms.dao.ICodeDao;
import cn.com.yeexun.businessTerms.dao.ICodeSetDao;
import cn.com.yeexun.businessTerms.entity.Code;
import cn.com.yeexun.businessTerms.entity.CodeSet;
import cn.com.yeexun.businessTerms.service.ICodeService;
import cn.com.yeexun.businessTerms.service.ICodeSetService;
import cn.com.yeexun.dataElement.dao.IDataElementDao;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.meta_data.entity.Metadata;
import cn.com.yeexun.meta_data.service.IMetadataService;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import cn.com.yeexun.utils.FileUtil;
import cn.com.yeexun.utils.PropertyPlaceholder;
import cn.com.yeexun.utils.VerifyException;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class CodeSetService extends BaseService<CodeSet> implements
		ICodeSetService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CodeSetService.class);
//	public List<CodeSet> menuCommon; 
	
	private static final int EXCEL_READ_START_ROW = 8;
	
	@Autowired
	private ICodeSetDao codeSetDao;
	
	@Autowired
	private ICodeDao codeDao;
	
	@Autowired
	private IMetadataService metadataService;
	
	@Autowired
	private ICodeService codeService;
	
	@Autowired
	private IDataElementDao dataElementDao;
	
	@Override
	public List<CodeSet>  getCodeSet() throws Exception{
		List<CodeSet> lists = new ArrayList<CodeSet>();
		lists = menuList(codeSetDao.getAllCodeSet(0));
		return lists;
	}
	
	public List<CodeSet> menuList(List<CodeSet> menu){  
//		this.menuCommon = menu;
		List<CodeSet> list = new ArrayList<CodeSet>();
	    for (CodeSet x : menu) {
	     // Map<String,Object> mapArr = new LinkedHashMap<String, Object>(); 
	      if(x.getParentId() == -1){
	    	  
	    	 x.setChildren(menuChild(x.getId(), menu));
	    	 list.add(x);
//	        mapArr.put("id", x.getId()); 
//	        mapArr.put("name", x.getName());  
//	        mapArr.put("parentId", x.getParentId());  
//	        mapArr.put("isCodelist", x.getIsCodeset());
//	        mapArr.put("createTime", x.getCreateTime());
//	        mapArr.put("lastModifyTime", x.getLastModifyTime());
//	        mapArr.put("explanation", x.getExplanation());
//	        mapArr.put("express", x.getExpress());
//	        mapArr.put("codeRule", x.getCodeRule());
//	        mapArr.put("children", menuChild(x.getId()));  
//	        list.add(mapArr); 
	      } 
	    }   
	    return list; 
	  }
	
	private List<CodeSet> menuChild(long id, List<CodeSet> menu){ 
	    List<CodeSet> lists = new ArrayList<CodeSet>(); 
	    for(CodeSet a:menu){ 
	      //Map<String,Object> childArray = new LinkedHashMap<String, Object>(); 
	      if(a.getParentId() == id){ 
//	        childArray.put("id", a.getId()); 
//	        childArray.put("name", a.getName()); 
//	        childArray.put("parentId", a.getParentId()); 
//	        childArray.put("isCodelist", a.getIsCodeset());
//	        childArray.put("createTime", a.getCreateTime());
//	        childArray.put("lastModifyTime", a.getLastModifyTime());
//	        childArray.put("explanation", a.getExplanation());
//	        childArray.put("express", a.getExpress());
//	        childArray.put("codeRule", a.getCodeRule());
//	        childArray.put("children", menuChild(a.getId()));
	    	if(a.getIsCodeset().equals("1")){
	    		a.setChildren(menuChild(a.getId(), menu));
	    	}
	        lists.add(a); 
	      } 
	    } 
	    return lists; 
	  }

	@Override
	public void updateAndAddCodeSet(CodeSet codeSetEntity) throws Exception {
		if (codeSetEntity.getId() > 0) {  // 编辑
			// 判断元数据标中是否存在这个代码集的信息，如果存在，要更新
			Metadata example = new Metadata();
			example.setMetamodelId(3L);
			example.setParentCode("codeSet^" + codeSetEntity.getId());
			example.setDeleteFlag(Constant.NOT_DELETE);
			List<Metadata> metadatas = metadataService.search(example);
			if (metadatas != null && metadatas.size() == 1) {
				Metadata metadata = metadatas.get(0);
				metadata.setName(codeSetEntity.getName());
				metadataService.save(metadata);
			}
		}
		save(codeSetEntity);
	}

	@Override
	public List<CodeSet> getLowerMenu(Long id) throws Exception {
		List<CodeSet> lists = new ArrayList<CodeSet>();
		List<CodeSet> lowerList = new ArrayList<CodeSet>();
		lists = codeSetDao.getAllCodeSet(0);
		lowerList = treeMenuList(lists,id);
		lowerList.add(this.getById(id));
		return lowerList;
	}
	
//	List<CodeSet> childMenu = new ArrayList<CodeSet>();
	
	public List<CodeSet> treeMenuList(List<CodeSet> menuList,Long id){
		List<CodeSet> childMenu = new ArrayList<CodeSet>();
		for(CodeSet cs:menuList){
			if(cs.getParentId() == id){
				treeMenuList(menuList,cs.getId());
				childMenu.add(cs);
				//menuList.remove(dc);
			}
		}
		return childMenu;
	}
/*
	@Override
	public void deleteCodeSets(Long id) throws Exception {
		List<CodeSet> lists = getLowerMenu(id);
		List<Code> listCode = codeDao.getAllCode(0);
		Set<Long> idsSet = new HashSet<Long>();
		for(CodeSet cs:lists){
			idsSet.add(cs.getId());
			cs.setIsdel(1);
			this.update(cs);
			String uniqueCode = cs.getUniqueCode();
			Metadata meta = new Metadata();
			meta.setCode(cs.getCode());
			meta.setParentCode("codeSet^" + cs.getId());
			meta.setMetamodelId(3L);
			List<Metadata> codeSets = metadataService.getMetaByUniqueCode(meta);
			List<Long> ids = new ArrayList<Long>();
			for (Metadata metadata : codeSets) {
				ids.add(metadata.getId());
			}
			if (ids != null && ids.size() > 0) {
				metadataService.deleteList(ids);
			}
			
		}
		List<Long> idList = new ArrayList<Long>(idsSet);
		//逻辑删除代码集下的代码
		for(Long ids:idList){
			for(Code code:listCode){
				if(ids == code.getCodesetId()){
					code.setIsdel(1);
					codeDao.updateByPrimaryKey(code);
				}
			}
		}
		//删除元数据中的数据集  TODO
		
	}
	*/
	
	/**
	 * 获取当前目录及其子目录下的所有代码。
	 * @param id
	 * @return
	 */
	private void getCodes(Long id, List<Code> codes) {
		
		// 先判断该目录下是否有代码
		Example example = new Example(Code.class);
//		List<Integer> statusList = new ArrayList<>();
//		statusList.add(1);
//		statusList.add(2);
//		statusList.add(0);
//		statusList.add(4);
		example.createCriteria().andEqualTo("isdel", 0).andEqualTo("codesetId", id);
		List<Code> currentDirCodes = codeDao.selectByExample(example);
		codes.addAll(currentDirCodes);
		// 获取该层下面的所有子目录，并判断是否所有目录均为空目录。
		List<CodeSet> codeSets = codeSetDao.getByParentId(id);
		if (codeSets != null && codeSets.size() > 0) {
			for (CodeSet codeSet : codeSets) {
				getCodes(codeSet.getId(), codes);
			}
		}
		
	}
	
	@Override
	public void deleteCodeSets(Long id) throws Exception {
		
		// 先判断该目录及其子目录是否都是空目录
		List<Code> codes = new ArrayList<>();
		getCodes(id, codes);
		if (codes.size() > 0) {
			throw new CommonException("该目录下存在非空的代码集，先删除代码！");
		}
		List<CodeSet> lists = getLowerMenu(id);
		List<Code> listCode = codeDao.getAllCode(0);
		Set<Long> idsSet = new HashSet<Long>();
		for(CodeSet cs:lists){
			idsSet.add(cs.getId());
			cs.setIsdel(1);
			this.update(cs);
			String uniqueCode = cs.getUniqueCode();
			Metadata meta = new Metadata();
			meta.setCode(cs.getCode());
			meta.setParentCode("codeSet^" + cs.getId());
			meta.setMetamodelId(3L);
			List<Metadata> codeSets = metadataService.getMetaByUniqueCode(meta);
			List<Long> ids = new ArrayList<Long>();
			for (Metadata metadata : codeSets) {
				ids.add(metadata.getId());
			}
			if (ids != null && ids.size() > 0) {
				metadataService.deleteList(ids);
			}
			
		}
		List<Long> idList = new ArrayList<Long>(idsSet);
		//逻辑删除代码集下的代码
		for(Long ids:idList){
			for(Code code:listCode){
				if(ids == code.getCodesetId()){
					code.setIsdel(1);
					codeDao.updateByPrimaryKey(code);
				}
			}
		}
		//删除元数据中的数据集  TODO
		
	}
	

	@Override
	public CodeSet echoCodeSet(Long id) throws Exception {
		CodeSet codeSet = codeSetDao.getCodeSet(0, id);
		return codeSet;
	}

	@Override
	public boolean isDuplicate(CodeSet codeSetEntity) throws Exception {
		boolean b = false;
		if(StringUtils.isNotBlank(codeSetEntity.getName())){
			List<CodeSet> lists = codeSetDao.getCodeSetList(codeSetEntity.getName(),codeSetEntity.getCode(),codeSetEntity.getParentId(),codeSetEntity.getIsCodeset());
			if(lists != null && lists.size() > 0) {
				if(1==lists.size() && lists.get(0).getId() == codeSetEntity.getId()){
					b = false;
				}else{
					b = true;
				}
			}
		} else {
			throw new VerifyException("代码集名称或者代码为空！");
		}
		/*if(StringUtils.isNotBlank(codeSetEntity.getName()) && StringUtils.isNotBlank(codeSetEntity.getCode())){
			List<CodeSet> lists = codeSetDao.getCodeSetList(codeSetEntity.getName(),codeSetEntity.getCode(),codeSetEntity.getParentId(),codeSetEntity.getIsCodeset());
			if(lists != null && lists.size() > 0) {
				b = true;
			}
		} else {
			throw new VerifyException("代码集名称或者代码为空！");
		}*/
		return b;
	}

	@Override
	public int getAllCodeSetNum() throws Exception {
		int num = 0;
		List<CodeSet> codeSetList = codeSetDao.getAllCodeSetNum();
		if(codeSetList != null && codeSetList.size() > 0){
			num = codeSetList.size();
		}
		return num;
	}

	@Override
	@Transactional
	public void importFromExcel(File file, Integer parentId) {
		// 读取Excel文件
		XSSFWorkbook xssfwb = null;
		Map<CodeSet, List<Code>> codesets = null;
		try {
			try {
				xssfwb = new XSSFWorkbook(file);
			} catch (InvalidFormatException | IOException e) {
				throw new CommonException("读取数据集excel文件异常：", e);
			}
			XSSFSheet sheet = xssfwb.getSheetAt(0);
			String valueOfA2 = sheet.getRow(1).getCell(0).getStringCellValue();
			if (!"代码集列表".equals(valueOfA2.trim())) {
				throw new CommonException("Excel文件格式异常，请下载正确的模板填写后重新上传！");
			}
			// 获取到最后一行的行号
			int lastRowNum = sheet.getLastRowNum();
//			System.out.println("lastRowNum:" + lastRowNum);
			codesets = new LinkedHashMap<>();
			for (int i = EXCEL_READ_START_ROW; i <= lastRowNum; ) {
				XSSFRow row = sheet.getRow(i);
				// 获取一行有多少列
				int physicalNumberOfCells = row.getPhysicalNumberOfCells();
//				System.out.println(physicalNumberOfCells);
				List<Code> codes = new ArrayList<>();
				String codesetName = null;
				CodeSet codeSet = null;
				int mergeRows = 1;
				for (int j = 0; j < physicalNumberOfCells; j++) {
					// 判断是否有合并单元格
					Map<String, Integer> map = isMergedRegion(sheet, i, j);
					if (map != null && j == 0) {  // 当前这个单元格是合并单元格，并且j == 0，可以判断出当前单元格是"代码集名称"
						row.getCell(j).setCellType(CellType.STRING);
						codesetName = row.getCell(j).getStringCellValue();
						// 用于读取下一行时，跳过合并的行数。
						mergeRows = map.get("lastRow") - map.get("firstRow") + 1;
					} else if (/*map != null && */j == 1) {  // 这个单元格是"编码规则"
						codeSet = new CodeSet();
						codeSet.setName(codesetName);
						row.getCell(j).setCellType(CellType.STRING);
						String rule = row.getCell(j).getStringCellValue();
						codeSet.setCodeRule(rule);
						codesets.put(codeSet, codes);
//						System.out.println(JSON.toJSONString(codesets));
					} else if (map == null && j == 0) {  // 也有可能代码集下只有一个代码，就不存在合并行，j == 0判断是"代码集名称"
						row.getCell(j).setCellType(CellType.STRING);
						codesetName = row.getCell(j).getStringCellValue();
//						mergeRows = map.get("lastRow") - map.get("firstRow") + 1;
					} else {
						// 读取代码列表内容
						for (int k = 0; k < mergeRows; k++) {
							Code code = new Code();
							XSSFRow codeRow = sheet.getRow(i + k);
							codeRow.getCell(j).setCellType(CellType.STRING);
							String codeStr = codeRow.getCell(j).getStringCellValue();
							code.setCode(codeStr);
							codeRow.getCell(++j).setCellType(CellType.STRING);
							String codeName = codeRow.getCell(j).getStringCellValue();
							code.setName(codeName);
							codesets.get(codeSet).add(code);
							j--;
						}
						j = physicalNumberOfCells;
					}
				}
				// 读完了一个代码集和代码集下所有的代码，读取下一个代码集
				i += mergeRows;
			}
		} catch (Exception e) {
			if (e instanceof CommonException) {
				throw e;
			} else {
				throw new CommonException("导入Excel异常！", e);
			}
			
		} finally {
			try {
				xssfwb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (codesets != null && codesets.size() > 0) {
			for (Map.Entry<CodeSet, List<Code>> eachCodeset : codesets.entrySet()) {
				CodeSet codeset = eachCodeset.getKey();
				codeset.setParentId(parentId);
				codeset.setCreateTime(new Date());
				codeset.setLastModifyTime(new Date());
				codeset.setIsCodeset(String.valueOf(0));
				codeset.setIsdel(Integer.parseInt(Constant.NOT_DELETE));
				// 判断该目录下的代码集名称是否重复
				boolean b;
				try {
					b = isDuplicate(codeset);
				} catch (Exception e) {
					throw new CommonException(e.getMessage(), e);
				}
				if(b) {
					throw new CommonException("代码名称或者代码重复！");
				}
				codeSetDao.insert(codeset);
				// 更新uniqueCode
				codeset.setUniqueCode("codeset^" + codeset.getId());
				codeSetDao.updateByPrimaryKeySelective(codeset);
				List<Code> codes = eachCodeset.getValue();
				for (Code code : codes) {
					code.setCodesetId(codeset.getId());
					code.setStatus(String.valueOf(Code.STATUS_DRAFT));
					code.setIsdel(Integer.parseInt(Constant.NOT_DELETE));
					code.setIsmenu("1");
					code.setCreateTime(new Date());
					code.setLastModifyTime(new Date());
					
					// 判断该代码集下的代码名称是否重复
					boolean codeIsExist;
					try {
						codeIsExist = codeService.isDuplicate(code);
					} catch (Exception e) {
						throw new CommonException(e.getMessage(), e);
					}
					if(codeIsExist) {
						throw new CommonException("代码名称或者代码重复！");
					}
					codeDao.insert(code);
				}
//				codeDao.insertList(codes);
				// 更新uniqueCode
				for (Code code : codes) {
					code.setUniqueCode("code^" + code.getId());
					codeDao.updateByPrimaryKeySelective(code);
				}
			}
		}
	}
	
	private Map<String, Integer> isMergedRegion(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
//            System.out.println("firstColumn:" + firstColumn + ",lastColumn:" 
//            			+ lastColumn + ",firstRow:" + firstRow + ",lastRow:" + lastRow);
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                	Map<String, Integer> map = new HashMap<>();
                	map.put("firstColumn", firstColumn);
                	map.put("lastColumn", lastColumn);
                	map.put("firstRow", firstRow);
                	map.put("lastRow", lastRow);
                    return map;
                }
            }
        }
        return null;
    }
	
	@Override
	public void getAllParent(Long codesetId, StringBuilder parentIds) {
		CodeSet currentCodeset = codeSetDao.selectByPrimaryKey(codesetId);
		if (currentCodeset.getParentId() != -1) {
			getAllParent(Long.valueOf(currentCodeset.getParentId()), parentIds);
			parentIds.append(currentCodeset.getId()).append(",");
		} else {
			parentIds.append(currentCodeset.getId() + ",");
		}
		
	}
	
	@Override
	public void getAllParentName(Long codesetId, StringBuilder parentNames) {
		CodeSet currentCodeset = codeSetDao.selectByPrimaryKey(codesetId);
		if (currentCodeset.getParentId() != -1) {
			getAllParentName(Long.valueOf(currentCodeset.getParentId()), parentNames);
			parentNames.append(currentCodeset.getName()).append("/");
		} else {
			parentNames.append(currentCodeset.getName() + "/");
		}
		
	}
	
	@Override
	public List<CodeSet> getAuditedCodeSet() {
		// 先获取到所有非空的代码集
		List<CodeSet> codesets = codeSetDao.getAuditedCodeSet();
		// 获取到所有的代码集目录
		List<CodeSet> codesetDirs = codeSetDao.getCodesetDir();
		// 拼装成树。
		List<CodeSet> treeList = new ArrayList<>();
		// 用于最顶层节点去重
		List<Long> rootDirIds = new ArrayList<>();
		for (CodeSet codeSet : codesets) {
			findParent(codeSet, codesetDirs, treeList, rootDirIds);
		}
		return treeList;
	}
	
	/**
	 * 递归查询父级节点，拼装成树。
	 * @param codeSet
	 * @param codesetDirs
	 * @param treeList
	 */
	private void findParent(CodeSet codeSet, List<CodeSet> codesetDirs, List<CodeSet> treeList
			, List<Long> rootDirIds) {
		if (codeSet.getParentId() == -1 && !rootDirIds.contains(codeSet.getId())) {
			treeList.add(codeSet);
			rootDirIds.add(codeSet.getId());
			return;
		}
		for (CodeSet dir : codesetDirs) {
			if (codeSet.getParentId() == dir.getId()) {
				if (dir.getChildren() == null) {
					List<CodeSet> children = new ArrayList<>();
					children.add(codeSet);
					dir.setChildren(children);
				} else {
					dir.getChildren().add(codeSet);
				}
				findParent(dir, codesetDirs, treeList, rootDirIds);
			}
		}
	}

	@Override
	public void changeCode(Long id, String changeInfo) {
		// 先判断，如果代码所属代码集已经在数据元中被使用，禁止变更
		Code code = codeDao.selectByPrimaryKey(id);
		Example dataElementExample = new Example(DataElementEntity.class);
		dataElementExample.createCriteria().andEqualTo("codeset", code.getCodesetId())
				.andEqualTo("isdel", Constant.NOT_DELETE);
		List<DataElementEntity> dataElement = dataElementDao.selectByExample(dataElementExample);
		if (dataElement != null && dataElement.size() > 0) {
			throw new CommonException("该代码所属代码集已经被数据元使用，无法变更！");
		}
		code.setChangeInfo(changeInfo);
		code.setStatus("3");
		codeDao.updateByPrimaryKeySelective(code);
	}
	
	@Override
	public String exportToExcel(Long parentId) {
		
		Example example = new Example(CodeSet.class);
		Criteria criteria = example.createCriteria().andEqualTo("isCodeset", "0").andEqualTo("isdel", 0);
		
		if (parentId != null) {
			criteria.andEqualTo("parentId", parentId);
		}
		List<CodeSet> codesets = codeSetDao.selectByExample(example);
		if (codesets == null || codesets.size() <= 0) {
			throw new CommonException("该目录下不存在代码集！");
		}
		String templatePath = FileUtil.getJarRootDir() 
				+ PropertyPlaceholder.getContextProperty("excelTemplateDir")
				+ PropertyPlaceholder.getContextProperty("codesetTemplateFile");
		File templateFile = new File(templatePath);
		String targetPath = FileUtil.getJarRootDir()
				+ PropertyPlaceholder.getContextProperty("errorDataTempDir");
		File targerDir = new File(targetPath);
		if (targerDir.exists()) {
			if (!targerDir.isDirectory()) {
				targerDir.delete();
				targerDir.mkdirs();
			}
		} else {
			targerDir.mkdirs();
		}
		String targetFileName = "codeset" + System.currentTimeMillis() + ".xlsx";
		File targetFile = new File(targetPath + targetFileName);
		XSSFWorkbook wb = null;
		try {
			FileUtils.copyFile(templateFile, targetFile);
			wb = new XSSFWorkbook(new FileInputStream(targetFile));
			XSSFSheet sheet = wb.getSheetAt(0);
			int offset = 0;
			for (CodeSet codeSet : codesets) {
				Example codeExample = new Example(Code.class);
				codeExample.createCriteria().andEqualTo("codesetId", codeSet.getId()).andEqualTo("isdel", 0);
				List<Code> codes = codeDao.selectByExample(codeExample);
				if (codes != null && codes.size() > 0) {
					int startRow = EXCEL_READ_START_ROW + offset;
					for (int i = 0; i < codes.size(); i++) {
						Code code = codes.get(i);
						XSSFRow row  = sheet.createRow(EXCEL_READ_START_ROW + offset);
						if (i == 0) {
							row.createCell(0).setCellValue(codeSet.getName());
							row.createCell(1).setCellValue(codeSet.getCodeRule());
						}
						offset += 1;
						row.createCell(2).setCellValue(code.getCode());
						row.createCell(3).setCellValue(code.getName());
					}
					if (codes.size() > 1) {
						CellRangeAddress region1 = new CellRangeAddress(startRow, startRow + codes.size() - 1 
								, 0, 0);
						sheet.addMergedRegion(region1);
						CellRangeAddress region2 = new CellRangeAddress(startRow, startRow + codes.size() - 1
								, 1, 1);
						sheet.addMergedRegion(region2);
					}
					
				}
			}
//			if (!targetFile.exists()) {
//				targetFile.createNewFile();
//			}
			OutputStream fos = new FileOutputStream(targetFile);
			wb.write(fos);
//			wb.close();
//			fos.close();
			
		} catch (Exception e) {
			/*try {
				wb.close();
			} catch (IOException e1) {
			}*/
			LOG.warn(e.getMessage(), e);
			throw new CommonException("代码集导出异常！");
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
			}
		}
		
		return targetFileName;
		
	}

	
}
