package cn.com.yeexun.meta_data.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.dataSource.service.impl.DatasourceService2;
import cn.com.yeexun.dispatch.dao.IDispatchHistoryDao;
import cn.com.yeexun.dispatch.entity.DispatchHistory;
import cn.com.yeexun.meta_data.dao.IMetadataDao;
import cn.com.yeexun.meta_data.entity.Metadata;
//import cn.com.yeexun.meta_data.entity.MetadataCompareVo;
//import cn.com.yeexun.meta_data.service.IMetadataRelationService;
import cn.com.yeexun.meta_data.service.IMetadataService;
import cn.com.yeexun.utils.CommonException;
import cn.com.yeexun.utils.Constant;
import tk.mybatis.mapper.entity.Example;

@Service
public class MetadataService extends BaseService<Metadata> implements IMetadataService{
	
	private static Logger logger = LoggerFactory.getLogger(MetadataService.class);
	
	@Autowired
	private IMetadataDao metadataDao;
	
//	@Autowired
//	private IMetadataRelationService metadataRelationService;
	
	@Autowired
	private IDispatchHistoryDao dispatchHistoryDao;
	
//	@Autowired
//	private IDataSourceService dataSourceService;
	
	@Autowired
	private DatasourceService2 datasourceService2;
	
	@Override
	public List<Metadata> getMetadataVersion(Metadata meta)
			throws Exception {
		return metadataDao.getMetadataVersion(meta);
	}
	
	@Override
	public List<Metadata> getMetadataVersionPage(Metadata metadata, Page<Metadata> page)
			throws Exception {
		return metadataDao.getMetadataVersionPage(metadata, page);
	}
	
	@Override
	public List<Metadata> getMetaByUniqueCode(Metadata meta) throws Exception {
		return metadataDao.getMetadataVersion(meta);
	}
	
	@Override
	public void deleteList(List<Long> ids) throws Exception {
		metadataDao.deleteList(ids);
	}

	@Override
	public List<Metadata> getMenuList(String menuType)
			throws Exception {
		return metadataDao.getMenuList(menuType);
	}

	@Override
	public List<Metadata> serchMetadata(String menuType, String searchKey)
			throws Exception {
		return metadataDao.serchMetadata(menuType, searchKey);
	}

	@Override
	public List<Metadata> getSerchMetadata(List<Metadata> metadataList) throws Exception {
		
		//包括目录的要显示的所有元数据
		List<Metadata> newlist = new ArrayList<>();
		for(Metadata meta : metadataList){
			newlist.add(meta);
			Long pid = meta.getParentId();
			if(0 != pid){
				Metadata pMeta = getById(pid);
				if (!newlist.contains(pMeta)) {
	                newlist.add(pMeta);
	            }
				while(0 != pMeta.getParentId()){
					Metadata ppMeta = getById(pMeta.getParentId());
					if (!newlist.contains(ppMeta)) {
		                newlist.add(ppMeta);
		            }
					pMeta = ppMeta;
				}
			}
			
		} 
		List<Metadata> result = new ArrayList<>();
		for(Metadata meta:newlist){
			if(0 == meta.getParentId()){
				List<Metadata> child = getChild(meta.getId(), newlist);
				if(null!=child && child.size()>0){
					for(Metadata tmp:child){
						if("Y".equals(tmp.getIsMenu())){
							List<Metadata> child2 = getChild(tmp.getId(), newlist);
							if(null!=child2 && child2.size()>0){
								tmp.setChildren(child2);
							}
						}
					}
					meta.setChildren(child);
				}
				result.add(meta);
			}
		}
		return result;
	}
	
	//递归查找子目录
	public List<Metadata> getChild(Long pid, List<Metadata> list){
		List<Metadata> result = new ArrayList<>();
        for (Metadata meta : list) {
            if (null == meta || null == pid) {
                continue;
            }
            if (pid == meta.getParentId().longValue()) {
                //此目录的父id和parentId相等说明是一级目录，则设置其子目录
            	meta.setChildren(getChild(meta.getId(),list));
                result.add(meta);
            }
        }
        return result;
	}
	
	/**
	 * 依据父id查找元数据
	 */
	@Override
	public List<Metadata> getmetadataByPid(Long pid) throws Exception {
		return metadataDao.getmetadataByPid(pid);
	}

	
	
}
