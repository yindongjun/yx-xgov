/**
 * @file BaseController.java
 * @date 2016年4月9日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.yeexun.common.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import cn.com.common.ssm.engine.controller.CommonController;
import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.common.ssm.engine.utils.EntityDissector;
import cn.com.yeexun.utils.CodeUtil;



/**
 * ssm框架通用controller
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年4月9日 上午10:38:42
 * @since yeexun
 */

public abstract class BaseController<T> extends CommonController {

  private Logger logger = Logger.getLogger(BaseController.class);

  public abstract IBaseService<T> service();
  

  
  /**
   * 查询所有接口
   * @param request
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/list_all_action", method = RequestMethod.GET)
  public String listAll(HttpServletRequest request) {
    Map<String, Object> map = CodeUtil.getSuccessMap();
    try {
      Map<String, Object> paramMap = transferRequestParamToMap(request);
      Page<T> page = new Page<T>();
      page.setStart(MapUtils.getIntValue(paramMap, "start", 0));
      page.setLength(MapUtils.getIntValue(paramMap, "length", 10));
      List<T> result = service().search((T)EntityDissector.newSuperGeneric(getClass()),page);
      map = CodeUtil.getSuccessMap();
      map.put(CodeUtil.RESULT_DATA, result);
      map.put(CodeUtil.RESULT_DRAW, request.getParameter("draw"));
      map.put(CodeUtil.RESULT_DATA_COUNT, page.getCount());
    } catch (Exception e) {
      logger.error("error:",e);
      map = CodeUtil.getErrorRequestMap();
    }
    return JSON.toJSONString(map);
  }
  
  protected void savePrepare(T o) {
  }
  
  /**
   * 保存接口
   * @param id
   * @param entity
   * @param request
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/save_action", method = RequestMethod.POST)
  public String saveForm(T entity, HttpServletRequest request) {
    Map<String, Object> map = CodeUtil.getSuccessMap();
    try {
      savePrepare(entity);
      service().save(entity);
    } catch (Exception e) {
      logger.error("error:",e);
      map = CodeUtil.getSystemErrorMap();
    }
    return JSON.toJSONString(map);
  }
  
  /**
   * 根据id获取对象接口
   * @param id
   * @param request
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/get_id", method = RequestMethod.GET)
  public String getId(long id, HttpServletRequest request) {
    Map<String, Object> map = CodeUtil.getSuccessMap();
    try {
      if(id < 1){
        map = CodeUtil.getBadRequestMap();
        return JSON.toJSONString(map);
      }
      map.put(CodeUtil.RESULT_DATA, service().getById(id));
      return JSON.toJSONString(map);
    } catch (Exception e) {
      logger.error("error:",e);
      map = CodeUtil.getSystemErrorMap();
    }
    return JSON.toJSONString(map);
  }

  /**
   * 批量删除
   * @param ids
   * @param request
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/delete_action", method = RequestMethod.POST)
  public String delete(List<Long> ids, HttpServletRequest request) {
    Map<String, Object> map = CodeUtil.getSuccessMap();
    try {
      if(ids == null || ids.size() == 0){
        return JSON.toJSONString(CodeUtil.getBadRequestMap());
      }
      service().delete(ids);
    } catch (Exception e) {
      logger.error("error:",e);
      map = CodeUtil.getSystemErrorMap();
    }
    return JSON.toJSONString(map);
  }

  /**
   * 删除单个
   * @param id
   * @param request
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/del_action", method = RequestMethod.GET)
  public String deleteOne(Long id, HttpServletRequest request) {
    Map<String, Object> map = CodeUtil.getSuccessMap();
    try {
      if(id == null || id == 0){
        map = CodeUtil.getErrorRequestMap();
        return JSON.toJSONString(map);
      }
      service().delete(id);
    } catch (Exception e) {
      logger.error("error", e);
      map = CodeUtil.getSystemErrorMap();
    }
    return JSON.toJSONString(map);
  }

}