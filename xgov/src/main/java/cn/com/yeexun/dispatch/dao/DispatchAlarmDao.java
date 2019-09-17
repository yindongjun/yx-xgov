package cn.com.yeexun.dispatch.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dispatch.entity.DispatchAlarm;

public interface DispatchAlarmDao extends BaseDao<DispatchAlarm> {
	Integer verifyOpen(@Param(value="alarmId")Long alarmId);
	List<DispatchAlarm> getAllAlarm(@Param(value="page")Page<DispatchAlarm> page, @Param(value="alarmName") String alarmName);
	int changeStatus(@Param(value="alarmId")Long alarmId,@Param(value="status")int status);
}
