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

import com.momohelp.model.Notice;
import com.momohelp.service.NoticeService;

/**
 * 网站公告
 *
 * @author Administrator
 *
 */
@Controller
public class NoticeController {

	@Autowired
	private NoticeService noticeService;

	/**
	 * 网站公告
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/notice/" }, method = RequestMethod.GET)
	public ModelAndView _i_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("i/notice/1.0.2/index");
		return result;
	}

	/**
	 * 公告信息
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/notice/info" }, method = RequestMethod.GET)
	public ModelAndView _i_infoUI(@RequestParam(required = true) String id) {
		ModelAndView result = new ModelAndView("i/notice/1.0.2/info");
		return result;
	}

	/**** 后台 ****/

	/**
	 * 网站公告（列表）
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/manage/notice/" }, method = RequestMethod.GET)
	public ModelAndView _manage_indexUI(HttpSession session) {
		ModelAndView result = new ModelAndView("manage/notice/1.0.2/index");
		return result;
	}

	/**
	 * 新建公告
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/manage/notice/add" }, method = RequestMethod.GET)
	public ModelAndView _manage_addUI(HttpSession session) {
		ModelAndView result = new ModelAndView("manage/notice/1.0.2/add");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/manage/notice/add" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_add(Notice notice, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// TODO
		return result;
	}

	/**
	 * 修改公告
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/manage/notice/edit" }, method = RequestMethod.GET)
	public ModelAndView _manage_editUI(@RequestParam(required = true) String id) {
		ModelAndView result = new ModelAndView("manage/notice/1.0.2/edit");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = { "/manage/notice/edit" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_edit(Notice notice, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// TODO
		return result;
	}

	/**
	 * 删除公告
	 *
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/manage/notice/remove" }, method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> _manage_remove(
			@RequestParam(required = true) String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// TODO
		return result;
	}
}