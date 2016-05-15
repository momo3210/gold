package com.momohelp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.model.Notice;
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
	public int save(Notice entity) {
		entity.setCreate_time(new Date());
		return super.save(entity);
	}

	@Override
	public int updateNotNull(Notice entity) {
		entity.setUser_id(null);
		entity.setCreate_time(null);
		return super.updateNotNull(entity);
	}

	@Override
	public List<Notice> findByNotice(Notice notice, int page, int rows) {
		Example example = new Example(Notice.class);
		example.setOrderByClause("create_time desc");
		// TODO
		PageHelper.startPage(page, rows);
		return selectByExample(example);
	}
}
