package cn.com.yeexun.collectTask.service;

import java.util.List;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
//import cn.com.yeexun.collectTask.entity.CollectTask;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.meta_data.collector.MetadataCollector;
import cn.com.yeexun.meta_data.entity.Metadata;

public interface ICollectTaskService /*extends IBaseService<CollectTask>*/{

	/**
	 * 查询taskList 如果有datasourceName 则表示按照数据源名称进行查找
	 * @param dataSourceName
	 * @return
	 * @throws Exception
	 */
//	List<CollectTask> getTaskListByDsName(String dataSourceName)throws Exception;

	/**
	 * 提交采集任务
	 * @param taskId
	 * @throws Exception
	 */
//	void submitTask(long taskId)throws Exception;
	
	/**
	 * 新增采集任务
	 * @param task
	 * @throws Exception
	 */
//	void addTask(CollectTask task)throws Exception;
	
	/**
	 * 采集任务的执行
	 * @param taskId
	 * @param collectId
	 * @throws Exception
	 */
//	void collectMetadata(long taskId, long collectId)throws Exception;
	/**
	 * 根据数据源的类型或者相应的采集器
	 * @param databaseType
	 * @return
	 * @throws Exception
	 */
	MetadataCollector getMetadataCollector(String databaseType)throws Exception;

	/**
	 * 根据元数据的类型获取元数据的目录
	 * @param metadataType
	 * @return
	 */
//	List<Metadata> getMetadataDirsTree(String metadataType);
	/**
	 * 根据task的某一属性的值查询任务列表
	 * @param collectTask
	 * @return
	 */
//	List<CollectTask> getCollectionsPage(String collectTask, Page<CollectTask> page);
	/**
	 * 删除采集任务
	 * @param id
	 */
//	void deleteCollectTask(Long id);
	/**
	 * 判断数据源是否是关系型
	 * @param source
	 * @return
	 */
	public  Boolean isRDBMS(DataSource source);

//	List<CollectTask> getExedCollectionsPage(Page<CollectTask> page, String taskName);

//	CollectTask getCollectTask(Long id);
	
//	void delDateSource(Long sourceId)throws Exception;
}
