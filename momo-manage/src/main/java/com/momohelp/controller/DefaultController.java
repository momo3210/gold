package com.momohelp.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.momohelp.model.Notice;
import com.momohelp.model.User;
import com.momohelp.service.NoticeService;
import com.momohelp.service.PrizeService;
import com.momohelp.service.UserService;

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
	private PrizeService prizeService;

	@Autowired
	private UserService userService;

	/**
	 * 生成短信验证码
	 *
	 * @return
	 */
	private String genSMS() {
		// int i = (int) ((Math.random() * 5 + 1) * 1000);
		// String id = String.valueOf(i);
		// if (4 < id.length()) {
		// id = id.substring(0, 4);
		// }
		// return id;
		return "1234";
	}

	/**
	 * 发送短信验证码
	 *
	 * @param session
	 * @param mobile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/sendSMS" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_sendSMS(HttpSession session,
			@RequestParam(required = true) String mobile) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		// 获取最后一次发送短信的时间
		Object last_time = session.getAttribute("verify.sms.lastTime");

		if (null != last_time) {
			// 当前时间加2分钟
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, -2);

			if (c.getTime().before((Date) last_time)) {
				result.put("msg", new String[] { "请不要频繁发送，稍等2分钟" });
				return result;
			}
		}

		session.setAttribute("verify.sms", genSMS());
		session.setAttribute("verify.sms.lastTime", new Date());

		// SmSWebService service = new SmSWebService();
		// SmSWebServiceSoap serviceSoap = service.getSmSWebServiceSoap();
		// WsSendResponse response = serviceSoap.sendSms("154", "MOMO668",
		// "123456", mobile,
		// "您本次验证码:" + session.getAttribute("verify.sms")
		// + "，感谢您的支持，祝您生活愉快！！", "", "");
		// result.put("code", response.getReturnStatus());

		result.put("success", true);
		return result;
	}

	/**
	 * 首页
	 *
	 * @param session
	 * @param notice
	 * @return
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView _i_indexUI(HttpSession session, Notice notice) {

		ModelAndView result = new ModelAndView("i/default/1.0.2/index");

		List<Notice> list_notice = noticeService.findByNotice(notice, 1, 6);
		result.addObject("data_list_notice", list_notice);

		// 买盘匹配
		User buy_record = userService.buy_record__list__4(session.getAttribute(
				"session.user.id").toString());
		result.addObject("data_buy_record", buy_record.getFarms());

		// 卖盘匹配
		User sell_record = userService.sell_record__list__4(session
				.getAttribute("session.user.id").toString());
		result.addObject("data_sell_record", sell_record.getSells());

		// 冻结列表
		// List<Prize> list_prize_freeze = prizeService.findByUserId(session
		// .getAttribute("session.user.id").toString());
		//
		// double d = 0;
		//
		// for (int i = 0; i < list_prize_freeze.size(); i++) {
		// Prize item = list_prize_freeze.get(i);
		//
		// if (item.getFlag() == 0) {
		// d += item.getMoney();
		// }
		// }

		result.addObject(
				"data_num_freeze",
				userService.getFreezeByUserId__4(session.getAttribute(
						"session.user.id").toString()));

		result.addObject("data_user", sell_record);
		result.addObject("nav_choose", ",02,");
		return result;
	}

	/***** ***** ***** ***** ***** 后台 ***** ***** ***** ***** *****/

	@RequestMapping(value = { "/manage/" }, method = RequestMethod.GET)
	public ModelAndView _manage_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("m/default/index");
		result.addObject("session_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",10,");
		return result;
	}

	@RequestMapping(value = { "/manage/matching_1" }, method = RequestMethod.GET)
	public ModelAndView _manage_matching_1UI(HttpSession session) {
		ModelAndView result = new ModelAndView("m/default/matching_1");
		result.addObject("session_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",1101,");
		return result;
	}

	@RequestMapping(value = { "/manage/matching_2" }, method = RequestMethod.GET)
	public ModelAndView _manage_matching_2UI(HttpSession session) {
		ModelAndView result = new ModelAndView("m/default/matching_2");
		result.addObject("session_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",1102,");
		return result;
	}

	@RequestMapping(value = { "/manage/matching_3" }, method = RequestMethod.GET)
	public ModelAndView _manage_matching_3UI(HttpSession session) {
		ModelAndView result = new ModelAndView("m/default/matching_3");
		result.addObject("session_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",1103,");
		return result;
	}

	@RequestMapping(value = { "/manage/matching_4" }, method = RequestMethod.GET)
	public ModelAndView _manage_matching_4UI(HttpSession session) {
		ModelAndView result = new ModelAndView("m/default/matching_4");
		result.addObject("session_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",1104,");
		return result;
	}

}