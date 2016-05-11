package com.momohelp.service;

import java.util.List;

import com.momohelp.model.Notice;

/**
 *
 * @author Administrator
 *
 */
public interface NoticeService extends IService<Notice> {

	List<Notice> findByNotice(Notice notice, int page, int rows);
}
