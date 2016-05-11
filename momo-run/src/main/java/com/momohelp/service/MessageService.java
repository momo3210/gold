package com.momohelp.service;

import java.util.List;

import com.momohelp.model.Message;

/**
 *
 * @author Administrator
 *
 */
public interface MessageService extends IService<Message> {

	List<Message> findByMessage(Message message, int page, int rows);
}
