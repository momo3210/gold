package com.momohelp.mapper;

import java.util.List;
import java.util.Map;

import com.momohelp.model.Commission;
import com.momohelp.model.SysCount;
import com.momohelp.model.User;
import com.momohelp.model.UserCount;
import com.momohelp.model.UserRecommend;
import com.momohelp.util.MyMapper;

/**
 *
 * @author Administrator
 *
 */
public interface UserMapper extends MyMapper<User> {

	/**
	 * 获取用户推荐清单
	 *
	 * @param map
	 * @return
	 */
	List<UserRecommend> findRecommend(Map<String, Object> map);

	/**
	 * 佣金清单
	 *
	 * @param user_id
	 * @return
	 */
	List<Commission> findCommission(Map<String, Object> map);

	/**
	 * 统计直推、团队等数量
	 *
	 * @param map
	 * @return
	 */
	UserCount findUserCount(Map<String, Object> map);

	SysCount findSysCount(Map<String, Object> map);
}