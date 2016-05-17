package com.momohelp.mapper;

import java.util.List;

import com.momohelp.model.ModelLV;
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
	List<ModelLV> countMemberNOAndlevel(String key);

	// 查询当前用户下 贫农已经排单而且首次排单 ，并且打款成功的人数
	Integer countLvNO(String pid, String poorPeasant);
}