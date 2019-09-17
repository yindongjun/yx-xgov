/**
 * @file OptionTagSupport.java
 * @date 2016年7月18日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.tags;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;

import cn.com.common.ssm.engine.utils.EntityDissector;

/**
 * 
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年7月18日 下午7:56:30
 */
public class OptionTagSupport extends SimpleTagSupport {

	private Logger logger = Logger.getLogger(getClass());

	protected Boolean lazy = false;

	protected String var;

	protected String label;

	protected String value;

	protected List<String> selectedValues = new ArrayList<String>();

	public void setSelectedValue(String selectedValue) {
		try {
			JSONArray values = JSONArray.parseArray(selectedValue);
			if (CollectionUtils.isNotEmpty(values)) {
				for (Object value : values) {
					this.selectedValues.add((String) value);
				}
			}
			return;
		} catch (Exception e) {
			logger.error("不能解析为json数组", e);
		}

		// 这里就祈祷枚举值value定义得比较规范不包含英文逗号吧
		if (StringUtils.isNotBlank(selectedValue) && selectedValue.contains(",")) {
			String[] values = selectedValue.split(",");
			for (String value : values) {
				this.selectedValues.add(value);
			}
		} else {// 单个值
			this.selectedValues.add(selectedValue);
		}
	}

	protected String getLabel(Object o) {
		String label = "";
		try {
			String[] fieldNames = label();
			if (o != null && !ArrayUtils.isEmpty(fieldNames)) {
				for (String fieldName : fieldNames) {
					Field labelField = EntityDissector.getField(o.getClass(), fieldName);
					labelField.setAccessible(true);
					label += "|" + labelField.get(o).toString();
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return StringUtils.isNotBlank(label) ? label.substring(1) : label;
	}

	protected String getValue(Object o) {
		String value = "";
		try {
			String[] fieldNames = value();
			if (o != null && !ArrayUtils.isEmpty(fieldNames)) {
				for (String fieldName : fieldNames) {
					Field valueField = EntityDissector.getField(o.getClass(), fieldName);
					valueField.setAccessible(true);
					value += "|" + valueField.get(o).toString();
				}

			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return StringUtils.isNotBlank(value) ? value.substring(1) : value;
	}

	protected void invoke(Object result) throws JspException, IOException {

		if (result instanceof List) {
			List<?> list = (List<?>) result;
			for (Object o : list) {
				this.getJspContext().setAttribute(var, o);
				this.getJspBody().invoke(null);
			}
		} else if (result instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<Object, Object> map = (Map<Object, Object>) result;
			for (Object o : map.entrySet()) {
				this.getJspContext().setAttribute(var, o);
				this.getJspBody().invoke(null);
			}
		} else {
			logger.warn("结果集类型异常");
		}

	}

	protected void write(Object result) throws IOException {
		if (result instanceof List) {
			List<?> list = (List<?>) result;
			for (Object o : list) {
				JspWriter out = this.getJspContext().getOut();
				String selectedTag = selectedValues.contains(getValue(o)) ? "selected" : "";
				String optionHtml = "<option value=\"" + getValue(o) + "\"" + " " + selectedTag + ">" + getLabel(o) + "</option>";
				out.println(optionHtml);
			}
		} else if (result instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<Object, Object> map = (Map<Object, Object>) result;

			for (Map.Entry<Object, Object> o : map.entrySet()) {
				JspWriter out = this.getJspContext().getOut();
				String selectedTag = selectedValues.contains(getValue(o)) ? "selected" : "";
				String optionHtml = "<option value=\"" + o.getKey() + "\"" + " " + selectedTag + ">" + o.getValue() + "</option>";
				out.println(optionHtml);
			}
		} else {
			logger.warn("结果集类型异常");
		}
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLazy(Boolean lazy) {
		this.lazy = lazy;
	}

	protected String[] value() {// TODO 自定义分隔符
		return StringUtils.isNotBlank(value) ? this.value.split("\\|") : null;
	}

	protected String[] label() {
		return StringUtils.isNotBlank(label) ? this.label.split("\\|") : null;
	}
}
