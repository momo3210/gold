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

	@RequestMapping(value = { "/t/{user_id}" }, method = RequestMethod.GET)
	public ModelAndView _i_tUI(HttpSession session, @PathVariable String user_id) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/t");
		result.addObject("user_id", user_id);
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