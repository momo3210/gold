package com.momohelp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.momohelp.model.Notice;
import com.momohelp.model.Prize;
import com.momohelp.model.User;
import com.momohelp.service.BuyService;
import com.momohelp.service.NoticeService;
import com.momohelp.service.PrizeService;
import com.momohelp.service.SellService;
import com.momohelp.service.UserService;
import com.momohelp.util.StringUtil;

/**
 *
 * @author Administrator
 *
 */
@Controller
public class DefaultController {

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private BuyService buyService;

	@Autowired
	private SellService sellService;

	@Autowired
	private PrizeService prizeService;

	@Autowired
	private UserService userService;

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
	 * 验证码
	 *
	 * @param session
	 * @param verifyCode
	 * @return
	 */
	private String[] verify(HttpSession session, String verifyCode) {
		String code = session.getAttribute("session.verifyCode").toString();
		// TODO
		return (verifyCode.equals(code)) ? null : new String[] { "图形验证码输入错误" };
	}

	/**
	 * 验证手机号
	 *
	 * @param session
	 * @param verifyCode
	 * @return
	 */
	private String[] verifySms(HttpSession session, String mobile) {
		mobile = StringUtil.isEmpty(mobile);
		if (null == mobile) {
			return new String[] { "短信验证失败" };
		}

		if (null == session.getAttribute("sms_mobile")) {
			return new String[] { "短信验证失败" };
		}

		String code = session.getAttribute("sms_mobile").toString();
		return code.equals(mobile) ? null : new String[] { "短信验证失败" };
	}

	@RequestMapping(value = { "/t/{user_id}" }, method = RequestMethod.GET)
	public ModelAndView _i_tUI(HttpSession session, @PathVariable String user_id) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/t");
		result.addObject("user_id", user_id);
		result.addObject("data_token", genToken(session));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/t/" }, method = RequestMethod.POST, produces = "application/json")
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

		String[] verifySms = verifySms(session, user.getVerifycode_sms());
		if (null != verifySms) {
			result.put("msg", verifySms);
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

	@ResponseBody
	@RequestMapping(value = { "/sendSms" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_changePwd(HttpSession session, User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		user.setMobile(StringUtil.isEmpty(user.getMobile()));
		if (null == user.getMobile()) {
			result.put("msg", new String[] { "请输入手机号" });
			return result;
		}

		session.setAttribute("sms_mobile", user.getMobile());

		session.setAttribute("sms_mobile", "2233");

		// SmSWebService service = new SmSWebService();
		// SmSWebServiceSoap serviceSoap = service.getSmSWebServiceSoap();
		// WsSendResponse response = serviceSoap.sendSms("154", "MOMO668",
		// "123456", __u.getMobile(), "您本次验证码:" + user.getVerifycode_sms()
		// + "，感谢您的支持，祝您生活愉快！！", "", "");
		// response.getReturnStatus();

		// String[] msg = userService.sendSms(session.getAttribute(
		// "session.user.id").toString());
		//
		// if (null != msg) {
		// result.put("msg", msg);
		// return result;
		// }

		result.put("success", true);
		return result;
	}

	/**
	 * 首页
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView _i_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/index");

		Notice notice = new Notice();
		List<Notice> list_notice = noticeService.findByNotice(notice, 1, 6);
		result.addObject("data_list_notice", list_notice);

		result.addObject("nav_choose", ",02,");
		result.addObject("data_user", session.getAttribute("session.user"));

		// 买盘匹配
		User buy_record = userService.buy_record__list__4(session.getAttribute(
				"session.user.id").toString());
		result.addObject("data_buy_record", buy_record);

		// 卖盘匹配
		User sell_record = userService.sell_record__list__4(session
				.getAttribute("session.user.id").toString());
		result.addObject("data_sell_record", sell_record);

		// 买盘
		// List<Buy> list_buy =
		// buyService.findUnFinishDeal(session.getAttribute(
		// "session.user.id").toString());
		// result.addObject("data_list_buy", list_buy);

		// 卖盘
		// List<Sell> list_sell = sellService.findUnFinishDeal(session
		// .getAttribute("session.user.id").toString());
		// result.addObject("data_list_sell", list_sell);

		List<Prize> dongjieliebiao = prizeService.findByUserId(session
				.getAttribute("session.user.id").toString());

		double d = 0;

		for (int i = 0; i < dongjieliebiao.size(); i++) {
			Prize item = dongjieliebiao.get(i);

			if (item.getFlag() == 0) {
				d += item.getMoney();
			}
		}

		result.addObject("data_dj", d);

		return result;
	}

	/**** 后台 ****/

	@RequestMapping(value = { "/manage/" }, method = RequestMethod.GET)
	public ModelAndView _manage_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("m/default/index");
		result.addObject("data_user", session.getAttribute("session.user"));
		return result;
	}

	@RequestMapping(value = { "/manage/user/" }, method = RequestMethod.GET)
	public ModelAndView _manage_userUI(HttpSession session) {
		ModelAndView result = new ModelAndView("m/user/index");

		result.addObject("data_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",08,0801,");
		List<User> list = userService.selectByExample(null);
		result.addObject("data_list", list);

		return result;
	}

	@RequestMapping(value = { "/manage/user/edit" }, method = RequestMethod.GET)
	public String _i_editUI(Map<String, Object> map, HttpSession session,
			@RequestParam(required = true) String id) {

		User user = userService.selectByKey(id);
		map.put("edit_user", user);
		map.put("data_user", session.getAttribute("session.user"));
		map.put("nav_choose", ",08,0801,");
		return "m/user/edit";
	}

	@ResponseBody
	@RequestMapping(value = { "/manage/user/edit" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_edit(User user, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		userService.updateNotNull(user);

		result.put("success", true);
		return result;
	}

	@RequestMapping(value = { "/manage/user/add" }, method = RequestMethod.GET)
	public String _i_addtUI(Map<String, Object> map, HttpSession session) {
		map.put("data_user", session.getAttribute("session.user"));
		map.put("nav_choose", ",08,0801,");
		return "m/user/add";
	}

}