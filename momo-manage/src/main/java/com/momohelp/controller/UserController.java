package com.momohelp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.foreworld.util.StringUtil;
import net.foreworld.util.encryptUtil.MD5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.momohelp.model.Cfg;
import com.momohelp.model.MaterialRecord;
import com.momohelp.model.User;
import com.momohelp.service.CfgService;
import com.momohelp.service.MaterialRecordService;
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

	@Autowired
	private CfgService cfgService;

	@Autowired
	private MaterialRecordService materialRecordService;

	/**
	 * 安全密码验证
	 *
	 * @param key
	 * @param pass_safe
	 * @return
	 */
	private String[] checkSafe(String key, String pass_safe) {
		User user = userService.selectByKey(key);

		if (MD5.encode(pass_safe).equals(user.getUser_pass_safe())) {
			return null;
		} // IF

		return new String[] { "安全密码输入错误" };
	}

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
		session.setAttribute("session.lv", 1);
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
		String user_id = session.getAttribute("session.user.id").toString();
		// TODO
		User user = userService.selectByKey(user_id);
		map.put("data_user", user);
		// TODO
		map.put("nav_choose", ",03,0301,");
		return "i/user/1.0.1/profile";
	}

	@ResponseBody
	@RequestMapping(value = { "/user/profile" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_profile(
			@RequestParam(required = true) String verifyCode, User user,
			HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();

		// TODO
		String svc = session.getAttribute("session.verifyCode").toString();
		if (!verifyCode.equals(svc)) {
			result.put("msg", new String[] { "验证码输入错误" });
			result.put("success", false);
			return result;
		} // IF

		// 设置主键
		user.setId(session.getAttribute("session.user.id").toString());

		String[] checkSafe = checkSafe(user.getId(), user.getUser_pass_safe());
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			result.put("success", false);
			return result;
		} // IF

		String[] msg = userService.editInfo(user);
		if (null != msg) {
			result.put("msg", msg);
			result.put("success", false);
			return result;
		}

		// TODO
		result.put("success", true);
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
	public Map<String, Object> _i_createAccount(
			@RequestParam(required = true) String verifyCode, User user,
			HttpSession session) {
		// TODO
		Map<String, Object> result = new HashMap<String, Object>();

		// TODO
		String svc = session.getAttribute("session.verifyCode").toString();
		if (!verifyCode.equals(svc)) {
			result.put("msg", new String[] { "验证码输入错误" });
			result.put("success", false);
			return result;
		} // IF

		// 我的信息
		User myInfo = (User) session.getAttribute("session.user");
		user.setDepth(1 + myInfo.getDepth());
		user.setFamily_id(myInfo.getFamily_id());
		user.setPid(myInfo.getId());

		String[] msg = userService.saveNew(user);

		if (null != msg) {
			result.put("success", false);
			result.put("msg", msg);
			return result;
		}

		result.put("success", true);
		return result;
	}

	/**
	 * 推荐清单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/recommend" }, method = RequestMethod.GET)
	public ModelAndView _i_recommendUI(HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "100") int rows) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/recommend");

		String user_id = session.getAttribute("session.user.id").toString();
		// TODO
		User user = new User();
		user.setPid(user_id);

		List<User> list = userService.findByUser(user, page, Integer.MAX_VALUE);
		result.addObject("data_list", list);

		// TODO
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

		User user = (User) session.getAttribute("session.user");

		String lv = user.getLv();

		String min = null, max = null;

		if ("05".equals(lv)) {
			min = "2001";
			max = "2002";
		} else if ("06".equals(lv)) {
			min = "2003";
			max = "2004";
		} else if ("07".equals(lv)) {
			min = "2005";
			max = "2006";
		} else if ("08".equals(lv)) {
			min = "2007";
			max = "2008";
		}

		Cfg minObj = cfgService.selectByKey(min);
		Cfg maxObj = cfgService.selectByKey(max);

		result.addObject("data_lv_min", minObj.getValue_());
		result.addObject("data_lv_max", maxObj.getValue_());

		// TODO
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

		// 卖出鸡苗上限
		Cfg maxObj = cfgService.selectByKey("2011");

		result.addObject("data_sell_max", maxObj.getValue_());

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
	public String _i_buyTicketUI(Map<String, Object> map, HttpSession session) {
		String user_id = session.getAttribute("session.user.id").toString();
		// TODO
		User user = userService.selectByKey(user_id);
		map.put("data_user", user);
		// TODO
		map.put("nav_choose", ",06,0602,");
		return "i/user/1.0.1/buyTicket";
	}

	@ResponseBody
	@RequestMapping(value = { "/user/buyTicket" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_buyTicket(
			@RequestParam(required = true) String user_pass_safe,
			MaterialRecord materialRecord, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 安全密码验证
		String[] checkSafe = checkSafe(session.getAttribute("session.user.id")
				.toString(), user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			result.put("success", false);
			return result;
		} // IF

		// 组合数据
		materialRecord.setUser_id(session.getAttribute("session.user.id")
				.toString());
		materialRecord.setStatus(0);
		materialRecord.setType_id(1);
		materialRecord.setComment("购买门票 +1");

		String[] msg = materialRecordService.saveNew(materialRecord);
		if (null != msg) {
			result.put("msg", msg);
			result.put("success", false);
			return result;
		}

		// TODO
		result.put("success", true);
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

		String user_id = session.getAttribute("session.user.id").toString();
		// TODO
		User user = userService.selectByKey(user_id);
		result.addObject("data_user", user);

		result.addObject("nav_choose", ",06,0603,");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/buyFood" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_buyFood(
			@RequestParam(required = true) String user_pass_safe,
			MaterialRecord materialRecord, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 安全密码验证
		String[] checkSafe = checkSafe(session.getAttribute("session.user.id")
				.toString(), user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			result.put("success", false);
			return result;
		} // IF

		// 组合数据
		materialRecord.setUser_id(session.getAttribute("session.user.id")
				.toString());
		materialRecord.setStatus(0);
		materialRecord.setType_id(2);
		materialRecord.setComment("购买饲料 +1");

		String[] msg = materialRecordService.saveNew(materialRecord);
		if (null != msg) {
			result.put("msg", msg);
			result.put("success", false);
			return result;
		}

		// TODO
		result.put("success", true);
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

	@ResponseBody
	@RequestMapping(value = { "/user/virementTicket" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_virementTicket(
			@RequestParam(required = true) String user_pass_safe,
			MaterialRecord materialRecord, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		// 参数验证
		materialRecord.setTrans_user_id(StringUtil.isEmpty(materialRecord
				.getTrans_user_id()));
		if (null == materialRecord.getTrans_user_id()) {
			result.put("msg", new String[] { "接收人不能为空" });
			return result;
		} // IF

		// TODO
		String my_id = session.getAttribute("session.user.id").toString();
		if (my_id.equals(materialRecord.getTrans_user_id())) {
			result.put("msg", new String[] { "接收人不能是自己" });
			return result;
		} // IF

		// 安全密码验证
		String[] checkSafe = checkSafe(session.getAttribute("session.user.id")
				.toString(), user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			return result;
		} // IF

		// 组合数据
		materialRecord.setUser_id(my_id);
		materialRecord.setStatus(1);
		materialRecord.setType_id(1);

		String[] msg = materialRecordService.saveNew(materialRecord);
		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		// TODO
		result.put("success", true);
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

	@ResponseBody
	@RequestMapping(value = { "/user/virementFood" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_virementFood(
			@RequestParam(required = true) String user_pass_safe,
			MaterialRecord materialRecord, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		// 参数验证
		materialRecord.setTrans_user_id(StringUtil.isEmpty(materialRecord
				.getTrans_user_id()));
		if (null == materialRecord.getTrans_user_id()) {
			result.put("msg", new String[] { "接收人不能为空" });
			return result;
		} // IF

		// TODO
		String my_id = session.getAttribute("session.user.id").toString();
		if (my_id.equals(materialRecord.getTrans_user_id())) {
			result.put("msg", new String[] { "接收人不能是自己" });
			return result;
		} // IF

		// 安全密码验证
		String[] checkSafe = checkSafe(session.getAttribute("session.user.id")
				.toString(), user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			return result;
		} // IF

		// 组合数据
		materialRecord.setUser_id(my_id);
		materialRecord.setStatus(1);
		materialRecord.setType_id(2);

		String[] msg = materialRecordService.saveNew(materialRecord);
		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		// TODO
		result.put("success", true);
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