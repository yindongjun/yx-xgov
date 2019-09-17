/**
 * @file EnumTag.java
 * @date 2016年7月8日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.tags;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.com.common.ssm.engine.helper.SpringContextHolder;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.common.ssm.engine.tags.support.EntityHolder;

/**
 * 通用查询tag <br/>
 * 参数优先级:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;servieName > beanName > simpleBeanName <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;invoke > condition <br/>
 * 
 * @author yong.zhou
 * @version 1.0.0, 2016年7月8日 上午9:39:40
 */
public class EntitySearchTag extends OptionTagSupport {

	private Logger logger = Logger.getLogger(getClass());

	public static final String TYPE_OPTION = "option";

	// 单条数据
	public static final String TYPE_ENTITY = "entity";

	// 单条数据的某个字段
	public static final String TYPE_FIELD = "field";

	// 字段list
	public static final String TYPE_FIELD_LIST = "fieldList";

	// 数据列表
	public static final String TYPE_LIST = "list";

	// 指定字段值的map
	public static final String TYPE_MAP = "map";

	private String type;

	private String beanName;

	private String simpleBeanName;

	private String condition;

	private String serviceName;

	private String invoke;// 只支持返回类型为list和各种entity类型的方法

	// 非页面参数
	private String methodName;

	private Class<?>[] parameterTypes;

	private Object[] args;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doTag() throws JspException, IOException {

		Object result = dataPrepare();

		if (StringUtils.equals(TYPE_ENTITY, type) || StringUtils.equals(TYPE_FIELD, type)) {

			Object entity = null;
			if (result instanceof List) {
				entity = CollectionUtils.isNotEmpty(((List<?>) result)) ? ((List<?>) result).get(0) : null;
			} else {// type entity时如果不是list则认为是单个对象,如果invoke方法设错了概不负责
				entity = result;
			}

			if (StringUtils.equals(TYPE_ENTITY, type)) {

				this.getJspContext().setAttribute(var, entity);

			} else if (StringUtils.equals(TYPE_FIELD, type)) {

				if (BooleanUtils.isFalse(lazy)) {
					this.getJspContext().setAttribute(var, getLabel(entity));
				} else {
					this.getJspContext().getOut().println(getLabel(entity));
				}

			}
		} else if (StringUtils.equals(TYPE_LIST, type)) {

			this.getJspContext().setAttribute(var, JSONObject.toJSON(result));

		} else if (StringUtils.equals(TYPE_MAP, type)) {
			Map map = new HashMap();
			if (result instanceof Map) {
				map = (Map) result;
			} else if (result instanceof List) {

				List<?> resultList = (List<?>) result;

				if ((StringUtils.equals(value, "id") || StringUtils.endsWith(value, "Id")) && !StringUtils.contains(value, "|")) {
					for (Object o : resultList) {
						map.put(NumberUtils.toLong(getValue(o)), getLabel(o));
					}
				} else {
					for (Object o : resultList) {
						map.put(getValue(o), getLabel(o));
					}
				}

			} else {
				logger.warn("结果集类型异常");
			}
			this.getJspContext().setAttribute(var, map);

		} else if (StringUtils.equals(TYPE_OPTION, type)) {

			if (BooleanUtils.isTrue(lazy)) {
				write(result);
			} else {
				invoke(result);
			}

		}
	}

	private Object getCondition() {
		if (StringUtils.isBlank(this.condition)) {
			return null;
		}
		try {
			Class<?> clazz = StringUtils.isNotBlank(beanName) ? Class.forName(beanName) : EntityHolder.getEntityClass(this.simpleBeanName);
			Object condition = null;
			condition = clazz.newInstance();

			String[] params = this.condition.split(";");
			for (String param : params) {
				String fieldName = param.split("=")[0];
				String fieldValue = param.split("=")[1];

				Field field = null;
				if (StringUtils.equals(fieldName, "id")) {
					field = clazz.getSuperclass().getDeclaredField(fieldName);
				} else {
					field = clazz.getDeclaredField(fieldName);
				}
				field.setAccessible(true);

				String fieldType = field.getType().toString();
				if (fieldType.endsWith("int")) {
					field.setInt(condition, NumberUtils.toInt(fieldValue));
				} else if (fieldType.endsWith("Integer")) {
					field.set(condition, Integer.valueOf(fieldValue));
				} else if (fieldType.endsWith("long")) {
					field.setLong(condition, NumberUtils.toLong(fieldValue));
				} else if (fieldType.endsWith("Long")) {
					field.set(condition, Long.valueOf(fieldValue));
				} else {
					field.set(condition, fieldValue);
				}
			}
			return condition;
		} catch (Exception e) {
			logger.error("", e);
		}

		return null;
	}

	private Object dataPrepare() {
		Object result = null;
		BaseService<?> service = service();
		try {
			if (service != null) {
				if (StringUtils.isBlank(invoke)) {
					result = service.searchObject(getCondition());
					result = CollectionUtils.isEmpty((List<?>) result) ? new ArrayList<Object>()
							: result;
				} else {
					Method method = service.getClass().getMethod(methodName,
							parameterTypes);
					result = method.invoke(service, args);
					// if (invokeResult instanceof List) {
					// result = (List<?>) invokeResult;
					// } else {// 不是list则认为是单个对象
					// List<Object> temp = new ArrayList<Object>();
					// temp.add(invokeResult);
					// result = temp;
					// }
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}

			
		return result;
	}

	private void parseInvoke() {

		if (StringUtils.isNotBlank(invoke)) {// TODO 要以正则表达式匹配格式

			methodName = invoke.split(" ")[0].trim();

			String paramString = invoke.split(" ")[1].trim();

			String[] params = paramString.split(",");

			parameterTypes = new Class<?>[params.length];

			args = new Object[params.length];

			for (int i = 0; i < params.length; i++) {
				String param = params[i];
				String parameterType = param.split(":")[0].trim();
				String parameterValue = param.split(":")[1].trim();

				if (StringUtils.equals(parameterType, "string")) {
					parameterTypes[i] = String.class;
					args[i] = parameterValue;
				} else if (StringUtils.equals(parameterType, "int")) {
					parameterTypes[i] = int.class;
					args[i] = NumberUtils.toInt(parameterValue);
				} else if (StringUtils.equals(parameterType, "long")) {
					parameterTypes[i] = long.class;
					args[i] = NumberUtils.toLong(parameterValue);
				} else if (StringUtils.equals(parameterType, "boolean")) {
					parameterTypes[i] = boolean.class;
					args[i] = BooleanUtils.toBoolean(parameterValue);
				}

			}
		}
	}

	private BaseService<?> service() {

		if (StringUtils.isBlank(serviceName)) {
			// 此处暂时要求 service命名规范(entityname + 'Service')
			this.simpleBeanName = StringUtils.isNotBlank(beanName) ? beanName.substring(beanName.lastIndexOf(".") + 1) : simpleBeanName;
			serviceName = simpleBeanName.substring(0, 1).toLowerCase() + simpleBeanName.substring(1) + "Service";
		}

		BaseService<?> service = (BaseService<?>) SpringContextHolder.getBean(serviceName);

		return service;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
		this.simpleBeanName = beanName.substring(beanName.lastIndexOf(".") + 1);
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setSimpleBeanName(String simpleBeanName) {
		this.simpleBeanName = simpleBeanName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setInvoke(String invoke) {
		this.invoke = invoke;
		parseInvoke();
	}

}
