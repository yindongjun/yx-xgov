package cn.com.yeexun.qualityTask.service;

import java.util.List;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.qualityTask.entity.CheckResultCount;

public interface ICheckResultCountService extends IBaseService<CheckResultCount> {
	/**
	 * 查询问题数据
	 * @param status
	 * @param designSourceId
	 * @param ownerId
	 * @return
	 * @throws Exception
	 */
	List<CheckResultCount> queryProblemDataPage(Integer status, Long sourceId, Long ownerId, String tableName,
			String taskName, Page page) throws Exception;
	
	/**
	 * 查询问题数据表中所属数据源名称
	 * @return
	 * @throws Exception
	 */
	List<DataSource> getAllDBList() throws Exception;

	/**
	 * 处理问题数据
	 * @param id
	 * @throws Exception
	 */
	void dealProblemData(String ids, String dealPeople, String dealComment) throws Exception;
	
	/**
	 * 查看操作记录
	 * @param id
	 * @return
	 */
	CheckResultCount operationRecord(Long id) throws Exception;

}
