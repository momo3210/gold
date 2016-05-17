package com.momohelp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.mapper.UserMapper;
import com.momohelp.model.ModelLV;
import com.momohelp.model.User;
import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Service("userService")
public class UserServiceImpl extends BaseService<User> implements UserService {


	@Override
	public User getByName(String name) {
		User user = null;

		
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
		
		Example.Criteria criteria = example.createCriteria();
		
		String _email = email;
		if (null != _email)
			criteria.andEqualTo("email", _email);

		List<User> list = selectByExample(example);

		
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
		
		Example.Criteria criteria = example.createCriteria();
		
		String _mobile = mobile;
		if (null != _mobile)
			criteria.andEqualTo("mobile", _mobile);

		List<User> list = selectByExample(example);

		
		return (null == list || 0 == list.size()) ? null : list.get(0);
	}

	@Override
	public int resetPwdByKey(String key) {
		User user = new User();
		user.setId(key);
		
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
		save(newUser);

		
		return null;
	}

	@Override
	public String[] changePwd(String key, String old_pass, String new_pass) {
		
		User user = selectByKey(key);
		if (null == user) {
			return new String[] { "没有找到此用户" };
		}


		if (old_pass.equals(user.getUser_pass())) {
			return new String[] { "原始密码错误" };
		}

		User _user = new User();
		_user.setId(key);
		_user.setUser_pass(new_pass);
		super.updateNotNull(_user);

		return null;
	}

	@Override
	public int resetPwdSafeByKey(String key) {
		User user = new User();
		user.setId(key);
		return super.updateNotNull(user);
	}

	@Override
	public String[] changePwdSafe(String key, String old_pass, String new_pass) {
		
		User user = selectByKey(key);
		if (null == user) {
			return new String[] { "没有找到此用户" };
		}
		if (!old_pass.equals(user.getUser_pass())) {
			return new String[] { "原始密码错误" };
		}
		User _user = new User();
		_user.setId(key);
		_user.setUser_pass_safe(new_pass);
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
		return new String[] { "安全密码输入错误" };
	}


	@Override
	public List<User> findByUser(User user, int page, int rows) {
		Example example = new Example(User.class);
		example.setOrderByClause("create_time desc");
		
		if (null != user) {
			Example.Criteria criteria = example.createCriteria();

			
			String apikey = user.getApikey();
			if (null != apikey) {
				criteria.andEqualTo("apikey", apikey);
			}

			
			String seckey = user.getSeckey();
			if (null != seckey) {
				criteria.andEqualTo("seckey", seckey);
			}

			
			String pid = user.getPid();
			if (null != pid) {
				criteria.andEqualTo("pid", pid);
			}

		}
		PageHelper.startPage(page, rows);
		return selectByExample(example);
	}

	@Override
	public List<ModelLV> countMemberNOAndlevel(String key) {
		return ((UserMapper) mapper).countMemberNOAndlevel(key);
	}

	@Override
	public Integer countLvNO(String pid, String poorPeasant) {
		
		return ((UserMapper) mapper).countLvNO(pid, poorPeasant);
	}
}
