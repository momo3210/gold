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

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView i_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/index");
		return result;
	}

	/**** ****/

	@RequestMapping(value = { "/manage/" }, method = RequestMethod.GET)
	public ModelAndView manage_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("manage/default/1.0.2/index");
		return result;
	}
}