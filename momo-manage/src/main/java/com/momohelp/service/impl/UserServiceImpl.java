package com.momohelp.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

	private User findByUser(User user) {
		Example example = new Example(User.class);
		// TODO
		if (null != user) {
			Example.Criteria criteria = example.createCriteria();

			// TODO
			String email = StringUtil.isEmpty(user.getEmail());
			if (null != email) {
				criteria.andEqualTo("email", email);
			}

			// TODO
			String mobile = StringUtil.isEmpty(user.getMobile());
			if (null != mobile) {
				criteria.andEqualTo("mobile", mobile);
			}

			// TODO
			String nickname = StringUtil.isEmpty(user.getNickname());
			if (null != nickname) {
				criteria.andEqualTo("nickname", nickname);
			}
		}

		// TODO
		List<User> list = selectByExample(example);
		return (null == list || 0 == list.size()) ? null : list.get(0);
	}

	@Override
	public User getByName(String name) {
		User user = null;

		user = new User();
		user.setEmail(name);
		user = findByUser(user);
		if (null != user) {
			return user;
		}

		user = new User();
		user.setMobile(name);
		user = findByUser(user);
		if (null != user) {
			return user;
		}

		return null;
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
		// 参数验证
		user.setNickname(StringUtil.isEmpty(user.getNickname()));
		if (null == user.getNickname()) {
			return new String[] { "昵称不能为空" };
		}

		user.setReal_name(StringUtil.isEmpty(user.getReal_name()));
		if (null == user.getReal_name()) {
			return new String[] { "姓名不能为空" };
		}

		user.setMobile(StringUtil.isEmpty(user.getMobile()));
		if (null == user.getMobile()) {
			return new String[] { "手机号码不能为空" };
		}

		user.setEmail(StringUtil.isEmpty(user.getEmail()));
		if (null == user.getEmail()) {
			return new String[] { "电子邮箱不能为空" };
		}

		user.setUser_pass(StringUtil.isEmpty(user.getUser_pass()));
		if (null == user.getUser_pass()) {
			return new String[] { "登陆密码不能为空" };
		}

		user.setUser_pass_safe(StringUtil.isEmpty(user.getUser_pass_safe()));
		if (null == user.getUser_pass_safe()) {
			return new String[] { "安全密码不能为空" };
		}

		/**********/

		User _user = null;

		_user = new User();
		_user.setNickname(user.getNickname());
		_user = findByUser(_user);
		if (null != _user) {
			return new String[] { "昵称已存在" };
		} // IF

		_user = new User();
		_user.setMobile(user.getMobile());
		_user = findByUser(_user);
		if (null != _user) {
			return new String[] { "手机号码已存在" };
		} // IF

		_user = new User();
		_user.setEmail(user.getEmail());
		_user = findByUser(_user);
		if (null != _user) {
			return new String[] { "电子邮箱已存在" };
		} // IF

		/**********/

		// 附加数据
		user.setUser_pass(MD5.encode(user.getUser_pass()));
		user.setUser_pass_safe(MD5.encode(user.getUser_pass_safe()));
		user.setCreate_time(new Date());

		user.setTotal_dynamic(0.00);
		user.setTotal_food(0);
		user.setTotal_static(0.00);
		user.setTotal_ticket(0);

		user.setNum_dynamic(0.00);
		user.setNum_food(0);
		user.setNum_static(0.00);
		user.setNum_ticket(0);

		// 贫农
		user.setLv("05");
		user.setStatus(1);
		user.setRole_id(2);

		user.setId(genId());
		save(user);

		// TODO
		return null;
	}

	@Override
	public String[] changePwd(String key, String old_pass, String new_pass) {
		// TODO
		if ("".equals(new_pass.trim())) {
			return new String[] { "新登陆密码不能为空" };
		}

		// TODO
		User user = selectByKey(key);
		if (null == user) {
			return new String[] { "没有找到此用户" };
		}

		// TODO
		if (!MD5.encode(old_pass).equals(user.getUser_pass())) {
			return new String[] { "原登陆密码错误" };
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
		if ("".equals(new_pass.trim())) {
			return new String[] { "新安全密码不能为空" };
		}

		// TODO
		User user = selectByKey(key);
		if (null == user) {
			return new String[] { "没有找到此用户" };
		}

		// TODO
		if (!MD5.encode(old_pass).equals(user.getUser_pass_safe())) {
			return new String[] { "原安全密码错误" };
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
		} // IF

		User newUser = new User();
		newUser.setNickname(user.getNickname());
		newUser.setAlipay_account(user.getAlipay_account());
		newUser.setWx_account(user.getWx_account());
		newUser.setBank(user.getBank());
		newUser.setBank_account(user.getBank_account());
		newUser.setBank_name(user.getBank_name());

		newUser.setId(user.getId());

		super.updateNotNull(newUser);

		return null;
	}

	private String[] checkSafe(String key, String pass_safe) {
		User user = selectByKey(key);

		if (MD5.encode(pass_safe).equals(user.getUser_pass_safe())) {
			return null;
		} // IF

		return new String[] { "安全密码输入错误" };
	}

	/**
	 * 生成主键
	 *
	 * @return
	 */
	private String genId() {
		return "08351506";
	}

	@Override
	public List<User> findByUser(User user, int page, int rows) {
		Example example = new Example(User.class);
		example.setOrderByClause("create_time desc");
		// TODO
		if (null != user) {
			Example.Criteria criteria = example.createCriteria();

			// TODO
			String apikey = StringUtil.isEmpty(user.getApikey());
			if (null != apikey) {
				criteria.andEqualTo("apikey", apikey);
			}

			// TODO
			String seckey = StringUtil.isEmpty(user.getSeckey());
			if (null != seckey) {
				criteria.andEqualTo("seckey", seckey);
			}

			// TODO
			String pid = StringUtil.isEmpty(user.getPid());
			if (null != pid) {
				criteria.andEqualTo("pid", pid);
			}

		}
		PageHelper.startPage(page, rows);
		return selectByExample(example);
	}

	@Override
	public Map<String, Object> countMemberNOAndlevel(String key) {
		return ((UserService) mapper).countMemberNOAndlevel(key);
	}
}
