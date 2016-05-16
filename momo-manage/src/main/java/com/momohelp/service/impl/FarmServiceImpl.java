package com.momohelp.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.model.Cfg;
import com.momohelp.model.Farm;
import com.momohelp.model.MaterialRecord;
import com.momohelp.model.User;
import com.momohelp.service.CfgService;
import com.momohelp.service.FarmService;
import com.momohelp.service.MaterialRecordService;
import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Service("farmService")
public class FarmServiceImpl extends BaseService<Farm> implements FarmService {

	@Autowired
	private UserService userService;

	@Autowired
	private CfgService cfgService;

	@Autowired
	private MaterialRecordService materialRecordService;

	@Override
	public int save(Farm entity) {
		entity.setFlag_out(0);
		entity.setFlag_calc_bonus(0);
		entity.setNum_current(entity.getNum_buy());
		entity.setNum_deal(0);
		return super.save(entity);
	}

	@Override
	public int updateNotNull(Farm entity) {
		entity.setUser_id(null);
		entity.setCreate_time(null);
		entity.setTime_out(null);
		entity.setTime_ripe(null);
		entity.setNum_buy(null);
		entity.setPid(null);
		return super.updateNotNull(entity);
	}

	/**
	 * 获取出局时间
	 *
	 * @param date
	 * @return
	 */
	private Date getOutTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, 24 * 20);
		return c.getTime();
	}

	/**
	 * 获取成熟时间
	 *
	 * @param date
	 * @return
	 */
	private Date getRipeTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, 24 * 7);
		return c.getTime();
	}

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd 00:00:00");

	/**
	 * 获取明天的日期，例如 2016-05-16 00:00:00
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	private Date getTomorrow(Date date) throws ParseException {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DAY_OF_MONTH, 1);
		return sdf.parse(sdf.format(ca.getTime()));
	}

	/**
	 * 买入鸡苗
	 *
	 * 1、查看门票是否小于1，如果是则返回
	 *
	 * 2、验证购买的数量是否是100的倍数
	 *
	 * 3、验证购买的数量是否符合我的身份
	 *
	 * 3、验证最后一笔排单，买卖双方是否完全交易成功（未完）
	 *
	 * 4、更新我的帐户信息--门票数量-1
	 *
	 * 5、插入 w_material_use 一条购买鸡苗使用门票的记录
	 *
	 * 6、插入 w_farm_chick 一条购买鸡苗的记录
	 */
	@Override
	public String[] buy(Farm farm) {
		farm.setNum_buy((null == farm.getNum_buy()) ? 0 : farm.getNum_buy());

		// 获取我的帐户信息（实时）
		User user = userService.selectByKey(farm.getUser_id());

		String[] checkMyTicket = checkMyTicket(user);
		if (null != checkMyTicket) {
			return checkMyTicket;
		}

		String[] checkNum = checkNum(user, farm.getNum_buy());
		if (null != checkNum) {
			return checkNum;
		}

		/***** 收集数据 *****/

		// 取当前日期
		Date date = new Date();
		Date tomorrow = null;

		try {
			tomorrow = getTomorrow(date);
		} catch (ParseException e) {
			return new String[] { "日期异常" };
		}

		farm.setCreate_time(date);
		farm.setTime_out(getOutTime(tomorrow));
		farm.setTime_ripe(getRipeTime(tomorrow));

		/***** 获取会员的最后一单 *****/
		Farm last_farm = findLast(farm.getUser_id());
		farm.setPid((null == last_farm) ? "0" : last_farm.getId());

		/***** 添加一条操作记录 *****/
		saveMaterialRecord(farm);

		updateUserTicket(user);

		save(farm);
		return null;
	}

	/**
	 * 检测我的门票还有没有？
	 *
	 * 1、没有，则返回
	 *
	 * 2、 更新用户表门票 -1
	 *
	 * @param user
	 * @param user_id
	 * @return
	 */
	private String[] checkMyTicket(User user) {
		return (1 > user.getNum_ticket()) ? new String[] { "没有门票了，请购买" } : null;
	}

	/**
	 * 更新用户门票数量
	 *
	 * @param user
	 */
	private void updateUserTicket(User user) {
		User new_user = new User();
		new_user.setId(user.getId());
		new_user.setNum_ticket(user.getNum_ticket() - 1);
		userService.updateNotNull(new_user);
	}

	/**
	 * 添加操作记录
	 *
	 * w_material_use 添加一条操作记录
	 *
	 * @param farm
	 */
	private void saveMaterialRecord(Farm farm) {
		MaterialRecord materialRecord = new MaterialRecord();
		materialRecord.setUser_id(farm.getUser_id());
		double d = farm.getNum_buy();
		materialRecord.setNum_use(d);
		materialRecord.setStatus(1);
		materialRecord.setType_id(5);
		materialRecord.setComment(null);
		materialRecord.setTrans_user_id(null);
		// 后续再说
		materialRecord.setNum_balance(null);
		materialRecord.setFlag_plus_minus(1);

		materialRecordService.save(materialRecord);
	}

	/**
	 * 检测购买的鸡苗数量是否合法
	 *
	 * @param user
	 * @param num_buy
	 * @return
	 */
	private String[] checkNum(User user, int num_buy) {
		// 100 的倍数
		Cfg cfg = cfgService.selectByKey("0106");

		if (0 != num_buy % Integer.valueOf(cfg.getValue_())) {
			return new String[] { "输入的数量不正确" };
		}

		// 获取上下限
		Integer[] range = getRangeByUserLv(user.getLv());

		if (range[0] <= num_buy && num_buy <= range[1]) {
			return null;
		}

		return new String[] { "请输入正确的数量范围" };
	}

	/**
	 * 获取会员能买入鸡苗的上下限
	 *
	 * @param lv
	 * @return
	 */
	private Integer[] getRangeByUserLv(String lv) {
		String min = null, max = null;

		if ("05".equals(lv)) {
			min = "2001";
			max = "2002";
		} else if ("06".equals(lv)) {
			min = "2003";
			max = "2004";
		} else if ("07".equals(lv)) {
			min = "2005";
			max = "2006";
		} else if ("08".equals(lv)) {
			min = "2007";
			max = "2008";
		}

		Cfg minObj = cfgService.selectByKey(min);
		Cfg maxObj = cfgService.selectByKey(max);

		return new Integer[] { Integer.valueOf(minObj.getValue_()),
				Integer.valueOf(maxObj.getValue_()) };
	}

	@Override
	public Farm findLast(String user_id) {
		Example example = new Example(Farm.class);
		example.setOrderByClause("create_time desc");
		// TODO
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);

		PageHelper.startPage(1, 1);
		List<Farm> list = selectByExample(example);
		return (null == list || 0 == list.size()) ? null : list.get(0);
	}

}
