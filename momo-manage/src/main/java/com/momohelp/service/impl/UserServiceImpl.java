package com.momohelp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
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
			return getRootById(id);
		case 1:
			return getId_1(id);
		case 2:
			return getId_2(id);
		case 4:
			return getAllById(id);
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

		User user = getRootById(id);
		if (null == user) {
			return null;
		}

		user.setFarms(farmService.findUnDealByUserId___4(id));

		return user;
	}

	/**
	 * 卖盘
	 *
	 * @param id
	 * @return
	 */
	private User getId_2(String id) {

		User user = getRootById(id);
		if (null == user) {
			return null;
		}

		user.setSells(sellService.findUnDealByUserId__4(id));

		return user;

	}

	/**
	 * 买入卖出等等综合使用
	 *
	 * @param id
	 * @return
	 */
	@Override
	public User getAllById(String id) {

		User user = getRootById(id);

		if (null == user) {
			return null;
		}

		// 最后一笔排单
		user.setLastFarm(farmService.getLastByUserId__4(id));

		// 最后一笔卖出记录
		user.setLastSell(sellService.getLastByUserId___4(id));

		// 获取当前月的卖盘
		user.setMonthSells(sellService.findMonthSellByUserId____4(id));

		return user;
	}

	/**
	 * 喂鸡
	 *
	 * @param id
	 * @return
	 */
	private User getId_5(String id) {

		User user = getRootById(id);
		if (null == user) {
			return null;
		}

		user.setFarms(farmService.findFeedByUserId__3(id));

		return user;
	}

	/**
	 * 孵化
	 *
	 * @param id
	 * @return
	 */
	private User getId_6(String id) {

		User user = getRootById(id);
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
	private User getRootById(String id) {

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

	private List<User> findByUser__4(User user, int page, int rows) {
		Example example = new Example(User.class);
		example.setOrderByClause("create_time desc");

		if (null != user) {
			Example.Criteria criteria = example.createCriteria();

			String apikey = StringUtil.isEmpty(user.getApikey());
			if (null != apikey) {
				criteria.andEqualTo("apikey", apikey);
			}

			String seckey = StringUtil.isEmpty(user.getSeckey());
			if (null != seckey) {
				criteria.andEqualTo("seckey", seckey);
			}

			String pid = StringUtil.isEmpty(user.getPid());
			if (null != pid) {
				criteria.andEqualTo("pid", pid);
			}

			String mobile = StringUtil.isEmpty(user.getMobile());
			if (null != mobile) {
				criteria.andEqualTo("mobile", mobile);
			}

			String email = StringUtil.isEmpty(user.getEmail());
			if (null != email) {
				criteria.andEqualTo("email", email);
			}

			String nickname = StringUtil.isEmpty(user.getNickname());
			if (null != nickname) {
				criteria.andEqualTo("nickname", nickname);
			}
		}

		PageHelper.startPage(page, rows);
		return selectByExample(example);
	}

	@Override
	public List<User> findChildren___4(String id, int page, int rows) {
		User user = new User();
		user.setPid(id);

		return findByUser__4(user, page, rows);
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

	@Override
	public String[] register(User user) {

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

		// 附加数据
		user.setUser_pass(MD5.encode(user.getUser_pass()));
		user.setUser_pass_safe(MD5.encode(user.getUser_pass_safe()));

		user.setNum_static(0.00);
		user.setNum_dynamic(0.00);
		user.setNum_ticket(0);
		user.setNum_food(0);

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

		_user.setPid(user.getPid());
		_user.setReal_name(user.getReal_name());
		_user.setAlipay_account(user.getAlipay_account());
		_user.setWx_account(user.getWx_account());
		_user.setStatus(1);

		_user.setMobile(user.getMobile());
		_user.setEmail(user.getEmail());
		_user.setNickname(user.getNickname());

		_user.setLv("05");

		_user.setNum_static(user.getNum_static());
		_user.setNum_dynamic(user.getNum_dynamic());
		_user.setNum_ticket(user.getNum_ticket());
		_user.setNum_food(user.getNum_food());

		_user.setTotal_static(user.getNum_static());
		_user.setTotal_dynamic(user.getNum_dynamic());
		_user.setTotal_ticket(user.getNum_ticket());
		_user.setTotal_food(user.getNum_food());

		_user.setCreate_time(new Date());

		_user.setUser_pass(user.getUser_pass());
		_user.setUser_pass_safe(user.getUser_pass_safe());

		_user.setDepth(1 + p_user.getDepth());
		_user.setFamily_id(p_user.getFamily_id());

		super.save(_user);
		return null;
	}

	private static final String DEFAULT_USER_PASS = MD5.encode("123456");

	@Override
	public String[] createUser(User user) {
		if (null == user) {
			return new String[] { "非法操作" };
		}

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

	@Override
	public void editInfo(User user) {

		User _user = new User();
		_user.setId(user.getId());

		_user.setReal_name(user.getReal_name());
		_user.setAlipay_account(user.getAlipay_account());
		_user.setWx_account(user.getWx_account());
		_user.setBank(user.getBank());
		_user.setBank_account(user.getBank_account());
		_user.setBank_name(user.getBank_name());

		super.updateNotNull(_user);
	}

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
	public String[] changePwd(String id, String old_pass, String new_pass) {

		new_pass = StringUtil.isEmpty(new_pass);
		if (null == new_pass) {
			return new String[] { "新登陆密码不能为空" };
		}

		User user = selectByKey(id);

		if (!MD5.encode(old_pass).equals(user.getUser_pass())) {
			return new String[] { "原登陆密码错误" };
		}

		// 创建新用户对象
		User _user = new User();
		_user.setId(id);
		_user.setUser_pass(MD5.encode(new_pass));

		super.updateNotNull(_user);
		return null;
	}

	@Override
	public String[] changePwdSafe(String id, String old_pass, String new_pass) {

		new_pass = StringUtil.isEmpty(new_pass);
		if (null == new_pass) {
			return new String[] { "新安全密码不能为空" };
		}

		User user = selectByKey(id);

		if (!MD5.encode(old_pass).equals(user.getUser_pass())) {
			return new String[] { "原安全密码错误" };
		}

		// 创建新用户对象
		User _user = new User();
		_user.setId(id);
		_user.setUser_pass_safe(MD5.encode(new_pass));

		super.updateNotNull(_user);
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

		User user = new User();
		user.setMobile(name);
		user = getByUser(user);
		return user;
	}

	@Override
	public String[] sendSms(String id) {

		// SmSWebService service = new SmSWebService();
		// SmSWebServiceSoap serviceSoap = service.getSmSWebServiceSoap();
		// WsSendResponse response = serviceSoap.sendSms("154", "MOMO668",
		// "123456", __u.getMobile(), "您本次验证码:" + user.getVerifycode_sms()
		// + "，感谢您的支持，祝您生活愉快！！", "", "");

		// response.getReturnStatus();

		return null;
	}

	private String genId2() {
		int i = (int) ((Math.random() * 5 + 1) * 1000);
		String id = String.valueOf(i);
		if (4 < id.length()) {
			id = id.substring(0, 4);
		}
		return id;
	}

	@Override
	public User sell_record__list__4(String id) {

		User user = selectByKey(id);

		if (null == user) {
			return null;
		}

		// 获取用户未完全交易的卖盘
		user.setSells(sellService.findUnDealByUserId__4(user.getId()));

		return user;
	}

	@Override
	public User getMeAndParent__4(String id) {

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
	public User buy_record__list__4(String id) {

		User user = selectByKey(id);

		if (null == user) {
			return null;
		}

		// 获取用户未完全交易的买盘
		user.setFarms(farmService.findUnDealByUserId___4(user.getId()));

		return user;
	}

	@Override
	public User buyTime___4(String id) {

		User user = selectByKey(id);

		if (null == user) {
			return null;
		}

		// 最近的一笔排单
		user.setLastFarm(farmService.getLastByUserId__4(user.getId()));

		return user;
	}

	@Override
	public User sellTime___4(String id) {

		User user = selectByKey(id);

		if (null == user) {
			return null;
		}

		// 最后一笔卖出记录
		user.setLastSell(sellService.getLastByUserId___4(id));

		// 获取当前月的卖盘
		user.setMonthSells(sellService.findMonthSellByUserId____4(id));

		return user;
	}
}
