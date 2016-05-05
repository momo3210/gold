package com.momohelp.service.impl;

import java.util.List;

import net.foreworld.util.StringUtil;
import net.foreworld.util.encryptUtil.MD5;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.model.User;
import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Service("userService")
public class UserServiceImpl extends BaseService<User> implements UserService {

	private static final String DEFAULT_USER_PASS = MD5.encode("123456");

	@Override
	public User getByName(String name) {
		User user = null;

		// TODO
		user = getByEmail(name);
		if (null != user)
			return user;

		user = getByMobile(name);
		if (null != user)
			return user;

		return null;
	}

	/**
	 * 查询用户
	 *
	 * @param email
	 * @return
	 */
	private User getByEmail(String email) {
		Example example = new Example(User.class);
		// TODO
		Example.Criteria criteria = example.createCriteria();
		// TODO
		String _email = StringUtil.isEmpty(email);
		if (null != _email)
			criteria.andEqualTo("email", _email);

		List<User> list = selectByExample(example);

		// TODO
		return (null == list || 0 == list.size()) ? null : list.get(0);
	}

	/**
	 * 查询用户
	 *
	 * @param mobile
	 * @return
	 */
	private User getByMobile(String mobile) {
		Example example = new Example(User.class);
		// TODO
		Example.Criteria criteria = example.createCriteria();
		// TODO
		String _mobile = StringUtil.isEmpty(mobile);
		if (null != _mobile)
			criteria.andEqualTo("mobile", _mobile);

		List<User> list = selectByExample(example);

		// TODO
		return (null == list || 0 == list.size()) ? null : list.get(0);
	}

	@Override
	public List<User> findByUser(User user, int page, int rows) {
		Example example = new Example(User.class);
		example.setOrderByClause("create_time desc");
		// TODO
		PageHelper.startPage(page, rows);
		return selectByExample(example);
	}

	@Override
	public int resetPwdByKey(String key) {
		User user = new User();
		user.setId(key);
		user.setUser_pass(DEFAULT_USER_PASS);
		// TODO
		return super.updateNotNull(user);
	}

	@Override
	public String[] saveNew(User user) {
		// TODO
		return null;
	}

	@Override
	public int updateNotNull(User user) {
		user.setUser_pass(null);
		user.setCreate_time(null);

		// TODO
		return super.updateNotNull(user);
	}

	@Override
	public String[] changePwd(String user_id, String old_pass, String new_pass) {
		old_pass = StringUtil.isEmpty(old_pass);

		// TODO
		User user = selectByKey(user_id);
		if (null == user)
			return new String[] { "用户不存在" };

		// TODO
		if (!MD5.encode(old_pass).equals(user.getUser_pass()))
			return new String[] { "原始密码错误" };

		User _user = new User();
		_user.setId(user_id);
		_user.setUser_pass(MD5.encode(new_pass));
		super.updateNotNull(_user);

		return null;
	}

	@Override
	public int resetPwdSafeByKey(String key) {
		User user = new User();
		user.setId(key);
		user.setUser_pass_safe(DEFAULT_USER_PASS);
		// TODO
		return super.updateNotNull(user);
	}

	@Override
	public String[] changePwdSafe(String user_id, String old_pass,
			String new_pass) {
		old_pass = StringUtil.isEmpty(old_pass);

		// TODO
		User user = selectByKey(user_id);
		if (null == user)
			return new String[] { "用户不存在" };

		// TODO
		if (!MD5.encode(old_pass).equals(user.getUser_pass_safe()))
			return new String[] { "原始密码错误" };

		User _user = new User();
		_user.setId(user_id);
		_user.setUser_pass_safe(MD5.encode(new_pass));
		super.updateNotNull(_user);

		return null;
	}

}
