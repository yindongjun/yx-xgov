package cn.com.yeexun.meta_data.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.yeexun.metaModel.entity.MetamodelCombEntity;
import cn.com.yeexun.metaModel.entity.MetamodelEntity;
import cn.com.yeexun.metaModel.service.IMetamodelCombService;
import cn.com.yeexun.metaModel.service.IMetamodelService;
import cn.com.yeexun.meta_data.entity.Metadata;
import cn.com.yeexun.meta_data.entity.MetadataAttrEntity;
import cn.com.yeexun.meta_data.service.IMetadataAttrService;
import cn.com.yeexun.meta_data.service.IMetadataService;
import cn.com.yeexun.utils.CodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("meta_data")
@Api("元数据维护")
public class MetadataController {

	private Logger logger = Logger.getLogger(MetadataController.class);
	
	@Autowired
	private IMetadataService metadataService;
	
	@Autowired
	private IMetadataAttrService metadataAttrService;
//	
	@Autowired
	private IMetamodelCombService metamodelCombService;
//	
	@Autowired
	private IMetamodelService metamodelService;
	
	/*
	
	@ResponseBody
	@RequestMapping(value = "/addMenu",method = RequestMethod.GET)
	@ApiOperation("新建目录")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "parentId", value = "上级目录id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "menuName", value = "新建目录名称", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "type", value = "新建目录所属类型", required = true, dataType = "String")
	  })
	public String addMenu(Long parentId, String menuName, String type){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Metadata meta = new Metadata();
			if(null == parentId){
				meta.setParentId(0L);
			}else{
				meta.setParentId(parentId);
			}
			meta.setName(menuName);
			meta.setMetadataType(type);
			meta.setDeleteFlag(Constant.NOT_DELETE);
			meta.setIsMenu("Y");
			
			service().save(meta);
			map = CodeUtil.getSuccessMap();
		}  catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/editMenu",method = RequestMethod.GET)
	@ApiOperation("编辑目录")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "目录id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "menuName", value = "修改后目录名称", required = true, dataType = "String"),
	  })
	public String editMenu(Long id, String menuName){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Metadata meta = service().getById(id);
			meta.setName(menuName);
			service().save(meta);
			map = CodeUtil.getSuccessMap();
		}  catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/moveMenu",method = RequestMethod.GET)
	@ApiOperation("移动目录")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "目录id", required = true, dataType = "Long"),
	  })
	public String moveMenu(Long id, Long menuId){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Metadata meta = service().getById(id);
			meta.setParentId(menuId);
			service().save(meta);
			map = CodeUtil.getSuccessMap();
		}  catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delMenu",method = RequestMethod.GET)
	@ApiOperation("删除目录")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "目录id", required = true, dataType = "Long"),
	  })
	public String delMenu(Long id){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Metadata meta = service().getById(id);
			if("Y".equals(meta.getIsMenu())){
				List<Metadata> list = metadataService.getmetadataByPid(id);
				if(null == list || list.size() == 0){
					service().delete(id);
				}else{
					boolean exit = metadataService.existMeta(list);
					if(exit){
						throw new CommonException("该目录下包含元数据，请移到其他目录下再进行删除！！");
					}else{
						metadataService.delMetaByPid(id);
						
					}
				}
			}else{
				logger.info("其下的子元数据也会被删除，确定是否删除该元数据？");
			}
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e){
	    	map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
		} catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delMetadata",method = RequestMethod.GET)
	@ApiOperation("删除元数据")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "元数据id", required = true, dataType = "Long"),
	  })
	public String delMetadata(Long id){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Metadata meta = service().getById(id);
			if(null == meta.getCollectId()){
				meta.setDeleteFlag(Constant.IS_DELETE);
				metadataService.save(meta);
			}else{
				throw new CommonException("所选的元数据中包含采集任务获取的元数据，采集任务获取的元数据不能删除！");
			}
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e){
	    	map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
		} catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/addMeta",method = RequestMethod.GET)
	@ApiOperation("添加元数据")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "目录id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "modelId", value = "模型id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "name", value = "元数据名称", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "code", value = "元数据代码", required = true, dataType = "String")
	  })
	public String addMeta(Long id, Long modelId, String name, String code){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Metadata meta = new Metadata();
			meta.setParentId(id);
			meta.setName(name);
			meta.setCode(code);
			meta.setMetadataType("1");
			meta.setMetamodelId(modelId);
			meta.setDeleteFlag(Constant.NOT_DELETE);
			meta.setVersion(1);
			meta.setVersionDescription("新建");
			//设置创建用户
			service().save(meta);
			map = CodeUtil.getSuccessMap();
		
		} catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getVersion",method = RequestMethod.GET)
	@ApiOperation("获取元数据的版本信息")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "元数据的id", required = true, dataType = "Long")
	  })
	public String getVersion(Long id){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Metadata meta = metadataService.getById(id);
			List<Metadata> list = metadataService.getMetadataVersion(meta);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
		} catch (CommonException e){
	    	logger.error("获取元数据版本异常", e);
	    	map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
	    } catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/compareVersion",method = RequestMethod.GET)
	@ApiOperation("版本对比")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id1", value = "元数据的id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "id2", value = "元数据的id", required = true, dataType = "Long")
	  })
	public String compareVersion(Long id1, Long id2){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<String> name = new ArrayList<>();
			List<MetadataAttrEntity> attrs1 = metadataAttrService.getAttrbyMetaId(id1);
			List<MetadataAttrEntity> attrs2 = metadataAttrService.getAttrbyMetaId(id2);
			for(int i = 0; i < attrs1.size(); i++){
				if(!attrs1.get(i).getAttrValue().equals(attrs2.get(i).getAttrValue())){
					name.add(attrs1.get(i).getName());
				}
			}
			map = CodeUtil.getSuccessMap();
			map.put("data1", attrs1);
			map.put("data2", attrs2);
			map.put("name", name);
		} catch (CommonException e){
	    	logger.error("获取元数据版本异常", e);
	    	map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
	    } catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		
		return JSON.toJSONString(map);
	}*/
	
	@ResponseBody
	@RequestMapping(value = "/menu_list", method = RequestMethod.POST)
	@ApiOperation("依据元数据类型展示目录及依据元数据名称或目录进行查找")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "menuType", value = "元数据类型", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "searchKey", value = "查找元数据的目录名称或元数据名称", required = false, dataType = "String"),
	  })
	public String getMenu(String menuType, String searchKey, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//如果查询关键字存在，则查询相关的元数据，否则返回一级目录
			if(StringUtils.isNotEmpty(searchKey)){
				List<Metadata> list = new ArrayList<>();
				List<Metadata> metadataList = metadataService.serchMetadata(menuType, searchKey);
				if(metadataList != null && metadataList.size() > 0){
					//将查询到的相关的元数据以目录形式返回
					list = metadataService.getSerchMetadata(metadataList);
				}
				map = CodeUtil.getSuccessMap();
				map.put(CodeUtil.RESULT_DATA, list);
			}else{
				//返回第一级目录信息
				List<Metadata> menuList= metadataService.getMenuList(menuType);
				map = CodeUtil.getSuccessMap();
				map.put(CodeUtil.RESULT_DATA, menuList);
			}
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/findByMenuId", method = RequestMethod.GET)
	@ApiOperation("元数据维护目录Tree异步加载")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "menuId", value = "元数据id", required = true, dataType = "Long"),
	  })
	public String findByMenuId(Long menuId, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//查询目录下的子节点
			List<Metadata> menuList= metadataService.getmetadataByPid(menuId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, menuList);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/basic_info", method = RequestMethod.GET)
	@ApiOperation("元数据维护基本信息显示")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "元数据id", required = true, dataType = "Long"),
	  })
	public String findById(Long id, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Metadata metadata= metadataService.getById(id);
			//获取模型名称
			MetamodelEntity metamodel = metamodelService.getById(metadata.getMetamodelId());
			metadata.setMetaModelName(metamodel.getName());
			//属性值
			List<MetadataAttrEntity> attrs = metadataAttrService.getAttrbyMetaId(id);
			//组合关系
			List<MetamodelCombEntity> combins = metamodelCombService.getByComModelId(metadata.getMetamodelId());
			metadata.setAttrs(attrs);
			metadata.setCombins(combins);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, metadata);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	/*
	@ResponseBody
	@RequestMapping(value = "/getModelInfo", method = RequestMethod.GET)
	@ApiOperation("元模型属性展示")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "元数据id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "modelId", value = "元模型id", required = true, dataType = "Long")
	  })
	public String getModelInfo(Long id, Long modelId, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//title为根据元模型id查找元模型属性即MetamodelAttrEntity,前端直接设定吧
			List<MetamodelCombEntity> combins = metamodelCombService.getByComModelId(modelId);
			List<String> title = new ArrayList<>();
			for(MetamodelCombEntity combin : combins){
				title.add(combin.getModelName());
			}
			
			List<Metadata> metaList = metadataService.getmetaByPidAndModel(id, modelId);
			for(Metadata meta : metaList){
				List<MetadataAttrEntity> attrs = metadataAttrService.getAttrbyMetaId(meta.getId());
				if(null != attrs && attrs.size()>0){
					meta.setAttrs(attrs);
				}
			}
			JSONObject data = new JSONObject();
//			data.put("title", JSON.toJSONString(title));
			data.put("value", metaList);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, data);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateMetadataAndAttrs", method = RequestMethod.GET)
	@ApiOperation("元数据属性修改")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "元数据id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "name", value = "元数据名称", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "attrs", value = "元数据属性列表", required = true, dataType = "String"),
	  })
	public String updateMetadataAndAttrs(Long id, String name, String attrs, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Metadata meta = metadataService.getById(id);
			List<MetadataAttrEntity> attrList1 = JSONArray.parseArray(attrs, MetadataAttrEntity.class);
			List<MetadataAttrEntity> attrList2 = metadataAttrService.getAttrbyMetaId(meta.getId());
			//元数据的名称进行了修改
			if(StringUtils.isNotEmpty(name) && !name.equals(meta.getName()) 
					|| !(null==name && null==meta.getName())){
				Metadata newMeta = new Metadata();
				BeanUtils.copyProperties(meta, newMeta, new String[] {"id"});
				meta.setDeleteFlag(Constant.IS_DELETE);
				int version = meta.getVersion();
				metadataService.save(meta);
				
				newMeta.setName(name);
				newMeta.setVersion(version+1);
				newMeta.setVersionDescription("修改");
				metadataService.save(newMeta);
				
				List<Metadata> list = metadataService.getmetadataByPid(meta.getId());
				if(null!=list && list.size()>0){
					for(Metadata tmp:list){
						tmp.setParentId(newMeta.getId());
						metadataService.save(tmp);
					}
				}
				for(MetadataAttrEntity attr : attrList1){
					attr.setMetadataId(newMeta.getId());
					metadataAttrService.save(attr);
				}
			}else{
				if(!attrList1.equals(attrList2)){
					Metadata newMeta = new Metadata();
					BeanUtils.copyProperties(meta, newMeta, new String[] {"id"});
					meta.setDeleteFlag(Constant.IS_DELETE);
					int version = meta.getVersion();
					metadataService.save(meta);
					newMeta.setVersion(version+1);
					newMeta.setVersionDescription("修改");
					metadataService.save(newMeta);
					
					List<Metadata> list = metadataService.getmetadataByPid(meta.getId());
					if(null!=list && list.size()>0){
						for(Metadata tmp:list){
							tmp.setParentId(newMeta.getId());
						}
					}
					for(MetadataAttrEntity attr : attrList1){
						attr.setMetadataId(newMeta.getId());
						metadataAttrService.save(attr);
					}
				}
			}
			
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation(value = "根据数据源id获取未发布的采集结果的采集历史id", consumes="application/x-www-form-urlencoded")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "sourceId", value = "数据源id", required = true, dataType = "long")
	})
	@RequestMapping(value = "/unrelease-collecthis",method = RequestMethod.GET)
	@ResponseBody
	public String findUnreleaseCollectHis(Long sourceId, Integer start, Integer length) {
		Map<String,Object> map = null;
		try {
			Page<DispatchHistory> page = new Page<>();
			page.setStart(start);
			page.setLength(length);
			List<DispatchHistory> dispatchHistories = metadataService.findUnreleaseCollectHisPage(sourceId, page);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, dispatchHistories);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (CommonException e) {
			logger.info("查询未发布的采集历史出错：", e);
			map = CodeUtil.getBadRequestMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation(value = "版本比对", consumes="application/x-www-form-urlencoded")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "sourceId", value = "数据源id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "collectHisId", value = "采集历史id", required = true, dataType = "long")
	})
	@RequestMapping(value = "/compare",method = RequestMethod.GET)
	@ResponseBody
	public String compareVersionByCollectHidId(Long sourceId, Long collectHisId) {
		Map<String,Object> map = null;
		try {
			MetadataCompareVo compareVo 
					= metadataService.compareUnreleaseVersWithLastestReleaseVer(sourceId, collectHisId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, compareVo);
		} catch (CommonException e) {
			logger.info("版本比对出错：", e);
			map = CodeUtil.getBadRequestMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation(value = "版本审核", consumes="application/x-www-form-urlencoded")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "collectHisId", value = "采集历史id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "sourceId", value = "采集数据源id", required = true, dataType = "long"),
		@ApiImplicitParam(name = "status", value = "1通过，2驳回", required = true, dataType = "long"),
		@ApiImplicitParam(name = "desc", value = "描述信息", required = false, dataType = "string"),
	})
	@RequestMapping(value = "/review", method = RequestMethod.POST)
	@ResponseBody
	public String reviewVersion(Long collectHisId, Long sourceId, Integer status, String desc) {
		Map<String,Object> map = null;
		try {
			metadataService.reviewVersion(collectHisId, sourceId, status, desc);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			logger.info("版本审核出错：", e);
			map = CodeUtil.getBadRequestMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/search_meta",method = RequestMethod.GET)
	@ApiOperation("查找元数据-元数据分析")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "name", value = "元数据名称或代码", required = true, dataType = "String"),
	  })
	public String serchMeta(String name, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = transferRequestParamToMap(request);
			Page<Metadata> page = new Page<Metadata>();
			page.setStart(MapUtils.getIntValue(paramMap, "start", 0));
			page.setLength(MapUtils.getIntValue(paramMap, "length", 10));
			List<Metadata> metadataList = metadataService.serchMetadataV2Page(name, page);
			page.setData(metadataList);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, metadataList);
			map.put(CodeUtil.RESULT_DRAW, request.getParameter("draw"));
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
 		} catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}*/
	
	
}
