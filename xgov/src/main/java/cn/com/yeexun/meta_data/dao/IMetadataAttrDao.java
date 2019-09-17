package cn.com.yeexun.meta_data.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.meta_data.entity.MetadataAttrEntity;

public interface IMetadataAttrDao extends BaseDao<MetadataAttrEntity>{

	public List<MetadataAttrEntity> getAttrbyMetaId(@Param("metadataId") Long metadataId);
}
