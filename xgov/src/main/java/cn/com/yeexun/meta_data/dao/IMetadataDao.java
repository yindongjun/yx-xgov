package cn.com.yeexun.meta_data.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dispatch.entity.DispatchHistory;
import cn.com.yeexun.meta_data.entity.Metadata;

public interface IMetadataDao extends BaseDao<Metadata>{
	
	List<Metadata> getMetadataVersion(@Param("metadata") Metadata metadata)throws Exception;
//	
	public List<Metadata> getMenuList(@Param("menuType") String menuType)throws Exception;
//	
	public List<Metadata> serchMetadata(@Param("menuType") String menuType, @Param("searchKey") String searchKey)throws Exception;
//	
//	public List<Metadata> serchMetadataV2Page(@Param("searchKey") String searchKey, @Param("page") Page<Metadata> page)throws Exception;
//	
	public List<Metadata> getmetadataByPid(@Param("pid") Long pid)throws Exception;
//	
//	public List<Metadata> getmetadataByModelId(@Param("modelId") Long modelId, @Param("searchKey") String searchKey)throws Exception;
//
//	public List<Metadata> getmetaByPidAndModel(@Param("pid") Long pid, @Param("modelId") Long modelId)throws Exception;
//	
//	public List<Metadata> getMetaByUniqueCode(Metadata metadata);
//
//	public List<Metadata> getmetadataByModelId(@Param("modelId") Long modelId)throws Exception;
//	
//	public List<Metadata> getmetadataByName(@Param("name") String name)throws Exception;
//	
//	List<Metadata> getUnreleaseCollects(Long sourceId);
	
//	MetadataCompareVo compareWithLastestVersion(Long collectId);
	
//	List<Metadata> getLastestVer(@Param("sourceId") Long sourceId, @Param("metamodelId") Long metamodelId
//			, @Param("parentId") Long parentId);
//	
//	List<Metadata> getUnreleaseVer(@Param("sourceId") Long sourceId, @Param("collectId") Long collectId
//			, @Param("metamodelId") Long metamodelId);
//
//	List<Metadata> getMetadataDirs(String metadataType);
//	
//	List<Metadata> getNodesByParentId(@Param("parentId") Long parentId, @Param("metadataType") String metadataType);
//
//	void deleteOldVersion(@Param("collectHisId") Long collectHisId
//			, @Param("sourceId") Long sourceId
//			, @Param("sourceLevalModelId") Long sourceLevalModelId);
//	
	void deleteList(@Param("list") List<Long> list);
//
//	void releaseVersion(@Param("metadata") Metadata metadata, @Param("datasourceType") String datasourceType);
//
//	List<DispatchHistory> findUnreleaseCollectHisPage(@Param("sourceId") Long sourceId
//			, @Param("page") Page<DispatchHistory> page);
//
//	void deleteMetadata(@Param("collectId") Long collectId, @Param("sourceId") Long sourceId);
//	
	List<Metadata> getMetadataVersionPage(@Param("metadata")Metadata metadata, @Param("page") Page<Metadata> page)throws Exception;
//
//	List<Metadata> getBySourceId(@Param("sourceId")Long sourceId)throws Exception;
//
//	void deleteBySourceId(@Param("sourceId") Long sourceId)throws Exception;


}
