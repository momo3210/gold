package com.momohelp.controller.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/manage/user/login" }, method = RequestMethod.GET)
	public ModelAndView loginUI() {
		ModelAndView result = new ModelAndView("manage/user/1.0.1/login");
		return result;
	}
}
