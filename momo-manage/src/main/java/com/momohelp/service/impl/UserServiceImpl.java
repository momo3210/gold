package com.momohelp.service.impl;

import java.util.List;

import net.foreworld.util.StringUtil;
import net.foreworld.util.encryptUtil.MD5;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.mapper.UserMapper;
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
	public User findByName(String user_name) {
		return ((UserMapper) getMapper()).findByName(user_name);
	}

	@Override
	public List<User> findByUser(User user, int page, int rows) {
		Example example = new Example(User.class);
		example.setOrderByClause("create_time desc");
		example.selectProperties("id", "user_name", "email", "create_time",
				"status", "real_name", "alipay_account");
		// TODO
		if (null != user) {
			Example.Criteria criteria = example.createCriteria();
			// TODO
			String nickname = StringUtil.isEmpty(user.getNickname());
			if (null != nickname) {
				criteria.andLike("nickname", "%" + nickname + "%");
			}
		}
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

}
