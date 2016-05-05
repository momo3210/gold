package com.momohelp.service;

import java.util.List;

import com.momohelp.model.User;

/**
 *
 * @author Administrator
 *
 */
public interface UserService extends IService<User> {

	User findByName(String name);

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

	String[] saveNew(User user);

	/**
	 * 登陆密码修改
	 *
	 * @param user_id
	 * @param old_pass
	 * @param new_pass
	 * @return
	 */
	String[] changePwd(String user_id, String old_pass, String new_pass);

	/**
	 * 安全密码修改
	 *
	 * @param user_id
	 * @param old_pass
	 * @param new_pass
	 * @return
	 */
	String[] changePwdSafe(String user_id, String old_pass, String new_pass);
}