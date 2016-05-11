package com.momohelp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.model.Notice;
import com.momohelp.model.User;
import com.momohelp.service.NoticeService;

/**
 *
 * @author Administrator
 *
 */
@Service("noticeService")
public class NoticeServiceImpl extends BaseService<Notice> implements
		NoticeService {

	@Override
	public List<Notice> findByNotice(Notice notice, int page, int rows) {
		Example example = new Example(User.class);
		example.setOrderByClause("create_time desc");
		// TODO
		PageHelper.startPage(page, rows);
		return selectByExample(example);
	}
}
