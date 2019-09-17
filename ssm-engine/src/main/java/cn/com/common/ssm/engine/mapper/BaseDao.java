/**
 * @file BaseDao.java
 * @date 2016年4月7日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertUseGeneratedKeysMapper;

/**
 * 
 * ssm框架通用dao
 * 
 * @author yong.zhou
 * @version 1.0.0, 2016年4月7日 上午10:45:45
 * @since yeexun
 */
public interface BaseDao<T> extends Mapper<T>, InsertUseGeneratedKeysMapper<T> {
	/**
	 * 批量插入，支持数据库自增字段，支持回写
	 *
	 * @param recordList
	 * @return
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@InsertProvider(type = CustomProvider.class, method = "dynamicSQL")
	int insertList(List<T> recordList);

	@DeleteProvider(type = CustomProvider.class, method = "dynamicSQL")
	int deleteByIds(List<Long> ids);

	@DeleteProvider(type = CustomProvider.class, method = "dynamicSQL")
	int deleteById(long id);

	@SelectProvider(type = CustomProvider.class, method = "dynamicSQL")
	Integer getLastIndex(T entity);

	@SelectProvider(type = CustomProvider.class, method = "dynamicSQL")
	int countQuery(@Param("condition") Condition<T> condition);

	@SelectProvider(type = CustomProvider.class, method = "dynamicSQL")
	List<T> extensibleSearch(@Param("condition") Condition<T> condition, @Param("page") Page<T> page);

	@SelectProvider(type = CustomProvider.class, method = "dynamicSQL")
	List<T> search(T entity);

	@SelectProvider(type = CustomProvider.class, method = "dynamicSQL")
	List<T> searchObject(Object entity);

	@SelectProvider(type = CustomProvider.class, method = "dynamicSQL")
	List<T> listByIds(List<Long> ids);

	@Select("${value}")
	List<T> executeQuery(String value);

	@Select("${value}")
	List<Object> query(String value);

//	@Update("${0}")
//	int executeUpdate(String sql);
	
	@Update("${value}")
	int executeUpdate(String value);
}
