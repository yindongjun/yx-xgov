/**
 * @file EntityDissector.java
 * @date 2016年8月5日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.ibatis.javassist.Modifier;
import org.apache.log4j.Logger;

/**
 * 
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年8月5日 上午11:23:22
 */
public class EntityDissector {

	private static final Logger logger = Logger.getLogger(EntityDissector.class);

	private static Set<String> superFields = new HashSet<String>() {
		private static final long serialVersionUID = 2646941787257309262L;
		{
			add("id");
			add("createTime");
			add("modifyTime");
		}
	};

	public static String getTableName(Class<?> clazz) {
		String tableName = "";
		try {
			Table table = clazz.getAnnotation(Table.class);
			if (table != null && StringUtils.isNotBlank(table.name())) {
				tableName = table.name();
			} else {
				camelToUnderline(clazz.getSimpleName());
			}

		} catch (Exception e) {
			logger.error("", e);
		}
		return tableName;
	}

	public static Field getField(Class<?> clazz, String fieldName) {
		try {
			if (superFields.contains(fieldName)) {
				return clazz.getSuperclass().getDeclaredField(fieldName);
			} else {
				return clazz.getDeclaredField(fieldName);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	public static List<Field> getDBFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		try {
			for (Field field : clazz.getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers())) {
					Transient transientAnno = field.getAnnotation(Transient.class);
					if (transientAnno == null) {
						fields.add(field);
					}
				}
			}
			for (Field field : clazz.getSuperclass().getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers())) {
					Transient transientAnno = field.getAnnotation(Transient.class);
					if (transientAnno == null) {
						fields.add(field);
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return fields;
	}

	public static String getColumnName(Field field) {
		String columnName = "";
		try {
			if (!Modifier.isStatic(field.getModifiers())) {
				Column columnAnno = field.getAnnotation(Column.class);
				if (columnAnno != null && StringUtils.isNotBlank(columnAnno.name())) {
					columnName = columnAnno.name();
				} else {
					columnName = camelToUnderline(field.getName());
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return columnName;
	}

	public static String getColumnName(Class<?> clazz, String fieldName) {
		String columnName = "";
		try {
			Field field = getField(clazz, fieldName);

			if (!Modifier.isStatic(field.getModifiers())) {
				Column columnAnno = field.getAnnotation(Column.class);
				if (columnAnno != null && StringUtils.isNotBlank(columnAnno.name())) {
					columnName = columnAnno.name();
				} else {
					columnName = camelToUnderline(fieldName);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return columnName;
	}

	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append("_");
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static Object newSuperGeneric(Class<?> clazz) {
		try {
			Type genType = clazz.getGenericSuperclass();
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			Class<?> entityClass = (Class<?>) params[0];
			return entityClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("", e);
		}
		return null;
	}

	public static Object map2Obj(Class<?> clazz, Map<String, Object> map) {
		Object o = null;
		try {
			o = clazz.newInstance();
			List<Field> fields = getDBFields(clazz);
			for (Field field : fields) {
				if (map.containsKey(field.getName())) {
					field.setAccessible(true);
					String fieldTypeClassName = field.getType().getName();
					if (StringUtils.equals(fieldTypeClassName, "java.lang.String")) {
						field.set(o, String.valueOf(map.get(field.getName())));
					} else if (StringUtils.equals(fieldTypeClassName, "java.lang.Long") || StringUtils.equals(fieldTypeClassName, "long")) {
						field.set(o, NumberUtils.toLong((String.valueOf(map.get(field.getName())))));
					} else if (StringUtils.equals(fieldTypeClassName, "java.lang.Integer") || StringUtils.equals(fieldTypeClassName, "int")) {
						field.set(o, NumberUtils.toInt((String.valueOf(map.get(field.getName())))));
					} else if (StringUtils.equals(fieldTypeClassName, "java.util.Date")) {
						field.set(o, (Date) map.get(field.getName()));
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("", e);
		}

		return o;
	}
/*
	public static void main(String[] args) {
		System.out.println(long.class.getName());
	}*/
}
