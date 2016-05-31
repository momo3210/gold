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

import com.momohelp.model.Buy;
import com.momohelp.model.BuySell;
import com.momohelp.model.Notice;
import com.momohelp.model.Sell;
import com.momohelp.model.User;
import com.momohelp.service.BuySellService;
import com.momohelp.service.BuyService;
import com.momohelp.service.NoticeService;
import com.momohelp.service.PrizeService;
import com.momohelp.service.SellService;
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

	@Autowired
	private SellService sellService;

	@Autowired
	private BuyService buyService;

	@Autowired
	private BuySellService buySellService;

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
	public ModelAndView _manage_matching_1UI(HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			Sell sell) {
		ModelAndView result = new ModelAndView("m/default/matching_1");
		result.addObject("session_user", session.getAttribute("session.user"));

		List<Sell> list = sellService.findBySell__4(sell, page, rows);
		result.addObject("data_list", list);

		result.addObject("search_user_id", sell.getUser_id());

		result.addObject("page_prev", page - 1);
		result.addObject("page_current", page);
		result.addObject("page_next", page + 1);

		result.addObject("nav_choose", ",11,1101,");
		return result;
	}

	@RequestMapping(value = { "/manage/matching_2" }, method = RequestMethod.GET)
	public ModelAndView _manage_matching_2UI(HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			Buy buy) {
		ModelAndView result = new ModelAndView("m/default/matching_2");
		result.addObject("session_user", session.getAttribute("session.user"));

		List<Buy> list = buyService.findByBuy__4(buy, page, rows);
		result.addObject("data_list", list);

		result.addObject("search_user_id", buy.getUser_id());

		result.addObject("page_prev", page - 1);
		result.addObject("page_current", page);
		result.addObject("page_next", page + 1);

		result.addObject("nav_choose", ",11,1102,");
		return result;
	}

	@RequestMapping(value = { "/manage/matching_3" }, method = RequestMethod.GET)
	public ModelAndView _manage_matching_3UI(HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			BuySell buySell) {
		ModelAndView result = new ModelAndView("m/default/matching_3");
		result.addObject("session_user", session.getAttribute("session.user"));

		List<BuySell> list = buySellService.findByBuySell__4(buySell, page,
				rows);
		result.addObject("data_list", list);

		result.addObject("search_buy_user_id", buySell.getP_buy_user_id());
		result.addObject("search_sell_user_id", buySell.getP_sell_user_id());

		result.addObject("page_prev", page - 1);
		result.addObject("page_current", page);
		result.addObject("page_next", page + 1);

		result.addObject("nav_choose", ",11,1103,");
		return result;
	}

	@RequestMapping(value = { "/manage/matching_4" }, method = RequestMethod.GET)
	public ModelAndView _manage_matching_4UI(HttpSession session) {
		ModelAndView result = new ModelAndView("m/default/matching_4");
		result.addObject("session_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",11,1104,");
		return result;
	}

}