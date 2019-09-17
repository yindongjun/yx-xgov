package cn.com.yeexun.metaModel.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.common.controller.BaseController;
import cn.com.yeexun.metaModel.entity.MetamodelAttrEntity;
import cn.com.yeexun.metaModel.entity.MetamodelCombEntity;
import cn.com.yeexun.metaModel.entity.MetamodelDepEntity;
import cn.com.yeexun.metaModel.entity.MetamodelEntity;
import cn.com.yeexun.metaModel.service.IMetamodelAttrService;
import cn.com.yeexun.metaModel.service.IMetamodelCombService;
import cn.com.yeexun.metaModel.service.IMetamodelDepService;
import cn.com.yeexun.metaModel.service.IMetamodelService;
import cn.com.yeexun.meta_data.entity.Metadata;
//import cn.com.yeexun.meta_data.entity.MetadataCompareVo;
import cn.com.yeexun.meta_data.service.IMetadataService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;

@Controller
@RequestMapping("meta_model")
@Api("元数据模型")
public class MetamodelController extends BaseController<MetamodelEntity>{

	private Logger logger = Logger.getLogger(MetamodelController.class);

	@Autowired
	private IMetamodelCombService metamodelCombService;

	@Autowired
	private IMetamodelAttrService metamodelAttrService;

	@Autowired
	private IMetamodelDepService metamodelDepService;

//	@Autowired
//	private IMetamodelService metamolService;

	@Autowired
	private IMetamodelService metamodelService;

	@Autowired
	private IMetadataService metadataService;

	@Override
	public IBaseService<MetamodelEntity> service() {
		return metamodelService;
	}
	
	@ResponseBody
	@RequestMapping(value = "/findDepModel",method = RequestMethod.GET)
	@ApiOperation("获取依赖模型")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "modelId", value = "元模型的id", required = true, dataType = "Long")
	  })
	public String findDepModel(Long modelId){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Set<Long> ids1 = new HashSet<>();
			List<MetamodelDepEntity> modelDeps= metamodelDepService.getByModelId(modelId);
			for(MetamodelDepEntity model:modelDeps){
				ids1.add(Long.valueOf(model.getModelId()));
				ids1.add(Long.valueOf(model.getDepModelId()));
			}
			List<Long> ids = new ArrayList<>(ids1);
			List<MetamodelEntity> models = metamodelService.listByIds(ids);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, models);
	    }catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/findDepMetaTree",method = RequestMethod.GET)
	@ApiOperation("获取依赖模型树形显示列表（关联关系）")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "modelId", value = "元模型的id", required = true, dataType = "Long"),
	  })
	public String findDepMetaTree(Long modelId, Long selectModelId, String searchKey){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Metadata> result = new ArrayList<>();
			if(null != selectModelId){
//				List<Metadata> metaList = metadataService.getmetadataByModelId(selectModelId, searchKey);
//				result = metadataService.getmetaLateMenu(metaList);
			}else{
				Set<Long> ids1 = new HashSet<>();
				List<MetamodelDepEntity> modelDeps= metamodelDepService.getByModelId(modelId);
				for(MetamodelDepEntity model:modelDeps){
					ids1.add(Long.valueOf(model.getModelId()));
					ids1.add(Long.valueOf(model.getDepModelId()));
				}
				for(Long id : ids1){
//					List<Metadata> metaList = metadataService.getmetadataByModelId(id, searchKey);
//					List<Metadata> result2 = metadataService.getmetaLateMenu(metaList);
//					result.addAll(result2);
				}
			}
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, result);
	    }catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value="/getMetamodelAttr" ,method = RequestMethod.GET)
	@ApiOperation("获取元模型属性")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "元模型id" , value = "modelId" ,required = true , dataType = "Long")
			)
	public String getMetamodelAttr(Long modelId) {
		Map<String ,Object> map = new HashMap<String , Object>();
		try {
			List<MetamodelAttrEntity> list = metamodelAttrService.findMetamodelAttrByid(modelId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
		} catch (Exception e) {
			logger.error("error: ",e);
			map=CodeUtil.getErrorRequestMap();
		}
		
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value="/getMetamodelComb" ,method = RequestMethod.GET)
	@ApiOperation("获取元模型组合关系")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "元模型id" , value = "modelId" ,required = true , dataType = "Long")
			)
	public String getMetamodelComb(Long modelId) {
		Map<String ,Object> map = new HashMap<String , Object>();
		Map<String ,Object> comMap = new HashMap<String , Object>();
		try {
			List<MetamodelCombEntity> list = metamodelCombService.getAllByComModelId(modelId);
			List<MetamodelCombEntity> com = new ArrayList<MetamodelCombEntity>();
			List<MetamodelCombEntity> comed = new ArrayList<MetamodelCombEntity>();
			for(MetamodelCombEntity  modelCom : list) {
				if(Long.valueOf(modelCom.getModelId()) == modelId ) {
					com.add(modelCom);
				}
				if(Long.valueOf(modelCom.getCombinModelId()) == modelId ){
					comed.add(modelCom);
				}
			}
			comMap.put("com", com);
			comMap.put("comed", comed);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA,comMap);
		} catch (Exception e) {
			logger.error("error: ",e);
			map=CodeUtil.getErrorRequestMap();
		}
		
		return JSON.toJSONString(map,SerializerFeature.DisableCircularReferenceDetect);
	}
	
	@ResponseBody
	@RequestMapping(value="/getMetamodelDep" ,method = RequestMethod.GET)
	@ApiOperation("获取元模型依赖关系")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "元模型id" , value = "modelId" ,required = true , dataType = "Long")
			)
	public String getMetamodelDep(Long modelId) {
		Map<String ,Object> map = new HashMap<String , Object>();
		Map<String ,Object> depMap = new HashMap<String , Object>();
		try {
			List<MetamodelDepEntity> list = metamodelDepService.getByModelId(modelId);
			List<MetamodelDepEntity> dep = new ArrayList<MetamodelDepEntity>();
			List<MetamodelDepEntity> deped = new ArrayList<MetamodelDepEntity>();
			for(MetamodelDepEntity  modelDep : list) {
				if(Long.valueOf(modelDep.getModelId()) == modelId) {
					dep.add(modelDep);
				}
				if(Long.valueOf(modelDep.getDepModelId()) == modelId){
					deped.add(modelDep);
				}
			}
			depMap.put("dep", dep);
			depMap.put("deped", deped);
			map = CodeUtil.getSuccessMap();
//			map.put("dep", dep);
//			map.put("deped", deped);
			map.put(CodeUtil.RESULT_DATA,depMap);
		} catch (Exception e) {
			logger.error("error: ",e);
			map=CodeUtil.getErrorRequestMap();
		}
		
		return JSON.toJSONString(map,SerializerFeature.DisableCircularReferenceDetect);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getMetamodelMenu", method = RequestMethod.GET)
	@ApiOperation("获取元模型菜单")
	@ApiImplicitParams({
	})
	public String getMetamodelMenu(Long modelId , HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<MetamodelEntity> menuMetamodle = metamodelService.getMetamodelMenu(modelId);
			map.put(CodeUtil.RESULT_DATA, menuMetamodle);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value="/fuzzyQueryMetamodel", method = RequestMethod.GET)	
	@ApiOperation("根据name和code,模糊查询元模型")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "包含元模型id" , value = "request" )
	})
	public String fuzzyQueryMetamodel(String name,String code,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			List<List> list = metamodelService.fuzzyQueryMetamodel(name, code);
			
			map.put(CodeUtil.RESULT_DATA,list);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value="/itemListMetamodel", method = RequestMethod.GET)	
	@ApiOperation("列出元模型子菜单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "包含元模型id" , value = "request" )
	})
	public String itemList(Long id ,HttpServletRequest request) {
		Map<String ,Object> map = new HashMap<String ,Object>();
		try {
			List<MetamodelEntity> list = metamodelService.itemListMetamodel(id);
			map.put(CodeUtil.RESULT_DATA, list);
			
		} catch (Exception e) {
			logger.error("error",e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
}
