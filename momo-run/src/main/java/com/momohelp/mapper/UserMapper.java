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
	// 获取当前用户下 对应的直接一代 各个等级人员数量
	/***
	 * 
	 * @param key
	 *            当前用户id
	 * @return
	 */
	Map<String, Object> countMemberNOAndlevel(String key);
}