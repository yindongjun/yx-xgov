package cn.com.yeexun.dispatch.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dispatch.dao.DispatchAlarmDao;
import cn.com.yeexun.dispatch.entity.DispatchAlarm;
import cn.com.yeexun.dispatch.service.IDispatchAlarmService;
import cn.com.yeexun.utils.CommonException;
import tk.mybatis.mapper.entity.Example;

@Service
public class DispatchAlarmService extends BaseService<DispatchAlarm> implements IDispatchAlarmService {
	
	@Autowired
	private DispatchAlarmDao alarmDao;
	
//	@Autowired
//	private DispatchAlarm dispatchAlarm;
	@Override
	public List<DispatchAlarm> getAllAlarm(Page<DispatchAlarm> page, String alarmName) {
		//获得所有alarm，将recivePeople的userId，换成userName
		List<DispatchAlarm> alarmList = alarmDao.getAllAlarm(page, alarmName);
		/*for(DispatchAlarm alarm : alarmList) {
			alarm.setReceivePeople(this.getNameByIds(alarm.getReceivePeople()));
		}*/
		return alarmList;
	}



	/**
	 * 获取recive_people的ids，返回字符串
	 * @param ids
	 * @return
	 */
	@Override
	public String getNameByIds(String idss) {
		try {
			List<String> idL = Arrays.asList(idss.split(","));
			if(idL != null && idL.size() != 0 && idL.get(0) != "" && !idL.contains(null) && !idL.contains("")) {
				List<Long> ids = new ArrayList<Long>();
				for(String str : idL) {
					ids.add(Long.valueOf(str));
				}
				/*StringBuffer sb= new StringBuffer();
				List<User> users = userService.listByIds(ids);
				for(User user : users) {
					sb.append(user.getLoginName()).append(",");
				}
				return sb.deleteCharAt(sb.length()-1).toString();*/
				if(ids.get(0)==0){
					return "admin";
				}else{
					return "其他";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查看报警是否被引用
	 * @param alarmId
	 * @return
	 */
	@Override
	public int verifyOpen(Long alarmId) {
		return alarmDao.verifyOpen(alarmId);
	}
	/**
	 * 改变报警状态
	 * @param alarmId
	 * @param status
	 * @return
	 */
	@Override
	public boolean changeStatus(Long alarmId,int status) {
		int result = alarmDao.changeStatus(alarmId,status);
		if(result>0 && result != 0) {
			return true;
		}else {
			return false;
		}
	}


	@Override
	public void saveOrUpdate(Long alarmId, String alarmName, String alarmReasion, String alarmMethod,
			String receivePeople, Integer status, Integer isUsed) throws Exception {
		List<DispatchAlarm> alarms = null;
		if (alarmId == null || alarmId == 0) {
			Example example = new Example(DispatchAlarm.class);
			example.createCriteria().andEqualTo("alarmName", alarmName);
			alarms = alarmDao.selectByExample(example);
		} else {
			Example example = new Example(DispatchAlarm.class);
			example.createCriteria().andEqualTo("alarmName", alarmName).andNotEqualTo("id", alarmId);
			alarms = alarmDao.selectByExample(example);
		}
		if (alarms != null && alarms.size() > 0) {
			throw new CommonException("告警名称重复！");
		}
		
		//如果是新增报警配置将alarm Id设置为0
		if(alarmId == null || alarmId == 0){
			alarmId = 0L;
			status = 1;
		}
		DispatchAlarm alarmE =new DispatchAlarm(alarmId,alarmName, Integer.valueOf(alarmReasion), Integer.valueOf(alarmMethod), receivePeople,
				status, isUsed, 1L, 1L, new Date(), new Date());
		this.save(alarmE);
	}
}
 