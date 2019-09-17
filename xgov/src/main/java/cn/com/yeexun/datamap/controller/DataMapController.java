package cn.com.yeexun.datamap.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataElement.service.IDataElementService;
import cn.com.yeexun.dataElement.service.IRelationShipService;
import cn.com.yeexun.datamap.service.IDataMapService;
import cn.com.yeexun.metaModel.service.IMetamodelService;
import cn.com.yeexun.meta_data.service.IMetadataAttrService;
import cn.com.yeexun.meta_data.service.IMetadataService;
import cn.com.yeexun.standardAudit.service.IStandardAuditService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;

@Controller
@RequestMapping("dataMap")
@Api("数据地图")
public class DataMapController {
	
	private Logger logger = Logger.getLogger(DataMapController.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
	
	@Autowired
	private IDataMapService dataMapService ;
	
	@ResponseBody
	@RequestMapping(value = "/index0",method = RequestMethod.GET)
	@ApiOperation("数据地图首页")
	@ApiImplicitParams({
	  })
	public String showDataMap(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject dataMap = dataMapService.getFirstMap();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, dataMap);
		} catch (CommonException e) {
			logger.error("数据地图数据获取失败:", e);
		    map = CodeUtil.getErrorRequestMap();
		    map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("数据地图数据获取失败:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/index1",method = RequestMethod.GET)
	@ApiOperation("标准管理区显示")
	@ApiImplicitParams({
	  })
	public String showDetailInfo(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject dataMap = dataMapService.getDetailInfo();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, dataMap);
		} catch (CommonException e) {
			logger.error("标准管理区信息获取失败:", e);
		    map = CodeUtil.getErrorRequestMap();
		    map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("标准管理区信息获取失败:", e);
		    map = CodeUtil.getErrorRequestMap();
		    e.printStackTrace();
		}
		return JSON.toJSONString(map);
	}

}
