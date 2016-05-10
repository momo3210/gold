package com.momohelp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.foreworld.util.encryptUtil.MD5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

		User _user = userService.getByName(user.getEmail());
		if (null == _user) {
			result.put("msg", new String[] { "用户名或密码输入错误" });
			return result;
		} // IF

		String user_pass = MD5.encode(user.getUser_pass());
		if (!user_pass.equals(_user.getUser_pass())) {
			result.put("msg", new String[] { "用户名或密码输入错误" });
			return result;
		} // IF

		if (0 == _user.getStatus()) {
			result.put("msg", new String[] { "您已被限制登陆，请联系管理员" });
			return result;
		} // IF

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

	/**
	 * 普通密码
	 *
	 * @param session
	 * @param old_pass
	 * @param new_pass
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/changePwd" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_changePwd(HttpSession session,
			@RequestParam(required = true) String old_pass,
			@RequestParam(required = true) String new_pass) {
		Map<String, Object> result = new HashMap<String, Object>();

		// TODO
		String[] msg = userService.changePwd(
				session.getAttribute("session.user.id").toString(), old_pass,
				new_pass);

		if (null != msg) {
			result.put("msg", msg);
			result.put("success", false);
			return result;
		}

		result.put("success", true);
		return result;
	}

	/**
	 * 安全密码
	 *
	 * @param session
	 * @param old_pass
	 * @param new_pass
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/changePwdSafe" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_changePwdSafe(HttpSession session,
			@RequestParam(required = true) String old_pass,
			@RequestParam(required = true) String new_pass) {
		Map<String, Object> result = new HashMap<String, Object>();

		// TODO
		String[] msg = userService.changePwdSafe(
				session.getAttribute("session.user.id").toString(), old_pass,
				new_pass);

		if (null != msg) {
			result.put("msg", msg);
			result.put("success", false);
			return result;
		}

		result.put("success", true);
		return result;
	}

	/**
	 * 修改资料
	 *
	 * @param map
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/profile" }, method = RequestMethod.GET)
	public String _i_profileUI(Map<String, Object> map, HttpSession session) {
		Object obj = session.getAttribute("session.user");
		// TODO
		map.put("data_user", obj);
		map.put("nav_choose", ",03,0301,");
		// TODO
		return "i/user/1.0.1/profile";
	}

	@ResponseBody
	@RequestMapping(value = { "/user/profile" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_profile(User user, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();

		userService.editInfo(user);
		result.put("success", true);
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
		result.addObject("nav_choose", ",06,0601,");
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

	/**
	 * 佣金清单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/commission" }, method = RequestMethod.GET)
	public ModelAndView _i_commissionUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/commission");
		result.addObject("nav_choose", ",06,0606,");
		return result;
	}

	/**
	 * 静态对账单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/staticRecord" }, method = RequestMethod.GET)
	public ModelAndView _i_staticRecordUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/staticRecord");
		result.addObject("nav_choose", ",06,0607,");
		return result;
	}

	/**
	 * 动态对账单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/dynamicRecord" }, method = RequestMethod.GET)
	public ModelAndView _i_dynamicRecordUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/staticRecord");
		result.addObject("nav_choose", ",06,0608,");
		return result;
	}

	/**
	 * 门票对账单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/ticketRecord" }, method = RequestMethod.GET)
	public ModelAndView _i_ticketRecordUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/staticRecord");
		result.addObject("nav_choose", ",06,0609,");
		return result;
	}

	/**
	 * 饲料对账单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/foodRecord" }, method = RequestMethod.GET)
	public ModelAndView _i_foodRecordUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/staticRecord");
		result.addObject("nav_choose", ",06,0610,");
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