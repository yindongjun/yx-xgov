package cn.com.yeexun.meta_data.service;

import java.util.List;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.meta_data.entity.MetadataAttrEntity;

public interface IMetadataAttrService extends IBaseService<MetadataAttrEntity>{

	/**
	 * 根据元数据id获取元数据属性
	 * @param metadataId 元数据id
	 * @return
	 * @throws Exception
	 */
	public List<MetadataAttrEntity> getAttrbyMetaId(Long metadataId)throws Exception;
}
