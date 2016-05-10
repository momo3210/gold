package com.momohelp.service.impl;

import java.util.List;

import net.foreworld.util.StringUtil;
import net.foreworld.util.encryptUtil.MD5;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

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
		if (null != user) {
			return user;
		}

		user = getByMobile(name);
		if (null != user) {
			return user;
		}

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
	public int resetPwdByKey(String key) {
		User user = new User();
		user.setId(key);
		user.setUser_pass(DEFAULT_USER_PASS);
		// TODO
		return super.updateNotNull(user);
	}

	@Override
	public String[] saveNew(User user) {
		User _user = null;

		_user = getByMobile(user.getMobile());
		if (null != _user) {
			return new String[] { "手机号码已存在" };
		} // IF

		_user = getByEmail(user.getEmail());
		if (null != _user) {
			return new String[] { "电子邮箱已存在" };
		} // IF

		User newUser = new User();
		newUser.setNickname(user.getNickname());
		newUser.setReal_name(user.getReal_name());
		newUser.setMobile(user.getMobile());
		newUser.setEmail(user.getEmail());
		newUser.setUser_pass(MD5.encode(user.getUser_pass()));
		newUser.setUser_pass_safe(MD5.encode(user.getUser_pass_safe()));

		save(newUser);

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
	public String[] changePwd(String key, String old_pass, String new_pass) {
		// TODO
		User user = selectByKey(key);
		if (null == user) {
			return new String[] { "用户不存在" };
		}

		// TODO
		old_pass = StringUtil.isEmpty(old_pass);

		// TODO
		if (!MD5.encode(old_pass).equals(user.getUser_pass())) {
			return new String[] { "原始密码错误" };
		}

		User _user = new User();
		_user.setId(key);
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
	public String[] changePwdSafe(String key, String old_pass, String new_pass) {
		// TODO
		User user = selectByKey(key);
		if (null == user) {
			return new String[] { "用户不存在" };
		}

		// TODO
		old_pass = StringUtil.isEmpty(old_pass);

		// TODO
		if (!MD5.encode(old_pass).equals(user.getUser_pass())) {
			return new String[] { "原始密码错误" };
		}

		User _user = new User();
		_user.setId(key);
		_user.setUser_pass_safe(MD5.encode(new_pass));
		super.updateNotNull(_user);

		return null;
	}

	@Override
	public String[] editInfo(User user) {
		String[] checkSafe = checkSafe(user.getId(), user.getUser_pass_safe());
		if (null != checkSafe) {
			return checkSafe;
		}

		User newUser = new User();
		newUser.setNickname(user.getNickname());
		newUser.setAlipay_account(user.getAlipay_account());
		newUser.setWx_account(user.getWx_account());
		newUser.setBank(user.getBank());
		newUser.setBank_account(user.getBank_account());
		newUser.setBank_name(user.getBank_name());

		super.updateNotNull(newUser);

		return null;
	}

	private String[] checkSafe(String key, String pass_safe) {
		return new String[] { "安全密码错误" };
	}

	/**
	 * 生成主键
	 *
	 * @return
	 */
	private String genId() {
		return "08351506";
	}
}
