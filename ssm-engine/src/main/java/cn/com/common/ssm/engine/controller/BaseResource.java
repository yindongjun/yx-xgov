/**
 * @file BaseController.java
 * @date 2016年4月9日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.common.ssm.engine.service.BaseService;

/**
 * ssm框架通用controller restful风格
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年4月9日 上午10:38:42
 * @since yeexun
 */

public abstract class BaseResource<T> {

	private Logger logger = Logger.getLogger(BaseResource.class);

	public abstract BaseService<T> service();

	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public T save(@RequestBody T entity) {
		try {
			service().save(entity);
		} catch (Exception e) {
			logger.error("保存失败", e);
		}
		return entity;
	}

	@ResponseBody
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public T insert(@RequestBody T entity) {
		try {
			service().insert(entity);
		} catch (Exception e) {
			logger.error("新增失败", e);
		}
		return entity;
	}

	// @ResponseBody
	// @RequestMapping(value = "/insertAutoIndex", method = RequestMethod.POST)
	// public T insertAutoIndex(T entity, T condition) {
	// service().insertAutoIndex(entity, condition);
	// return entity;
	// }

	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public T update(@RequestBody T entity) {
		try {
			service().update(entity);
		} catch (Exception e) {
			logger.error("更新失败", e);
			return null;
		}
		return entity;
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public int delete(@RequestBody T entity) {
		try {
			return service().delete(entity);
		} catch (Exception e) {
			logger.error("删除失败", e);
			return 0;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
	public int delete(@RequestBody List<Long> ids) {
		try {
			return service().delete(ids);
		} catch (Exception e) {
			logger.error("删除失败", e);
			return 0;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/deleteById", method = RequestMethod.POST)
	public int delete(@RequestBody long id) {
		try {
			return service().delete(id);
		} catch (Exception e) {
			logger.error("删除失败", e);
			return 0;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public List<T> listAll() {
		try {
			return service().listAll();
		} catch (Exception e) {
			logger.error("listAll()", e);
			return null;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public List<T> search(T entity) {
		try {
			return service().search(entity);
		} catch (Exception e) {
			logger.error("search()", e);
			return null;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	public T getById(long id) {
		try {
			return service().getById(id);
		} catch (Exception e) {
			logger.error("getById()", e);
			return null;
		}
	}

}
