package com.momohelp.service;

import java.util.List;

import com.momohelp.model.ModelLV;
import com.momohelp.model.User;

/**
 *
 * @author Administrator
 *
 */
public interface UserService extends IService<User> {

	User getByName(String name);

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

	/**
	 * 获取当前用户下 对应的直接一代 各个等级人员数量
	 *
	 * @param key
	 *            当前用户id
	 *
	 * @return
	 */
	List<ModelLV> countMemberNOAndlevel(String key);
	// 查询当前用户下 贫农已经排单而且首次排单 ，并且打款成功的人数  
	Integer countLvNO(String pid, String poorPeasant);

}