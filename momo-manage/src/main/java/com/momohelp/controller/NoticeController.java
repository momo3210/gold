package com.momohelp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.momohelp.service.NoticeService;

/**
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

	/**** 后台 ****/
}