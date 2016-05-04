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
	 * 我的牧场
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/pasture" }, method = RequestMethod.GET)
	public ModelAndView _i_pastureUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/pasture");
		result.addObject("nav_choose", ",01,");
		return result;
	}

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
		result.addObject("nav_choose", ",03,0302,");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/changePwd" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_changePwd(User user, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// TODO
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
		result.addObject("nav_choose", ",03,0301,");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/profile" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_profile(User user, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// TODO
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

	/**
	 * 新建帐户
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/createAccount" }, method = RequestMethod.GET)
	public ModelAndView _i_createAccountUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/createAccount");
		result.addObject("nav_choose", ",04,0401,");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/createAccount" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_createAccount(User user, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// TODO
		return result;
	}

	/**
	 * 推荐清单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/recommend" }, method = RequestMethod.GET)
	public ModelAndView _i_recommendUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/recommend");
		result.addObject("nav_choose", ",04,0402,");
		return result;
	}

	/**
	 * 买入鸡苗
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/buyMo" }, method = RequestMethod.GET)
	public ModelAndView _i_buyMoUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/buyMo");
		result.addObject("nav_choose", ",05,0501,");
		return result;
	}

	/**** 后台 ****/

	/**
	 * 登陆
	 *
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/login" }, method = RequestMethod.GET)
	public ModelAndView _manage_loginUI() {
		ModelAndView result = new ModelAndView("manage/user/1.0.1/login");
		return result;
	}

	/**
	 * 退出
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/logout" }, method = RequestMethod.GET)
	public String _manage_logoutUI(HttpSession session) {
		session.invalidate();
		return "redirect:/manage/user/login";
	}
}