package com.momohelp.controller;

import java.util.Date;
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

		User _user = new User();
		_user.setId("M08351506");
		_user.setReal_name("张三");
		_user.setMobile("13837100001");
		_user.setEmail("10010@qq.com");
		_user.setNickname("哼哈");

		// TODO
		session.setAttribute("session.user", _user);
		session.setAttribute("session.user.id", _user.getId());
		session.setAttribute("session.time", (new Date()).toString());

		result.put("success", true);
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
	public String _i_profileUI(Map<String, Object> map) {
		map.put("nav_choose", ",03,0301,");
		// TODO
		User user = new User();
		map.put("data_user", user);
		user.setId("M08351506");
		user.setReal_name("张三");
		user.setMobile("13837100001");
		user.setEmail("10010@qq.com");
		user.setNickname("哼哈");
		// TODO
		return "i/user/1.0.1/profile";
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

	/**
	 * 卖出鸡苗
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/sellMo" }, method = RequestMethod.GET)
	public ModelAndView _i_sellMoUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/sellMo");
		result.addObject("nav_choose", ",05,0502,");
		return result;
	}

	/**
	 * 买入记录
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/buyRecord" }, method = RequestMethod.GET)
	public ModelAndView _i_buyRecordUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/buyRecord");
		result.addObject("nav_choose", ",05,0503,");
		return result;
	}

	/**
	 * 卖出记录
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/sellRecord" }, method = RequestMethod.GET)
	public ModelAndView _i_sellRecordUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/sellRecord");
		result.addObject("nav_choose", ",05,0504,");
		return result;
	}

	/**
	 * 购买门票
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/buyTicket" }, method = RequestMethod.GET)
	public ModelAndView _i_buyTicketUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/buyTicket");
		result.addObject("nav_choose", ",06,0602,");
		return result;
	}

	/**
	 * 购买饲料
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/buyFood" }, method = RequestMethod.GET)
	public ModelAndView _i_buyFoodUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/buyFood");
		result.addObject("nav_choose", ",06,0603,");
		return result;
	}

	/**
	 * 门票转账
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/virementTicket" }, method = RequestMethod.GET)
	public ModelAndView _i_virementTicketUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/virementTicket");
		result.addObject("nav_choose", ",06,0604,");
		return result;
	}

	/**
	 * 饲料转账
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/virementFood" }, method = RequestMethod.GET)
	public ModelAndView _i_virementFoodUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/virementFood");
		result.addObject("nav_choose", ",06,0605,");
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