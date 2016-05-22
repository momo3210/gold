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

	User getId(int flag, String id);

	User getByUser(int flag, User user);

	List<User> findByUser(int flag, User user, int page, int rows);

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
	 * @param id
	 * @return
	 */
	int resetPwd(String id);

	/**
	 * 安全密码重置
	 *
	 * @param id
	 * @return
	 */
	int resetPwdSafe(String id);

	/**
	 * 创建新用户（普通用户使用）
	 *
	 * @param user
	 * @return
	 */
	String[] register(User user);

	/**
	 * 创建新用户（管理员使用）
	 *
	 * @param user
	 * @return
	 */
	String[] createUser(User user);

	/**
	 * 修改资料
	 *
	 * @param user
	 * @return
	 */
	String[] editInfo(User user);

	/**
	 * 管理员可以修改全部的信息
	 *
	 * @param user
	 * @return
	 */
	String[] editInfoAll(User user);

	/**
	 * 登陆密码修改
	 *
	 * @param id
	 * @param old_pass
	 * @param new_pass
	 * @return
	 */
	String[] changePwd(String id, String old_pass, String new_pass);

	/**
	 * 安全密码修改
	 *
	 * @param id
	 * @param old_pass
	 * @param new_pass
	 * @return
	 */
	String[] changePwdSafe(String id, String old_pass, String new_pass);

}