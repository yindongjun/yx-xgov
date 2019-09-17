package cn.com.yeexun.dataSet.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dataSet.entity.ElementSetEntity;

public interface IElementSetService extends IBaseService<ElementSetEntity>{

	/**
	 * 根据数据集id找到相关的数据元关联
	 * @param setId 数据集id
	 * @return
	 * @throws Exception
	 */
	public List<ElementSetEntity> getBySetId(Long setId)throws Exception;
}
