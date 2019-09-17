/**
 * @file BaseProvider.java
 * @date 2016年4月7日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.mapper;

import static cn.com.common.ssm.engine.mapper.Condition.CUSTOM;
import static cn.com.common.ssm.engine.mapper.Condition.EQUAL;
import static cn.com.common.ssm.engine.mapper.Condition.RANGE;

import java.util.Set;

import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * ssm框架通用dao实现
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年4月7日 上午10:47:44
 * @since yeexun
 */
public class CustomProvider extends MapperTemplate {
	
	private static Logger logger = LoggerFactory.getLogger(CustomProvider.class);

	/**
	 * @param mapperClass
	 * @param mapperHelper
	 */
	public CustomProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	/**
	 * 根据ID批量删除(必须有ID字段)
	 * 
	 * @param ms
	 */
	public String deleteByIds(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		// 开始拼sql
		sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
		sql.append("<where>");
		sql.append(" id in");
		sql.append("<foreach collection=\"list\" item=\"item\" open=\"(\"  separator=\",\" close=\")\">");
		sql.append("${item}");
		sql.append("</foreach>");
		sql.append("</where>");

		return sql.toString();
	}

	/**
	 * 根据ID单个删除(必须有ID字段)
	 * 
	 * @param ms
	 */
	public String deleteById(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		// 开始拼sql
		sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
		sql.append("where id = #{1}");

		return sql.toString();
	}

	/**
	 * 批量插入
	 *
	 * @param ms
	 */
	public String insertList(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
		sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
		sql.append(" VALUES ");
		sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
		sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
		// 获取全部列
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		// 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		for (EntityColumn column : columnList) {
			if (!column.isId() && column.isInsertable()) {
				sql.append(column.getColumnHolder("record") + ",");
			}
		}
		sql.append("</trim>");
		sql.append("</foreach>");
		return sql.toString();
	}

	/**
	 * 获取排序
	 *
	 * @param ms
	 */
	public String getLastIndex(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		sql.append("select max(sort_index) ");
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		sql.append(SqlHelper.whereAllIfColumns(entityClass, isNotEmpty()));
		return sql.toString();
	}

	/**
	 * 
	 * @param ms
	 */
	public String countQuery(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectCount(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		sql.append(" ");
		sql.append(parseCondition(entityClass));
		return sql.toString();
	}

	/**
	 * 查询，条件：不为空的字段，number不为0的字段
	 * 
	 * @param ms
	 */
	public String extensibleSearch(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		setResultType(ms, entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectAllColumns(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));

		sql.append(parseCondition(entityClass));

		sql.append(SqlHelper.orderByDefault(entityClass));

		sql.append("<if test=\"page != null\">");
		sql.append("limit #{page.start},#{page.length}");
		sql.append("</if>");

		return sql.toString();
	}

	private String parseCondition(Class<?> entityClass) {
		StringBuilder sql = new StringBuilder();
		sql.append("<where>");
		sql.append(" 1=1 ");
		sql.append("<if test=\"condition." + EQUAL + "!= null\">");
		// 获取全部列
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		for (EntityColumn column : columnList) {
			String param = "condition." + EQUAL + "." + column.getProperty();
			if (column.getJavaType().equals(int.class) || column.getJavaType().equals(long.class) || column.getJavaType().equals(Integer.class) || column.getJavaType().equals(Long.class)) {
				sql.append("<if test=\"" + param + " != null and " + param + "!= 0 \">");
				sql.append(" and " + column.getColumn() + "=#{" + param + "}");
				sql.append("</if>");
			} else {
				sql.append("<if test=\"" + param + " != null and " + param + "!= '' \"> ");
				sql.append(" and " + column.getColumn() + "=#{" + param + "}");
				sql.append("</if>");
			}
		}
		sql.append("</if>");

		// 范围查询 TODO
		sql.append("<if test=\"condition." + RANGE + "!= null\">");
		sql.append("<foreach collection=\"${condition." + RANGE + "}\" item=\"item\" open=\"(\"  separator=\",\" close=\")\">");
		sql.append("${item}");
		sql.append("</foreach>");
		sql.append("</if>");

		sql.append("</where>");

		// 插入自定义脚本
		sql.append("<if test=\"condition." + CUSTOM + "!= null and condition." + CUSTOM + " !=''\">");
		sql.append(" ${condition." + CUSTOM + "} ");
		sql.append("</if>");

		return sql.toString();
	}

	public String search(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		setResultType(ms, entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectAllColumns(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		sql.append("<where>");
		sql.append(" 1=1 ");
		// 获取全部列
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		for (EntityColumn column : columnList) {
//			System.out.println(column.getProperty() + ":" + column.getColumn() + ":" + column.getJavaType());
			logger.debug(column.getProperty() + ":" + column.getColumn() + ":" + column.getJavaType());
			if (column.getJavaType().equals(int.class) || column.getJavaType().equals(long.class)) {
				sql.append("<if test=\"" + column.getProperty() + " != null and " + column.getProperty() + "!= 0 \"> and " + column.getColumn() + "=#{" + column.getProperty() + "}</if>");
			} else {
				sql.append("<if test=\"" + column.getProperty() + " != null and " + column.getProperty() + "!= '' \"> and " + column.getColumn() + "=#{" + column.getProperty() + "}</if>");

			}
		}
		sql.append("</where>");

		sql.append(SqlHelper.orderByDefault(entityClass));

		return sql.toString();
	}

	public String searchObject(MappedStatement ms) {
		return extensibleSearch(ms);
	}

	public String listByIds(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		setResultType(ms, entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectAllColumns(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		sql.append("<where>");
		sql.append(" id in");
		sql.append("<foreach collection=\"list\" item=\"item\" open=\"(\"  separator=\",\" close=\")\">");
		sql.append("${item}");
		sql.append("</foreach>");
		sql.append("</where>");
		return sql.toString();

	}
}
