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

import com.momohelp.model.Message;
import com.momohelp.service.MessageService;

/**
 *
 * @author Administrator
 *
 */
@Controller
public class MessageController {

	@Autowired
	private MessageService messageService;

	/**
	 * 在线工单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/message/" }, method = RequestMethod.GET)
	public ModelAndView _i_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/message/1.0.2/index");
		result.addObject("nav_choose", ",07,0702,");
		return result;
	}

	/**
	 * 发起留言
	 *
	 * @param message
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/message/add" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_add(Message message, HttpSession session) {
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
	@RequestMapping(value = { "/manage/message/" }, method = RequestMethod.GET)
	public ModelAndView _manage_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("manage/message/1.0.2/index");
		return result;
	}

	/**
	 * 回复留言
	 *
	 * @param message
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/manage/message/edit" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_edit(Message message, HttpSession session) {
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
	@RequestMapping(value = { "/manage/message/remove" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_remove(
			@RequestParam(required = true) String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// TODO
		return result;
	}
}