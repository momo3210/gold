package com.momohelp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.model.Message;
import com.momohelp.service.MessageService;

/**
 *
 * @author Administrator
 *
 */
@Service("messageService")
public class MessageServiceImpl extends BaseService<Message> implements
		MessageService {

	@Override
	public List<Message> findByMessage(Message message, int page, int rows) {
		Example example = new Example(Message.class);
		example.setOrderByClause("create_time desc");
		// TODO
		PageHelper.startPage(page, rows);
		return selectByExample(example);
	}

}