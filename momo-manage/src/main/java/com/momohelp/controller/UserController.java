package com.momohelp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.momohelp.model.User;
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

	/**
	 * 登陆
	 *
	 * @return
	 */
	@RequestMapping(value = { "/user/login" }, method = RequestMethod.GET)
	public ModelAndView _i_loginUI() {
		ModelAndView result = new ModelAndView("i/user/1.0.1/login");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/login" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_login(User user, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// TODO
		return result;
	}

	/**
	 * 退出
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/logout" }, method = RequestMethod.GET)
	public String _i_logoutUI(HttpSession session) {
		session.invalidate();
		return "redirect:/user/login";
	}

	/**
	 * 变更密码
	 *
	 * @return
	 */
	@RequestMapping(value = { "/user/changePwd" }, method = RequestMethod.GET)
	public ModelAndView _i_changePwdUI() {
		ModelAndView result = new ModelAndView("i/user/1.0.1/changePwd");
		return result;
	}

	/**
	 * 修改资料
	 *
	 * @return
	 */
	@RequestMapping(value = { "/user/profile" }, method = RequestMethod.GET)
	public ModelAndView _i_profileUI() {
		ModelAndView result = new ModelAndView("i/user/1.0.1/profile");
		return result;
	}

	/**
	 * 我的帐户
	 *
	 * @return
	 */
	@RequestMapping(value = { "/user/account" }, method = RequestMethod.GET)
	public ModelAndView _i_accountUI() {
		ModelAndView result = new ModelAndView("i/user/1.0.1/account");
		return result;
	}

	/**** 后台 ****/
}