package cn.com.yeexun.dispatch.service;

import java.util.List;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dispatch.entity.DispatchAlarm;

public interface IDispatchAlarmService extends IBaseService<DispatchAlarm>{

	List<DispatchAlarm> getAllAlarm(Page<DispatchAlarm> page, String alarmName);

	int verifyOpen(Long alarmId);

	boolean changeStatus(Long alarmId, int status);

	String getNameByIds(String idss);

	void saveOrUpdate(Long alarmId, String alarmName, String alarmReasion, String alarmMethod, String receivePeople,
			Integer status, Integer isUsed) throws Exception;
	
}
