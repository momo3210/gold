package com.momohelp.service.impl;

import java.util.Date;
import java.util.HashMap;
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

	@Override
	public int save(User entity) {
		entity.setCreate_time(new Date());
		return super.save(entity);
	}

	@Override
	public int updateNotNull(User entity) {
		entity.setCreate_time(null);
		return super.updateNotNull(entity);
	}

	/**
	 * 获取用户对象
	 *
	 * @param user_name
	 *            邮箱、手机号
	 * @return
	 */
	private User getByName(String user_name) {
		User user = null;

		user = new User();
		user.setEmail(user_name);
		user = findByUser(user);
		if (null != user) {
			return user;
		}

		user = new User();
		user.setMobile(user_name);
		user = findByUser(user);
		if (null != user) {
			return user;
		}

		return null;
	}

	/**
	 * 普通用户登陆
	 *
	 * 1、先是一堆验证
	 *
	 * 2、最后返回用户对象
	 */
	@Override
	public Map<String, Object> login(String user_name, String user_pass) {

		Map<String, Object> result = new HashMap<String, Object>();

		User user = getByName(user_name);

		if (null == user) {
			result.put("msg", new String[] { "用户名或密码输入错误" });
			return result;
		}

		if (!MD5.encode(user_pass).equals(user.getUser_pass())) {
			result.put("msg", new String[] { "用户名或密码输入错误" });
			return result;
		}

		if (0 == user.getStatus()) {
			result.put("msg", new String[] { "您已被限制登陆，请联系管理员" });
			return result;
		} // IF

		result.put("data", user);
		return result;
	}

	@Override
	public User findByUser(User user) {
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
		return (null == list || 1 != list.size()) ? null : list.get(0);
	}

	@Override
	public int resetPwdByKey(String key) {
		User user = new User();
		user.setId(key);
		user.setUser_pass(DEFAULT_USER_PASS);
		// TODO
		return updateNotNull(user);
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
		updateNotNull(_user);

		return null;
	}

	@Override
	public int resetPwdSafeByKey(String key) {
		User user = new User();
		user.setId(key);
		user.setUser_pass_safe(DEFAULT_USER_PASS);
		// TODO
		return updateNotNull(user);
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

		updateNotNull(_user);
		return null;
	}

	@Override
	public String[] editInfo(User user) {
		User newUser = new User();
		newUser.setNickname(user.getNickname());
		newUser.setAlipay_account(user.getAlipay_account());
		newUser.setWx_account(user.getWx_account());
		newUser.setBank(user.getBank());
		newUser.setBank_account(user.getBank_account());
		newUser.setBank_name(user.getBank_name());

		newUser.setId(user.getId());

		updateNotNull(newUser);
		return null;
	}

	/**
	 * 生成主键
	 *
	 * @return
	 */
	private String genId() {
		String id = null;
		User user = null;
		do {
			// 算法
			int i = (int) ((Math.random() * 9 + 1) * 10000000);
			id = String.valueOf(i);
			// END
			user = selectByKey(id);
		} while (null != user);
		return id;
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

}
