package com.momohelp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.momohelp.model.Buy;
import com.momohelp.model.Notice;
import com.momohelp.model.Prize;
import com.momohelp.model.Sell;
import com.momohelp.service.BuyService;
import com.momohelp.service.NoticeService;
import com.momohelp.service.PrizeService;
import com.momohelp.service.SellService;

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
		List<Sell> list_sell = sellService.findUnFinishDeal(session
				.getAttribute("session.user.id").toString());
		result.addObject("data_list_sell", list_sell);

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
}