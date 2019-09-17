package cn.com.yeexun.dataSet.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.dataSet.entity.ElementSetEntity;

public interface IElementSetDao extends BaseDao<ElementSetEntity>{

	public List<ElementSetEntity> getBySetId(@Param("setId") Long setId);
}
