package cn.com.yeexun.log.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.log.entity.SignInLog;


public interface ISignInDao extends BaseDao<SignInLog> {
	
	public List<SignInLog> searchSignInLogPage(@Param("personnel") String personnel, 
			@Param("startTime") String startTime, 
			@Param("endTime") String endTime,
		    @Param("page") Page<SignInLog> page);
		
}
