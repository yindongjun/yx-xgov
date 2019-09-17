package cn.com.yeexun.dataSet.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataSet.entity.DataSetEntity;

public interface IDataSetDao extends BaseDao<DataSetEntity>{

	public List<DataSetEntity> getMenuByPid(@Param("pid") Long pid);
	
	public List<DataSetEntity> getDateSetByPid(@Param("pid") Long pid);
	
	public List<DataSetEntity> getMenuByName(@Param("name") String name);
	
	public List<DataSetEntity> getMenuByCode(@Param("code") String code);
	
	public List<DataSetEntity> serchSet(@Param("pid") Long pid, @Param("name") String name, @Param("status") Integer status);
	
	public List<DataSetEntity> serchSetPage(@Param("pid") Long pid, @Param("name") String name,
			@Param("status") Integer status, @Param("page") Page<DataSetEntity> page);
	
	public DataSetEntity getMenuById(@Param("id") Long id);
	
	public List<DataSetEntity> getAllDataSet();
	
	void deleteList(@Param("list") List<Long> list);
}
