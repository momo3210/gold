package com.momohelp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

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
	public List<Notice> findByNotice(Notice notice, int page, int rows) {
		// TODO Auto-generated method stub
		return null;
	}

}
