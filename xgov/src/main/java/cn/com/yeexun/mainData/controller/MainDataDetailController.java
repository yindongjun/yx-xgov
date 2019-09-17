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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.collectTask.service.ICollectTaskService;
import cn.com.yeexun.common.controller.BaseController;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.entity.ExcuteResult;
import cn.com.yeexun.dataSource.entity.Meta;
//import cn.com.yeexun.dataSource.entity.ExcuteResult;
//import cn.com.yeexun.dataSource.entity.Meta;
import cn.com.yeexun.mainData.entity.MainDataDetail;
import cn.com.yeexun.mainData.service.impl.MainDataDetailService;
import cn.com.yeexun.meta_data.collector.MetadataCollector;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.CommonException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("main_data_detail")
@Api("主数据库管理")
public class MainDataDetailController extends BaseController<MainDataDetail>{

	
	private Logger logger = Logger.getLogger(MainDataDetailController.class);
	
	@Autowired
	private MainDataDetailService mainDataDetailService;
	
	@Autowired
	private ICollectTaskService collectTaskService;
	
	@Override
	public IBaseService<MainDataDetail> service() {
		return null;
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/getAllTables" ,method = RequestMethod.GET)
	@ApiOperation("查询数据源下不再主数据中的表")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "mainDataId",value ="主数据id")
			)
	public String getAllTables(Long mainDataId) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			
			List<Meta> tables = mainDataDetailService.getAllTables(mainDataId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, tables);
		} catch (CommonException e) {
			logger.error("error:",e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getErrorMap("");
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/addTables" ,method = RequestMethod.GET)
	@ApiOperation("添加表到主数据源")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tableName",value ="表名List<tableName>"),
			@ApiImplicitParam(name = "userId",value ="用户Id"),
			@ApiImplicitParam(name = "mainDataId",value ="主数据Id")
	})
	public String addTables(String tableName , Long userId ,Long mainDataId) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		//String[] 转成List<String>
		String[] strs = tableName.split(",");
		List<String> tableNames = new ArrayList<String>();
		for (String string : strs) {
			tableNames.add(string);
		}
		//TODO 验证主数据源中是否有表与添加的表重复
		try {
			List<MainDataDetail> mainDataDetails = new ArrayList<MainDataDetail>();
			//获取主数据中的数据表，将表名取出做对比
			List<MainDataDetail> exitTables = mainDataDetailService.getMainTables(mainDataId);
			List<String> exitTablesName = new ArrayList<String>();
			for (MainDataDetail mainDataDetail : exitTables) {
				exitTablesName.add(mainDataDetail.getTableName());
			}
			Date date = new Date();
			int num=0;
			for(String str : tableNames) {
				MainDataDetail mainDataDetail = new MainDataDetail();
				//判断该主数据表是否存在
				if(!exitTablesName.contains(tableName)) {
					mainDataDetail.setTableName(str);
					mainDataDetail.setMainDataId(mainDataId);
					mainDataDetail.setId(0L);
					mainDataDetail.setIsdel(0);
					mainDataDetail.setCreateTime(date);
					mainDataDetail.setUpdateTime(date);
					mainDataDetail.setCreateUser(userId);
					mainDataDetail.setUpdateUser(userId);
					mainDataDetails.add(mainDataDetail);
				}else{
					continue;
				}
			}
			num = mainDataDetailService.insertList(mainDataDetails);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, num);
		} catch (CommonException e) {
			logger.warn("warn：", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getErrorMap("增加表失败");
		}
		return JSON.toJSONString(map);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/getMainTables" ,method = RequestMethod.GET)
	@ApiOperation("查询主数据源下所有的表")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "mainDataId",value ="主数据源id")
			)
	public String getMainTables(Long mainDataId) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			List<MainDataDetail> tables = mainDataDetailService.getMainTables(mainDataId);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA, tables);
		} catch (CommonException e) {
			logger.warn("warn：", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getErrorMap("");
		}
		return JSON.toJSONString(map);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/deleteTables" ,method = RequestMethod.GET)
	@ApiOperation("删除数据源下的表")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "ids",value ="删除表的id")
			)
	public String deleteTables(String ids) {
		Map<String,Object> map = new HashMap<String,Object>();
		String[] strs = ids.split(",");
		List<Long> id = new ArrayList<Long>();
		for (String string : strs) {
			id.add((long) Integer.parseInt(string));
		}
		try {
			int result = mainDataDetailService.delete(id);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA,result);
		} catch (CommonException e) {
			logger.warn("warn：", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getFields" ,method = RequestMethod.GET)
	@ApiOperation("获取表的字段")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "id",value ="表id")
			)
	public String getFields(Long id) {
		Map<String,Object> map = new HashMap<String,Object>();
		MainDataDetail mainDataDetail = new MainDataDetail();
		try {
			List<Map<String, String>> fields = new ArrayList<Map<String, String>>();
			mainDataDetail = mainDataDetailService.getById(Long.valueOf(id));
			fields = mainDataDetailService.getFields(mainDataDetail);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA,fields);
		} catch (CommonException e) {
			logger.warn("warn：", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getShowData" ,method = RequestMethod.GET)
	@ApiOperation("展示表中的部分数据")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "id",value ="表id")
			)
	public String getShowData(Long id) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			MainDataDetail mainDataDetail = mainDataDetailService.getById(id);
			ExcuteResult dataMap = mainDataDetailService.getShowData(mainDataDetail);
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA,dataMap.getDatamap());
			map.put("columnList", dataMap.getColList());
		} catch (CommonException e) {
			logger.error("error:",e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch (Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getSystemErrorMap();
		}
		return JSON.toJSONString(map);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value="/getDataByCondition" ,method=RequestMethod.GET)
	@ApiOperation("通过传入参数查找数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id",value ="所查询的表的id"),
			@ApiImplicitParam(name = "conditionsBu",value = "包含fieldName、condition、value 形式为：[{\"field\":\"age\",\"operator\":\">=\",\"value\":\"2\"},{\"field\":\"name\",\"operator\":\"=\",\"value\":\"tom\"}]")
			})
	public String getDataByCondition(String conditionsFirst,Long id) {
		Map<String,Object> map = new HashMap<String,Object>();
		//将传入的JSON {},{}  转换成[{},{}]   将参数转成JSON数组
		StringBuffer conditions = new StringBuffer("["+conditionsFirst+"]");
		DataSource dataSource = mainDataDetailService.getDataSourceById(id);
		JSONArray conditionJSON = JSONArray.parseArray(conditions.toString());
		try {
			//获取到查询用到的数据，转换成集合类型
			MainDataDetail mainDataDetail = mainDataDetailService.getById(id);
			MetadataCollector collector = collectTaskService.getMetadataCollector(dataSource.getDatabaseType());
			StringBuffer sql = new StringBuffer(collector.createSelectSql(dataSource, mainDataDetail.getTableName()));
			//遍历拼接sql中的where条件
			if( conditionJSON.size()!=0 && conditionJSON !=null ) {
				sql.append(" WHERE"+" ");
				for(int i=0;i<conditionJSON.size();i++) {
					String field_name = ((JSONObject)conditionJSON.get(i)).getString("field");
					String operate = ((JSONObject)conditionJSON.get(i)).getString("operator");
					String value = ((JSONObject)conditionJSON.get(i)).getString("value");
					//如果有参数，则给参数加上括号
					if(field_name != "" &&  field_name !=null) {
						//模糊查询
						if(operate.toLowerCase().equals("like")) {
							sql.append(field_name+" "+operate+" "+"\'%"+value+"%\'");
						}else {
							sql.append(field_name+" "+operate+" "+"("+value+")");
						}
						
						
					}
					//如果是最后一个条件就不加AND和WHERE
					if(i == conditionJSON.size()-1) {
						break;
					}
					sql.append(" AND ");
				}
			}
			
			System.out.println(sql.toString());
			/*ExcuteResult result = collector.excute(dataSource, sql.toString());
			map = CodeUtil.getSuccessMap();
			map.put(CodeUtil.RESULT_DATA,result);*/
		} catch (CommonException e) {
			logger.warn("warn：", e);
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, e.getMessage());
		} catch(Exception e) {
			logger.error("error:",e);
			map = CodeUtil.getSystemErrorMap();
		}
	
		return JSON.toJSONString(map);
	}
	
}
