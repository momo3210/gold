package com.momohelp.mapper;

import java.util.Map;

import com.momohelp.model.User;
import com.momohelp.util.MyMapper;

/**
 *
 * @author Administrator
 *
 */
public interface UserMapper extends MyMapper<User> {
	// ��ȡ��ǰ�û��� ��Ӧ��ֱ��һ�� �����ȼ���Ա����
	/***
	 * 
	 * @param key
	 *            ��ǰ�û�id
	 * @return
	 */
	Map<String, Object> countMemberNOAndlevel(String key);
}