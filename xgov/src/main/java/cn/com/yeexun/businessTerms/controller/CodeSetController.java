package cn.com.yeexun.businessTerms.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.businessTerms.entity.Code;
import cn.com.yeexun.businessTerms.entity.CodeSet;
import cn.com.yeexun.businessTerms.service.ICodeService;
import cn.com.yeexun.businessTerms.service.ICodeSetService;
import cn.com.yeexun.common.controller.BaseController;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataElement.service.IDataElementService;
import cn.com.yeexun.standardAudit.entity.StandardAudit;
import cn.com.yeexun.standardAudit.service.IStandardAuditService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import cn.com.yeexun.utils.FileUtil;
import cn.com.yeexun.utils.PropertyPlaceholder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("code")
@Api("业务术语")
public class CodeSetController extends BaseController<CodeSet> {
	
	private Logger logger = Logger.getLogger(CodeSetController.class);
	
	@Autowired
	private ICodeSetService codeSetService;
	@Autowired
	private ICodeService codeService;
	
	@Autowired
	private IDataElementService dataElementService;
	
	@Autowired
	private IStandardAuditService standardAuditService;
	
	@ResponseBody
	@RequestMapping(value = "/getCodeSet", method = RequestMethod.GET)
	@ApiOperation("获取代码集菜单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest")
	})
	public String getCodeSet(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		try {
			List<CodeSet> menuList = codeSetService.getCodeSet();
			map.put(CodeUtil.RESULT_DATA, menuList);
			map.put("code",CodeUtil.CODE_SUCCESS);
			map.put("message", CodeUtil.CODE_SUCCESS_MESSAGE);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAuditedCodeSet", method = RequestMethod.GET)
	@ApiOperation("获取代码集菜单,代码集下的所有代码全部通过审核")
	@ApiImplicitParams({
	})
	public String getAuditedCodeSet(){
		Map<String, Object> map = new HashMap<>();
		try {
			List<CodeSet> menuList = codeSetService.getAuditedCodeSet();
			map.put(CodeUtil.RESULT_DATA, menuList);
			map.put("code",CodeUtil.CODE_SUCCESS);
			map.put("message", CodeUtil.CODE_SUCCESS_MESSAGE);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getCodesFromSet", method = RequestMethod.GET)
	@ApiOperation("点击代码集菜单获取代码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "代码集id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest")
	})
	public String getCodesFromSet(Long id, String status, Integer start, Integer length){
		Map<String, Object> map = new HashMap<>();
		try {
			Page<Code> page = new Page<>();
			page.setStart(start);
			page.setLength(length);
			List<Code> menuList = codeService.getCodesFromSetPage(id, status, page);
			map.put(CodeUtil.RESULT_DATA, menuList);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
			map.put("code",CodeUtil.CODE_SUCCESS);
			map.put("message", CodeUtil.CODE_SUCCESS_MESSAGE);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getCodes", method = RequestMethod.GET)
	@ApiOperation("获取下级代码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "代码集id", required = true, dataType = "long")
	})
	public String getCodeByPid(Long id){
		Map<String, Object> map = new HashMap<>();
		try {
			//test
			List<Code> menuList = codeService.getCodeByPid(id);
			map.put(CodeUtil.RESULT_DATA, menuList);
			map.put("code",CodeUtil.CODE_SUCCESS);
			map.put("message", CodeUtil.CODE_SUCCESS_MESSAGE);
		} catch (Exception e) {
			logger.error("error", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	@ResponseBody
	@RequestMapping(value = "/addCode", method = RequestMethod.POST)
	@ApiOperation("添加或修改代码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pid", value = "父代码id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "code", value = "代码", required = true, dataType = "String"),
		@ApiImplicitParam(name = "codesetId", value = "代码集id", required = true, dataType = "int"),
		@ApiImplicitParam(name = "name", value = "代码名称", required = true, dataType = "String"),
		@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest")
	})
	public String addCode(Long pid,String code,Long codesetId,String name){
		Map<String, Object> map = new HashMap<>();
		Code codeEntity = new Code();
		codeEntity.setId(0);
		codeEntity.setCode(code);
		codeEntity.setName(name);
		codeEntity.setCodesetId(codesetId);
		codeEntity.setParentId(pid);
		codeEntity.setIsdel(0);
		codeEntity.setIsmenu("1");
		codeEntity.setStatus("0");
		codeEntity.setCreateTime(new Date());
		//codeEntity.setUniqueCode("code^"+code);
		boolean b = false;
		try {
			b = codeService.isDuplicate(codeEntity);
			if(b) {
				//throw new VerifyException("代码名称或者代码重复！");
				map.put(CodeUtil.MESSAGE_TEXT, "代码名称或者代码重复！");
				map.put(CodeUtil.CODE_TEXT, CodeUtil.CODE_REQUEST_ERROR);
			} else {
				codeService.addCode(codeEntity,pid);
				codeEntity.setUniqueCode("code^"+codeEntity.getId());
				codeService.save(codeEntity);
				map = CodeUtil.getSuccessMap();
			}
		} catch (Exception e) {
			logger.error("error", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateCode", method = RequestMethod.POST)
	@ApiOperation("修改代码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "code", value = "代码", required = true, dataType = "String"),
		@ApiImplicitParam(name = "codesetId", value = "代码集id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "name", value = "代码名称", required = true, dataType = "String"),
		@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest")
	})
	public String updateCode(Long id,String code,Long codesetId,String name) throws Exception{
		Map<String, Object> map = new HashMap<>();
		Code codeEntity = new Code();
		//HttpServletRequest request = null;
		if(id == null){
			
			return addCode((long) -1,code,codesetId,name);
			//return JSON.toJSONString(CodeUtil.getBadRequestMap());
		}
		Code oldCodeEntity = codeService.getById(id);
		codeEntity.setId(id);
		codeEntity.setCode(code);
		codeEntity.setName(name);
		codeEntity.setCodesetId(codesetId);
		codeEntity.setIsdel(0);
		if(!"3".equals(oldCodeEntity.getStatus())){
			codeEntity.setStatus("0");
		}
		codeEntity.setLastModifyTime(new Date());
		boolean b = false;
		try {
			b = codeService.isDuplicate(codeEntity);
			if(b) {
				//throw new VerifyException("代码名称或者代码重复！");
				map.put(CodeUtil.MESSAGE_TEXT, "代码名称或者代码重复！");
				map.put(CodeUtil.CODE_TEXT, CodeUtil.CODE_REQUEST_ERROR);
			} else {
				codeService.save(codeEntity);
				map = CodeUtil.getSuccessMap();
			}
		} catch (Exception e) {
			logger.error("error", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateAndAddCodeSet", method = RequestMethod.POST)
	@ApiOperation("添加或修改代码集")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "代码id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "code", value = "代码集序号", required = true, dataType = "String"),
		@ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String"),
		@ApiImplicitParam(name = "pid", value = "父id", required = true, dataType = "int"),
		@ApiImplicitParam(name = "iscodeset", value = "是否代码集", required = true, dataType = "String"),
		@ApiImplicitParam(name = "explanation", value = "说明", required = true, dataType = "String"),
		@ApiImplicitParam(name = "express", value = "表示", required = true, dataType = "String"),
		@ApiImplicitParam(name = "coderule", value = "编码规则", required = true, dataType = "String"),
		@ApiImplicitParam(name = "createPolicy", value = "制定依据", required = true, dataType = "String"),
		@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest")
	})
	public String updateAndAddCodeSet(Long id,String code,String name,int pid,String iscodeset
			,String explanation,String express,String coderule, String createPolicy){
		Map<String, Object> map = new HashMap<>();
		CodeSet codeSetEntity = new CodeSet();
		if (id != null) {
			codeSetEntity.setId(id);
		} else {
			codeSetEntity.setId(0);
		}
		//不能修改代码集序号，因为UniqueCode是codeset^+code
		codeSetEntity.setName(name);
		codeSetEntity.setParentId(pid);
		codeSetEntity.setIsdel(0);
		codeSetEntity.setIsCodeset(iscodeset);
		codeSetEntity.setExplanation(explanation);
		codeSetEntity.setExpress(express);
		codeSetEntity.setCodeRule(coderule);
		codeSetEntity.setCode(code);
		codeSetEntity.setCreatePolicy(createPolicy);
		//codeSetEntity.setUniqueCode("codeset^"+code);
		boolean b = false;
		try {
			b = codeSetService.isDuplicate(codeSetEntity);
			if(b) {
				//throw new VerifyException("代码名称或者代码重复！");
				map.put(CodeUtil.MESSAGE_TEXT, "名称或者代码重复！");
				map.put(CodeUtil.CODE_TEXT, CodeUtil.CODE_REQUEST_ERROR);
			} else {
				codeSetService.updateAndAddCodeSet(codeSetEntity);
				codeSetEntity.setUniqueCode("codeset^"+codeSetEntity.getId());
				codeSetService.save(codeSetEntity);
				map = CodeUtil.getSuccessMap();
			}
		} catch (CommonException e) {
			logger.warn("编辑代码集异常：", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteCodes", method = RequestMethod.GET)
	@ApiOperation("删除code(删除父节点时，所有子节点都删除)")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "code所有父节点id", required = true, dataType = "String"),
		@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest")
	})
	public String deleteCodes(String ids){
		Map<String, Object> map = new HashMap<>();
		if(StringUtils.isBlank(ids)){
			return JSON.toJSONString(CodeUtil.getBadRequestMap());
		}
		String[] item = ids.split(",");
		List<Long> idslist = new ArrayList<Long>(item.length);
		
		for (String idStr : item) {
			long id = Long.parseLong(idStr);
			idslist.add(id);
		}
		try {
			// 判断如果要删除的代码所属的代码集关联了数据元，就不能删除。
			/*for (Long codeId : idslist) {
				Code code = codeService.getById(codeId);
				DataElementEntity example = new DataElementEntity();
				example.setCodeset(String.valueOf(code.getCodesetId()));
				example.setIsdel(Constant.NOT_DELETE);
				List<DataElementEntity> elemets = dataElementService.search(example);
				if (elemets != null && elemets.size() > 0) {
					throw new CommonException("该代码所属代码集已经与数据元绑定，不能删除！");
				}
			}*/
			
			for(long id:idslist){
				codeService.deleteCode(id);
			}
			List<Code> codeList = codeService.listByIds(idslist);
			codeService.deleteCodeMetadata(codeList);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			logger.warn("删除代码异常：", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error", e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/back",method = RequestMethod.POST)
	@ApiOperation("代碼撤回操作，撤回后状态变为草稿")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "代码id", required = true, dataType = "Long")
	  })
	public String back(Long id) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Code code = codeService.getById(id);
			code.setStatus(String.valueOf(Code.STATUS_DRAFT));
			codeService.save(code);
			// 将审核表的记录更改为撤回状态
			StandardAudit standardAudit  = standardAuditService.getByFlowId("2", id);
			standardAudit.setStatus(StandardAudit.STATUS_BACK);
			standardAuditService.save(standardAudit);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("代码撤回出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/sendAudit",method = RequestMethod.GET)
	@ApiOperation("送审代码")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "ids", value = "需要送审的代码id", required = true, dataType = "String"),
	  })
	public String sendAudit(String ids){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(StringUtils.isBlank(ids)){
				return JSON.toJSONString(CodeUtil.getBadRequestMap());
			}
			String[] item = ids.split(",");
			List<Long> idslist = new ArrayList<Long>(item.length);
			for (String idStr : item) {
				long id = Long.parseLong(idStr);
				idslist.add(id);
			}
			if(!codeService.sendAudit(idslist)){
				map.put(CodeUtil.MESSAGE_TEXT, "所选中存在已审核或者待审核的代码！重新选择");
			}else{
				map = CodeUtil.getSuccessMap();
			}
		} catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	@ResponseBody
	@RequestMapping(value = "/deleteCodeSets", method = RequestMethod.GET)
	@ApiOperation("删除CodeSet(删除父节点时，所有子节点都删除)")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "代码集id", required = true, dataType = "long"),
	  })
	public String deleteCodeSets(Long id){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			codeSetService.deleteCodeSets(id);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			map = CodeUtil.getErrorRequestMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
			
		} catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	@ResponseBody
	@RequestMapping(value = "/getCodeById", method = RequestMethod.GET)
	@ApiOperation("编辑代码时回显")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "代码id", required = true, dataType = "long"),
	  })
	public String getCodeById(Long id){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Code> list = codeService.getCode(id);
			map.put(CodeUtil.RESULT_DATA, list);
			map.put("code",CodeUtil.CODE_SUCCESS);
			map.put("message", CodeUtil.CODE_SUCCESS_MESSAGE);
		} catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getCodeSetById", method = RequestMethod.GET)
	@ApiOperation("编辑代码集时回显")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "代码id", required = true, dataType = "long"),
	  })
	public String getCodeSetById(Long id){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			CodeSet codeSet = codeSetService.echoCodeSet(id);
			map.put(CodeUtil.RESULT_DATA, codeSet);
			map.put("code",CodeUtil.CODE_SUCCESS);
			map.put("message", CodeUtil.CODE_SUCCESS_MESSAGE);
		} catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getCodeNumByNowTime", method = RequestMethod.GET)
	@ApiOperation("获取近四周业务数据代码数量")
	@ApiImplicitParams({
	  })
	public String getCodeNumByNowTime(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put(CodeUtil.RESULT_DATA, codeService.getCodeNumByNowTime());
			map.put("code",CodeUtil.CODE_SUCCESS);
			map.put("message", CodeUtil.CODE_SUCCESS_MESSAGE);
		} catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@Override
	public IBaseService<CodeSet> service() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/change_code",method = RequestMethod.GET)
	@ApiOperation("代码变更")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据元id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "changeInfo", value = "变更原因", required = true, dataType = "String")
	  })
	public String changeCode(Long id, String changeInfo, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			codeSetService.changeCode(id, changeInfo);
//			StandardAudit standAudit = standardAuditService.getByFlowId("2", id);
//			standardAuditService.delete(standAudit);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			logger.warn("数据元变更出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		}catch (Exception e) {
			logger.error("数据元变更出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/importFromExcel",method = RequestMethod.POST)
	@ApiOperation("从Excel中导入业务术语")
	@ApiImplicitParams({
	  })
	public String importFromExcel(HttpServletRequest request, Integer parentId) {
		Map<String, Object> map = CodeUtil.getSuccessMap();
		File newfile = null;
		try {
			// 将当前上下文初始化给 CommonsMutipartResolver（多部分解析器）
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			// 检查form中是否有enctype="multipart/form-data"
			if (multipartResolver.isMultipart(request)) {
				// 将request变成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 获取multiRequest 中所有的文件名
//				String delimiter = multiRequest.getParameter("delimiter");
				Iterator<String> iter = multiRequest.getFileNames();
				if (iter.hasNext()) {
					// 一次遍历所有文件
					MultipartFile file = multiRequest.getFile(iter.next().toString());
					if (file != null) {
//						String path = request.getSession().getServletContext()
//								.getRealPath("/WEB-INF/upload/term");
						String path = FileUtil.getJarRootDir() 
								+ PropertyPlaceholder.getContextProperty("importFileDir");
						File importDir = new File(path);
						if (importDir.exists()) {
							if (!importDir.isDirectory()) {
								importDir.delete();
								importDir.mkdirs();
							}
						} else {
							importDir.mkdirs();
						}
						// 上传
						String originalFilename = file.getOriginalFilename();
						if (!originalFilename.substring(originalFilename.lastIndexOf("."), 
								originalFilename.length()).equals(".xlsx")) {
							throw new CommonException("文件格式错误，下载正确的模板填写后重新上传！");
						}
						newfile = new File(path, file.getOriginalFilename() + System.currentTimeMillis());
						// 上传前检查文件是否存在
						file.transferTo(newfile);
					}
				}
			}
			// 读取Excel文件
			codeSetService.importFromExcel(newfile, parentId);
		}catch (CommonException e) {
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
			logger.error("从Excel中导入代码集异常：", e);
		} catch (IllegalStateException e) {
			map = CodeUtil.getSystemErrorMap();
			logger.error("从Excel中导入代码集异常：", e);
		}  catch (IOException e) {
			map = CodeUtil.getSystemErrorMap();
			logger.error("从Excel中导入代码集异常：", e);
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("下载Excel模板")
	@ApiImplicitParams({
//	      @ApiImplicitParam(name = "tableDesignId", value = "tableDesignId", required = true, dataType = "int"),
//	      @ApiImplicitParam(name = "verifyType", value = "verifyType", required = true, dataType = "string")
	  })
	@RequestMapping(value = "/downloadTemplate",method = RequestMethod.GET)
	@ResponseBody
	public String exportToExcel(HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> map = null;
		try {
//			FileUtil.downloadFileToBrowser(request.getSession().getServletContext()
//					.getRealPath("/") 
//					+ PropertyPlaceholder.getContextProperty("excelTemplateDir") + PropertyPlaceholder.getContextProperty("codesetTemplateFile")
//					, PropertyPlaceholder.getContextProperty("codesetTemplateFile"), response);
			
			FileUtil.downloadFileToBrowser(FileUtil.getJarRootDir() 
					+ PropertyPlaceholder.getContextProperty("excelTemplateDir")
					+ PropertyPlaceholder.getContextProperty("codesetTemplateFile")
					, PropertyPlaceholder.getContextProperty("codesetTemplateFile"), response);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("导出问题数据出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@RequestMapping(value = "/exportToExcel",method = RequestMethod.GET)
	@ResponseBody
	public String exportToExcel(Long parentId, HttpServletResponse response){
		Map<String, Object> map = null;
		try {
			String fileName = codeSetService.exportToExcel(parentId);
			FileUtil.downloadFileToBrowser(FileUtil.getJarRootDir() 
					+ PropertyPlaceholder.getContextProperty("errorDataTempDir") + fileName
					, fileName, response);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			logger.error("导出问题数据出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		} catch (Exception e) {
			logger.error("导出问题数据出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
}
