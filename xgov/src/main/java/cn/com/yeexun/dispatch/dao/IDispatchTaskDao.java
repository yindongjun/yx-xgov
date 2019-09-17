package cn.com.yeexun.dispatch.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.entity.TaskOverviewVo;

public interface IDispatchTaskDao extends BaseDao<DispatchTask>{

	DispatchTask getByTaskIdAndType(@Param("taskId")long taskId, @Param("type")String type)throws Exception;
	
	List<DispatchTask> findDispatchTaskPage(@Param("dispatchTask") DispatchTask dispatchTask
			,@Param("taskName") String taskName, @Param("page") Page<DispatchTask> page);
	
	TaskOverviewVo overview();
	
	DispatchTask getTaskById(@Param("id")long id);
	
	DispatchTask findByTaskIdAndType(@Param("taskId") Long taskId, @Param("type") String type);

}
