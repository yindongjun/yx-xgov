package cn.com.yeexun.dataSource.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dataSource.entity.DataSourceLayer;
import cn.com.yeexun.dataSource.entity.DatasourceSourceLayerEntity;
import cn.com.yeexun.dataSource.entity.MetadataDatasource;
import cn.com.yeexun.dataSource.service.IDataSourceLayerService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;

@Controller
@RequestMapping("source_layer")
public class DataSourceLayerController {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceLayerController.class);
	
	@Autowired
	private IDataSourceLayerService datasourceLayerService;
	
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdateLayer", method = RequestMethod.POST)
	public String saveOrUpdateLayer(DataSourceLayer layer) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			datasourceLayerService.saveOrUpdateLayer(layer);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			LOG.warn("添加数据源分层异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			LOG.error("添加数据源分层异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
		}
        return JSON.toJSONString(map); 
	}
	
	@ResponseBody
	@RequestMapping(value = "/getLayers", method = RequestMethod.POST)
	public String getLayers() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DataSourceLayer> layers = datasourceLayerService.getLayers();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, layers);
			
		} catch (CommonException e) {
			LOG.warn("添加数据源分层异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			LOG.error("添加数据源分层异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
		}
        return JSON.toJSONString(map); 
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteLayer", method = RequestMethod.POST)
	public String deleteTopic(Long layerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			datasourceLayerService.deleteLayer(layerId);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			LOG.warn("删除数据源分层异常：", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			LOG.error("删除数据源分层异常：", e);
			map = CodeUtil.getSystemErrorMap();
		}
        return JSON.toJSONString(map); 
	}
	
	@ResponseBody
	@RequestMapping(value = "/addSourceOnLayer", method = RequestMethod.POST)
	public String addSourceOnLayer(Long layerId, Long sourceId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			datasourceLayerService.addSourceOnLayer(layerId, sourceId);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			LOG.warn("配置数据源分层异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			LOG.error("配置数据源分层异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
		}
        return JSON.toJSONString(map); 
	}
	
	@ResponseBody
	@RequestMapping(value = "/removeSourceFromLayer", method = RequestMethod.POST)
	public String removeSourceFromLayer(Long layerId, Long sourceId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			datasourceLayerService.removeSourceFromLayer(layerId, sourceId);
			map = CodeUtil.getSuccessMap();
		} catch (CommonException e) {
			LOG.warn("从分层移除数据源异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			LOG.error("从分层移除数据源异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
		}
        return JSON.toJSONString(map); 
	}
	
	@ResponseBody
	@RequestMapping(value = "/getSourceByLayerId", method = RequestMethod.POST)
	public String getSourceByLayerId(Long layerId, Integer start, Integer length) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Page<DatasourceSourceLayerEntity> page = new Page<>();
			page.setStart(start);
			page.setLength(length);
			List<MetadataDatasource> sources = datasourceLayerService.getSourceByLayerId(layerId, page);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, sources);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
			
		} catch (CommonException e) {
			LOG.warn("根据分层id查询数据源异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			LOG.error("根据分层id查询数据源异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
		}
        return JSON.toJSONString(map); 
	}
	
	/**
	 * 获取所有没有配置分层信息的数据源。
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSources", method = RequestMethod.POST)
	public String getSources() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<MetadataDatasource> sources = datasourceLayerService.getSources();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, sources);
			
		} catch (CommonException e) {
			LOG.warn("获取未分层数据源异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			LOG.error("获取未分层数据源异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
		}
        return JSON.toJSONString(map); 
	}
	
	@ResponseBody
	@RequestMapping(value = "/getLayerPath", method = RequestMethod.POST)
	public String getLayerPath(Long layerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String path = datasourceLayerService.getLayerPath(layerId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, path);
			
		} catch (CommonException e) {
			LOG.warn("获取未分层数据源异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			LOG.error("获取未分层数据源异常：", e);
			map = CodeUtil.getErrorMap(e.getMessage());
		}
        return JSON.toJSONString(map); 
	}

}
