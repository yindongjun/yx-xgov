package cn.com.yeexun.dataElement.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.meta_data.entity.MetadataInfo;

public interface IDataElementService extends IBaseService<DataElementEntity>{
	
	/**
	 * 根据uniqueCode获取相应的数据元
	 * @param uniqueCode 数据元唯一标识
	 * @return
	 * @throws Exception
	 */
	public DataElementEntity getByUniqueCode(String uniqueCode)throws Exception;
	
	/**
	 * 数据元列表展示（包括根据数据源、表名、状态、数据元类型、数据元名称查询功能）
	 * @param dataSourceId 数据源id
	 * @param tableName 表名
	 * @param status 状态值  0：草稿 1：待审核 2：已审核 3：变更中
	 * @param dataElementType 数据元类型
	 * @param dataElementName 数据元名称
	 * @param page 分页信息
	 * @return
	 * @throws Exception
	 */
	public List<DataElementEntity> showListByStatusPage(Long dataSourceId,
			String tableName, Integer status, 
			String dataElementType, String dataElementName, Page<DataElementEntity> page)throws Exception;
	
	/**
	 * 数据元提交审核
	 * @param ids 需要审核的数据元id
	 * @param status 标准审核状态(1:通过，2:退回)
	 * @throws Exception
	 */
	public void submit(List<Long> ids, String status) throws Exception;
	
	/**
	 * 通过ids获取相应的数据元详细信息
	 * @param ids
	 * @return
	 */
	public List<DataElementEntity> getByIds(String ids) throws Exception;
	
	/**
	 * 数据元已审核列表展示 （包括根据数据源、表名、状态、数据元类型、数据元名称查询功能）
	 * @param dataSourceId 数据源id
	 * @param tableName 表名
	 * @param dataElementType 数据元类型
	 * @param dataElementName 数据元名称
	 * @return
	 * @throws Exception
	 */
	public List<DataElementEntity> getAllElement(Long dataSourceId,
			String tableName, String dataElementType,
			String dataElementName)throws Exception;
	
	/**
	 * 获取数据元总数 有关联关系数量及无关联关系数量
	 * @return
	 * @throws Exception
	 */
	public Map<String, Integer> getElementCountInfo()throws Exception;
	
	/**
	 * 获取新增数据元的前5条数据
	 * @return
	 * @throws Exception
	 */
	public List<DataElementEntity> getElementTop5()throws Exception;
	
	/**
	 * 关联字段个数Top10的数据元list
	 * @return
	 * @throws Exception
	 */
	public List<DataElementEntity> getRelEleTop20() throws Exception;
	
	/**
	 * 查找该时间点之前创建的数据元个数
	 * @param date 时间点
	 * @return
	 * @throws Exception
	 */
	public JSONObject getCountByTime() throws Exception;
	
	public boolean checkRelation(Long sourceId, String tableName, String column)throws Exception;
	
	void importFromExcel(File file);
	public void saveRelation(Long dataElementId, String relationJson);

	String searchColumn(Long sourceId, Long elementId);
}
