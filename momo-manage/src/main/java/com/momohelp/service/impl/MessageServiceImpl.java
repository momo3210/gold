package com.momohelp.service.impl;

import org.springframework.stereotype.Service;

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

}