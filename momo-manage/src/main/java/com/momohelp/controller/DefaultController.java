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
	public ModelAndView indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("default/1.0.2/index");
		return result;
	}

	@RequestMapping(value = { "/welcome" }, method = RequestMethod.GET)
	public ModelAndView welcomeUI(HttpSession session) {
		ModelAndView result = new ModelAndView("default/1.0.2/welcome");
		return result;
	}
}
