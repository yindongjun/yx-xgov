package cn.com.common.ssm.engine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;
import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.common.ssm.engine.mapper.Condition;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.utils.EntityDissector;

/**
 * ssm框架通用service
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年4月9日 上午11:06:18
 * @since yeexun
 */
public class BaseService<T> implements IBaseService<T>{

	@Autowired
	protected BaseDao<T> baseDao;

	@Transactional
	public int save(T entity) throws Exception{
	
    	Unique unique = (Unique) entity;
		if (unique.getId() > 0) {
			return update(entity);
		} else {
			return insert(entity);
		}

	}

	@Transactional
	public int insert(T entity) throws Exception{
		if (entity instanceof OperationTimeAware) {
			((OperationTimeAware) entity).setCreateTime(new Date());
			((OperationTimeAware) entity).setLastModifyTime(new Date());
		}
		int resultCode = 0;
		if (entity instanceof Unique) {
			resultCode = baseDao.insertUseGeneratedKeys(entity);
		} else {
			resultCode = baseDao.insertSelective(entity);
		}
		return resultCode;
	}

	@Transactional
	public int insertList(List<T> entities) throws Exception{
		if (CollectionUtils.isNotEmpty(entities)) {
			for (T entity : entities) {
				if (entity instanceof OperationTimeAware) {
					((OperationTimeAware) entity).setCreateTime(new Date());
					((OperationTimeAware) entity).setLastModifyTime(new Date());
				}
			}
		}

		int resultCode = baseDao.insertList(entities);
		return resultCode;
	}

	public int update(T entity) throws Exception{
		if (entity instanceof OperationTimeAware) {
			((OperationTimeAware) entity).setLastModifyTime(new Date());
		}

		Integer resultCode = baseDao.updateByPrimaryKeySelective(entity);
		return resultCode;
	}

	@Transactional
	public int delete(T entity) throws Exception{
		return baseDao.delete(entity);
	}

	@Transactional
	public int delete(List<Long> ids) throws Exception{
		return baseDao.deleteByIds(ids);
	}

	@Transactional
	public int delete(long id) throws Exception{
		return baseDao.deleteById(id);
	}

	// = search(null)
	@Transactional(readOnly = true)
	public List<T> listAll() throws Exception{
		return baseDao.select(null);
	}

	/**
	 * 最纯净的单表查询
	 * 
	 * @param entity
	 * @return
	 * @see
	 */
	@Transactional(readOnly = true)
	public List<T> query(T entity) throws Exception{
		return baseDao.search(entity);
	}

	@Transactional(readOnly = true)
	public List<T> search(T entity) throws Exception{
		return search(new Condition<T>(entity), null);
	}

	@Transactional(readOnly = true)
	public List<T> search(T entity, Page<T> page) throws Exception{
		Condition<T> condition = new Condition<T>(entity);
		List<T> result = baseDao.extensibleSearch(condition, page);
		if (page != null) {
			page.setCount(countByCondition(condition));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> search(Condition<T> condition, Page<T> page) throws Exception{
		List<T> result = null;
		if (condition.getEqualCondition() == null) {
			condition.setEqualCondition((T) EntityDissector.newSuperGeneric(this.getClass()));
		}
		result = baseDao.extensibleSearch(condition, page);
		if (page != null) {
			page.setCount(countByCondition(condition));
		}
		
		return result;
	}

	@Transactional(readOnly = true)
	public int countByCondition(Condition<T> condition) throws Exception{
		return baseDao.countQuery(condition);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> searchObject(Object entity) throws Exception{// entitysearchtag使用，查询逻辑同search
		return search((T) entity);
	}

	@Transactional(readOnly = true)
	public List<T> listByIds(List<Long> ids) throws Exception{
		if (CollectionUtils.isEmpty(ids)) {
			return new ArrayList<T>();
		}
		return baseDao.listByIds(ids);
	}

	@Transactional(readOnly = true)
	public T getById(Long id) throws Exception{
		return baseDao.selectByPrimaryKey(id);
	}

	@Transactional
	public int executeUpdate(String sql) throws Exception{
		return baseDao.executeUpdate(sql);
	}

	@Transactional(readOnly = true)
	public List<Object> query(String sql) throws Exception{
		return baseDao.query(sql);
	}

	@Transactional(readOnly = true)
	public List<T> executeQuery(String sql) throws Exception{
		return baseDao.executeQuery(sql);
	}

}