package cn.com.yeexun.qualityTask.dao;

import java.util.List;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.qualityTask.entity.DesignTaskTable;

public interface IDesignTaskTableDao extends BaseDao<DesignTaskTable> {

	List<DesignTaskTable> findByTaskId(Long designTaskId);

	void deleteByTaskId(Long designTaskId);

}
