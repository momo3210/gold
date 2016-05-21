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

import com.momohelp.model.Buy;
import com.momohelp.model.BuySell;
import com.momohelp.model.Cfg;
import com.momohelp.model.Farm;
import com.momohelp.model.FarmFeed;
import com.momohelp.model.FarmHatch;
import com.momohelp.model.MaterialRecord;
import com.momohelp.model.Sell;
import com.momohelp.model.User;
import com.momohelp.service.BuySellService;
import com.momohelp.service.BuyService;
import com.momohelp.service.CfgService;
import com.momohelp.service.FarmFeedService;
import com.momohelp.service.FarmHatchService;
import com.momohelp.service.FarmService;
import com.momohelp.service.MaterialRecordService;
import com.momohelp.service.SellService;
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

	@Autowired
	private FarmService farmService;

	@Autowired
	private FarmFeedService farmFeedService;

	@Autowired
	private FarmHatchService farmHatchService;

	@Autowired
	private SellService sellService;

	@Autowired
	private BuyService buyService;

	@Autowired
	private BuySellService buySellService;

	/**
	 * 验证令牌
	 *
	 * @param session
	 * @param token
	 * @return
	 */
	private String[] validateToken(HttpSession session, String token) {
		token = StringUtil.isEmpty(token);

		if (null == token) {
			session.removeAttribute("token");
			return new String[] { "请不要重复提交" };
		}

		Object session_token = session.getAttribute("token");
		if (null == session_token) {
			return new String[] { "请不要重复提交" };
		}

		if (!token.equals(session_token.toString())) {
			session.removeAttribute("token");
			return new String[] { "请不要重复提交" };
		}

		session.removeAttribute("token");
		return null;
	}

	/**
	 * 生成令牌
	 *
	 * @param session
	 * @return
	 */
	private String genToken(HttpSession session) {
		int i = (int) ((Math.random() * 5 + 1) * 1000);
		String token = String.valueOf(i);
		session.setAttribute("token", token);
		return token;
	}

	/**
	 * 安全密码验证
	 *
	 * @param session
	 * @param pass_safe
	 * @return
	 */
	private String[] checkSafe(HttpSession session, String pass_safe) {
		String my_user_id = session.getAttribute("session.user.id").toString();
		User my_user = userService.selectByKey(my_user_id);
		// TODO
		return (MD5.encode(pass_safe).equals(my_user.getUser_pass_safe())) ? null
				: new String[] { "安全密码输入错误" };
	}

	/**
	 * 验证码
	 *
	 * @param session
	 * @param verifyCode
	 * @return
	 */
	private String[] verify(HttpSession session, String verifyCode) {
		String code = session.getAttribute("session.verifyCode").toString();
		// TODO
		return (verifyCode.equals(code)) ? null : new String[] { "验证码输入错误" };
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
		result.addObject("data_user", session.getAttribute("session.user"));
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

		Map<String, Object> login = userService.login(user.getEmail(),
				user.getUser_pass());

		if (login.containsKey("msg")) {
			result.put("msg", login.get("msg"));
			return result;
		}

		// 获取用户对象
		user = (User) login.get("data");

		// TODO
		session.setAttribute("session.user", user);
		session.setAttribute("session.user.id", user.getId());
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
	public ModelAndView _i_changePwdUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/changePwd");
		result.addObject("nav_choose", ",03,0302,");
		result.addObject("data_user", session.getAttribute("session.user"));
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
		result.put("success", false);

		// TODO
		String[] msg = userService.changePwd(
				session.getAttribute("session.user.id").toString(), old_pass,
				new_pass);

		if (null != msg) {
			result.put("msg", msg);
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
		result.put("success", false);

		// TODO
		String[] msg = userService.changePwdSafe(
				session.getAttribute("session.user.id").toString(), old_pass,
				new_pass);

		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		result.put("success", true);
		return result;
	}

	/**
	 * 确认打款
	 *
	 * @param session
	 * @param buySell
	 * @param verifyCode
	 * @param user_pass_safe
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/confirm" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_confirm(HttpSession session, BuySell buySell,
			@RequestParam(required = true) String verifyCode,
			@RequestParam(required = true) String user_pass_safe) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verify = verify(session, verifyCode);
		if (null != verify) {
			result.put("msg", verify);
			return result;
		}

		String[] checkSafe = checkSafe(session, user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			return result;
		} // IF

		result.put("success", true);
		return result;
	}

	/**
	 * 确认打款
	 *
	 * @param map
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/user/confirm" }, method = RequestMethod.GET)
	public String _i_confirmUI(Map<String, Object> map, HttpSession session,
			@RequestParam(required = true) String id) {

		BuySell buySell = buySellService.selectByKey(id);

		if (null == buySell) {
			return "redirect:/";
		}

		String user_id = session.getAttribute("session.user.id").toString();

		// 判断是买盘
		if (user_id.equals(buySell.getP_buy_user_id())) {
			map.put("nav_choose", ",05,0503,");
		} else {
			// 判断是卖盘
			if (user_id.equals(buySell.getP_sell_user_id())) {
				map.put("nav_choose", ",05,0504,");
			} else {
				return "redirect:/";
			}
		}

		map.put("data_buySell", buySell);

		// TODO
		map.put("data_user", session.getAttribute("session.user"));
		map.put("data_token", genToken(session));
		return "i/user/1.0.1/confirm";
	}

	/**
	 * 举报
	 *
	 * @param session
	 * @param buySell
	 * @param verifyCode
	 * @param user_pass_safe
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/tip_off" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_tip_off(HttpSession session, BuySell buySell,
			@RequestParam(required = true) String verifyCode,
			@RequestParam(required = true) String user_pass_safe) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verify = verify(session, verifyCode);
		if (null != verify) {
			result.put("msg", verify);
			return result;
		}

		String[] checkSafe = checkSafe(session, user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			return result;
		} // IF

		result.put("success", true);
		return result;
	}

	/**
	 * 举报
	 *
	 * @param map
	 * @param session
	 * @param id
	 * @param type
	 * @return
	 */
	@RequestMapping(value = { "/user/tip_off" }, method = RequestMethod.GET)
	public String _i_tip_offUI(Map<String, Object> map, HttpSession session,
			@RequestParam(required = true) String id) {

		BuySell buySell = buySellService.selectByKey(id);

		if (null == buySell) {
			return "redirect:/";
		}

		String user_id = session.getAttribute("session.user.id").toString();

		// 判断是买盘
		if (user_id.equals(buySell.getP_buy_user_id())) {
			map.put("nav_choose", ",05,0503,");
		} else {
			// 判断是卖盘
			if (user_id.equals(buySell.getP_sell_user_id())) {
				map.put("nav_choose", ",05,0504,");
			} else {
				return "redirect:/";
			}
		}

		map.put("data_buySell", buySell);

		// TODO
		map.put("data_user", session.getAttribute("session.user"));
		map.put("data_token", genToken(session));
		return "i/user/1.0.1/tip_off";
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
		result.put("success", false);

		String[] verify = verify(session, verifyCode);
		if (null != verify) {
			result.put("msg", verify);
			return result;
		}

		String[] checkSafe = checkSafe(session, user.getUser_pass_safe());
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			return result;
		} // IF

		// 设置主键
		user.setId(session.getAttribute("session.user.id").toString());

		String[] msg = userService.editInfo(user);
		if (null != msg) {
			result.put("msg", msg);
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
	public ModelAndView _i_accountUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/account");

		User user = userService.selectByKey(session.getAttribute(
				"session.user.id").toString());

		result.addObject("data_user", user);

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
		result.addObject("data_token", genToken(session));
		result.addObject("nav_choose", ",04,0401,");
		result.addObject("data_user", session.getAttribute("session.user"));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/createAccount" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_createAccount(
			@RequestParam(required = true) String token,
			@RequestParam(required = true) String verifyCode, User user,
			HttpSession session) {
		// TODO
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] validateToken = validateToken(session, token);
		if (null != validateToken) {
			result.put("msg", validateToken);
			return result;
		}

		// TODO
		String[] verify = verify(session, verifyCode);
		if (null != verify) {
			result.put("msg", verify);
			return result;
		}

		// 我的信息
		user.setPid(session.getAttribute("session.user.id").toString());

		String[] msg = userService.register(user);

		if (null != msg) {
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
		result.addObject("data_user", session.getAttribute("session.user"));
		return result;
	}

	/**
	 * 买入鸡苗
	 *
	 * @param verifyCode
	 * @param user_pass_safe
	 * @param farm
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/buyMo" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_buyMo(
			@RequestParam(required = true) String token,
			@RequestParam(required = true) String verifyCode,
			@RequestParam(required = true) String user_pass_safe, Farm farm,
			HttpSession session) {
		// TODO
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] validateToken = validateToken(session, token);
		if (null != validateToken) {
			result.put("msg", validateToken);
			return result;
		}

		String[] verify = verify(session, verifyCode);
		if (null != verify) {
			result.put("msg", verify);
			return result;
		}

		// 安全密码验证
		String[] checkSafe = checkSafe(session, user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			return result;
		} // IF

		farm.setUser_id(session.getAttribute("session.user.id").toString());

		String[] msg = farmService.buy(farm);
		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		// TODO
		result.put("success", true);
		return result;
	}

	/**
	 * 喂养鸡苗
	 *
	 * @param token
	 * @param verifyCode
	 * @param user_pass_safe
	 * @param farmFeed
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/feedMo" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_feedMo(
			@RequestParam(required = true) String token,
			@RequestParam(required = true) String verifyCode,
			@RequestParam(required = true) String user_pass_safe,
			FarmFeed farmFeed, HttpSession session) {
		// TODO
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] validateToken = validateToken(session, token);
		if (null != validateToken) {
			result.put("msg", validateToken);
			return result;
		}

		String[] verify = verify(session, verifyCode);
		if (null != verify) {
			result.put("msg", verify);
			return result;
		}

		// 安全密码验证
		String[] checkSafe = checkSafe(session, user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			return result;
		} // IF

		farmFeed.setUser_id(session.getAttribute("session.user.id").toString());

		String[] msg = farmFeedService.feed(farmFeed);
		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		// TODO
		result.put("success", true);
		return result;
	}

	/**
	 * 喂养鸡苗
	 *
	 * @param map
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/user/feedMo" }, method = RequestMethod.GET)
	public String _i_feedMoUI(Map<String, Object> map, HttpSession session,
			@RequestParam(required = false) String id) {

		String html = null;

		if (null == id || "".equals(id.trim())) {
			List<Farm> list = farmService.findCanFeed(session.getAttribute(
					"session.user.id").toString());
			map.put("data_list", list);

			html = "i/user/1.0.1/feedMo";
		} else {
			Farm farm = farmService.selectByKey(id);

			if (null == farm) {
				return "redirect:/user/feedMo";
			}

			// 判断今天是否已经喂过该批次的鸡苗了
			Map<String, Object> checkTodayFeed = farmFeedService
					.checkTodayFeed(farm.getId());
			if (null != checkTodayFeed) {
				if (checkTodayFeed.containsKey("msg")) {
					map.put("data_msg",
							((String[]) checkTodayFeed.get("msg"))[0]);
				}
			}

			map.put("data_id", id);
			map.put("data_farm", farm);
			html = "i/user/1.0.1/feedMo_id";
		}

		// TODO
		map.put("nav_choose", ",05,0505,");
		map.put("data_user", session.getAttribute("session.user"));
		map.put("data_token", genToken(session));

		return html;
	}

	/**
	 * 孵化工厂
	 *
	 * @param token
	 * @param verifyCode
	 * @param user_pass_safe
	 * @param farmHatch
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/hatchMo" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_hatchMo(
			@RequestParam(required = true) String token,
			@RequestParam(required = true) String verifyCode,
			@RequestParam(required = true) String user_pass_safe,
			FarmHatch farmHatch, HttpSession session) {
		// TODO
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] validateToken = validateToken(session, token);
		if (null != validateToken) {
			result.put("msg", validateToken);
			return result;
		}

		String[] verify = verify(session, verifyCode);
		if (null != verify) {
			result.put("msg", verify);
			return result;
		}

		// 安全密码验证
		String[] checkSafe = checkSafe(session, user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			return result;
		} // IF

		farmHatch
				.setUser_id(session.getAttribute("session.user.id").toString());

		String[] msg = farmHatchService.hatch(farmHatch);
		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		// TODO
		result.put("success", true);
		return result;
	}

	/**
	 * 孵化工厂
	 *
	 * @param map
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/user/hatchMo" }, method = RequestMethod.GET)
	public String _i_hatchMoUI(Map<String, Object> map, HttpSession session,
			@RequestParam(required = false) String id) {

		String html = null;

		if (null == id || "".equals(id.trim())) {
			List<Farm> list = farmService.findCanHatch(session.getAttribute(
					"session.user.id").toString());
			map.put("data_list", list);

			html = "i/user/1.0.1/hatchMo";
		} else {
			Farm farm = farmService.selectByKey(id);

			if (null == farm) {
				return "redirect:/user/hatchMo";
			}

			map.put("data_id", id);
			map.put("data_farm", farm);
			html = "i/user/1.0.1/hatchMo_id";
		}

		// TODO
		map.put("nav_choose", ",05,0506,");
		map.put("data_user", session.getAttribute("session.user"));
		map.put("data_token", genToken(session));

		return html;
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
		result.addObject("data_token", genToken(session));

		// TODO
		result.addObject("nav_choose", ",05,0501,");
		result.addObject("data_user", session.getAttribute("session.user"));
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
		result.addObject("data_token", genToken(session));

		result.addObject("nav_choose", ",05,0502,");
		result.addObject("data_user", session.getAttribute("session.user"));
		return result;
	}

	/**
	 * 卖出鸡苗
	 *
	 * @param token
	 * @param verifyCode
	 * @param user_pass_safe
	 * @param farm
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/sellMo" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_sellMo(
			@RequestParam(required = true) String token,
			@RequestParam(required = true) String verifyCode,
			@RequestParam(required = true) String user_pass_safe, Sell sell,
			HttpSession session) {
		// TODO
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] validateToken = validateToken(session, token);
		if (null != validateToken) {
			result.put("msg", validateToken);
			return result;
		}

		String[] verify = verify(session, verifyCode);
		if (null != verify) {
			result.put("msg", verify);
			return result;
		}

		// 安全密码验证
		String[] checkSafe = checkSafe(session, user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			return result;
		} // IF

		sell.setUser_id(session.getAttribute("session.user.id").toString());

		String[] msg = sellService.sell(sell);
		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		// TODO
		result.put("success", true);
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
		result.addObject("data_user", session.getAttribute("session.user"));

		List<Buy> list_buy = buyService.findUnFinishDeal(session.getAttribute(
				"session.user.id").toString());
		result.addObject("data_list_buy", list_buy);

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
		result.addObject("data_user", session.getAttribute("session.user"));

		List<Sell> list_sell = sellService.findUnFinishDeal(session
				.getAttribute("session.user.id").toString());
		result.addObject("data_list_sell", list_sell);

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
		String my_user_id = session.getAttribute("session.user.id").toString();
		// TODO
		User my_user = userService.selectByKey(my_user_id);
		map.put("data_user", my_user);
		map.put("data_token", genToken(session));

		// TODO
		map.put("nav_choose", ",06,0602,");
		return "i/user/1.0.1/buyTicket";
	}

	@ResponseBody
	@RequestMapping(value = { "/user/buyTicket" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_buyTicket(
			@RequestParam(required = true) String user_pass_safe,
			@RequestParam(required = true) String token,
			MaterialRecord materialRecord, HttpSession session) {
		// TODO
		materialRecord.setType_id(1);
		return _i_buy(user_pass_safe, token, materialRecord, session);
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

		String my_user_id = session.getAttribute("session.user.id").toString();
		// TODO
		User my_user = userService.selectByKey(my_user_id);
		result.addObject("data_user", my_user);
		result.addObject("data_token", genToken(session));

		result.addObject("nav_choose", ",06,0603,");
		return result;
	}

	/**
	 * 购买
	 *
	 * @param user_pass_safe
	 * @param token
	 * @param materialRecord
	 * @param session
	 * @return
	 */
	private Map<String, Object> _i_buy(String user_pass_safe, String token,
			MaterialRecord materialRecord, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] validateToken = validateToken(session, token);
		if (null != validateToken) {
			result.put("msg", validateToken);
			return result;
		}

		// 安全密码验证
		String[] checkSafe = checkSafe(session, user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			return result;
		} // IF

		// 组合数据
		materialRecord.setUser_id(session.getAttribute("session.user.id")
				.toString());
		materialRecord.setStatus(0);

		String[] msg = materialRecordService.buy(materialRecord);
		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		// TODO
		result.put("success", true);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/buyFood" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_buyFood(
			@RequestParam(required = true) String user_pass_safe,

			@RequestParam(required = true) String token,
			MaterialRecord materialRecord, HttpSession session) {
		// TODO
		materialRecord.setType_id(2);
		return _i_buy(user_pass_safe, token, materialRecord, session);
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
		result.addObject("data_user", session.getAttribute("session.user"));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/virementTicket" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_virementTicket(
			@RequestParam(required = true) String user_pass_safe,
			MaterialRecord materialRecord, HttpSession session) {
		// TODO
		materialRecord.setType_id(1);
		return _i_virement(user_pass_safe, materialRecord, session);
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
		result.addObject("data_user", session.getAttribute("session.user"));
		return result;
	}

	/**
	 * 转账
	 *
	 * @param user_pass_safe
	 * @param materialRecord
	 * @param session
	 * @return
	 */
	private Map<String, Object> _i_virement(String user_pass_safe,
			MaterialRecord materialRecord, HttpSession session) {
		// BEGIN
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		// 参数验证
		materialRecord.setTrans_user_id(StringUtil.isEmpty(materialRecord
				.getTrans_user_id()));
		if (null == materialRecord.getTrans_user_id()) {
			result.put("msg", new String[] { "请选择接收人" });
			return result;
		}

		// 安全密码验证
		String[] checkSafe = checkSafe(session, user_pass_safe);
		if (null != checkSafe) {
			result.put("msg", checkSafe);
			return result;
		} // IF

		// 组合数据
		materialRecord.setUser_id(session.getAttribute("session.user.id")
				.toString());

		String[] msg = materialRecordService.virement(materialRecord);
		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		// TODO
		result.put("success", true);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/virementFood" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_virementFood(
			@RequestParam(required = true) String user_pass_safe,
			MaterialRecord materialRecord, HttpSession session) {
		// TODO
		materialRecord.setType_id(2);
		return _i_virement(user_pass_safe, materialRecord, session);
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
		result.addObject("data_user", session.getAttribute("session.user"));
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
		result.addObject("data_user", session.getAttribute("session.user"));
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
		result.addObject("data_user", session.getAttribute("session.user"));
		return result;
	}

	/**
	 * 门票对账单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/bill" }, method = RequestMethod.GET)
	public ModelAndView _i_ticketRecordUI(
			@RequestParam(required = true) int type_id, HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/bill");

		String nav_choose = null;
		// TODO
		switch (type_id) {
		case 1:
			nav_choose = ",06,0609,";
			break;
		case 2:
			nav_choose = ",06,0610,";
			break;
		case 3:
			nav_choose = ",06,0607,";
			break;
		case 4:
			nav_choose = ",06,0608,";
			break;
		default:
			type_id = 1;
			nav_choose = ",06,0609,";
			break;
		}

		String my_user_id = session.getAttribute("session.user.id").toString();

		// TODO
		MaterialRecord materialRecord = new MaterialRecord();
		materialRecord.setUser_id(my_user_id);
		materialRecord.setType_id(type_id);

		List<MaterialRecord> list = materialRecordService
				.findByTypeId(materialRecord);

		result.addObject("data_list", list);
		result.addObject("data_type_id", type_id);

		// TODO
		result.addObject("nav_choose", nav_choose);
		result.addObject("data_user", session.getAttribute("session.user"));
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
		ModelAndView result = new ModelAndView("i/user/1.0.1/foodRecord");

		String my_user_id = session.getAttribute("session.user.id").toString();

		// TODO
		MaterialRecord materialRecord = new MaterialRecord();
		materialRecord.setUser_id(my_user_id);
		materialRecord.setTrans_user_id(my_user_id);
		materialRecord.setType_id(2);

		List<MaterialRecord> list = materialRecordService
				.findByTypeId(materialRecord);

		result.addObject("data_list", list);

		result.addObject("nav_choose", ",06,0610,");
		result.addObject("data_user", session.getAttribute("session.user"));
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