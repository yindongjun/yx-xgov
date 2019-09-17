package cn.com.yeexun.log.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.log.entity.OperationLog;

public interface IOperationDao extends BaseDao<OperationLog> {

	public List<OperationLog> searchOperationLogPage(@Param("personnel") String personnel,
			 @Param("operationMode") String operationMode, 
			 @Param("startTime")String startTime, 
			 @Param("endTime") String endTime,
			 @Param("page") Page<OperationLog> page);
	
	@Select("select operation_value from operation_map where 1=1 and operation_key=#{url}")
	public String geturlChinese(String url);
	
	
	public List<OperationLog> getOperationLogByNum(@Param("num")int i);

}
