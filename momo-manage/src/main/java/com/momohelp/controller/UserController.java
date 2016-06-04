package com.momohelp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.model.BuySell;
import com.momohelp.model.Farm;
import com.momohelp.model.FarmFeed;
import com.momohelp.model.FarmHatch;
import com.momohelp.model.Manager;
import com.momohelp.model.MaterialRecord;
import com.momohelp.model.Sell;
import com.momohelp.model.User;
import com.momohelp.service.BuySellService;
import com.momohelp.service.FarmFeedService;
import com.momohelp.service.FarmHatchService;
import com.momohelp.service.FarmService;
import com.momohelp.service.ManagerService;
import com.momohelp.service.MaterialRecordService;
import com.momohelp.service.SellService;
import com.momohelp.service.UserService;
import com.momohelp.util.StringUtil;
import com.momohelp.util.encryptUtil.MD5;

import freemarker.template.Template;

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
	private ManagerService managerService;

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
	private BuySellService buySellService;

	@Autowired
	private FreeMarkerConfigurer freemarkerConfigurer;

	/**
	 * 短信验证码
	 *
	 * @param session
	 * @param sms
	 * @return
	 */
	private String[] verifySMS(HttpSession session, String sms) {

		sms = StringUtil.isEmpty(sms);

		if (null == sms) {
			return new String[] { "请输入短信验证码" };
		}

		Object verify = session.getAttribute("verify.sms");

		if (null == verify) {
			return new String[] { "请获取短信验证码" };
		}

		if (!sms.equals(verify.toString())) {
			return new String[] { "短信验证失败" };
		}

		session.removeAttribute("verify.sms");
		return null;
	}

	/**
	 * 验证令牌
	 *
	 * @param session
	 * @param verify_token
	 * @return
	 */
	private String[] verifyToken(HttpSession session, String token) {

		token = StringUtil.isEmpty(token);

		if (null == token) {
			session.removeAttribute("verify.token");
			return new String[] { "请刷新页面" };
		}

		Object verify = session.getAttribute("verify.token");

		if (null == verify) {
			return new String[] { "请刷新页面" };
		}

		if (!token.equals(verify.toString())) {
			session.removeAttribute("verify.token");
			return new String[] { "请刷新页面" };
		}

		session.removeAttribute("verify.token");
		return null;
	}

	/**
	 * 生成令牌
	 *
	 * @param session
	 * @return
	 */
	private String genVerifyToken(HttpSession session) {
		int i = (int) ((Math.random() * 5 + 1) * 1000);
		session.setAttribute("verify.token", String.valueOf(i));
		return session.getAttribute("verify.token").toString();
	}

	/**
	 * 安全密码验证
	 *
	 * @param session
	 * @param user_pass_safe
	 * @return
	 */
	private String[] verifyPassSafe(HttpSession session, String user_pass_safe) {

		user_pass_safe = StringUtil.isEmpty(user_pass_safe);

		if (null == user_pass_safe) {
			return new String[] { "请输入安全密码" };
		}

		User user = userService.selectByKey(session.getAttribute(
				"session.user.id").toString());

		return user.getUser_pass_safe().equals(MD5.encode(user_pass_safe)) ? null
				: new String[] { "安全密码输入错误" };
	}

	/**
	 * 图形验证码
	 *
	 * @param session
	 * @param imgCode
	 * @return
	 */
	private String[] verifyImg(HttpSession session, String imgCode) {

		imgCode = StringUtil.isEmpty(imgCode);

		if (null == imgCode) {
			return new String[] { "请输入图形验证码" };
		}

		Object verify = session.getAttribute("verify.imgCode");

		if (null == verify) {
			return new String[] { "请刷新页面" };
		}

		return verify.toString().equals(imgCode.toLowerCase()) ? null
				: new String[] { "图形验证码输入错误" };
	}

	/**
	 * 推荐注册
	 *
	 * @param session
	 * @param map
	 * @param t
	 * @return
	 */
	@RequestMapping(value = { "/r" }, method = RequestMethod.GET)
	public String _i_rUI(HttpSession session, Map<String, Object> map,
			@RequestParam(required = false) String t) {

		session.removeAttribute("session.user");
		session.removeAttribute("session.user.id");
		session.removeAttribute("session.user.lv");
		session.removeAttribute("session.user.status");
		session.removeAttribute("session.time");

		t = StringUtil.isEmpty(t);

		if (null == t) {
			return "redirect:/";
		}

		map.put("data_user_pid", t);
		map.put("verify_token", genVerifyToken(session));
		return "i/user/1.0.1/rt";
	}

	// @RequestMapping(value = { "/t/{pid}" }, method = RequestMethod.GET)
	// public ModelAndView _i_tUI(HttpSession session, @PathVariable String pid)
	// {
	//
	// ModelAndView result = new ModelAndView("i/user/1.0.1/t");
	// result.addObject("data_pid", pid);
	// result.addObject("verify_token", genVerifyToken(session));
	// return result;
	// }

	@ResponseBody
	@RequestMapping(value = { "/r" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_t(HttpSession session,
			@RequestParam(required = true) String verify_token,
			@RequestParam(required = true) String verify_imgCode,
			@RequestParam(required = true) String verify_sms, User user) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifyToken = verifyToken(session, verify_token);
		if (null != verifyToken) {
			result.put("msg", verifyToken);
			return result;
		}

		String[] verifyImg = verifyImg(session, verify_imgCode);
		if (null != verifyImg) {
			result.put("msg", verifyImg);
			return result;
		}

		String[] verifySMS = verifySMS(session, verify_sms);
		if (null != verifySMS) {
			result.put("msg", verifySMS);
			return result;
		}

		String[] msg = userService.register(user);

		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		result.put("success", true);
		return result;
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
		result.addObject("data_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",01,");
		return result;
	}

	/**
	 * 登陆
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/login" }, method = RequestMethod.GET)
	public ModelAndView _i_loginUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/login");
		result.addObject("verify_token", genVerifyToken(session));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/login" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_login(HttpSession session,
			@RequestParam(required = true) String user_name,
			@RequestParam(required = true) String user_pass,
			@RequestParam(required = true) String verify_token) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifyToken = verifyToken(session, verify_token);
		if (null != verifyToken) {
			result.put("msg", verifyToken);
			return result;
		}

		Map<String, Object> login = userService.login(user_name, user_pass);

		if (login.containsKey("msg")) {
			result.put("msg", login.get("msg"));
			return result;
		}

		// 获取用户对象
		User user = (User) login.get("data");

		session.setAttribute("session.user", user);
		session.setAttribute("session.user.id", user.getId());
		session.setAttribute("session.user.lv", 2);
		session.setAttribute("session.user.status", user.getStatus());
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
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/user/changePwd" }, method = RequestMethod.GET)
	public ModelAndView _i_changePwdUI(HttpSession session) {

		ModelAndView result = new ModelAndView("i/user/1.0.1/changePwd");
		result.addObject("data_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",03,0302,");
		return result;
	}

	/**
	 * 普通密码修改
	 *
	 * @param session
	 * @param old_pass
	 * @param new_pass
	 * @param verify_sms
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/changePwd" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_changePwd(HttpSession session,
			@RequestParam(required = true) String old_pass,
			@RequestParam(required = true) String new_pass,
			@RequestParam(required = true) String verify_sms) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifySMS = verifySMS(session, verify_sms);
		if (null != verifySMS) {
			result.put("msg", verifySMS);
			return result;
		}

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
	 * 安全密码修改
	 *
	 * @param session
	 * @param old_pass
	 * @param new_pass
	 * @param verify_sms
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/changePwdSafe" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_changePwdSafe(HttpSession session,
			@RequestParam(required = true) String old_pass,
			@RequestParam(required = true) String new_pass,
			@RequestParam(required = true) String verify_sms) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifySms = verifySMS(session, verify_sms);
		if (null != verifySms) {
			result.put("msg", verifySms);
			return result;
		}

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
	 * @param verify_imgCode
	 * @param user_pass_safe
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/confirm" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_confirm(HttpSession session,
			@RequestParam(required = true) String verify_imgCode,
			@RequestParam(required = true) String user_pass_safe,
			BuySell buySell) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifyImg = verifyImg(session, verify_imgCode);
		if (null != verifyImg) {
			result.put("msg", verifyImg);
			return result;
		}

		String[] verifyPassSafe = verifyPassSafe(session, user_pass_safe);
		if (null != verifyPassSafe) {
			result.put("msg", verifyPassSafe);
			return result;
		}

		String[] confirm = buySellService.confirm(buySell, session
				.getAttribute("session.user.id").toString());

		if (null != confirm) {
			result.put("msg", confirm);
			return result;
		}

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
	public String _i_confirmUI(HttpSession session, Map<String, Object> map,
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
		map.put("verify_token", genVerifyToken(session));
		map.put("data_user", session.getAttribute("session.user"));
		return "i/user/1.0.1/confirm";
	}

	private static final SimpleDateFormat sdf_2 = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static final SimpleDateFormat sdf_1 = new SimpleDateFormat(
			"yyyyMMdd");

	/**
	 * 文件后缀
	 *
	 * @param file_name
	 * @return
	 */
	private String getExtName(String file_name) {
		int i = file_name.lastIndexOf(".");
		String ext = (-1 == i) ? "" : file_name.substring(i);
		return ext.toLowerCase();
	}

	/**
	 * 浏览图片
	 *
	 * @param session
	 * @param id
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(path = "/user/confirm_upload", method = RequestMethod.GET)
	public String _i_confirm_uploadUI(HttpSession session,
			@RequestParam(required = true) String id,
			HttpServletResponse response) throws IOException {

		BuySell buySell = buySellService.selectByKey(id);

		if (null == buySell) {
			return "redirect:/";
		}

		String user_id = session.getAttribute("session.user.id").toString();

		if (user_id.equals(buySell.getP_buy_user_id())) {
		} else if (user_id.equals(buySell.getP_sell_user_id())) {
		} else {
			return "redirect:/";
		}

		response.setContentType("image/jpeg");
		File file = new File("C:/momohelp/" + buySell.getP_buy_user_img());

		if (!file.exists()) {
			return null;
		}

		InputStream in = new FileInputStream(file);
		OutputStream os = response.getOutputStream();
		byte[] b = new byte[1024];
		while (in.read(b) != -1) {
			os.write(b);
		}
		in.close();
		os.flush();
		os.close();
		return null;
	}

	/**
	 * 上传图片
	 *
	 * @param session
	 * @param file
	 * @return
	 */
	@RequestMapping(path = "/user/confirm_upload", method = RequestMethod.POST)
	public Map<String, Object> _i_confirm_upload(HttpSession session,
			@RequestParam(value = "file") MultipartFile file) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		if (file.isEmpty()) {
			result.put("msg", "请选择图片");
			return result;
		}

		// 文件后缀
		String file_ext = getExtName(file.getOriginalFilename());

		if ("".equals(file_ext)) {
			result.put("msg", "请选择图片");
			return result;
		}

		if (!".jpg".equals(file_ext)) {
			result.put("msg", "请选择正确的图片格式");
			return result;
		}

		Date date = new Date();

		String date_1 = sdf_1.format(date);

		File folder = new File("c://momohelp/" + date_1);
		// 如果文件夹不存在则创建
		if (!folder.exists() && !folder.isDirectory()) {
			folder.mkdir();
		}

		String date_2 = sdf_2.format(date);

		String file_name = session.getAttribute("session.user.id").toString()
				+ "_" + date_2 + file_ext;

		// byte[] bytes = file.getBytes();

		try {
			file.transferTo(new File("c://momohelp/" + date_1 + "/" + file_name));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "上传图片失败");
			return result;
		}

		result.put("data", date_1 + "/" + file_name);
		result.put("success", true);
		return result;
	}

	/**
	 * 举报
	 *
	 * @param session
	 * @param buySell
	 * @param verify_imgCode
	 * @param user_pass_safe
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/tip_off" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_tip_off(HttpSession session,
			@RequestParam(required = true) String verify_imgCode,
			@RequestParam(required = true) String user_pass_safe,
			BuySell buySell) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifyImg = verifyImg(session, verify_imgCode);
		if (null != verifyImg) {
			result.put("msg", verifyImg);
			return result;
		}

		String[] verifyPassSafe = verifyPassSafe(session, user_pass_safe);
		if (null != verifyPassSafe) {
			result.put("msg", verifyPassSafe);
			return result;
		}

		// 举报人id
		buySell.setTip_off_user_id(session.getAttribute("session.user.id")
				.toString());

		String[] tip_off = buySellService.tip_off(buySell);

		if (null != tip_off) {
			result.put("msg", tip_off);
			return result;
		}

		result.put("success", true);
		return result;
	}

	/**
	 * 举报
	 *
	 * @param session
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/user/tip_off" }, method = RequestMethod.GET)
	public String _i_tip_offUI(HttpSession session, Map<String, Object> map,
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
		map.put("verify_token", genVerifyToken(session));
		map.put("data_user", session.getAttribute("session.user"));
		return "i/user/1.0.1/tip_off";
	}

	/**
	 * 修改资料
	 *
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/user/profile" }, method = RequestMethod.GET)
	public String _i_profileUI(HttpSession session, Map<String, Object> map) {

		User user = userService.selectByKey(session.getAttribute(
				"session.user.id").toString());
		map.put("data_user", user);

		map.put("nav_choose", ",03,0301,");
		return "i/user/1.0.1/profile";
	}

	@ResponseBody
	@RequestMapping(value = { "/user/profile" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_profile(HttpSession session,
			@RequestParam(required = true) String verify_sms,
			@RequestParam(required = true) String verify_imgCode,
			@RequestParam(required = true) String user_pass_safe, User user) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifyImg = verifyImg(session, verify_imgCode);
		if (null != verifyImg) {
			result.put("msg", verifyImg);
			return result;
		}

		String[] verifyPassSafe = verifyPassSafe(session, user_pass_safe);
		if (null != verifyPassSafe) {
			result.put("msg", verifyPassSafe);
			return result;
		}

		String[] verifySMS = verifySMS(session, verify_sms);
		if (null != verifySMS) {
			result.put("msg", verifySMS);
			return result;
		}

		// 设置主键
		user.setId(session.getAttribute("session.user.id").toString());
		userService.editInfo(user);

		result.put("success", true);
		return result;
	}

	/**
	 * 我的帐户
	 *
	 * @param session
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
		result.addObject("verify_token", genVerifyToken(session));
		result.addObject("data_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",04,0401,");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/createAccount" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_createAccount(HttpSession session,
			@RequestParam(required = true) String verify_token,
			@RequestParam(required = true) String verify_imgCode, User user) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifyToken = verifyToken(session, verify_token);
		if (null != verifyToken) {
			result.put("msg", verifyToken);
			return result;
		}

		String[] verifyImg = verifyImg(session, verify_imgCode);
		if (null != verifyImg) {
			result.put("msg", verifyImg);
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
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = { "/user/recommend" }, method = RequestMethod.GET)
	public ModelAndView _i_recommendUI(HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "100") int rows) {

		ModelAndView result = new ModelAndView("i/user/1.0.1/recommend");

		List<User> list = userService.findChildren___4(
				session.getAttribute("session.user.id").toString(), page,
				Integer.MAX_VALUE);
		result.addObject("data_list", list);

		result.addObject("data_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",04,0402,");
		return result;
	}

	/**
	 * 买入鸡苗
	 *
	 * @param session
	 * @param verify_token
	 * @param verify_imgCode
	 * @param user_pass_safe
	 * @param farm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/buyMo" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_buyMo(HttpSession session,
			@RequestParam(required = true) String verify_token,
			@RequestParam(required = true) String verify_imgCode,
			@RequestParam(required = true) String user_pass_safe, Farm farm) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifyToken = verifyToken(session, verify_token);
		if (null != verifyToken) {
			result.put("msg", verifyToken);
			return result;
		}

		String[] verifyImg = verifyImg(session, verify_imgCode);
		if (null != verifyImg) {
			result.put("msg", verifyImg);
			return result;
		}

		// 安全密码验证
		String[] verifyPassSafe = verifyPassSafe(session, user_pass_safe);
		if (null != verifyPassSafe) {
			result.put("msg", verifyPassSafe);
			return result;
		}

		farm.setUser_id(session.getAttribute("session.user.id").toString());

		String[] msg = farmService.buy(farm);
		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		result.put("success", true);
		return result;
	}

	/**
	 * 喂养鸡苗
	 *
	 * @param session
	 * @param verify_token
	 * @param verify_imgCode
	 * @param user_pass_safe
	 * @param farmFeed
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/feedMo" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_feedMo(HttpSession session,
			@RequestParam(required = true) String verify_token,
			@RequestParam(required = true) String verify_imgCode,
			@RequestParam(required = true) String user_pass_safe,
			FarmFeed farmFeed) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifyToken = verifyToken(session, verify_token);
		if (null != verifyToken) {
			result.put("msg", verifyToken);
			return result;
		}

		String[] verifyImg = verifyImg(session, verify_imgCode);
		if (null != verifyImg) {
			result.put("msg", verifyImg);
			return result;
		}

		// 安全密码验证
		String[] verifyPassSafe = verifyPassSafe(session, user_pass_safe);
		if (null != verifyPassSafe) {
			result.put("msg", verifyPassSafe);
			return result;
		}

		farmFeed.setUser_id(session.getAttribute("session.user.id").toString());

		String[] msg = farmFeedService.feed(farmFeed);
		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		result.put("success", true);
		return result;
	}

	/**
	 * 喂养鸡苗
	 *
	 * @param session
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/user/feedMo" }, method = RequestMethod.GET)
	public String _i_feedMoUI(HttpSession session, Map<String, Object> map,
			@RequestParam(required = false) String id) {

		String uri = null;

		id = StringUtil.isEmpty(id);

		if (null == id) {

			List<Farm> list = farmService.feedMo_list___4(session.getAttribute(
					"session.user.id").toString());
			map.put("data_list", list);

			uri = "i/user/1.0.1/feedMo";

		} else {

			Farm farm = farmService.feedMo_farm_feed_list___4(id, session
					.getAttribute("session.user.id").toString());

			if (null == farm) {
				return "redirect:/user/feedMo";
			}

			// 判断今天是否已经喂过该批次的鸡苗了
			String[] checkTodayFeed = farmFeedService.checkTodayFeed___4(farm
					.getLastFarmFeed());
			if (null != checkTodayFeed) {
				map.put("data_msg", checkTodayFeed[0]);
			}

			map.put("data_farm", farm);
			map.put("verify_token", genVerifyToken(session));

			uri = "i/user/1.0.1/feedMo_id";
		}

		map.put("data_user", session.getAttribute("session.user"));
		map.put("nav_choose", ",05,0505,");
		return uri;
	}

	/**
	 * 孵化工厂
	 *
	 * @param session
	 * @param verify_token
	 * @param verify_imgCode
	 * @param user_pass_safe
	 * @param farmHatch
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/hatchMo" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_hatchMo(HttpSession session,
			@RequestParam(required = true) String verify_token,
			@RequestParam(required = true) String verify_imgCode,
			@RequestParam(required = true) String user_pass_safe,
			FarmHatch farmHatch) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifyToken = verifyToken(session, verify_token);
		if (null != verifyToken) {
			result.put("msg", verifyToken);
			return result;
		}

		String[] verifyImg = verifyImg(session, verify_imgCode);
		if (null != verifyImg) {
			result.put("msg", verifyImg);
			return result;
		}

		// 安全密码验证
		String[] verifyPassSafe = verifyPassSafe(session, user_pass_safe);
		if (null != verifyPassSafe) {
			result.put("msg", verifyPassSafe);
			return result;
		}

		farmHatch
				.setUser_id(session.getAttribute("session.user.id").toString());

		String[] msg = farmHatchService.hatch(farmHatch);

		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		result.put("success", true);
		return result;
	}

	/**
	 * 孵化工厂
	 *
	 * @param session
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/user/hatchMo" }, method = RequestMethod.GET)
	public String _i_hatchMoUI(HttpSession session, Map<String, Object> map,
			@RequestParam(required = false) String id) {

		String uri = null;

		id = StringUtil.isEmpty(id);

		if (null == id) {

			List<Farm> list = farmService.hatchMo_list__4(session.getAttribute(
					"session.user.id").toString());
			map.put("data_list", list);

			uri = "i/user/1.0.1/hatchMo";

		} else {

			Farm farm = farmService.hatchMo_farm_hatch_list___4(id, session
					.getAttribute("session.user.id").toString());

			if (null == farm) {
				return "redirect:/user/hatchMo";
			}

			map.put("data_farm", farm);
			map.put("verify_token", genVerifyToken(session));

			uri = "i/user/1.0.1/hatchMo_id";
		}

		map.put("data_user", session.getAttribute("session.user"));
		map.put("nav_choose", ",05,0506,");
		return uri;
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

		result.addObject("verify_token", genVerifyToken(session));

		result.addObject("data_user", session.getAttribute("session.user"));
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

		// 卖出鸡苗上限 2011
		result.addObject("verify_token", genVerifyToken(session));

		result.addObject("data_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",05,0502,");
		return result;
	}

	/**
	 * 卖出鸡苗
	 *
	 * @param session
	 * @param verify_sms
	 * @param verify_token
	 * @param verify_imgCode
	 * @param user_pass_safe
	 * @param sell
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/user/sellMo" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_sellMo(HttpSession session,
			@RequestParam(required = true) String verify_sms,
			@RequestParam(required = true) String verify_token,
			@RequestParam(required = true) String verify_imgCode,
			@RequestParam(required = true) String user_pass_safe, Sell sell) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifyToken = verifyToken(session, verify_token);
		if (null != verifyToken) {
			result.put("msg", verifyToken);
			return result;
		}

		String[] verifyImg = verifyImg(session, verify_imgCode);
		if (null != verifyImg) {
			result.put("msg", verifyImg);
			return result;
		}

		String[] verifyPassSafe = verifyPassSafe(session, user_pass_safe);
		if (null != verifyPassSafe) {
			result.put("msg", verifyPassSafe);
			return result;
		}

		String[] verifySMS = verifySMS(session, verify_sms);
		if (null != verifySMS) {
			result.put("msg", verifySMS);
			return result;
		}

		sell.setUser_id(session.getAttribute("session.user.id").toString());

		String[] msg = sellService.sell(sell);

		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

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

		User user = userService.buy_record__list__4(session.getAttribute(
				"session.user.id").toString());

		result.addObject("data_buy_record", user.getFarms());
		result.addObject("data_user", user);

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

		User user = userService.sell_record__list__4(session.getAttribute(
				"session.user.id").toString());
		result.addObject("data_sell_record", user.getSells());

		result.addObject("data_user", user);

		result.addObject("nav_choose", ",05,0504,");
		return result;
	}

	/**
	 * 购买门票
	 *
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/user/buyTicket" }, method = RequestMethod.GET)
	public String _i_buyTicketUI(HttpSession session, Map<String, Object> map) {

		User user = userService.selectByKey(session.getAttribute(
				"session.user.id").toString());

		map.put("data_user", user);
		map.put("verify_token", genVerifyToken(session));

		map.put("nav_choose", ",06,0602,");
		return "i/user/1.0.1/buyTicket";
	}

	@ResponseBody
	@RequestMapping(value = { "/user/buyTicket" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_buyTicket(HttpSession session,
			@RequestParam(required = true) String user_pass_safe,
			@RequestParam(required = true) String verify_token,
			MaterialRecord materialRecord) {

		materialRecord.setType_id(1);
		return _i_buy(session, user_pass_safe, verify_token, materialRecord);
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

		User user = userService.selectByKey(session.getAttribute(
				"session.user.id").toString());

		result.addObject("data_user", user);
		result.addObject("verify_token", genVerifyToken(session));

		result.addObject("nav_choose", ",06,0603,");
		return result;
	}

	/**
	 * 购买
	 *
	 * @param session
	 * @param user_pass_safe
	 * @param verify_token
	 * @param materialRecord
	 * @return
	 */
	private Map<String, Object> _i_buy(HttpSession session,
			String user_pass_safe, String verify_token,
			MaterialRecord materialRecord) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		String[] verifyToken = verifyToken(session, verify_token);
		if (null != verifyToken) {
			result.put("msg", verifyToken);
			return result;
		}

		// 安全密码验证
		String[] verifyPassSafe = verifyPassSafe(session, user_pass_safe);
		if (null != verifyPassSafe) {
			result.put("msg", verifyPassSafe);
			return result;
		}

		// 组合数据
		materialRecord.setUser_id(session.getAttribute("session.user.id")
				.toString());
		materialRecord.setStatus(0);

		String[] msg = materialRecordService.buy(materialRecord);

		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		result.put("success", true);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/buyFood" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_buyFood(HttpSession session,
			@RequestParam(required = true) String user_pass_safe,
			@RequestParam(required = true) String verify_token,
			MaterialRecord materialRecord) {

		materialRecord.setType_id(2);
		return _i_buy(session, user_pass_safe, verify_token, materialRecord);
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
		result.addObject("data_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",06,0604,");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/virementTicket" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_virementTicket(HttpSession session,
			@RequestParam(required = true) String user_pass_safe,
			MaterialRecord materialRecord) {

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
		result.addObject("data_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",06,0605,");
		return result;
	}

	private Map<String, Object> _i_virement(String user_pass_safe,
			MaterialRecord materialRecord, HttpSession session) {

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
		String[] verifyPassSafe = verifyPassSafe(session, user_pass_safe);
		if (null != verifyPassSafe) {
			result.put("msg", verifyPassSafe);
			return result;
		}

		// 组合数据
		materialRecord.setUser_id(session.getAttribute("session.user.id")
				.toString());

		String[] msg = materialRecordService.virement(materialRecord);

		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		result.put("success", true);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/user/virementFood" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_virementFood(HttpSession session,
			@RequestParam(required = true) String user_pass_safe,
			MaterialRecord materialRecord) {

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
		result.addObject("data_user", session.getAttribute("session.user"));
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
		result.addObject("data_user", session.getAttribute("session.user"));
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
		result.addObject("data_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",06,0608,");
		return result;
	}

	/**
	 * 门票、饲料、动态、静态对账单
	 *
	 * @param session
	 * @param map
	 * @param type_id
	 * @return
	 */
	@RequestMapping(value = { "/user/bill" }, method = RequestMethod.GET)
	public String _i_ticketRecordUI(HttpSession session,
			Map<String, Object> map, @RequestParam(required = true) int type_id) {

		String nav_choose = null;

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
			return "redirect:/user/bill?type_id=1";
		}

		MaterialRecord materialRecord = new MaterialRecord();
		materialRecord.setUser_id(session.getAttribute("session.user.id")
				.toString());
		materialRecord.setType_id(type_id);

		List<MaterialRecord> list = materialRecordService
				.findByTypeId(materialRecord);

		map.put("data_list", list);
		map.put("data_type_id", type_id);

		map.put("data_user", session.getAttribute("session.user"));
		map.put("nav_choose", nav_choose);
		return "i/user/1.0.1/bill";
	}

	/***** ***** ***** ***** ***** 后台 ***** ***** ***** ***** *****/

	/**
	 * 展示树用
	 */
	@ResponseBody
	@RequestMapping(value = { "/manage/user/list" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_user_list(
			@RequestParam(required = true) String pid,
			@RequestParam(required = false, defaultValue = "1") int json) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);

		Example example = new Example(User.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("pid", pid);
		List<User> list = userService.selectByExample(example);

		if (1 == json) {
			result.put("data", list);
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("data_grid_list", list);

			try {
				Template template = freemarkerConfigurer.getConfiguration()
						.getTemplate("m/user/_pagelet/list.html");
				result.put("data", FreeMarkerTemplateUtils
						.processTemplateIntoString(template, map));
			} catch (Exception ignore) {
				result.put("success", false);
			}
		}

		return result;
	}

	@RequestMapping(value = { "/manage/user/" }, method = RequestMethod.GET)
	public String _manage_userUI(HttpSession session, Map<String, Object> map,
			@RequestParam(required = false) String id) {

		id = StringUtil.isEmpty(id);

		// 树的数据
		if (null == id) {

			// 获取父id为0的
			Example example = new Example(User.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("pid", "0");
			List<User> list_user = userService.selectByExample(example);

			map.put("data_tree_list", list_user);
			map.put("data_grid_list", list_user);

		} else {

			Example example = new Example(User.class);
			Example.Criteria criteria = example.createCriteria();
			// 查询用户昵称
			criteria.andEqualTo("mobile", id);
			List<User> list = userService.selectByExample(example);

			User user = (null == list || 1 != list.size()) ? null : list.get(0);

			if (null == user) {
				return "redirect:/manage/user/";
			}

			List<User> list_user = new ArrayList<User>();
			list_user.add(user);
			map.put("data_tree_list", list_user);
			map.put("data_grid_list", list_user);
		}

		map.put("search_user_id", id);

		map.put("session_user", session.getAttribute("session.user"));
		map.put("nav_choose", ",08,0801,");
		return "m/user/index";
	}

	/**
	 * 用户管理
	 *
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/add" }, method = RequestMethod.GET)
	public String _manage_user_addUI(HttpSession session,
			Map<String, Object> map) {
		map.put("session_user", session.getAttribute("session.user"));
		map.put("nav_choose", ",08,0801,");
		return "m/user/add";
	}

	@ResponseBody
	@RequestMapping(value = { "/manage/user/add" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_user_add(HttpSession session, User user) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		user.setUser_pass("123456");
		user.setUser_pass_safe("123456");

		String[] msg = userService.createUser(user);

		if (null != msg) {
			result.put("msg", msg);
			return result;
		}

		result.put("success", true);
		return result;
	}

	/**
	 * 会员详情
	 *
	 * @param session
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/info" }, method = RequestMethod.GET)
	public String _manage_user_infoUI(HttpSession session,
			Map<String, Object> map, @RequestParam(required = true) String id) {

		User user = userService.selectByKey(id);

		if (null == user) {
			return "redirect:/manage/user/";
		}

		map.put("data_user", user);

		// 买盘
		User buy_record = userService.buy_record__list__4(user.getId());
		map.put("data_buy_record", buy_record.getFarms());

		// 卖盘
		User sell_record = userService.sell_record__list__4(user.getId());
		map.put("data_sell_record", sell_record.getSells());

		map.put("session_user", session.getAttribute("session.user"));
		map.put("nav_choose", ",08,0801,");
		return "m/user/info";
	}

	/**
	 * 用户修改
	 *
	 * @param session
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/edit" }, method = RequestMethod.GET)
	public String _manage_user_editUI(HttpSession session,
			Map<String, Object> map, @RequestParam(required = true) String id) {

		User user = userService.selectByKey(id);

		if (null == user) {
			return "redirect:/manage/user/";
		}

		map.put("data_user", user);

		map.put("session_user", session.getAttribute("session.user"));
		map.put("nav_choose", ",08,0801,");
		return "m/user/edit";
	}

	@ResponseBody
	@RequestMapping(value = { "/manage/user/edit" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_user_edit(HttpSession session, User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		user.setPid(null);
		userService.updateNotNull(user);

		result.put("success", true);
		return result;
	}

	/**
	 * 重置密码123456
	 *
	 * @param session
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/manage/user/resetPwd" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_user_resetPwd(HttpSession session,
			User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		User _user = new User();
		_user.setId(user.getId());
		_user.setUser_pass(MD5.encode("123456"));
		_user.setUser_pass_safe(_user.getUser_pass());
		userService.updateNotNull(_user);

		result.put("success", true);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/manage/user/remove" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_user_remove(HttpSession session,
			User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		User _user = userService.selectByKey(user.getId());

		if (null == _user) {
			result.put("msg", "修改状态失败");
			return result;
		}

		User __user = new User();
		__user.setId(_user.getId());
		__user.setStatus((1 == _user.getStatus()) ? 0 : 1);
		userService.updateNotNull(__user);

		result.put("success", true);
		return result;
	}

	/**
	 * 登陆
	 *
	 * @return
	 */
	@RequestMapping(value = { "/manage/user/login" }, method = RequestMethod.GET)
	public ModelAndView _manage_user_loginUI() {
		ModelAndView result = new ModelAndView("m/user/login");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/manage/user/login" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_user_login(HttpSession session,
			@RequestParam(required = true) String user_name,
			@RequestParam(required = true) String user_pass) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		Map<String, Object> login = managerService.login(user_name, user_pass);

		if (login.containsKey("msg")) {
			result.put("msg", login.get("msg"));
			return result;
		}

		// 获取用户对象
		Manager user = (Manager) login.get("data");

		session.setAttribute("session.user", user);
		session.setAttribute("session.user.id", user.getId());
		session.setAttribute("session.user.lv", 1);
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
	@RequestMapping(value = { "/manage/user/logout" }, method = RequestMethod.GET)
	public String _manage_user_logoutUI(HttpSession session) {
		session.invalidate();
		return "redirect:/manage/user/login";
	}
}