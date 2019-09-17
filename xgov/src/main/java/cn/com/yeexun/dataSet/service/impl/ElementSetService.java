package cn.com.yeexun.dataSet.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.dataSet.dao.IElementSetDao;
import cn.com.yeexun.dataSet.entity.ElementSetEntity;
import cn.com.yeexun.dataSet.service.IElementSetService;

@Service
public class ElementSetService extends BaseService<ElementSetEntity> implements IElementSetService{

	@Autowired
	private IElementSetDao elementSetDao;
	
	@Override
	public List<ElementSetEntity> getBySetId(Long setId) throws Exception {
		return elementSetDao.getBySetId(setId);
	}

}
