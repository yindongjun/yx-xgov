package cn.com.yeexun.metaModel.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.metaModel.dao.MetamodelDao;
import cn.com.yeexun.metaModel.entity.MetamodelEntity;
import cn.com.yeexun.metaModel.service.IMetamodelService;

@Service
public class MetamodelService extends BaseService<MetamodelEntity> implements IMetamodelService {

	@Autowired
	private MetamodelDao metamodelDao;

	@Override
	public List<MetamodelEntity> getMetamodelMenu(Long modelId) throws Exception {
		// 一级model
		List<MetamodelEntity> firstMenu = new ArrayList<MetamodelEntity>();
		List<MetamodelEntity> allMenu = metamodelDao.queryMetamodel();
		List<MetamodelEntity> metamodelMenu = new ArrayList<MetamodelEntity>();
		if (allMenu.size() == 0 || allMenu == null) {
			return null;
		}
		// 如果modelId为空或0则展示全部菜单
		if (modelId == null || modelId.intValue() == 0) {
			for (MetamodelEntity metamodelFirst : allMenu) {
				if (metamodelFirst.getParentId() == -1 && metamodelFirst.getIsMenu().equals("Y")) {
					firstMenu.add(metamodelFirst);
					// second metamodel
					List<MetamodelEntity> secondMenu = new ArrayList<MetamodelEntity>();
					for (MetamodelEntity metamodelSecond : allMenu) {
						if (metamodelFirst.getId() == metamodelSecond.getParentId()
								&& metamodelSecond.getIsMenu().equals("Y")) {
							secondMenu.add(metamodelSecond);
							// third menu
							List<MetamodelEntity> thirdMenu = new ArrayList<MetamodelEntity>();
							for (MetamodelEntity metamodelThird : allMenu) {
								if (metamodelThird.getParentId() == metamodelSecond.getId()
										&& metamodelThird.getIsMenu().equals("Y")) {
									thirdMenu.add(metamodelThird);
									// fourth menu
									// List<MetamodelEntity> fourthMenu = new ArrayList<MetamodelEntity>();
									// for(MetamodelEntity metamodelFourth : allMenu) {
									// if(metamodelFourth.getParentId() == metamodelThird.getId()) {
									// fourthMenu.add(metamodelFourth);
									// }
									// }
									// metamodelThird.setChildren(fourthMenu);
								}
							}
							metamodelSecond.setChildren(thirdMenu);
						}
					}
					metamodelFirst.setChildren(secondMenu);
				}
			}
			return firstMenu;
		}
		// 如果modelId是某个值,则展示和该Id有关的整个菜单
		else {
			firstMenu = metamodelDao.findMetamodelById(modelId);
			List<MetamodelEntity> middle = new ArrayList<MetamodelEntity>();
			metamodelMenu = firstMenu;
			Boolean doloop = true;
			
			// 向下遍历
			if (firstMenu.get(0).getIsMenu().equals("Y")) {
				middle = metamodelDao.findChildrenMetamodel(firstMenu.get(0).getId());
				for (MetamodelEntity metamodelE1 : middle) {
					if(metamodelE1.getIsMenu().equals("Y")) {
						List<MetamodelEntity> list1 = metamodelDao.findChildrenMetamodel(metamodelE1.getId());
						for(MetamodelEntity metamodelE2 : list1) {
							if(metamodelE2.getIsMenu().equals("Y")){
								List<MetamodelEntity> list2 = metamodelDao.findChildrenMetamodel(metamodelE2.getId());
								for(MetamodelEntity metamodelE3 : list1) {
									List<MetamodelEntity> list3 = metamodelDao.findChildrenMetamodel(metamodelE2.getId());
									metamodelE3.setChildren(list3);
								}
								metamodelE2.setChildren(list2);
							}
						}
						metamodelE1.setChildren(list1);
					}	
				}
				metamodelMenu=middle;
			}
			
			
			// 向上遍历
			while (!(metamodelMenu.get(0).getParentId() == -1)) {
				// 找到父菜单
				middle = metamodelMenu;
				metamodelMenu = metamodelDao.findMetamodelById(middle.get(0).getParentId());
				metamodelMenu.get(0).setChildren(middle);
			}
			// 得到拥有向上全部菜单的metamodelMenu
			
		}

		return metamodelMenu;
	}

	/**
	 * 模糊查询metamodel
	 */
	@Override
	public List<List> fuzzyQueryMetamodel(String name, String code) {
		MetamodelService metamodelService = new MetamodelService();
		List<List> alltMenul = new ArrayList<List>();
		List<MetamodelEntity> firstMenul = new ArrayList<MetamodelEntity>();
		try {
			for (MetamodelEntity metamodel : metamodelDao.fuzzyQueryMetamodel(name, code)) {
				firstMenul = metamodelService.getMetamodelMenu(metamodel.getId());
				alltMenul.add(firstMenul);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return alltMenul;
	}
	
	/**
	 * 列出子元模型
	 */
	@Override
	public List<MetamodelEntity> itemListMetamodel(Long id){
		
		return metamodelDao.findChildrenMetamodel(id);
	}
	
}
