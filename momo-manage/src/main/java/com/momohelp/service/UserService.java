package com.momohelp.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.momohelp.model.User;

/**
 *
 * @author Administrator
 *
 */
public interface UserService extends IService<User> {

	/**
	 * 买入操作时使用
	 *
	 * @param id
	 * @return
	 */
	User buyTime___4(String id);

	/*****************/

	/**
	 * 买入时使用
	 *
	 * @param id
	 * @return
	 */
	User getBuyTimeById__1(String id);

	/**
	 * 获取用户所有的未完全交易的买盘（展示时使用）
	 *
	 * @param id
	 * @return
	 */
	User getUnDealBuyById__1(String id);

	/**
	 * 获取用户所有的未完全交易的卖盘（展示时使用）
	 *
	 * @param id
	 * @return
	 */
	User getUnDealSellById__1(String id);

	/**
	 * 获取我和我的父级
	 *
	 * @param id
	 * @return
	 */
	User getMeAndParent__1(String id);

	/**** -------------- */

	/****************************/

	User getAllById(String id);

	User getId(int flag, String id);

	String[] sendSms(String id);

	/**
	 * 普通用户登陆
	 *
	 * @param user_name
	 * @param user_pass
	 * @return
	 */
	Map<String, Object> login(String user_name, String user_pass);

	/**
	 * 查找用户的直推（下一级）
	 *
	 * @param user_id
	 * @param page
	 * @param rows
	 * @return
	 */
	List<User> findChildren(String user_id, int page, int rows);

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
	@Transactional
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