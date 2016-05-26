package com.momohelp.controller;

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
import com.momohelp.model.Prize;
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
			// TODO
		}

		session.setAttribute("verify.sms", genSMS());

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

		result.addObject("data_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",02,");
		return result;
	}

	/***** ***** ***** ***** ***** 后台 ***** ***** ***** ***** *****/

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