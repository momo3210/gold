package com.momohelp.controller;

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

import com.momohelp.model.Message;
import com.momohelp.model.User;
import com.momohelp.service.MessageService;
import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Controller
public class MessageController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	/**
	 * 在线工单
	 *
	 * @param session
	 * @param page
	 * @param rows
	 * @param message
	 * @return
	 */
	@RequestMapping(value = { "/message/" }, method = RequestMethod.GET)
	public ModelAndView _i_indexUI(HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "100") int rows,
			Message message) {

		ModelAndView result = new ModelAndView("i/message/1.0.2/index");

		message.setUser_id(session.getAttribute("session.user.id").toString());

		List<Message> list = messageService.findByMessage(message, page,
				Integer.MAX_VALUE);
		result.addObject("data_list", list);
		result.addObject("data_user", session.getAttribute("session.user"));

		result.addObject("nav_choose", ",07,0702,");
		return result;
	}

	@RequestMapping(value = { "/message/add" }, method = RequestMethod.GET)
	public String _i_profile22UI(HttpSession session, Map<String, Object> map) {

		User user = userService.selectByKey(session.getAttribute(
				"session.user.id").toString());
		map.put("data_user", user);

		map.put("nav_choose", ",07,0702,");
		return "i/message/1.0.2/add";
	}

	@ResponseBody
	@RequestMapping(value = { "/message/add" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_profile11(HttpSession session, Message message) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		message.setUser_id(session.getAttribute("session.user.id").toString());
		messageService.save(message);

		result.put("success", true);
		return result;
	}

	// /**
	// * 发起留言
	// *
	// * @param session
	// * @param message
	// * @return
	// */
	// @ResponseBody
	// @RequestMapping(value = { "/message/add" }, method = RequestMethod.POST,
	// produces = "application/json")
	// public Map<String, Object> _i_add(HttpSession session, Message message) {
	//
	// Map<String, Object> result = new HashMap<String, Object>();
	// result.put("success", false);
	// return result;
	// }

	/**** 后台 ****/

	/**
	 * 在线工单
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/manage/message/" }, method = RequestMethod.GET)
	public ModelAndView _manage_indexUI(HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "100") int rows,
			Message message) {

		ModelAndView result = new ModelAndView("m/message/index");

		List<Message> list = messageService.findByMessage(message, page,
				Integer.MAX_VALUE);
		result.addObject("data_list", list);
		result.addObject("data_user", session.getAttribute("session.user"));

		result.addObject("nav_choose", ",09,0902,");
		return result;
	}

	@RequestMapping(value = { "/manage/message/edit" }, method = RequestMethod.GET)
	public String _i_profile2233UI(HttpSession session,
			Map<String, Object> map, Message message) {

		User user = userService.selectByKey(session.getAttribute(
				"session.user.id").toString());
		map.put("data_user", user);

		Message _message = messageService.selectByKey(message.getId());
		map.put("data_message", _message);

		map.put("nav_choose", ",09,0902,");
		return "m/message/edit";
	}

	@ResponseBody
	@RequestMapping(value = { "/manage/message/edit" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _i_profile1144(HttpSession session,
			Message message) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);

		message.setReply_time(new Date());

		message.setReply_user_id(session.getAttribute("session.user.id")
				.toString());
		messageService.updateNotNull(message);

		result.put("success", true);
		return result;
	}

	// /**
	// * 回复留言
	// *
	// * @param message
	// * @param session
	// * @return
	// */
	// @ResponseBody
	// @RequestMapping(value = { "/manage/message/edit" }, method =
	// RequestMethod.POST, produces = "application/json")
	// public Map<String, Object> _manage_edit(Message message, HttpSession
	// session) {
	// Map<String, Object> result = new HashMap<String, Object>();
	// result.put("success", false);
	// // TODO
	// return result;
	// }
	//
	// /**
	// * 删除留言
	// *
	// * @param id
	// * @return
	// */
	// @ResponseBody
	// @RequestMapping(value = { "/manage/message/remove" }, method =
	// RequestMethod.POST, produces = "application/json")
	// public Map<String, Object> _manage_remove(
	// @RequestParam(required = true) String id) {
	// Map<String, Object> result = new HashMap<String, Object>();
	// result.put("success", false);
	// // TODO
	// return result;
	// }
}