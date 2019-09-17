package cn.com.yeexun.dataSet.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dataSet.entity.DataSetEntity;

public interface IDataSetService extends IBaseService<DataSetEntity>{

	/**
	 * 查找子目录
	 * @param pid 上级目录id
	 * @return
	 * @throws Exception
	 */
	public List<DataSetEntity> getMenuByPid(Long pid)throws Exception;
	
	/**
	 * 根据名称查找目录
	 * @param name 目录名称
	 * @return
	 * @throws Exception
	 */
	public List<DataSetEntity> getMenuByName(String name)throws Exception;
	
	/**
	 * 根据代码查找目录
	 * @param code 代码名称
	 * @return
	 * @throws Exception
	 */
	public List<DataSetEntity> getMenuByCode(String code)throws Exception;
	
	/**
	 * 对象list中是否包含数据集
	 * @param list 数据集list
	 * @return
	 * @throws Exception
	 */
	public boolean existSet(List<DataSetEntity> list) throws Exception;
	
	/**
	 * 删除目录及其下面的子目录
	 * @param pid 目录id
	 * @throws Exception
	 */
	public void delMenuByPid(Long pid) throws Exception;
	
	/**
	 * 查找该目录下的数据集
	 * @param pid 目录id
	 * @param name 数据集名称
	 * @param status 数据集状态 0：草稿 1：待审核 2：已审核 3：变更中
	 * @return
	 * @throws Exception
	 */
	public List<DataSetEntity> serchSet(Long pid, String name, Integer status) throws Exception;
	
	/**
	 * 数据集提交审核
	 * @param ids 需要审核的数据集id
	 * @param status 标准审核状态值 (1:通过，2:退回)
	 * @throws Exception
	 */
	public void submit(List<Long> ids, String status) throws Exception;
	
	/**
	 * 根据id获取数据集信息
	 * @param id 数据集id
	 * @return
	 * @throws Exception
	 */
	public DataSetEntity getMenuById(Long id)throws Exception;
	
	/**
	 * 查找该目录下的数据集
	 * @param pid 目录id
	 * @param name 数据集名称
	 * @param status 数据集状态 0：草稿 1：待审核 2：已审核 3：变更中
	 * @param page 分页信息
	 * @return
	 * @throws Exception
	 */
	public List<DataSetEntity> serchSetPage(Long pid, String name,
			Integer status, Page<DataSetEntity> page)throws Exception;
	
	
	public int getAllDataSetNum() throws Exception;
	
	public List<DataSetEntity> getDateSetByPid(Long pid)throws Exception;
	
}
