package com.momohelp.service;

import java.util.List;
import java.util.Map;

import com.momohelp.model.User;

/**
 *
 * @author Administrator
 *
 */
public interface UserService extends IService<User> {

	/**
	 * 普通用户登陆
	 *
	 * @param user_name
	 * @param user_pass
	 * @return
	 */
	Map<String, Object> login(String user_name, String user_pass);

	/**
	 * 单个
	 *
	 * @param user
	 * @return
	 */
	User findByUser(User user);

	/**
	 * 多个
	 *
	 * @param user
	 * @param page
	 * @param rows
	 * @return
	 */
	List<User> findByUser(User user, int page, int rows);

	/**
	 * 登陆密码重置
	 *
	 * @param key
	 * @return
	 */
	int resetPwdByKey(String key);

	/**
	 * 安全密码重置
	 *
	 * @param key
	 * @return
	 */
	int resetPwdSafeByKey(String key);

	/**
	 * 创建新用户
	 *
	 * @param user
	 * @return
	 */
	String[] saveNew(User user);

	/**
	 * 修改资料
	 *
	 * @param user
	 * @return
	 */
	String[] editInfo(User user);

	/**
	 * 登陆密码修改
	 *
	 * @param key
	 * @param old_pass
	 * @param new_pass
	 * @return
	 */
	String[] changePwd(String key, String old_pass, String new_pass);

	/**
	 * 安全密码修改
	 *
	 * @param key
	 * @param old_pass
	 * @param new_pass
	 * @return
	 */
	String[] changePwdSafe(String key, String old_pass, String new_pass);

}