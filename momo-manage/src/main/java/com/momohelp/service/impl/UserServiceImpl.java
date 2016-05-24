package com.momohelp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.mapper.SellMapper;
import com.momohelp.model.Sell;
import com.momohelp.model.User;
import com.momohelp.service.FarmFeedService;
import com.momohelp.service.FarmHatchService;
import com.momohelp.service.FarmService;
import com.momohelp.service.SellService;
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

	@Autowired
	private FarmFeedService farmFeedService;

	@Autowired
	private FarmHatchService farmHatchService;

	@Autowired
	private SellService sellService;

	@Override
	public User getId(int flag, String id) {
		switch (flag) {
		case 0:
			return getId_0(id);
		case 1:
			return getId_1(id);
		case 2:
			return getId_2(id);
		case 4:
			return getId_4(id);
		case 5:
			return getId_5(id);
		case 6:
			return getId_6(id);
		default:
			return null;
		}
	}

	/**
	 * 买盘
	 *
	 * @param id
	 * @return
	 */
	private User getId_1(String id) {

		User user = getId_0(id);
		if (null == user) {
			return null;
		}

		user.setFarms(farmService.findUnDealByUserId(id));

		return user;
	}

	/**
	 * 卖盘
	 *
	 * @param id
	 * @return
	 */
	private User getId_2(String id) {

		User user = getId_0(id);
		if (null == user) {
			return null;
		}

		user.setSells(sellService.findUnDealByUserId(id));

		return user;

	}

	/**
	 * 买入卖出等等综合使用
	 *
	 * @param id
	 * @return
	 */
	private User getId_4(String id) {

		User user = getId_0(id);
		if (null == user) {
			return null;
		}

		// 最后一笔排单
		user.setLastFarm(farmService.getLastByUserId(id));
		// 最后一笔卖出记录
		user.setLastSell(sellService.getLastByUserId(id));

		// 获取当前月的卖盘
		user.setMonthSells(sellService.findMonthSellByUserId(id));

		return user;
	}

	/**
	 * 喂鸡
	 *
	 * @param id
	 * @return
	 */
	private User getId_5(String id) {

		User user = getId_0(id);
		if (null == user) {
			return null;
		}

		user.setFarms(farmService.findFeedByUserId(id));

		return user;
	}

	/**
	 * 孵化
	 *
	 * @param id
	 * @return
	 */
	private User getId_6(String id) {

		User user = getId_0(id);
		if (null == user) {
			return null;
		}

		user.setFarms(farmService.findHatchByUserId(id));

		return user;
	}

	/**
	 * 根
	 *
	 * @param id
	 * @return
	 */
	private User getId_0(String id) {

		User user = selectByKey(id);
		if (null == user) {
			return null;
		}

		// 获取父亲
		if (!"0".equals(user.getPid())) {
			User p_user = selectByKey(user.getPid());
			user.setP_user(p_user);
		}


		return user;
	}

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
			result.put("msg", new String[] { "已被限制登陆，请联系管理员" });
			return result;
		}

		result.put("data", user);
		return result;
	}

	@Override
	public List<User> findChildren(String user_id, int page, int rows) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int resetPwd(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int resetPwdSafe(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] register(User user) {
		if (null == user) {
			return new String[] { "非法操作" };
		}

		user.setReal_name(StringUtil.isEmpty(user.getReal_name()));
		if (null == user.getReal_name()) {
			return new String[] { "姓名不能为空" };
		}

		user.setUser_pass(StringUtil.isEmpty(user.getUser_pass()));
		if (null == user.getUser_pass()) {
			return new String[] { "登陆密码不能为空" };
		}

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

	private String[] save_prev(User user) {

		user.setMobile(StringUtil.isEmpty(user.getMobile()));
		if (null == user.getMobile()) {
			return new String[] { "手机号码不能为空" };
		}

		user.setEmail(StringUtil.isEmpty(user.getEmail()));
		if (null == user.getEmail()) {
			return new String[] { "电子邮箱不能为空" };
		}

		user.setNickname(StringUtil.isEmpty(user.getNickname()));
		if (null == user.getNickname()) {
			return new String[] { "昵称不能为空" };
		}

		/***** *****/

		// 获取父亲
		User p_user = selectByKey(user.getPid());
		if (null == p_user) {
			return new String[] { "您输入的会员编号不存在，请重新输入" };
		}

		/***** *****/

		User __user = null;

		__user = new User();
		__user.setMobile(user.getMobile());
		__user = getByUser(__user);
		if (null != __user) {
			return new String[] { "手机号码已存在" };
		}

		__user = new User();
		__user.setEmail(user.getEmail());
		__user = getByUser(__user);
		if (null != __user) {
			return new String[] { "电子邮箱已存在" };
		}

		__user = new User();
		__user.setNickname(user.getNickname());
		__user = getByUser(__user);
		if (null != __user) {
			return new String[] { "昵称已存在" };
		}

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

	@Override
	public String[] createUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] editInfo(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] editInfoAll(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] changePwd(String id, String old_pass, String new_pass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] changePwdSafe(String id, String old_pass, String new_pass) {
		// TODO Auto-generated method stub
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
			if (8 < id.length()) {
				id = id.substring(0, 8);
			}
			id = "M" + id;
			user = selectByKey(id);
		} while (null != user);
		return id;
	}

	private User getByUser(User user) {
		Example example = new Example(User.class);
		Example.Criteria criteria = example.createCriteria();

		String email = StringUtil.isEmpty(user.getEmail());
		if (null != email) {
			criteria.andEqualTo("email", email);
		}

		String mobile = StringUtil.isEmpty(user.getMobile());
		if (null != mobile) {
			criteria.andEqualTo("mobile", mobile);
		}

		String nickname = StringUtil.isEmpty(user.getNickname());
		if (null != nickname) {
			criteria.andEqualTo("nickname", nickname);
		}

		List<User> list = selectByExample(example);
		return (null == list || 1 != list.size()) ? null : list.get(0);
	}

	private User getByName(String name) {
		User user = null;

		user = new User();
		user.setEmail(name);
		user = getByUser(user);
		if (null != user) {
			return user;
		}

		user = new User();
		user.setMobile(name);
		user = getByUser(user);
		if (null != user) {
			return user;
		}

		return null;
	}
}
