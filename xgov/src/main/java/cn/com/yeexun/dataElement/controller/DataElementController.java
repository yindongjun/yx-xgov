package cn.com.yeexun.dataElement.controller;

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

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
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
import cn.com.yeexun.businessTerms.entity.CodeSet;
import cn.com.yeexun.businessTerms.service.ICodeSetService;
import cn.com.yeexun.common.controller.BaseController;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataElement.entity.RelationShipEntity;
import cn.com.yeexun.dataElement.service.IDataElementService;
import cn.com.yeexun.dataElement.service.IRelationShipService;
import cn.com.yeexun.dataSet.service.IElementSetService;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.service.IDispatchTaskService;
import cn.com.yeexun.meta_data.entity.Metadata;
import cn.com.yeexun.meta_data.entity.MetadataAttrEntity;
import cn.com.yeexun.meta_data.entity.MetadataInfo;
import cn.com.yeexun.meta_data.service.IMetadataAttrService;
import cn.com.yeexun.meta_data.service.IMetadataService;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;
import cn.com.yeexun.qualityTask.service.IDesignTableInfoService;
import cn.com.yeexun.qualityTask.service.IQualityTaskDetailService;
import cn.com.yeexun.standardAudit.entity.StandardAudit;
import cn.com.yeexun.standardAudit.service.IStandardAuditService;
import cn.com.yeexun.user.entity.UserDto;
import cn.com.yeexun.user.service.UserService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import cn.com.yeexun.utils.FileUtil;
import cn.com.yeexun.utils.PropertyPlaceholder;
import cn.com.yeexun.utils.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("data_element")
@Api("数据元")
public class DataElementController extends BaseController<DataElementEntity>{

	private Logger logger = Logger.getLogger(DataElementController.class);
	
	@Autowired
	private IDataElementService dataElementService;
	
	@Autowired
	private IRelationShipService relationShipService;
	
	@Autowired
	private IStandardAuditService standardAuditService;
	
	@Autowired
	private IMetadataService metadataService;
	
	@Autowired
	private IMetadataAttrService metadataAttrService;
	
	@Autowired
	private ICodeSetService codeSetService;
	
	@Autowired
	private UserService userService;
	
//	@Autowired
//	private IQualityRuleDetailService qualityRuleDetailService;
	
//	@Autowired
//	private IDesignSourceInfoService designSourceService;
	
	@Autowired
	private IDesignTableInfoService designTableService;
	
	@Autowired
	private IQualityTaskDetailService qualityTaskDetailService;
	
	@Autowired
	private IDispatchTaskService dispatchTaskService;
	
	@Autowired
	private IElementSetService elementSetService;
	
	@Override
	public IBaseService<DataElementEntity> service() {
		return dataElementService;
	}

	@ResponseBody
	@RequestMapping(value = "/dataElement_list",method = RequestMethod.POST)
	@ApiOperation("数据元列表显示")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "dataSourceId", value = "数据源id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "tableName", value = "表名", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "status", value = "数据元状态值", required = true, dataType = "int"),
	      @ApiImplicitParam(name = "dataElementType", value = "数据元类型", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "dataElementName", value = "数据元名称", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "start", value = "起始记录数", required = true, dataType = "int"),
	      @ApiImplicitParam(name = "length", value = "显示记录条数", required = true, dataType = "int"),
	  })
	public String showDataElement(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DataElementEntity> list = new ArrayList<>();
			Map<String, Object> paramMap = transferRequestParamToMap(request);
			long dataSourceId = MapUtils.getLongValue(paramMap, "dataSourceId");
			String tableName = MapUtils.getString(paramMap, "tableName");
			String status1 = MapUtils.getString(paramMap, "status");
			Integer status = null;
			if(StringUtils.isNotEmpty(status1)){
				status = Integer.parseInt(status1);
			}
//			int status = MapUtils.getIntValue(paramMap, "status");
			String dataElementType = MapUtils.getString(paramMap, "dataElementType");
			String dataElementName = MapUtils.getString(paramMap, "dataElementName");
			Page<DataElementEntity> page = new Page<DataElementEntity>();
			page.setStart(MapUtils.getIntValue(paramMap, "start", 0));
			page.setLength(MapUtils.getIntValue(paramMap, "length", 10));
			list = dataElementService.showListByStatusPage(dataSourceId, tableName, status, dataElementType, dataElementName, page);
			page.setData(list);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
			map.put(CodeUtil.RESULT_DRAW, request.getParameter("draw"));
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("数据元列表显示出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/get_all_element",method = RequestMethod.GET)
	@ApiOperation("数据元列表显示已审核")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "dataSourceId", value = "数据源id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "tableName", value = "表名", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "dataElementType", value = "数据元类型", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "dataElementName", value = "数据元名称", required = true, dataType = "String"),
	  })
	public String getAllElement(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DataElementEntity> list = new ArrayList<>();
			Map<String, Object> paramMap = transferRequestParamToMap(request);
			long dataSourceId = MapUtils.getLongValue(paramMap, "dataSourceId");
			String tableName = MapUtils.getString(paramMap, "tableName");
			String dataElementType = MapUtils.getString(paramMap, "dataElementType");
			String dataElementName = MapUtils.getString(paramMap, "dataElementName");
			list = dataElementService.getAllElement(dataSourceId, tableName, dataElementType, dataElementName);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
			map.put(CodeUtil.RESULT_DRAW, request.getParameter("draw"));
		} catch (Exception e) {
			logger.error("数据元列表显示出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/addDataElement",method = RequestMethod.POST)
	@ApiOperation("新建及编辑数据元")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据元id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "dataElementName", value = "数据元名称", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "dataElementCode", value = "数据元代码", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "dataElementType", value = "数据元类型", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "dataElementAttr", value = "数据元类型值", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "status", value = "数据元状态", required = true, dataType = "Integer"),
	      @ApiImplicitParam(name = "definition", value = "数据元定义", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "codeset", value = "数据元值域id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "regularExpression", value = "数据元正则表达式", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "remark", value = "数据元备注", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "createTime", value = "数据元创建时间", required = true, dataType = "Date"),
	      @ApiImplicitParam(name = "dataRangeFront", value = "数据元数据范围起始值", required = true, dataType = "Integer"),
	      @ApiImplicitParam(name = "dataRangeEnd", value = "数据元数据范围结束值", required = true, dataType = "Integer"),
	      @ApiImplicitParam(name = "isdel", value = "数据元删除标志位", required = true, dataType = "Integer"),
	      @ApiImplicitParam(name = "standardAlias", value = "标准别名", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "standardType", value = "标准类型", required = true, dataType = "String")
	  })
	public String addDataElement(DataElementEntity dataElement){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String uniqueCode = dataElement.getDataElementCode() + dataElement.getDataElementType();
			DataElementEntity meta = dataElementService.getByUniqueCode(uniqueCode);
			String codeSetIdStr = dataElement.getCodeset();
			if (StringUtils.isNotEmpty(codeSetIdStr)) {
				long codeSetId = Long.parseLong(codeSetIdStr);
				CodeSet codeSet = codeSetService.getById(codeSetId);
				if ("1".equals(codeSet.getIsCodeset())) {
					throw new CommonException("值域请选择代码集！");
				}
			}
			//判断为新增操作还是编辑操作
			if(0 == dataElement.getId()){
				if(null != meta){
					throw new CommonException("英文名称加数据类型形成的唯一标识码已存在");
				}else{
					dataElement.setUniqueCode(uniqueCode);
					dataElement.setStatus(0);
					dataElement.setIsdel(Constant.NOT_DELETE);
				}
				// 根据英文名称判断是否已经存在；
				DataElementEntity example = new DataElementEntity();
				example.setDataElementCode(dataElement.getDataElementCode());
				example.setIsdel(String.valueOf(Constant.NOT_DELETE));
				List<DataElementEntity> dataElements = dataElementService.query(example); 
				if (dataElements != null && dataElements.size() > 0) {
					throw new CommonException("英文名称重复！");
				}
			}else{
				DataElementEntity meta2 = dataElementService.getById(dataElement.getId());
				dataElement.setStatus(meta2.getStatus());
				
			}
			if(3 == dataElement.getStatus()){
				dataElement.setStatus(DataElementEntity.STATUS_CHANGE);
			}else{
				dataElement.setStatus(DataElementEntity.STATUS_DRAFT);
			}
			
			dataElementService.save(dataElement);
			// 判断数据元修改是否会影响到调度,如果会就先将调度暂停，并将design_table_info中对应的记录状态设置为草稿中
			/*List<QualityTaskDetail> taskDetails = qualityTaskDetailService.findByElementDistinct(
					dataElement.getId());
			if (taskDetails != null && taskDetails.size() > 0) {
				for (QualityTaskDetail detail : taskDetails) {
					DesignTableInfo tableInfo = designTableService.getBySourceAndTable(
							detail.getDatasourceId(), detail.getTableName());
					if (tableInfo != null) {
						tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
						designTableService.update(tableInfo);
						DispatchTask dispatchTask = dispatchTaskService.findByTaskIdAndType(tableInfo.getId(), "1");
						if (dispatchTask != null) {
							dispatchTaskService.pause(dispatchTask.getId());
						}
					}
				}
			}*/
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e){
	    	map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
		} catch (Exception e) {
			logger.error("新建或编辑数据元出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/sendAudit",method = RequestMethod.GET)
	@ApiOperation("数据元送审")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "ids", value = "需要送审的数据元id", required = true, dataType = "String"),
	  })
	public String sendAudit(String ids, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(StringUtils.isBlank(ids)){
				return JSON.toJSONString(CodeUtil.getBadRequestMap());
			}
			String token = request.getHeader("token");
			UserDto currentUser = userService.currentUser(token);
			String[] item = ids.split(",");
			for (String idStr : item) {
				long id = Long.parseLong(idStr);
				DataElementEntity element = service().getById(id);
				element.setFrontStatus(element.getStatus());
				element.setStatus(1);
				service().save(element);
				//保存到审核表中去
				//这里不能每次新建审核，要改变原有的数据元的审核的状态 20190227 ywz
				StandardAudit standardAudit  = standardAuditService.getByFlowId("0", id);
				if(standardAudit == null){
					standardAudit = new StandardAudit();
				}
				standardAudit.setFlowId((int)id);
				standardAudit.setStatus("0");
				standardAudit.setCreateTime(new Date());
				standardAudit.setLastModifyTime(new Date());
				standardAudit.setTaskName(element.getDataElementName());
				//送审人
				standardAudit.setSubmitter(currentUser.getName());
				standardAudit.setType("0");
				standardAudit.setSubmitTime(new Date());
				standardAuditService.save(standardAudit);
			}
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("数据元送审出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteElement",method = RequestMethod.GET)
	@ApiOperation("数据元删除")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "ids", value = "需要删除的数据元id", required = true, dataType = "String"),
	  })
	public String deleteElement(String ids){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(StringUtils.isBlank(ids)){
				return JSON.toJSONString(CodeUtil.getBadRequestMap());
			}
			String[] item = ids.split(",");
			List<DataElementEntity> elementList = new ArrayList<>();
			for (String idStr : item) {
				long id = Long.parseLong(idStr);
				DataElementEntity element = service().getById(id);
				int status = element.getStatus();
				if(DataElementEntity.STATUS_DRAFT == status 
						|| DataElementEntity.STATUS_REJECT == status 
						|| DataElementEntity.STATUS_CHANGE == status){
					element.setIsdel(Constant.IS_DELETE);
					service().save(element);
				} else {
					elementList.add(element);
				}
			}
			if(elementList.size()>0){
				throw new CommonException("只有草稿、变更中和已退回的数据元可以被删除！");
			}
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e){
			logger.warn("数据元删除异常:", e);
		    map = CodeUtil.getErrorRequestMap();
		    map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("数据元删除错误:", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/relation_ship",method = RequestMethod.GET)
	@ApiOperation("数据元相关的关联关系列表展示")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "dataElementId", value = "数据元Id", required = true, dataType = "Long"),
	  })
	public String getRelationShip(Long dataElementId){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<RelationShipEntity> list = relationShipService.getByElementId(dataElementId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
		} catch (Exception e) {
			logger.error("数据元关联关系列表显示错误:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/unbindRelation",method = RequestMethod.GET)
	@ApiOperation("解除关联关系")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "dataElementId", value = "数据元Id", required = true, dataType = "Long"),
	  })
	public String unbindRelation(Long dataSoureId, String tableName, String column){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			RelationShipEntity relation = relationShipService.getByColumn(dataSoureId, tableName, column);
			relationShipService.delete(relation);
			qualityTaskDetailService.remove(dataSoureId, tableName, column);
			List<QualityTaskDetail> qualityTaskDetails = qualityTaskDetailService.findByDesignTableId(dataSoureId, tableName);
			DesignTableInfo tableInfo = designTableService.getBySourceAndTable(dataSoureId, tableName);
			if (tableInfo != null) {
				if (qualityTaskDetails == null || qualityTaskDetails.size() <= 0) {
					tableInfo.setStatus(DesignTableInfo.STATUS_UNCOMMIT);
				} else {
					tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
				}
				designTableService.update(tableInfo);
				DispatchTask dispatchTask = dispatchTaskService.findByTaskIdAndType(tableInfo.getId(), "1");
				if (dispatchTask != null) {
					dispatchTaskService.pause(dispatchTask.getId());
				}
			}
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("解除关联关系出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/saveRelation",method = RequestMethod.POST)
	@ApiOperation("保存关联关系")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "dataElementId", value = "数据元Id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "relationJson", value = "关联关系JSON", required = true, dataType = "String")
	  })
	public String saveRelation(Long dataElementId, String relationJson){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			dataElementService.saveRelation(dataElementId, relationJson);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("数据元关联关系保存出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	
	
	@ResponseBody
	@RequestMapping(value = "/check_relation",method = RequestMethod.GET)
	@ApiOperation("数据元删除关联关系校验")
	@ApiImplicitParams({
	  })
	public String checkRelation(Long sourceId, String tableName, String column, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			boolean isDelete = dataElementService.checkRelation(sourceId, tableName, column);
			if(isDelete){
				throw new CommonException("质量规则已配置该关联关系，不能解除该关联关系");
			}else{
				map = CodeUtil.getSuccessMap();
			}
		} catch(CommonException e){
			logger.error("数据元校验出错:", e);
			map = CodeUtil.getErrorMap(e.getMessage());
		    e.printStackTrace();
		} catch (Exception e) {
			logger.error("数据元校验出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/change_element",method = RequestMethod.POST)
	@ApiOperation("数据元变更")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据元id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "changeInfo", value = "变更原因", required = true, dataType = "String")
	  })
	public String changeElement(Long id, String changeInfo, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DataElementEntity set = service().getById(id);
			set.setChangeInfo(changeInfo);
			set.setStatus(DataElementEntity.STATUS_CHANGE);
			service().save(set);
//			StandardAudit standAudit = standardAuditService.getByFlowId("0", id);
//			standardAuditService.delete(standAudit);
			// 判断数据元修改是否会影响到调度,如果会就先将调度暂停，并将design_table_info中对应的记录状态设置为草稿中
			List<QualityTaskDetail> taskDetails = qualityTaskDetailService.findByElementDistinct(id);
			if (taskDetails != null && taskDetails.size() > 0) {
				for (QualityTaskDetail detail : taskDetails) {
					DesignTableInfo tableInfo = designTableService.getBySourceAndTable(
							detail.getDatasourceId(), detail.getTableName());
					if (tableInfo != null) {
						tableInfo.setStatus(DesignTableInfo.STATUS_DRAFT);
						designTableService.update(tableInfo);
						/*DispatchTask dispatchTask = dispatchTaskService.findByTaskIdAndType(tableInfo.getId(), "1");
						if (dispatchTask != null) {
							dispatchTaskService.pause(dispatchTask.getId());
						}*/
					}
				}
			}
			// 解除关联关系
			//删除与该数据元相关的关联关系
			Integer status = set.getStatus();
			if(0==status || 3==status || 4==status){
				List<RelationShipEntity> shipList = relationShipService.getByElementId(id);
				if(null != shipList && shipList.size()>0){
					for(RelationShipEntity ship : shipList){
						ship.setIsdel(Constant.IS_DELETE);
						relationShipService.save(ship);
						// 判断影响到的design_table_info 是否要回到带配置状态
						/*List<QualityTaskDetail> qualityTaskDetails = qualityTaskDetailService
								.findByDesignTableId(ship.getSourceId(), ship.getTablename());
						if (qualityTaskDetails == null || qualityTaskDetails.size() <= 0) {
							DesignTableInfo tableInfo = designTableService.getBySourceAndTable(ship.getSourceId(), ship.getTablename());
							tableInfo.setStatus(DesignTableInfo.STATUS_UNCOMMIT);
						}*/
					}
				}
				//状态为变更中的数据元删除时要更新元数据中的版本
				if(3==status){
					Metadata meta = new Metadata();
					meta.setCode(set.getDataElementCode());
					meta.setMetamodelId(21L);
					List<Metadata> metas = metadataService.getMetaByUniqueCode(meta);
					List<Long> ids2 = new ArrayList<>();
					for(Metadata model:metas){
						ids2.add(model.getId());
					}
					metadataService.deleteList(ids2);
					Metadata newMeta = new Metadata();
					//复制实体类
					BeanUtils.copyProperties(metas.get(0), newMeta, new String[] {"id"});
					newMeta.setVersion(metas.get(0).getVersion()+1);
					newMeta.setCreateTime(new Date());
					newMeta.setDeleteFlag(Constant.IS_DELETE);
//					newMeta.setCreateUserId(createUserId);
					newMeta.setLastModifyTime(new Date());
					newMeta.setVersionDescription("删除");
					metadataService.save(newMeta);
					//保存元数据属性值
					for(int i = 1; i < 14; i++){
						MetadataAttrEntity attr = new MetadataAttrEntity();
						attr.setMetadataId(newMeta.getId());
						attr.setModelAttrId((long)i);
						switch(i){
							case 1:
								attr.setAttrValue(set.getDataElementName());
								break;
							case 2:
								attr.setAttrValue(set.getDataElementCode());
								break;
							case 3:
								attr.setAttrValue(set.getDataElementType());
								break;
							case 4:
								attr.setAttrValue(set.getDataElementAttr());
								break;
							case 5:
								attr.setAttrValue(set.getDefinition());
								break;
							case 6:
								//需要修改为值域名称
//								attr.setAttrValue(element.getCodeset().toString());
								if(StringUtils.isNotBlank(set.getCodeset())){
									Long codeSet = Long.parseLong(set.getCodeset());
									CodeSet code = codeSetService.echoCodeSet(codeSet);
									if(null!=code){
										attr.setAttrValue(code.getName());
									}
								}
								break;
							case 7:
								if(null != set.getDataRangeFront() && null != set.getDataRangeEnd()){
									String range = set.getDataRangeFront() + "~" + set.getDataRangeEnd();
									attr.setAttrValue(range);
								}
								break;
							case 8:
								attr.setAttrValue(set.getRegularExpression());
								break;
							case 9:
								attr.setAttrValue(set.getRemark());
								break;
							case 10:
								//数据元分类
//								attr.setAttrValue(element.getDataElementFlag());
								break;
							case 11:
								attr.setAttrValue(set.getCreateTime().toString());
								break;
							case 12:
								attr.setAttrValue(set.getLastModifyTime().toString());
								break;
							case 13:
								attr.setAttrValue(newMeta.getVersion().toString());
								break;
							default:
							    //...;
							    break;
						}
						metadataAttrService.save(attr);
					}
				}
			}
			
			
			
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("数据元变更出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/back",method = RequestMethod.POST)
	@ApiOperation("数据元撤回操作，撤回后状态变为草稿")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据元id", required = true, dataType = "Long")
	  })
	public String back(Long id) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DataElementEntity dataElement = dataElementService.getById(id);
			dataElement.setStatus(DataElementEntity.STATUS_DRAFT);
			dataElementService.save(dataElement);
			// 将审核表的记录更改为撤回状态
			StandardAudit standardAudit  = standardAuditService.getByFlowId("0", id);
			standardAudit.setStatus(StandardAudit.STATUS_BACK);
			standardAuditService.save(standardAudit);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("数据元撤回出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/history",method = RequestMethod.GET)
	@ApiOperation("数据元历史版本信息")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "dataElementId", value = "数据元Id", required = true, dataType = "Long"),
	  })
	public String history(Long dataElementId, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = transferRequestParamToMap(request);
			Page<Metadata> page = new Page<Metadata>();
			page.setStart(MapUtils.getIntValue(paramMap, "start", 0));
			page.setLength(MapUtils.getIntValue(paramMap, "length", 10));
			DataElementEntity element = service().getById(dataElementId);
			Metadata meta1 = new Metadata();
			meta1.setCode(element.getDataElementCode());
			meta1.setMetamodelId(21L);
//			List<Metadata> list = metadataService.getMetadataVersion(meta1);
			List<Metadata> list = metadataService.getMetadataVersionPage(meta1,page);
			if(null != list && list.size() > 0){
				for(Metadata meta : list){
					//属性值
					List<MetadataAttrEntity> attrs = metadataAttrService.getAttrbyMetaId(meta.getId());
					meta.setAttrs(attrs);
				}
			}
			page.setData(list);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
			map.put(CodeUtil.RESULT_DRAW, request.getParameter("draw"));
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("数据元查看历史版本出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/editElement",method = RequestMethod.GET)
	@ApiOperation("数据元编辑")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据元id", required = true, dataType = "Long"),
	  })
	public String editElement(Long id){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DataElementEntity element = service().getById(id);
			if(StringUtils.isNotBlank(element.getCodeset())){
				CodeSet codeset = codeSetService.getById(Long.parseLong(element.getCodeset()));
				element.setCodesetName(codeset.getName());
			}
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, element);
		} catch (Exception e) {
			logger.error("数据元送审出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/get_element_count",method = RequestMethod.GET)
	@ApiOperation("获取数据元总个数及有关联关系个数和无关联关系个数")
	@ApiImplicitParams({
	  })
	public String getElementCount(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Integer> count = dataElementService.getElementCountInfo();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, count);
		} catch (Exception e) {
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/get_new_5",method = RequestMethod.GET)
	@ApiOperation("获取新建数据元的前5条数据")
	@ApiImplicitParams({
	  })
	public String getNew5(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DataElementEntity> newAdd = dataElementService.getElementTop5();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, newAdd);
		} catch (Exception e) {
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/importFromExcel",method = RequestMethod.POST)
	@ApiOperation("从Excel中导入数据元")
	@ApiImplicitParams({
	  })
	public String importFromExcel(HttpServletRequest request) {
//		System.out.println("-----------------------------------" + request.getSession().getServletContext()
//								.getRealPath("/WEB-INF/upload"));
		Map<String, Object> map = CodeUtil.getSuccessMap();
		File newfile = null;
		try {
			// 将当前上下文初始化给 CommonsMutipartResolver（多部分解析器）
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			// 检查form中是否有enctype="multipart/form-data"
			if (multipartResolver.isMultipart(request)) {
//				map = CodeUtil.getSuccessMap();
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
//								.getRealPath("/WEB-INF/upload");
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
						String originalFilename = file.getOriginalFilename();
						if (!originalFilename.substring(originalFilename.lastIndexOf("."), 
								originalFilename.length()).equals(".xlsx")) {
							throw new CommonException("文件格式错误，下载正确的模板填写后重新上传！");
						}
						// 上传
						newfile = new File(path, file.getOriginalFilename());
						// 上传前检查文件是否存在
						file.transferTo(newfile);
					}
				}
			} else {
				throw new CommonException("文件上传失败！");
			}
			// 读取Excel文件
			if (newfile != null) {
				dataElementService.importFromExcel(newfile);
			} else {
				throw new CommonException("上传的文件不存在，请检查文件是否上传成功！");
			}
			
		}catch (CommonException e) {
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
			logger.warn("从Excel文件导入数据元异常:", e);
		} catch (IllegalStateException e) {
			map = CodeUtil.getSystemErrorMap();
			logger.error("从Excel文件导入数据元异常", e);
		}  catch (IOException e) {
			map = CodeUtil.getSystemErrorMap();
			logger.error("从Excel文件导入数据元异常", e);
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
		
		
//		System.out.println("下载Excel模板=============" 
//				+ request.getSession().getServletContext().getRealPath("/") 
//				+ PropertyPlaceholder.getContextProperty("excelTemplateDir") 
//				+ PropertyPlaceholder.getContextProperty("dataelementTemplateFile"));
		
		
		/*try {
			String path = ResourceUtils.getURL("classpath:").getPath();
			System.out.println("----------------------"
					+ path);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		
		Map<String, Object> map = null;
		try {
			/*FileUtil.downloadFileToBrowser(request.getSession().getServletContext()
					.getRealPath("/") 
					+ PropertyPlaceholder.getContextProperty("excelTemplateDir") + PropertyPlaceholder.getContextProperty("dataelementTemplateFile")
					, PropertyPlaceholder.getContextProperty("dataelementTemplateFile"), response);*/
			
			FileUtil.downloadFileToBrowser(FileUtil.getJarRootDir() 
					+ PropertyPlaceholder.getContextProperty("excelTemplateDir")
					+ PropertyPlaceholder.getContextProperty("dataelementTemplateFile")
					, PropertyPlaceholder.getContextProperty("dataelementTemplateFile"), response);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("导出问题数据出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ApiOperation("字段推荐")
	@RequestMapping(value = "/searchColumn",method = RequestMethod.POST)
	@ResponseBody
	public String searchColumn(Long sourceId, Long elementId) {
		
		Map<String, Object> map = null;
		try {
			String resp = dataElementService.searchColumn(sourceId, elementId);
//			String metadataInfos = ResponseUtil.getStringData(resp);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, resp);
		} catch (CommonException e) {
			logger.error("导出问题数据出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		    map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("导出问题数据出错：", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	

}
