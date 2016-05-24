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

import com.momohelp.model.Buy;
import com.momohelp.model.Notice;
import com.momohelp.model.Prize;
import com.momohelp.model.Sell;
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

		// 买盘
		List<Buy> list_buy = buyService.findUnFinishDeal(session.getAttribute(
				"session.user.id").toString());
		result.addObject("data_list_buy", list_buy);

		// 卖盘
//		List<Sell> list_sell = sellService.findUnFinishDeal(session
//				.getAttribute("session.user.id").toString());
//		result.addObject("data_list_sell", list_sell);

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
		ModelAndView result = new ModelAndView("manage/default/1.0.2/index");
		return result;
	}

	@RequestMapping(value = { "/user/" }, method = RequestMethod.GET)
	public ModelAndView _manage_userUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/user/1.0.1/user");
		result.addObject("data_user", session.getAttribute("session.user"));
		result.addObject("nav_choose", ",08,0801,");

		List<User> list = userService.selectByExample(null);
		result.addObject("data_list", list);

		return result;
	}

	@RequestMapping(value = { "/user/edit" }, method = RequestMethod.GET)
	public String _i_editUI(Map<String, Object> map, HttpSession session,
			@RequestParam(required = true) String id) {

		User user = userService.selectByKey(id);

		// TODO
		map.put("data_user", user);
		map.put("nav_choose", ",08,0801,");
		return "i/user/1.0.1/edit";
	}

	@ResponseBody
	@RequestMapping(value = { "/user/edit" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_edit(User user, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		// 设置主键

		userService.updateNotNull(user);

		// TODO
		result.put("success", true);
		return result;
	}

	@RequestMapping(value = { "/user/add" }, method = RequestMethod.GET)
	public String _i_addtUI(Map<String, Object> map, HttpSession session) {

		// TODO
		map.put("data_user", session.getAttribute("session.user"));
		map.put("nav_choose", ",08,0801,");
		return "i/user/1.0.1/add";
	}

}