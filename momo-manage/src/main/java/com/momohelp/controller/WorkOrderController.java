package com.momohelp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.momohelp.model.WorkOrder;
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

	/**
	 * 发起留言
	 *
	 * @param workOrder
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/workOrder/add" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_add(WorkOrder workOrder, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// TODO
		return result;
	}

	/**** 后台 ****/

	/**
	 * 在线工单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/manage/workOrder/" }, method = RequestMethod.GET)
	public ModelAndView _manage_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("manage/workOrder/1.0.2/index");
		return result;
	}

	/**
	 * 回复留言
	 *
	 * @param workOrder
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/manage/workOrder/edit" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_edit(WorkOrder workOrder,
			HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// TODO
		return result;
	}

	/**
	 * 删除留言
	 *
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/manage/workOrder/remove" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_remove(
			@RequestParam(required = true) String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// TODO
		return result;
	}
}