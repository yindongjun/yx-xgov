package cn.com.yeexun.common.controller;


/**

 * @file CommonViewController.java
 * @date 2016年11月1日
 * @version 1.0.0
 *
 * Copyright (c) 2016 HKBEA, Inc. All Rights Reserved.
 */


import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年7月2日 下午1:25:49
 */
@Controller
@RequestMapping("common_view")
public class CommonViewController  {
	
	public String indexViewName() {
		return "layout/blank";
	}

	
	/***
	 * 返回主页
	 * 
	 * */
	@ResponseBody
	@RequestMapping(value = "/list_index_action", method = RequestMethod.GET)
	public ModelAndView listIndex(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(indexViewName());
		return mav;
	}
	
	
}
