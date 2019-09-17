package cn.com.yeexun.mainData.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
//import cn.com.yeexun.dataSource.service.impl.IDatasourceClient;
//import cn.com.yeexun.dataSource.service.impl.impl.DataSourceService;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.mainData.entity.MainData;
import cn.com.yeexun.mainData.service.IMainDataService;
import cn.com.yeexun.mainData.service.impl.MainDataDetailService;
import cn.com.yeexun.mainData.service.impl.MainDataService;
import cn.com.yeexun.meta_data.entity.Metadata;
import cn.com.yeexun.meta_data.service.IMetadataService;
//import cn.com.yeexun.meta_data.service.impl.MetadataService;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("main_data")
@Api("主数据管理")
public class MainDataController extends BaseController<MainData>{

	private Logger logger = Logger.getLogger(MainDataController.class);
	
	@Autowired
	private MainDataService mainDataService;
	
	@Autowired
	private MainDataDetailService mainDataDetailService;
	
	/*@Autowired
	private MetadataService metadataService;
	
	@Autowired
	private DataSourceService dataSourceService;*/
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	@Override
	public IBaseService<MainData> service() {
		return mainDataService;
	}


	
	@ResponseBody
	@RequestMapping(value = "/searchMainData" ,method = RequestMethod.GET)
	@ApiOperation("根据主数名字查找主数据源")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type",  value ="数据源类型" ,required =false ,dataType="String"),
			@ApiImplicitParam(name = "maindataName",  value ="主数据源名称" ,required =false ,dataType="String"),
			@ApiImplicitParam(name = "start", value = "起始下标", required = false, dataType = "int"),
			@ApiImplicitParam(name = "length", value = "每页条数", required = false, dataType = "int")
	})
	public String searchMainDataByName(String type, String maindataName,int start, int length) {
		Map<String,Object> map = new HashMap<String ,Object>();
		try {
			Page<MainData> page = new Page<MainData>();
			page.setStart(start);
			page.setLength(length);
			List<MainData> list = mainDataService.getMainDataByName(type, maindataName,page);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
			map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
		} catch (CommonException e) {
			logger.error("error:",e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getErrorMap("没有找到数据");
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/addMainData" ,method = RequestMethod.GET)
	@ApiOperation("新增主数据源")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dataSourceIds",value ="数据源id",required =true ,dataType="String"),
			@ApiImplicitParam(name = "userId",value ="用户id",required =true ,dataType="Long")
	})
	public String addMainData(String dataSourceIds,Long userId) {
		int result = 0;
		Map<String,Object> map = new HashMap<String ,Object>();
		MainData maindata = new MainData();
		String[] dataSourceIdsS = dataSourceIds.split(",");
		List<Long> dataSourceId = new ArrayList<Long>();
		for (String string : dataSourceIdsS) {
			dataSourceId.add(Long.valueOf(string));
		}
		try {
			//如果主数据已经添加该数据源，则不再添加
			int mainDataCount = mainDataService.getDataSByDSIds(dataSourceId).size();
			if(mainDataCount<1) {
				Date date = new Date();
				for (Long aDataSourceId : dataSourceId) {
//					DataSource dataSource = dataSourceService.getById(aDataSourceId);
					DataSource dataSource = datasourceService2.getDatasourceById(aDataSourceId);
					maindata.setDataName(dataSource.getDatasourceName());
					maindata.setDataSourceId(dataSource.getId());
					maindata.setCreateTime(date);
					maindata.setUpdateTime(date);
					maindata.setCreateUser(0l);//TODO
					maindata.setUpdateUser(0l);//TODO
					maindata.setIsdel(0);
					maindata.setServerAddress(dataSource.getDbname()+"@"+dataSource.getIp());
					if(mainDataService.insert(maindata)>0) {
						result++;
					}
					
				}
				
				map = CodeUtil.getSuccessMap();
				map.put(CodeUtil.RESULT_DATA, result);
			}else {
				map = CodeUtil.getErrorMap("其中有数据源已存在");
			}
			
		} catch (CommonException e) {
			logger.error("error:",e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getErrorMap("添加数据源失败");
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getDatasource" ,method = RequestMethod.GET)
	@ApiOperation("获取不包含在主数据里的数据源")
	
	public String getDatasource() {
		Map<String,Object> map = new HashMap<String ,Object>();
		try {
			List<DataSource>  list= mainDataService.getDatasource();
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, list);
		} catch (CommonException e) {
			logger.error("error:",e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getErrorMap("没有找到数据");
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteDataBase" ,method = RequestMethod.GET)
	@ApiOperation("删除用户主数据")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "id",required = true,value ="数据源id",dataType="Long")
			)
	public String deleteDataBase(Long id) {
		Map<String,Object> map = new HashMap<String ,Object>();
		try {
			int result = mainDataService.delete(id);
			mainDataDetailService.deleteTables(id);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, result);
		} catch (CommonException e) {
			logger.error("error:",e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getErrorMap("删除数据失败");
		}
		return JSON.toJSONString(map);
	}
	
}
