package cn.com.common.ssm.engine.service;

import java.util.List;
import cn.com.common.ssm.engine.mapper.Condition;
import cn.com.common.ssm.engine.mapper.Page;

/**
 * 
 *
 * @author mingxu.zou
 * @since yeexun
 */
public interface IBaseService <T>{

	public int save(T entity) throws Exception;

	public int insert(T entity) throws Exception;

	public int insertList(List<T> entities) throws Exception;

	public int update(T entity) throws Exception;

	public int delete(T entity) throws Exception;

	public int delete(List<Long> ids) throws Exception;

	public int delete(long id) throws Exception;
	
	public List<T> listAll() throws Exception;
	
	public List<T> query(T entity) throws Exception;
	
	public List<T> search(T entity) throws Exception;
	
	public List<T> search(T entity, Page<T> page) throws Exception;

	public List<T> search(Condition<T> condition, Page<T> page) throws Exception;

	public int countByCondition(Condition<T> condition) throws Exception;

	public List<T> searchObject(Object entity) throws Exception;

	public List<T> listByIds(List<Long> ids) throws Exception;

	public T getById(Long id) throws Exception;

	public int executeUpdate(String sql) throws Exception;

	public List<Object> query(String sql) throws Exception;

	public List<T> executeQuery(String sql) throws Exception;
}
