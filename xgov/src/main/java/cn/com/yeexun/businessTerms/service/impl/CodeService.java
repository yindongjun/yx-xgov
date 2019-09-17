package cn.com.yeexun.businessTerms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.businessTerms.dao.ICodeDao;
import cn.com.yeexun.businessTerms.entity.Code;
import cn.com.yeexun.businessTerms.entity.CodeSet;
import cn.com.yeexun.businessTerms.service.ICodeService;
import cn.com.yeexun.businessTerms.service.ICodeSetService;
import cn.com.yeexun.businessTerms.utils.CodeMetadataConstant;
import cn.com.yeexun.businessTerms.utils.CodeSetMetadataConstant;
import cn.com.yeexun.meta_data.dao.IMetadataDao;
import cn.com.yeexun.meta_data.entity.Metadata;
import cn.com.yeexun.meta_data.entity.MetadataAttrEntity;
import cn.com.yeexun.meta_data.service.IMetadataAttrService;
import cn.com.yeexun.meta_data.service.IMetadataService;
import cn.com.yeexun.standardAudit.entity.StandardAudit;
import cn.com.yeexun.standardAudit.service.IStandardAuditService;
import cn.com.yeexun.utils.Constant;
import cn.com.yeexun.utils.VerifyException;

@Service
public class CodeService extends BaseService<Code> implements ICodeService {
	
	@Autowired
	private ICodeDao codeDao;
	@Autowired
	private IMetadataDao metadataDao;
	@Autowired
	private IStandardAuditService standardAuditService;
	@Autowired
	private ICodeSetService codeSetService;
	@Autowired
	private IMetadataService metadataService;
	@Autowired
	private IMetadataAttrService metadataAttrService;
	
	
	public static Map<String,Object> mapArray = new LinkedHashMap<String, Object>();
//	public List<Code> menuCommon; 
//	public List<Object> list = new ArrayList<Object>();
	
	
	
	
	
	
//	public List<Object> getCodes1(Long id) throws Exception {
//		List<Code> lists = new ArrayList<Code>();
//		List<Code> listsAll = new ArrayList<Code>();
//		listsAll = codeDao.getAllCode(0);
//		for(Code code:listsAll){
//			if(code.getCodesetId() == id){
//				lists.add(code);
//			}
//		}
//		//lists = menuList(CodeService.this.listByIds(ids));
//		return menuList(lists);
//	}
	
	public List<Object> menuList(List<Code> menu){  
//		this.menuCommon = menu;
		List<Object> list = new ArrayList<Object>();
	    for (Code x : menu) {   
	      Map<String,Object> mapArr = new LinkedHashMap<String, Object>(); 
	      if(x.getParentId() == -1){ 
	        mapArr.put("id", x.getId()); 
	        mapArr.put("name", x.getName());  
	        mapArr.put("parentId", x.getParentId());  
	        mapArr.put("path", x.getPath());
	        mapArr.put("status", x.getStatus());
	        mapArr.put("children", menuChild(x.getId(), menu));  
	        list.add(mapArr); 
	      } 
	    }   
	    return list; 
	  }
	
	public List<?> menuChild(long id, List<Code> menu){ 
	    List<Object> lists = new ArrayList<Object>(); 
	    for(Code a:menu){ 
	      Map<String,Object> childArray = new LinkedHashMap<String, Object>(); 
	      if(a.getParentId() == id){ 
	        childArray.put("id", a.getId()); 
	        childArray.put("name", a.getName()); 
	        childArray.put("parentId", a.getParentId()); 
	        childArray.put("path", a.getPath());
	        childArray.put("status", a.getStatus());
	        childArray.put("children", menuChild(a.getId(), menu));
	        lists.add(childArray); 
	      } 
	    } 
	    return lists; 
	  }
	
	
	@Override
	public void addCode(Code codeEntity,Long pid) throws Exception {
		if(pid != null && pid > 0){
			Code code = this.getById(pid);
			code.setIsmenu("0");
			save(code);
		}
		save(codeEntity);
		
	}
	
	/**
	 * 判断代码表中是否有重复的代码或者代码名称
	 * 
	 * @param code
	 * @throws Exception
	 * @return
	 */
	@Override
	public boolean isDuplicate(Code codeEntity) throws Exception {
		//Code codeEntity = JSON.parseObject(code, Code.class);
		boolean b = false;
		if(StringUtils.isNotBlank(codeEntity.getName()) && StringUtils.isNotBlank(codeEntity.getCode())){
			List<Code> lists = codeDao.getCodeList(codeEntity.getId(),codeEntity.getName(),codeEntity.getCode(),codeEntity.getCodesetId());
			if(lists != null && lists.size() > 0) {
				b = true;
			}
		} else {
			throw new VerifyException("代码名称或者代码为空！");
		}
		return b;
	}
	
	@Override
	public void deleteCode(Long id) throws Exception {
		//逻辑删除子节点代码
		List<Code> lists = getLowerMenu(id);
		Set<Long> idsSet = new HashSet<Long>();
		for(Code code:lists){
			idsSet.add(code.getId());
			code.setIsdel(1);
			this.update(code);
		}
		//逻辑删除父节点代码
		Code codeEntity = this.getById(id);
		codeEntity.setIsdel(1);
		this.update(codeEntity);
		//设置其父节点是否还有别的下级  是否有下级(0:是，1:否)
		/*List<Code> codeList =  codeDao.getAllCodeByPid(0,codeEntity.getParentId());
		if(codeList != null && codeList.size()==0){
			Code codeFather = this.getById(codeEntity.getParentId());
			codeFather.setIsmenu("1");
			this.update(codeFather);
		}*/
	}
	
	public List<Code> getLowerMenu(Long id) throws Exception {
		List<Code> lists = new ArrayList<Code>();
		List<Code> lowerList = new ArrayList<Code>();
		lists = codeDao.getAllCode(0);
		lowerList = treeMenuList(lists,id);
		//lowerList.add(this.getById(id));
		return lowerList;
	}
	
	public List<Code> treeMenuList(List<Code> menuList,Long id){
		List<Code> childMenu = new ArrayList<Code>();
		for(Code code:menuList){
			if(code.getParentId() == id){
				treeMenuList(menuList,code.getId());
				childMenu.add(code);
				//menuList.remove(dc);
			}
		}
		return childMenu;
	}


	@Override
	public List<Code> getCodesFromSetPage(Long codeSetId, String status, Page<Code> page) throws Exception {
		List<Code> lists = codeDao.getAllCodeBycodesetIdPage(0, codeSetId, status, page);
		return lists;
	}
	
	@Override
	public List<Code> getCodesFromSet(Long codeSetId) throws Exception {
		List<Code> lists = codeDao.getAllCodeBycodesetId(0, codeSetId);
		return lists;
	}

	@Override
	public List<Code> getCodeByPid(Long pid) throws Exception {
		List<Code> lists = codeDao.getAllCodeByPid(0, pid);
		return lists;
	}

	@Override
	public boolean sendAudit(List<Long> idslist) throws Exception {
		boolean flag = true;
		for(long id:idslist){
			Code codeEntity = this.getById(id);
			if(codeEntity.getStatus().equals("1") || codeEntity.getStatus().equals("2")){
				flag = false;
				break;
			}
		}
		if(flag){
			for(long id:idslist){
				Code codeEntity = this.getById(id);
				codeEntity.setFrontStatus(codeEntity.getStatus());
				codeEntity.setStatus("1");
				this.save(codeEntity);
				//这里不能每次新建审核，要改变原有的业务术语的审核的状态，如果没有该业务术语的审核则新建。
				StandardAudit standardAudit  = standardAuditService.getByFlowId("2", id);
				if(standardAudit == null){
					standardAudit = new StandardAudit();
				}
				standardAudit.setFlowId((int)id);
				standardAudit.setStatus("0");
				standardAudit.setTaskName(codeEntity.getName());
				//送审人
				standardAudit.setSubmitter("admin");
				standardAudit.setType("2");
				standardAudit.setSubmitTime(new Date());
				standardAuditService.save(standardAudit);
			}
		}
		
		return flag;
	}

	@Override
	public List<Code> getCode(Long id) throws Exception {
		List<Code> codeList = codeDao.getCodeById(0,id);
		// 查出该代码的全路径
		StringBuilder allParentNames = new StringBuilder("/");
		codeSetService.getAllParentName(codeList.get(0).getCodesetId(), allParentNames);
		codeList.get(0).setPath(allParentNames.toString());
		return codeList;
	}

	@Override
	public void submit(List<Long> ids,String status) throws Exception {
		List<Code> codeList = this.listByIds(ids);
		if(status.equals("1")){
			//修改代码状态
			for(Long id:ids){
				Code code = this.getById(id);
				code.setStatus("2");
				this.save(code);
			}
			if(ids != null && ids.size() > 0){
				Code codeEntity = this.getById(ids.get(0));
				CodeSet codeSetEntity = codeSetService.getById(codeEntity.getCodesetId());
				Metadata codeSetMetadata = new Metadata();
				if(codeSetEntity != null){
					if(!isExistCodeSet(ids.get(0))){
						codeSetMetadata = addCodeSetMetadata(codeSetEntity,"新增");
					} else {
						Metadata meta = new Metadata();
						meta.setCode(codeSetEntity.getCode());
						meta.setParentCode("codeSet^" + codeSetEntity.getId());
						meta.setMetamodelId(3L);
						codeSetMetadata = metadataService.getMetaByUniqueCode(meta).get(0);
					}
				}
				
				addCodeMetadata(codeList,codeSetMetadata);
			}
		}else{
			//如果审核退回,则所有代码回归到审核之前的状态
			for(Long id:ids){
				Code code = this.getById(id);
//				code.setStatus(code.getFrontStatus());
				code.setStatus(String.valueOf(Code.STATUS_REJECT));
				this.save(code);
			}
		}
		
	}
	
	@Override
	public void deleteCodeMetadata(
			List<Code> codeList) throws Exception{
		List<MetadataAttrEntity> list = new ArrayList<MetadataAttrEntity>();
		for (Code code : codeList) {
			//查找是否有老版本  
			Metadata meta1 = new Metadata();
			meta1.setMetamodelId(23L);
//			meta1.setCode(code.getCode());
			meta1.setParentCode("code^" + code.getId());
			meta1.setPath("/");
			List<Metadata> detailList = metadataService.getMetadataVersion(meta1);
			int version = 0;
			if(detailList != null && detailList.size()>0){
				for (Metadata metadata : detailList) {
					if(metadata.getVersion() > version){
						version = metadata.getVersion();
					}
					metadata.setDeleteFlag(Constant.IS_DELETE);
					metadataService.update(metadata);
				}
			}
			Metadata meta = new Metadata();
			meta.setBuildin("N");
			meta.setCreateTime(code.getCreateTime());
			meta.setCreateUserId(0L);
			meta.setIsMenu("N");
			meta.setIsRelease(Constant.IS_RELEASE);
			meta.setLastModifyTime(code.getLastModifyTime());
			meta.setMetadataType(Constant.BUSSINESS_METADATA);
			meta.setMetamodelId(23L);
			meta.setName(code.getName());
			meta.setCode(code.getCode());
			meta.setParentCode("code^" + code.getId());
			meta.setParentId(3L);
			meta.setPath("/");
//			meta.setUniqueCode(code.getUniqueCode());
			meta.setVersion(version + 1);
			meta.setVersionDescription("删除");
			meta.setDeleteFlag(Constant.IS_DELETE);
			metadataService.save(meta);
			List<MetadataAttrEntity> attrs = addCodeMetadataAttr(code, meta.getId());
			list.addAll(attrs);
		}
		metadataAttrService.insertList(list);
	}
	
	
	public Metadata addCodeSetMetadata(CodeSet codeSet, String versionMessage) throws Exception{
		//找到元数据库中是否存在老版本的
		Metadata meta1 = new Metadata();
		meta1.setCode(codeSet.getCode());
		meta1.setParentCode("codeSet^" + codeSet.getId());
		meta1.setMetamodelId(3L);
		meta1.setPath("/");
		List<Metadata> oldVersions = metadataService.getMetadataVersion(meta1);
		int version = 0;
		if(oldVersions != null && oldVersions.size() > 0){
			for (Metadata metadata : oldVersions) {
				if (version < metadata.getVersion()) {
					version = metadata.getVersion();
				}
			}
		}
		Metadata meta = new Metadata();
		meta.setBuildin("N");
		meta.setCode(codeSet.getCode());
		meta.setParentCode("codeSet^" + codeSet.getId());
		meta.setCreateTime(codeSet.getCreateTime());
		meta.setCreateUserId(0L);
		meta.setIsMenu("N");
		meta.setIsRelease(Constant.IS_RELEASE);
		meta.setMetadataType(Constant.BUSSINESS_METADATA);
		meta.setMetamodelId(3L);
		meta.setName(codeSet.getName());
		meta.setParentId(3L);
		meta.setPath("/");
//		meta.setUniqueCode(codeSet.getUniqueCode());
		meta.setVersion(version + 1);
		meta.setVersionDescription(versionMessage);
		if("删除".equals(versionMessage)){
			meta.setDeleteFlag(Constant.IS_DELETE);
		}else{
			meta.setDeleteFlag(Constant.NOT_DELETE);
		}
		metadataService.save(meta);
		addCodeSetMetadataAttr(codeSet, meta.getId());
		return meta;
	}
	
	
	private void addCodeMetadata(List<Code> codeList,Metadata codeSetMetadata) throws Exception{
		List<MetadataAttrEntity> list = new ArrayList<MetadataAttrEntity>();
		for (Code code : codeList) {
			//查找是否有老版本  
			Metadata meta1 = new Metadata();
			meta1.setMetamodelId(23L);
//			meta1.setCode(code.getCode());  //修改T724主掉的，我也不知道对不对。。。
			meta1.setParentCode("code^" + code.getId());
			meta1.setPath("/");
			List<Metadata> detailList = metadataService.getMetadataVersion(meta1);
			int version = 0;
			List<Long> ids = new ArrayList<Long>();
			if(detailList != null && detailList.size()>0){
				for (Metadata metadata : detailList) {
					if(metadata.getVersion() > version){
						version = metadata.getVersion();
					}
					ids.add(metadata.getId());
				}
			}
			//将老版本逻辑删除（isdel=1）
			if(ids != null && ids.size() > 0){
				metadataService.deleteList(ids);
			}
			Metadata meta = new Metadata();
			meta.setBuildin("N");
			meta.setCreateTime(code.getCreateTime());
			meta.setCreateUserId(0L);
			meta.setIsMenu("N");
			meta.setIsRelease(Constant.IS_RELEASE);
			meta.setLastModifyTime(code.getLastModifyTime());
			meta.setMetadataType(Constant.BUSSINESS_METADATA);
			meta.setMetamodelId(23L);
			meta.setName(code.getName());
			meta.setCode(code.getCode());
			meta.setParentCode("code^" + code.getId());
			meta.setParentId(codeSetMetadata.getId());
			meta.setPath("/");
//			meta.setUniqueCode(code.getUniqueCode());
			meta.setVersion(version + 1);
			meta.setDeleteFlag(Constant.NOT_DELETE);
			if(version == 0){
				meta.setVersionDescription("新增");
			}else{
				meta.setVersionDescription("修改");
			}
			
			metadataService.save(meta);
			List<MetadataAttrEntity> attrs = addCodeMetadataAttr(code, meta.getId());
			list.addAll(attrs);
		}
		//metadataAttrService.insertList(list);
	}
	
	
	private void addCodeSetMetadataAttr(CodeSet codeSet, long id) throws Exception {
		List<MetadataAttrEntity> attrs = new ArrayList<MetadataAttrEntity>();
		MetadataAttrEntity attr1 = new MetadataAttrEntity();
		attr1.setAttrKey("CodeSetName");
		attr1.setAttrValue(codeSet.getName());
		attr1.setModelAttrId(CodeSetMetadataConstant.CodeSetName);
		attrs.add(attr1);
		MetadataAttrEntity attr2 = new MetadataAttrEntity();
		attr2.setAttrKey("CodeSetNUM");
		attr2.setAttrValue(codeSet.getCode());
		attr2.setModelAttrId(CodeSetMetadataConstant.CodeSetNUM);
		attrs.add(attr2);
		MetadataAttrEntity attr3 = new MetadataAttrEntity();
		attr3.setAttrKey("Illustration");
		attr3.setAttrValue(String.valueOf(codeSet.getExplanation()));
		attr3.setModelAttrId(CodeSetMetadataConstant.Illustration);
		attrs.add(attr3);
		MetadataAttrEntity attr4 = new MetadataAttrEntity();
		attr4.setAttrKey("Expression");
		attr4.setAttrValue(String.valueOf(codeSet.getExpress()));
		attr4.setModelAttrId(CodeSetMetadataConstant.Expression);
		attrs.add(attr4);
		MetadataAttrEntity attr5 = new MetadataAttrEntity();
		attr5.setAttrKey("CodeRules");
		attr5.setAttrValue(String.valueOf(codeSet.getCodeRule()));
		attr5.setModelAttrId(CodeSetMetadataConstant.CodeRules);
		attrs.add(attr5);
		MetadataAttrEntity attr6 = new MetadataAttrEntity();
		attr6.setAttrKey("CreateTime");
		attr6.setAttrValue(String.valueOf(codeSet.getCreateTime()));
		attr6.setModelAttrId(CodeSetMetadataConstant.CreateTime);
		attrs.add(attr6);
		MetadataAttrEntity attr7 = new MetadataAttrEntity();
		attr7.setAttrKey("UpdateTime");
		attr7.setAttrValue(String.valueOf(codeSet.getLastModifyTime()));
		attr7.setModelAttrId(CodeSetMetadataConstant.UpdateTime);
		attrs.add(attr7);
		for (MetadataAttrEntity metadataAttrEntity : attrs) {
			metadataAttrEntity.setCreateTime(new Date());
			metadataAttrEntity.setCreateUserId(0L);//TODO
			metadataAttrEntity.setMetadataId(id);
		}
		metadataAttrService.insertList(attrs);
	}
	
	private List<MetadataAttrEntity> addCodeMetadataAttr(Code code, long id) throws Exception {
		List<MetadataAttrEntity> attrs = new ArrayList<MetadataAttrEntity>();
		MetadataAttrEntity attr1 = new MetadataAttrEntity();
		attr1.setAttrKey("Code");
		attr1.setAttrValue(code.getCode());
		attr1.setModelAttrId(CodeMetadataConstant.Code);
		attrs.add(attr1);
		MetadataAttrEntity attr2 = new MetadataAttrEntity();
		attr2.setAttrKey("CodeName");
		attr2.setAttrValue(code.getName());
		attr2.setModelAttrId(CodeMetadataConstant.CodeName);
		attrs.add(attr2);
		MetadataAttrEntity attr3 = new MetadataAttrEntity();
		attr3.setAttrKey("CreateTime");
		attr3.setAttrValue(String.valueOf(code.getCreateTime()));
		attr3.setModelAttrId(CodeMetadataConstant.CreateTime);
		attrs.add(attr3);
		MetadataAttrEntity attr4 = new MetadataAttrEntity();
		attr4.setAttrKey("UpdateTime");
		attr4.setAttrValue(String.valueOf(code.getLastModifyTime()));
		attr4.setModelAttrId(CodeMetadataConstant.UpdateTime);
		attrs.add(attr4);
		for (MetadataAttrEntity metadataAttrEntity : attrs) {
			metadataAttrEntity.setCreateTime(new Date());
			metadataAttrEntity.setCreateUserId(0L);//TODO
			metadataAttrEntity.setMetadataId(id);
		}
		metadataAttrService.insertList(attrs);
		return attrs;
	}
	
	
	
	
	
	//判断元数据表里有没有当前代码所属的代码集
	@Override
	public boolean isExistCodeSet(Long id) throws Exception {
		boolean flag = false;
		Code codeEntity = this.getById(id);
		CodeSet codeSetEntity = codeSetService.getById(codeEntity.getCodesetId());
		if(codeSetEntity.getUniqueCode() != null){
			Metadata codeSet = new Metadata();
			codeSet.setCode(codeSetEntity.getCode());
			codeSet.setParentCode("codeSet^" + codeSetEntity.getId());
			codeSet.setMetamodelId(3L);
			codeSet.setPath("/");
			List<Metadata> metadataList = metadataDao.getMetadataVersion(codeSet);
			if(metadataList != null && metadataList.size() > 0){
				return true;
			}
		}
		return flag;
	}

	@Override
	public int getAllCodeNum() throws Exception {
		int num = 0;
		List<Code> codeList = codeDao.getAllCode(0);
		if(codeList != null && codeList.size() > 0){
			num = codeList.size();
		}
		return num;
	}

	@Override
	public List<Code> getCodeLastFive() throws Exception {
		return codeDao.getCodeLastFive();
	}

	@Override
	public List<Integer> getCodeNumByNowTime() throws Exception {
		List<Integer> code = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0);
		Date d = calendar.getTime();
		Integer num = codeDao.getCodeNumByNowTime(d);
		code.add((num+1)*120);
		for(int i=0;i<3;i++){
			calendar.add(Calendar.DATE, -7);
			Date d1 = calendar.getTime();
			Integer num1 = codeDao.getCodeNumByNowTime(d1);
			code.add((num1+1)*120);
		}
		return code;
	}
	
}
