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
	 * 3、验证最后一笔排单，买卖双方是否完全交易成功
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

		String[] checkMyTicket = checkMyTicket(user, farm.getUser_id());
		if (null != checkMyTicket) {
			return checkMyTicket;
		}

		String[] checkNum = checkNum(user, farm.getNum_buy());
		if (null != checkNum) {
			return checkNum;
		}

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

		save(farm);
		return null;
	}

	/**
	 * 检测我的门票还有没有？
	 *
	 * @param user
	 * @param user_id
	 * @return
	 */
	private String[] checkMyTicket(User user, String user_id) {
		return (0 < user.getNum_ticket()) ? null : new String[] { "没有门票了，请购买" };
	}

	/**
	 * 检测购买的鸡苗数量是否合法
	 *
	 * @param user
	 * @param num_buy
	 * @return
	 */
	private String[] checkNum(User user, int num_buy) {
		Cfg cfg = cfgService.selectByKey("0106");

		return null;
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
