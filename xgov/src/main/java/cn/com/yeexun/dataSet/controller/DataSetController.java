package cn.com.yeexun.dataSet.controller;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.common.controller.BaseController;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataElement.service.IDataElementService;
import cn.com.yeexun.dataSet.entity.DataSetEntity;
import cn.com.yeexun.dataSet.entity.ElementSetEntity;
import cn.com.yeexun.dataSet.service.IDataSetService;
import cn.com.yeexun.dataSet.service.IElementSetService;
import cn.com.yeexun.standardAudit.entity.StandardAudit;
import cn.com.yeexun.standardAudit.service.IStandardAuditService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("data_set")
@Api("数据集")
public class DataSetController extends BaseController<DataSetEntity>{

	private Logger logger = Logger.getLogger(DataSetController.class);
	
	@Autowired
	private IDataSetService dataSetService;
	
	@Autowired
	private IElementSetService elementSetService;
	
	@Autowired
	private IStandardAuditService standardAuditService;
	
	@Autowired
	private IDataElementService dataElementService;
	
	@Override
	public IBaseService<DataSetEntity> service() {
		return dataSetService;
	}
	
	@ResponseBody
	@RequestMapping(value = "/menu_list", method = RequestMethod.GET)
	@ApiOperation("数据集目录展示")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据集目录id", required = true, dataType = "Long"),
	  })
	public String getMenu(Long id, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DataSetEntity> menuList= dataSetService.getMenuByPid(id);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, menuList);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/edit_menu", method = RequestMethod.GET)
	@ApiOperation("数据集目录编辑回显")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据集目录id", required = true, dataType = "Long"),
	  })
	public String editMenu(Long id, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DataSetEntity menu = dataSetService.getMenuById(id);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, menu);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/add_menu", method = RequestMethod.POST)
	@ApiOperation("数据集添加及编辑目录")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据集目录id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "pid", value = "上级目录id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "name", value = "目录名称", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "code", value = "目录代码", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "desc", value = "目录描述", required = true, dataType = "String"),
	  })
	public String addMenu(Long id, Long pid, String name, String code, String desc, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DataSetEntity set = new DataSetEntity();
			if(null != id){
				set = service().getById(id);
			}
			//新建
			if(null == id){
				List<DataSetEntity> list1 = dataSetService.getMenuByName(name);
//				List<DataSetEntity> list2 = dataSetService.getMenuByCode(code);
				if(null != list1 && list1.size() > 0){
					throw new CommonException("目录名称已存在，请检查重新输入！");
				}
//				if(null != list2 && list2.size() > 0){
//					throw new CommonException("目录编号已存在，请检查重新输入！");
//				}
				set.setParentId(pid);
				set.setName(name);
				set.setCode(code);
				set.setDescrption(desc);
				set.setIsMenu("Y");
				set.setIsdel(Constant.NOT_DELETE);
				set.setCreateTime(new Date());
				set.setLastModifyTime(new Date());
				service().save(set);
			}else{
				List<DataSetEntity> list3 = dataSetService.getMenuByName(name);
//				List<DataSetEntity> list4 = dataSetService.getMenuByCode(code);
				if(null != list3 && list3.size() > 0){
					if(list3.size()==1){
						if(list3.get(0).getId()!=set.getId()){
							throw new CommonException("目录名称已存在，请检查重新输入！");
						}
					}else{
						throw new CommonException("目录名称已存在，请检查重新输入！");
					}
					
				}
//				if(null != list4 && list4.size() > 0){
//					if(list4.size()==1){
//						if(list4.get(0).getId()!=set.getId()){
//							throw new CommonException("目录名称已存在，请检查重新输入！");
//						}
//					}else{
//						throw new CommonException("目录编号已存在，请检查重新输入！");
//					}
//					
//				}
				set.setName(name);
				set.setCode(code);
				set.setDescrption(desc);
				set.setLastModifyTime(new Date());
				service().save(set);
			}
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e){
	    	map = CodeUtil.getErrorMap(e.getMessage());
	    	e.printStackTrace();
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
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
			DataSetEntity set = service().getById(id);
			if("Y".equals(set.getIsMenu())){
				List<DataSetEntity> list = dataSetService.getDateSetByPid(id);
				if(null == list || list.size() == 0){
					set.setIsdel(Constant.IS_DELETE);
					service().save(set);
				}else{
					boolean exit = dataSetService.existSet(list);
					if(exit){
						throw new CommonException("该目录下包含数据集，请先删除数据集再进行删除！！");
					}else{
						dataSetService.delMenuByPid(id);
					}
				}
			}else{
				logger.info("其下的子目录也会被删除，确定是否删除该目录？");
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
	@RequestMapping(value = "/serch_set", method = RequestMethod.POST)
	@ApiOperation("数据集查询功能")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "pid", value = "数据集目录id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "name", value = "数据集名称", required = false, dataType = "String"),
	      @ApiImplicitParam(name = "status", value = "数据集状态值", required = false, dataType = "String"),
	      @ApiImplicitParam(name = "start", value = "起始记录数", required = true, dataType = "int"),
	      @ApiImplicitParam(name = "length", value = "显示记录条数", required = true, dataType = "int"),
	  })
	public String serchSet(Long pid, String name, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = transferRequestParamToMap(request);
			Page<DataSetEntity> page = new Page<DataSetEntity>();
			page.setStart(MapUtils.getIntValue(paramMap, "start", 0));
			page.setLength(MapUtils.getIntValue(paramMap, "length", 10));
			String status1 = MapUtils.getString(paramMap, "status");
			Integer status = null;
			if(StringUtils.isNotEmpty(status1)){
				status = Integer.parseInt(status1);
			}
//			int status = MapUtils.getIntValue(paramMap, "status");
			List<DataSetEntity> setList= dataSetService.serchSetPage(pid, name, status, page);
			page.setData(setList);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, setList);
			map.put(CodeUtil.RESULT_DRAW, request.getParameter("draw"));
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/add_set",method = RequestMethod.POST)
	@ApiOperation("添加及编辑数据集")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据集id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "pid", value = "数据集上级目录id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "name", value = "数据集名称", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "code", value = "数据集代码", required = true, dataType = "String"),
	      @ApiImplicitParam(name = "ids", value = "数据集中选中的数据元id", required = true, dataType = "String"),
	  })
	public String addSet(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        String parentid = request.getParameter("pid");
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String ids = request.getParameter("ids");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			 if(StringUtils.isEmpty(ids)){
				 throw new CommonException("请至少选择一条数据元后进行保存！");
			 }
			long pid = Integer.parseInt(parentid);
			String[] item = ids.split(",");
			List<Long> list = new ArrayList<Long>(item.length);
			for (String idStr : item) {
				long temp = Long.parseLong(idStr);
				list.add(temp);
			}
			if(StringUtils.isEmpty(id)){
				//判断数据集名称是否已存在
				List<DataSetEntity> setlist = dataSetService.serchSet(pid, name, null);
				if(null!=setlist && setlist.size()>0){
					throw new CommonException("数据集名称已存在，请修改后进行保存！");
				}
				DataSetEntity set = new DataSetEntity();
				set.setParentId(pid);
				set.setName(name);
				set.setCode(code);
				set.setIsMenu("N");
				set.setIsdel(Constant.NOT_DELETE);
				set.setCreateTime(new Date());
				set.setLastModifyTime(new Date());
				set.setStatus(0);
				service().save(set);
				long setId = set.getId();
				for(Long elementId : list){
					ElementSetEntity elementSet = new ElementSetEntity();
					elementSet.setSetId(setId);
					elementSet.setElementId(elementId);
					elementSet.setCreateTime(new Date());
					elementSetService.save(elementSet);
				}
			}else{
				long setId = Integer.parseInt(id);
				DataSetEntity set = service().getById(setId);
				List<DataSetEntity> setlist = dataSetService.serchSet(pid, name, null);
				if(null!=setlist && setlist.size()>0){
					if(setlist.size()==1){
						if(setlist.get(0).getId()!=set.getId()){
							throw new CommonException("数据集名称已存在，请修改后进行保存！");
						}
					}else{
						throw new CommonException("数据集名称已存在，请修改后进行保存！");
					}
				}
				if(4 == set.getStatus()){
					set.setStatus(0);
				}
				set.setParentId(pid);
				set.setName(name);
				set.setCode(code);
				set.setLastModifyTime(new Date());
				service().save(set);
				//先删除该数据集中的数据元
				List<ElementSetEntity> tempList = elementSetService.getBySetId(setId);
				if(null!=tempList && tempList.size()>0){
					for(ElementSetEntity del : tempList){
						elementSetService.delete(del);
					}
				}
				//添加编辑后的数据元
				for(Long elementId : list){
					ElementSetEntity elementSet = new ElementSetEntity();
					elementSet.setSetId(setId);
					elementSet.setElementId(elementId);
					elementSet.setCreateTime(new Date());
					elementSetService.save(elementSet);
				}
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
	@RequestMapping(value = "/del_set",method = RequestMethod.GET)
	@ApiOperation("删除数据集")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "ids", value = "数据集id", required = true, dataType = "Long"),
	  })
	public String delSet(String ids) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(StringUtils.isBlank(ids)){
				return JSON.toJSONString(CodeUtil.getBadRequestMap());
			}
			String[] item = ids.split(",");
			List<DataSetEntity> setList = new ArrayList<>();
			for (String idStr : item) {
				long id = Long.parseLong(idStr);
				DataSetEntity set = dataSetService.getById(id);
				if(1==set.getStatus() || 2==set.getStatus()){
					setList.add(set);
				}else{
					List<ElementSetEntity> tempList = elementSetService.getBySetId(id);
					if(null!=tempList && tempList.size()>0){
						for(ElementSetEntity del : tempList){
							elementSetService.delete(del);
						}
					}
					set.setIsdel(Constant.IS_DELETE);
					service().save(set);
				}
			}
			if(setList.size()>0){
				throw new CommonException("只有草稿、变更中和已退回的数据集可以被删除！");
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
	@RequestMapping(value = "/back",method = RequestMethod.POST)
	@ApiOperation("数据元撤回操作，撤回后状态变为草稿")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据元id", required = true, dataType = "Long")
	  })
	public String back(Long id) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DataSetEntity dataset = dataSetService.getById(id);
			dataset.setStatus(DataSetEntity.STATUS_DRAFT);
			dataSetService.save(dataset);
			// 将审核表的记录更改为撤回状态
			StandardAudit standardAudit  = standardAuditService.getByFlowId("1", id);
			standardAudit.setStatus(StandardAudit.STATUS_BACK);
			standardAuditService.save(standardAudit);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("数据集撤回出错:", e);
		    map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}

	@ResponseBody
	@RequestMapping(value = "/change_set",method = RequestMethod.GET)
	@ApiOperation("数据集变更")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据集id", required = true, dataType = "Long"),
	      @ApiImplicitParam(name = "changeInfo", value = "变更原因", required = true, dataType = "String")
	  })
	public String changeSet(Long id, String changeInfo, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DataSetEntity set = service().getById(id);
			set.setChangeInfo(changeInfo);
			set.setStatus(3);
			set.setLastModifyTime(new Date());
			service().save(set);
//			StandardAudit standAudit = standardAuditService.getByFlowId("1", id);
//			standardAuditService.delete(standAudit);
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

	@ResponseBody
	@RequestMapping(value = "/sendAudit",method = RequestMethod.GET)
	@ApiOperation("数据集送审")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "ids", value = "需要送审的数据元id", required = true, dataType = "String"),
	  })
	public String sendAudit(String ids){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(StringUtils.isBlank(ids)){
				return JSON.toJSONString(CodeUtil.getBadRequestMap());
			}
			String[] item = ids.split(",");
			for (String idStr : item) {
				long id = Long.parseLong(idStr);
				DataSetEntity set = service().getById(id);
				set.setFrontStatus(set.getStatus());
				set.setStatus(1);
				service().save(set);
				//保存到审核表中去
				//这里不能每次新建审核，要改变原有的数据集的审核的状态 20190227 ywz
				StandardAudit standardAudit  = standardAuditService.getByFlowId("1", id);
				if(standardAudit == null){
					standardAudit = new StandardAudit();
				}
				standardAudit.setFlowId((int)id);
				standardAudit.setStatus("0");
				standardAudit.setCreateTime(new Date());
				standardAudit.setLastModifyTime(new Date());
				standardAudit.setTaskName(set.getName());
				//送审人
				standardAudit.setSubmitter("admin");
				standardAudit.setType("1");
				standardAudit.setSubmitTime(new Date());
				standardAuditService.save(standardAudit);
			}
			map = CodeUtil.getSuccessMap();
		} catch (Exception e) {
			logger.error("error:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/edit_set", method = RequestMethod.GET)
	@ApiOperation("数据集编辑回显")
	@ApiImplicitParams({
	      @ApiImplicitParam(name = "id", value = "数据集id", required = true, dataType = "Long"),
	  })
	public String editSet(Long id, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DataSetEntity set = dataSetService.getById(id);
			List<ElementSetEntity> elementSets = elementSetService.getBySetId(id);
			StringBuffer ids = new StringBuffer("(");
//			List<Long> ids1 = new ArrayList<>();
			for(ElementSetEntity tmp:elementSets){
				ids.append(tmp.getElementId()).append(",");
//				ids1.add(tmp.getElementId());
			}
			List<DataElementEntity> elements = dataElementService.getByIds(ids.deleteCharAt(ids.length()-1).append(")").toString());
			map = CodeUtil.getSuccessMap();
			map.put("set", set);
			map.put("element", elements);
		} catch (Exception e) {
			logger.error("error:", e);
			map = CodeUtil.getErrorRequestMap();
		}
		return JSON.toJSONString(map);
	}
}
