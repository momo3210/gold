package com.momohelp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Administrator
 *
 */
@Controller
public class DefaultController {

	/**
	 * 我的牧场
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/pasture" }, method = RequestMethod.GET)
	public ModelAndView _i_pastureUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/pasture");
		return result;
	}

	/**
	 * 首页
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView _i_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/index");
		return result;
	}

	/**** 后台 ****/

	@RequestMapping(value = { "/manage/" }, method = RequestMethod.GET)
	public ModelAndView _manage_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("manage/default/1.0.2/index");
		return result;
	}
}