package cn.com.yeexun.meta_data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.meta_data.dao.IMetadataAttrDao;
import cn.com.yeexun.meta_data.entity.MetadataAttrEntity;
import cn.com.yeexun.meta_data.service.IMetadataAttrService;

@Service
public class MetadataAttrService extends BaseService<MetadataAttrEntity> implements IMetadataAttrService{

	@Autowired
	private IMetadataAttrDao metadataAttrDao;

	/**
	 * 依据元数据id查找元数据属性
	 */
	@Override
	public List<MetadataAttrEntity> getAttrbyMetaId(Long metadataId)
			throws Exception {
		return metadataAttrDao.getAttrbyMetaId(metadataId);
	}
	
	
}
