package com.momohelp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.model.Farm;
import com.momohelp.model.User;
import com.momohelp.service.FarmService;
import com.momohelp.service.UserService;
import com.momohelp.util.StringUtil;
import com.momohelp.util.encryptUtil.MD5;

/**
 *
 * @author Administrator
 *
 */
@Service("userService")
public class UserServiceImpl extends BaseService<User> implements UserService {

	@Autowired
	private FarmService farmService;

	/**
	 * 保存之前的预判
	 *
	 * @param user
	 * @return
	 */
	private String[] save_prev(User user) {

		user.setMobile(StringUtil.isEmpty(user.getMobile()));
		if (null == user.getMobile()) {
			return new String[] { "手机号码不能为空" };
		} // if

		user.setEmail(StringUtil.isEmpty(user.getEmail()));
		if (null == user.getEmail()) {
			return new String[] { "电子邮箱不能为空" };
		} // if

		user.setNickname(StringUtil.isEmpty(user.getNickname()));
		if (null == user.getNickname()) {
			return new String[] { "昵称不能为空" };
		} // if

		/***** *****/

		// 获取父亲
		User p_user = selectByKey(user.getPid());
		if (null == p_user) {
			return new String[] { "您输入的会员编号不存在，请重新输入" };
		} // if

		/***** *****/

		User __user = null;

		__user = new User();
		__user.setMobile(user.getMobile());
		__user = getByUser_1(__user);
		if (null != __user) {
			return new String[] { "手机号码已存在" };
		} // if

		__user = new User();
		__user.setEmail(user.getEmail());
		__user = getByUser_1(__user);
		if (null != __user) {
			return new String[] { "电子邮箱已存在" };
		} // if

		__user = new User();
		__user.setNickname(user.getNickname());
		__user = getByUser_1(__user);
		if (null != __user) {
			return new String[] { "昵称已存在" };
		} // if

		/***** *****/

		User _user = new User();
		_user.setId(genId());

		_user.setMobile(user.getMobile());
		_user.setEmail(user.getEmail());
		_user.setNickname(user.getNickname());

		_user.setLv(user.getLv());

		_user.setNum_static(user.getNum_static());
		_user.setNum_dynamic(user.getNum_dynamic());
		_user.setNum_ticket(user.getNum_ticket());
		_user.setNum_food(user.getNum_food());

		_user.setTotal_static(user.getNum_static());
		_user.setTotal_dynamic(user.getNum_dynamic());
		_user.setTotal_ticket(user.getNum_ticket());
		_user.setTotal_food(user.getNum_food());

		_user.setCreate_time(new Date());
		_user.setRole_id(2);

		_user.setUser_pass(user.getUser_pass());
		_user.setUser_pass_safe(user.getUser_pass_safe());

		user.setDepth(1 + p_user.getDepth());
		user.setFamily_id(p_user.getFamily_id());

		super.save(_user);
		return null;
	}

	/**
	 * 新用户注册
	 *
	 * 1、一堆验证
	 *
	 * 2、判断用户是否存在
	 *
	 * 3、创建新用户
	 */
	@Override
	public String[] register(User user) {

		if (null == user) {
			return new String[] { "非法操作" };
		} // if

		user.setReal_name(StringUtil.isEmpty(user.getReal_name()));
		if (null == user.getReal_name()) {
			return new String[] { "姓名不能为空" };
		} // if

		user.setUser_pass(StringUtil.isEmpty(user.getUser_pass()));
		if (null == user.getUser_pass()) {
			return new String[] { "登陆密码不能为空" };
		} // if

		user.setUser_pass_safe(StringUtil.isEmpty(user.getUser_pass_safe()));
		if (null == user.getUser_pass_safe()) {
			return new String[] { "安全密码不能为空" };
		}

		/***** *****/

		// 附加数据
		user.setUser_pass(MD5.encode(user.getUser_pass()));
		user.setUser_pass_safe(MD5.encode(user.getUser_pass_safe()));

		user.setNum_static(0.00);
		user.setNum_dynamic(0.00);
		user.setNum_ticket(0);
		user.setNum_food(0);

		user.setStatus(0);

		return save_prev(user);
	}

	@Override
	public String[] createUser(User user) {

		if (null == user) {
			return new String[] { "非法操作" };
		} // if

		/***** *****/

		// 附加数据
		user.setUser_pass(DEFAULT_USER_PASS);
		user.setUser_pass_safe(DEFAULT_USER_PASS);
		user.setStatus(1);

		user.setNum_static(null == user.getNum_static() ? 0.00 : user
				.getNum_static());
		user.setNum_dynamic(null == user.getNum_dynamic() ? 0.00 : user
				.getNum_dynamic());
		user.setNum_ticket(null == user.getNum_ticket() ? 0 : user
				.getNum_ticket());
		user.setNum_food(null == user.getNum_food() ? 0 : user.getNum_food());

		return save_prev(user);
	}

	private List<User> findByUser_2(User user, int page, int rows) {
		return null;
	}

	private List<User> findByUser_1(User user, int page, int rows) {
		Example example = new Example(User.class);
		example.setOrderByClause("create_time desc");

		if (null != user) {
			Example.Criteria criteria = example.createCriteria();

			String apikey = StringUtil.isEmpty(user.getApikey());
			if (null != apikey) {
				criteria.andEqualTo("apikey", apikey);
			} // if

			String seckey = StringUtil.isEmpty(user.getSeckey());
			if (null != seckey) {
				criteria.andEqualTo("seckey", seckey);
			} // if

			String pid = StringUtil.isEmpty(user.getPid());
			if (null != pid) {
				criteria.andEqualTo("pid", pid);
			} // if

			String mobile = StringUtil.isEmpty(user.getMobile());
			if (null != mobile) {
				criteria.andEqualTo("mobile", mobile);
			} // if

			String email = StringUtil.isEmpty(user.getEmail());
			if (null != email) {
				criteria.andEqualTo("email", email);
			} // if

			String nickname = StringUtil.isEmpty(user.getNickname());
			if (null != nickname) {
				criteria.andEqualTo("nickname", nickname);
			} // if

		} // if

		PageHelper.startPage(page, rows);
		return selectByExample(example);
	}

	@Override
	public List<User> findByUser(int flag, User user, int page, int rows) {
		switch (flag) {
		case 1:
			return findByUser_1(user, page, rows);
		case 2:
			return findByUser_2(user, page, rows);
		default:
			return null;
		}
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
			if (8 < id.length()) {
				id.substring(0, 8);
			}
			// END
			user = selectByKey(id);
		} while (null != user);
		return "M" + id;
	}

	private User getId_2(String id) {
		return null;
	}

	private User getId_1(String id) {

		User user = selectByKey(id);
		if (null == user) {
			return null;
		} // if

		// 获取父亲
		if (!"0".equals(user.getPid())) {
			User p_user = selectByKey(user.getPid());
			user.setP_user(p_user);
		} // if

		// 获取用户的最后一次排单（鸡苗批次）
		List<Farm> list_farm = farmService.findByUserId(user.getId(), 1, 1);
		user.setLastFarm((null == list_farm || 0 == list_farm.size()) ? null
				: list_farm.get(0));

		return user;
	}

	@Override
	public User getId(int flag, String id) {

		switch (flag) {
		case 1:
			return getId_1(id);
		case 2:
			return getId_2(id);
		default:
			return null;
		}
	}

	private User getByUser_2(User user) {
		return null;
	}

	private User getByUser_1(User user) {
		Example example = new Example(User.class);
		Example.Criteria criteria = example.createCriteria();

		String email = StringUtil.isEmpty(user.getEmail());
		if (null != email) {
			criteria.andEqualTo("email", email);
		} // if

		String mobile = StringUtil.isEmpty(user.getMobile());
		if (null != mobile) {
			criteria.andEqualTo("mobile", mobile);
		} // if

		String nickname = StringUtil.isEmpty(user.getNickname());
		if (null != nickname) {
			criteria.andEqualTo("nickname", nickname);
		} // if

		List<User> list = selectByExample(example);
		return (null == list || 1 != list.size()) ? null : list.get(0);
	}

	@Override
	public User getByUser(int flag, User user) {

		if (null == user) {
			return null;
		} // if

		switch (flag) {
		case 1:
			return getByUser_1(user);
		case 2:
			return getByUser_2(user);
		default:
			return null;
		} // switch
	}

	private static final String DEFAULT_USER_PASS = MD5.encode("123456");

	@Override
	public int save(User entity) {
		return 0;
	}

	/**
	 * 获取用户对象
	 *
	 * @param name
	 *            邮箱、手机号
	 * @return
	 */
	private User getByName(String name) {
		User user = null;

		user = new User();
		user.setEmail(name);
		user = getByUser_1(user);
		if (null != user) {
			return user;
		} // if

		user = new User();
		user.setMobile(name);
		user = getByUser_1(user);
		if (null != user) {
			return user;
		} // if

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
		} // if

		if (!MD5.encode(user_pass).equals(user.getUser_pass())) {
			result.put("msg", new String[] { "用户名或密码输入错误" });
			return result;
		} // if

		if (0 == user.getStatus()) {
			result.put("msg", new String[] { "已被限制登陆，请联系管理员" });
			return result;
		} // if

		result.put("data", user);
		return result;
	}

	/**
	 * 修改登陆密码
	 */
	@Override
	public String[] changePwd(String id, String old_pass, String new_pass) {

		new_pass = StringUtil.isEmpty(new_pass);
		if (null == new_pass) {
			return new String[] { "新登陆密码不能为空" };
		} // if

		User user = selectByKey(id);
		if (null == user) {
			return new String[] { "没有找到该用户" };
		} // if

		if (!MD5.encode(old_pass).equals(user.getUser_pass())) {
			return new String[] { "原登陆密码错误" };
		} // if

		User _user = new User();
		_user.setId(id);
		_user.setUser_pass(MD5.encode(new_pass));

		super.updateNotNull(_user);
		return null;
	}

	/**
	 * 修改安全密码
	 */
	@Override
	public String[] changePwdSafe(String id, String old_pass, String new_pass) {

		new_pass = StringUtil.isEmpty(new_pass);
		if (null == new_pass) {
			return new String[] { "新安全密码不能为空" };
		} // if

		User user = selectByKey(id);
		if (null == user) {
			return new String[] { "没有找到该用户" };
		} // if

		if (!MD5.encode(old_pass).equals(user.getUser_pass_safe())) {
			return new String[] { "原安全密码错误" };
		} // if

		User _user = new User();
		_user.setId(id);
		_user.setUser_pass_safe(MD5.encode(new_pass));

		super.updateNotNull(_user);
		return null;
	}

	@Override
	public int resetPwd(String id) {
		User user = new User();
		user.setId(id);
		user.setUser_pass(DEFAULT_USER_PASS);

		return super.updateNotNull(user);
	}

	@Override
	public int resetPwdSafe(String id) {
		User user = new User();
		user.setId(id);
		user.setUser_pass_safe(DEFAULT_USER_PASS);

		return super.updateNotNull(user);
	}

	/**
	 * 修改用户信息
	 *
	 * 1、创建一个新用户对象
	 *
	 * 2、把参数赋值到新用户对象（增加安全性）
	 */
	@Override
	public String[] editInfo(User user) {

		User _user = new User();
		_user.setId(user.getId());

		_user.setReal_name(user.getReal_name());
		_user.setAlipay_account(user.getAlipay_account());
		_user.setWx_account(user.getWx_account());
		_user.setBank(user.getBank());
		_user.setBank_account(user.getBank_account());
		_user.setBank_name(user.getBank_name());

		super.updateNotNull(_user);
		return null;
	}

	/**
	 * 修改用户的全部信息
	 */
	@Override
	public String[] editInfoAll(User user) {

		User _user = new User();
		_user.setId(user.getId());

		_user.setMobile(user.getMobile());
		_user.setEmail(user.getEmail());
		_user.setNickname(user.getNickname());

		_user.setAlipay_account(user.getAlipay_account());
		_user.setWx_account(user.getWx_account());
		_user.setBank(user.getBank());
		_user.setBank_account(user.getBank_account());
		_user.setBank_name(user.getBank_name());

		_user.setNum_static(user.getNum_static());
		_user.setNum_dynamic(user.getNum_dynamic());
		_user.setNum_ticket(user.getNum_ticket());
		_user.setNum_food(user.getNum_food());

		super.updateNotNull(_user);
		return null;
	}

	@Override
	public List<User> findChildren(String user_id, int page, int rows) {

		User user = new User();
		user.setPid(user_id);

		return findByUser_1(user, page, rows);
	}

}
