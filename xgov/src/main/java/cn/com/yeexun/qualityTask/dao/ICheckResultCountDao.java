package cn.com.yeexun.qualityTask.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.qualityTask.entity.CheckResultCount;

public interface ICheckResultCountDao extends BaseDao<CheckResultCount> {
	List<CheckResultCount> queryProblemDataListPage(@Param("status") Integer status,@Param("sourceId") Long sourceId,@Param("ownerId") Long ownerId,
			@Param("tableName") String tableName, @Param("taskName") String taskName
			, @Param("page") Page<CheckResultCount> page);

	List<CheckResultCount> selectUndealTop5(@Param("tableInfoIds") String tableInfoIds);

	int selectCountRows(@Param("tableInfoIds") String tableInfoIds, @Param("dealStatus") Integer dealStatus);

	int selectCountRotalRows(@Param("tableInfoIds") String tableInfoIds);
}
