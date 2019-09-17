package cn.com.yeexun.documentManagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.resource.spi.CommException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.common.controller.BaseController;
import cn.com.yeexun.documentManagement.entity.DocumentContent;
import cn.com.yeexun.documentManagement.entity.DocumentFile;
import cn.com.yeexun.documentManagement.service.IDocumentContentService;
import cn.com.yeexun.documentManagement.service.IDocumentFileService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("documentcontent")
public class DocumentContentController extends BaseController<DocumentContent> {

	private Logger logger = Logger.getLogger(DocumentContentController.class);
	
	@Autowired
	private IDocumentContentService documentContentService;
	@Autowired
	private IDocumentFileService documentFileService;
	
	@ResponseBody
	@RequestMapping(value = "/getMenuDocumentContent", method = RequestMethod.GET)
	@ApiOperation("获取文档目录菜单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest")
	})
	public String getMenuDocumentContent(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try {
			List<Object> list = documentContentService.getMenuDocumentContent();
			map.put(CodeUtil.RESULT_DATA, list);
			map.put("code",CodeUtil.CODE_SUCCESS);
			map.put("message", CodeUtil.CODE_SUCCESS_MESSAGE);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/addDocumentContent", method = RequestMethod.POST)
	@ApiOperation("添加文档目录")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "parentId", value = "父id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "name", value = "目录名称", required = true, dataType = "String"),
		@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest")
	})
	public String addDocumentContent(Long id, Long parentId, String name,
			Long userId,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		
		try {
			if(documentContentService.isExistContent(parentId, name)){
				map.put("code",CodeUtil.CODE_REQUEST_ERROR);
				map.put("message","该文件夹已存在！");
			}else{
				documentContentService.addDocumentContent(id, parentId, name, userId);
				map = CodeUtil.getSuccessMap();
			}
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/getLowerMenu", method = RequestMethod.GET)
//	@ApiOperation("")
//	public String getLowerMenu(Long id){
//		Map<String,Object> map = new HashMap<>();
//		try {
//			List<DocumentContent> list = documentContentService.getLowerMenu(id);
//			map.put(CodeUtil.RESULT_DATA, list);
//		} catch (Exception e) {
//			logger.error("error:", e);
//			map = CodeUtil.getSystemErrorMap();
//			e.printStackTrace();
//		}
//		
//		return JSON.toJSONString(map);
//	}
	@ResponseBody
	@RequestMapping(value = "/deleteFolder", method = RequestMethod.GET)
	@ApiOperation("删除目录")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest")
	})
	public String deleteFolder(Long id,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try {
			documentContentService.deleteFolder(id);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ApiOperation("上传word和pdf文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),
			@ApiImplicitParam(name = "name", value = "文件名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "request", value = "文件", required = true, dataType = "multipart/form-data")
	})
	public String upload(Long id,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = documentContentService.upload(id,map,request);
		} catch (Exception e) {
			e.printStackTrace();
			map = CodeUtil.getSystemErrorMap();
		}
        return JSON.toJSONString(map); 
	}
	
	@ResponseBody
	@RequestMapping(value = "/getDocumentFile", method = RequestMethod.GET)
	@ApiOperation("查询文件夹下的文件，带分页")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "start", value = "分页起始", required = true, dataType = "int"),
		@ApiImplicitParam(name = "length", value = "分页长度", required = true, dataType = "int")
	})
	public String getDocumentFile(Long id, Integer start, Integer length){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Page<DocumentFile> page = new Page<>();
			page.setStart(start);
			page.setLength(length);
			List<DocumentFile> lists = documentContentService.getDocumentFile(id, page);
			map.put(CodeUtil.RESULT_DATA, lists);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
	@ApiOperation("删除文件")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "request", value = "前端发的参数", required = true, dataType = "HttpServletRequest")
	})
	public String deleteFile(Long id,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			documentFileService.deleteFile(id);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateFile", method = RequestMethod.POST)
	@ApiOperation("更新word和pdf文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),
			@ApiImplicitParam(name = "name", value = "文件名", required = true, dataType = "string"),
			@ApiImplicitParam(name = "request", value = "文件", required = true, dataType = "multipart/form-data")
	})
	public String updateFile(Long id,String name,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			documentFileService.updateFile(id,name,map,request);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e){
	    	map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/downLoadFile", method = RequestMethod.GET)
	@ApiOperation("下载文件")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long")
		
	})
	public String downLoadFile(Long id, HttpServletRequest request, HttpServletResponse resp){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			documentFileService.downLoadFile(id, resp);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getSystemErrorMap();
			e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getFileNumByNowTime", method = RequestMethod.GET)
	@ApiOperation("获取近四周文件量")
	public String getFileNumByNowTime(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put(CodeUtil.RESULT_DATA, documentFileService.getFileNumByNowTime());
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
	public IBaseService<DocumentContent> service() {
		return documentContentService;
	}
	
}
