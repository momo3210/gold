package com.momohelp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Administrator
 *
 */
@Controller
public class DefaultController {

	/**
	 * 首页
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView _i_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/index");
		return result;
	}

	/**
	 * 买入陌陌
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/buyMo" }, method = RequestMethod.GET)
	public ModelAndView _i_buyMoUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/buyMo");
		return result;
	}

	/**
	 * 卖出陌陌
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/sellMo" }, method = RequestMethod.GET)
	public ModelAndView _i_sellMoUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/sellMo");
		return result;
	}

	/**
	 * 买入记录
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/buyRecord" }, method = RequestMethod.GET)
	public ModelAndView _i_buyRecordUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/buyRecord");
		return result;
	}

	/**
	 * 卖出记录
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/sellRecord" }, method = RequestMethod.GET)
	public ModelAndView _i_sellRecordUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/sellRecord");
		return result;
	}

	/**
	 * 购买门票
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/buyTicket" }, method = RequestMethod.GET)
	public ModelAndView _i_buyTicketUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/buyTicket");
		return result;
	}

	/**
	 * 门票转账
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/ticketTransfer" }, method = RequestMethod.GET)
	public ModelAndView _i_ticketTransferUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/ticketTransfer");
		return result;
	}

	/**
	 * 佣金清单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/commission" }, method = RequestMethod.GET)
	public ModelAndView _i_commissionUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/commission");
		return result;
	}

	/**
	 * 静态对账单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/staticRecord" }, method = RequestMethod.GET)
	public ModelAndView _i_staticRecordUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/staticRecord");
		return result;
	}

	/**
	 * 动态对账单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/dynamicRecord" }, method = RequestMethod.GET)
	public ModelAndView _i_dynamicRecordUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/dynamicRecord");
		return result;
	}

	/**
	 * 门票对账单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/ticketRecord" }, method = RequestMethod.GET)
	public ModelAndView _i_ticketRecordUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/ticketRecord");
		return result;
	}

	/**
	 * 网站公告
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/notice" }, method = RequestMethod.GET)
	public ModelAndView _i_noticeUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/default/1.0.2/notice");
		return result;
	}

	/**** 后台 ****/

	@RequestMapping(value = { "/manage/" }, method = RequestMethod.GET)
	public ModelAndView _manage_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("manage/default/1.0.2/index");
		return result;
	}
}