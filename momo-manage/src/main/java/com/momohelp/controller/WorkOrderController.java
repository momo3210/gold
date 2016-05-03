package com.momohelp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.momohelp.service.WorkOrderService;

/**
 *
 * @author Administrator
 *
 */
@Controller
public class WorkOrderController {

	@Autowired
	private WorkOrderService workOrderService;

	/**
	 * 在线工单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/workOrder/" }, method = RequestMethod.GET)
	public ModelAndView _i_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/workOrder/1.0.2/index");
		return result;
	}

	/**** 后台 ****/
}