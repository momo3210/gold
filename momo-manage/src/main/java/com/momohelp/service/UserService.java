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

	int resetPwdByKey(String key);

	String[] saveNew(User user);

	String[] changePwd(String user_id, String old_pass, String new_pass);

}
