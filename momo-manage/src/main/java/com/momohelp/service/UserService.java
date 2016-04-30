package com.momohelp.service;

import java.util.List;

import com.momohelp.model.User;

/**
 *
 * @author Administrator
 *
 */
public interface UserService extends IService<User> {

	User findByName(String user_name);

	List<User> findByUser(User user, int page, int rows);

	int resetPwdByKeys(String keys);

	String[] saveNew(User user);

	String[] changePwd(String user_id, String old_pass, String new_pass);

	User findByApiKey(String apikey);

	User findBySecKey(String seckey);

	List<User> findByInviteUserId(String invite_user_id);
}
