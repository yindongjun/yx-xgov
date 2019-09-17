package cn.com.yeexun.dataSet.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dataElement.entity.DataElementEntity;
import cn.com.yeexun.dataSet.dao.IDataSetDao;
import cn.com.yeexun.dataSet.entity.DataSetEntity;
import cn.com.yeexun.dataSet.service.IDataSetService;

@Service
public class DataSetService extends BaseService<DataSetEntity> implements IDataSetService{

	@Autowired
	private IDataSetDao dataSetDao;
	
	@Override
	public List<DataSetEntity> getMenuByPid(Long pid) throws Exception {
		return dataSetDao.getMenuByPid(pid);
	}

	@Override
	public List<DataSetEntity> getMenuByCode(String code) throws Exception {
		return dataSetDao.getMenuByCode(code);
	}

	@Override
	public List<DataSetEntity> getMenuByName(String name) throws Exception {
		return dataSetDao.getMenuByName(name);
	}
	
	@Override
	public boolean existSet(List<DataSetEntity> list) throws Exception {
		for(DataSetEntity set:list){
			if(!"Y".equals(set.getIsMenu())){
				return true;
//				throw new CommonException("该目录下包含元数据，请移到其他目录下再进行删除！！");
			}else{
				List<DataSetEntity> setlist = getDateSetByPid(set.getId());
				if(null!=setlist && setlist.size()>0){
					return existSet(setlist);
				}
			}
		}
		return false;
	}
	
	@Override
	public void delMenuByPid(Long pid) throws Exception {
		List<Long> ids = new ArrayList<>();
		ids.add(pid);
		ids = getChild(ids, pid);
		if(null!=ids && ids.size()>0){
			dataSetDao.deleteList(ids);
//			delete(ids);
		}
	}
	
	private List<Long> getChild(List<Long> ids, Long pid) throws Exception{
		List<DataSetEntity> menulist = getMenuByPid(pid);
		if(null!=menulist && menulist.size()!=0){
			for(DataSetEntity set:menulist){
				ids.add(set.getId());
				return getChild(ids, set.getId());
			}
		}else{
			return ids;
		}
		return ids;
	}

	@Override
	public List<DataSetEntity> serchSet(Long pid, String name, Integer status)
			throws Exception {
		return dataSetDao.serchSet(pid, name, status);
	}
	
	/**
	 * 标准审核修改数据集状态值
	 */
	@Override
	public void submit(List<Long> ids, String status) throws Exception {
		//String[] item = ids.split(",");
		for (long id : ids) {
			//long id = Long.parseLong(idStr);
			DataSetEntity set = getById(id);
			if("1".equals(status)){
				//修改数据集的状态值
				set.setStatus(2);
				set.setLastModifyTime(new Date());
				save(set);
			}else{
				set.setStatus(DataSetEntity.STATUS_REJECT);
				set.setLastModifyTime(new Date());
				save(set);
			}
		}
	}

	@Override
	public DataSetEntity getMenuById(Long id) throws Exception {
		return dataSetDao.getMenuById(id);
	}

	@Override
	public List<DataSetEntity> serchSetPage(Long pid, String name,
			Integer status, Page<DataSetEntity> page) throws Exception {
		return dataSetDao.serchSetPage(pid, name, status, page);
	}

	@Override
	public int getAllDataSetNum() throws Exception {
		int num = 0;
		List<DataSetEntity> dsEntity = dataSetDao.getAllDataSet();
		if(dsEntity != null && dsEntity.size() > 0){
			num = dsEntity.size();
		}
		return num;
	}

	@Override
	public List<DataSetEntity> getDateSetByPid(Long pid) throws Exception {
		// TODO Auto-generated method stub
		return dataSetDao.getDateSetByPid(pid);
	}
	

}
