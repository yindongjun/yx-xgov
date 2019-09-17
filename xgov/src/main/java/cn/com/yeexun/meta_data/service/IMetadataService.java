package cn.com.yeexun.meta_data.service;

import java.util.List;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dispatch.entity.DispatchHistory;
import cn.com.yeexun.meta_data.entity.Metadata;

public interface IMetadataService extends IBaseService<Metadata>{

	/**
	 * 获取元数据的版本信息
	 * @param metadataId 元数据id
	 * @param uniqueCode 元数据的唯一标识符
	 * @return
	 * @throws Exception
	 */
	List<Metadata> getMetadataVersion(Metadata meta)throws Exception;
	List<Metadata> getMetadataVersionPage(Metadata metadata, Page<Metadata> page)throws Exception;

	/**
	 * 依据元数据类型获取根目录信息
	 * @param menuType 元数据类型0：业务元数据 1：技术元数据  2：管理元数据
	 * @return
	 * @throws Exception
	 */
	public List<Metadata> getMenuList(String menuType)throws Exception;
	
	/**
	 * 根据元数据名称查询元数据
	 * @param menuType 元数据类型
	 * @param searchKey 元数据名称
	 * @return
	 * @throws Exception
	 */
	public List<Metadata> serchMetadata(String menuType, String searchKey)throws Exception;
	
	/**
	 * 根据元数据名称查询元数据(带分页 不区分类型)
	 * @param searchKey元数据名称
	 * @param page 分页信息
	 * @return
	 * @throws Exception
	 */
//	public List<Metadata> serchMetadataV2Page(String searchKey, Page<Metadata> page)throws Exception;
	
	/**
	 * 将查询到的元数据以目录形式展示
	 * @param metadataList 元数据集合
	 * @return
	 * @throws Exception
	 */
	public List<Metadata> getSerchMetadata(List<Metadata> metadataList)throws Exception;
	
	/**
	 * 根据父id获取子元数据
	 * @param pid 父id
	 * @return
	 * @throws Exception
	 */
	public List<Metadata> getmetadataByPid(Long pid)throws Exception;
	
	/**
	 * 根据父id及模型id获取元数据
	 * @param pid 父id
	 * @param modelId 元模型id
	 * @return
	 * @throws Exception
	 */
//	public List<Metadata> getmetaByPidAndModel(Long pid, Long modelId)throws Exception;
	
	/**
	 * 根据元模型获取元数据 带模糊查询
	 * @param modelId 元模型id
	 * @param searchKey 元数据名称
	 * @return
	 * @throws Exception
	 */
//	public List<Metadata> getmetadataByModelId(Long modelId, String searchKey)throws Exception;
	
	/**
	 * 获取元数据及最近一级目录元数据
	 * @param list 元数据集合
	 * @return
	 * @throws Exception
	 */
//	public List<Metadata> getmetaLateMenu(List<Metadata> list)throws Exception;
	
	/**
	 * 判断集合中是否包含元数据
	 * @param list 元数据集合
	 * @return
	 * @throws Exception
	 */
//	public boolean existMeta(List<Metadata> list)throws Exception;
	
	/**
	 * 删除元数据及其子元数据
	 * @param pid 元数据id
	 * @throws Exception
	 */
//	public void delMetaByPid(Long pid)throws Exception;
	
	/**
	 * 根据uniqeCode获取元数据
	 * @param uniqueCode 元数据唯一标识码
	 * @return
	 * @throws Exception
	 */
	public List<Metadata> getMetaByUniqueCode(Metadata meta)throws Exception;
//	
//	MetadataCompareVo compareUnreleaseVersWithLastestReleaseVer(Long sourceId, Long collectId);
//
//	void reviewVersion(Long collectHisId, Long sourceId, Integer status, String desc);
//
	void deleteList(List<Long> ids)throws Exception;
//
//	List<DispatchHistory> findUnreleaseCollectHisPage(Long sourceId, Page<DispatchHistory> page);
//	
//	List<Metadata> getBySourceId(Long id)throws Exception;
	/**
	 * 根据数据源id删除metadata
	 * @param id
	 * @throws Exception
	 */
//	void deleteBySourceId(Long id)throws Exception;
}
