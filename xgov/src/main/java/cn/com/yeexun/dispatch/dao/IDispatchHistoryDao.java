package cn.com.yeexun.dispatch.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dispatch.entity.DispatchHistory;
import cn.com.yeexun.dispatch.entity.DispatchTask;
import cn.com.yeexun.dispatch.entity.TaskHisQueryVo;
import cn.com.yeexun.dispatch.entity.TaskOverviewVo;

public interface IDispatchHistoryDao extends BaseDao<DispatchHistory>{
	
	TaskOverviewVo overview();

	List<DispatchHistory> listLastestHistoriesPage(@Param("dispatchHistory") DispatchHistory dispatchHistory
			, @Param("page") Page<DispatchTask> page);

	List<DispatchHistory> listHistoriesByTaskIdPage(@Param("hisQueryVo") TaskHisQueryVo hisQueryVo
			, @Param("page") Page<DispatchTask> page);

}
