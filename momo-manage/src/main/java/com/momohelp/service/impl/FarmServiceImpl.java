package com.momohelp.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.model.Buy;
import com.momohelp.model.Farm;
import com.momohelp.model.FarmFeed;
import com.momohelp.model.MaterialRecord;
import com.momohelp.model.User;
import com.momohelp.service.BuyService;
import com.momohelp.service.FarmFeedService;
import com.momohelp.service.FarmHatchService;
import com.momohelp.service.FarmService;
import com.momohelp.service.MaterialRecordService;
import com.momohelp.service.UserService;
import com.momohelp.util.StringUtil;

/**
 *
 * @author Administrator
 *
 */
@Service("farmService")
public class FarmServiceImpl extends BaseService<Farm> implements FarmService {

	private Farm getByFarm_2(Farm farm) {
		return null;
	}

	private Farm getByFarm_1(Farm farm) {
		Example example = new Example(Farm.class);
		Example.Criteria criteria = example.createCriteria();

		String id = StringUtil.isEmpty(farm.getId());
		if (null != id) {
			criteria.andEqualTo("id", id);
		} // if

		String user_id = StringUtil.isEmpty(farm.getUser_id());
		if (null != user_id) {
			criteria.andEqualTo("user_id", user_id);
		} // if

		List<Farm> list = selectByExample(example);

		if (null == list || 1 != list.size()) {
			return null;
		} // if

		farm = list.get(0);

		List<FarmFeed> list_farmFeed = farmFeedService.findByFarmId(farm
				.getId());

		// 喂食记录
		farm.setFarmFeeds(list_farmFeed);
		// 最后一次喂食记录
		farm.setLastFarmFeed((null == list_farmFeed || 0 == list_farmFeed
				.size()) ? null : list_farmFeed.get(0));

		// 孵化记录
		farm.setFarmHatchs(farmHatchService.findByFarmId(farm.getId()));

		return farm;
	}

	@Override
	public Farm getByFarm(int flag, Farm farm) {

		if (null == farm) {
			return null;
		} // if

		switch (flag) {
		case 1:
			return getByFarm_1(farm);
		case 2:
			return getByFarm_2(farm);
		default:
			return null;
		} // switch
	}

	@Override
	public List<Farm> findByUserId(String user_id, int page, int rows) {
		Example example = new Example(Farm.class);
		example.setOrderByClause("create_time desc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);

		PageHelper.startPage(page, rows);
		List<Farm> list = selectByExample(example);

		if (null == list) {
			return null;
		} // if

		for (int i = 0, j = list.size(); i < j; i++) {
			Farm farm = list.get(i);
			// 喂食记录
			farm.setFarmFeeds(farmFeedService.findByFarmId(farm.getId()));
			// 孵化记录
			farm.setFarmHatchs(farmHatchService.findByFarmId(farm.getId()));
		} // for

		return list;
	}

	@Autowired
	private UserService userService;

	@Autowired
	private MaterialRecordService materialRecordService;

	@Autowired
	private BuyService buyService;

	@Autowired
	private FarmFeedService farmFeedService;

	@Autowired
	private FarmHatchService farmHatchService;

	@Override
	public int save(Farm entity) {
		entity.setNum_deal(0);
		entity.setFlag_calc_bonus(0);
		entity.setNum_reward(0);
		entity.setNum_current(entity.getNum_buy());

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
	 * 获取出局时间 20天
	 *
	 * @param date
	 * @return
	 */
	private Date getTimeOut(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, 24 * 20);
		return c.getTime();
	}

	/**
	 * 获取成熟时间 7天
	 *
	 * @param date
	 * @return
	 */
	private Date getTimeRipe(Date date) {
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
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);
		return sdf.parse(sdf.format(c.getTime()));
	}

	/**
	 * 买入鸡苗参数验证
	 *
	 * @param farm
	 * @return
	 */
	private Map<String, Object> buy_validationParameter(Farm farm) {

		Map<String, Object> result = new HashMap<String, Object>();

		farm.setNum_buy((null == farm.getNum_buy()) ? 0 : farm.getNum_buy());
		if (1 > farm.getNum_buy()) {
			result.put("msg", new String[] { "买入鸡苗数量必须大于 0" });
			return result;
		} // if

		// 100的倍数
		if (0 != farm.getNum_buy() % 100) {
			result.put("msg", new String[] { "买入数量请输入100的倍数" });
			return result;
		} // if

		result.put("data", farm);
		return result;
	}

	/**
	 * 买入鸡苗的各种验证（操作数据库相关的查询）
	 *
	 * 1、验证我的门票是否够用
	 *
	 * 2、验证购买的门票数量是否合法
	 *
	 * 3、验证我的最后一笔排单是否已经完全交易成功
	 *
	 * @param farm
	 * @param user
	 * @return
	 */
	private Map<String, Object> buy_validation(Farm farm, User user) {

		Map<String, Object> result = new HashMap<String, Object>();

		if (1 > user.getNum_ticket()) {
			result.put("msg", new String[] { "门票数量不足，请购买" });
			return result;
		} // if

		if (farm.getNum_buy() < user.getBuyMo().getMin()
				|| farm.getNum_buy() > user.getBuyMo().getMax()) {
			result.put("msg", new String[] { "请输入正确的数量范围" });
			return result;
		} // if

		// 用户的最后一次排单（鸡苗批次）
		Farm lastFarm = user.getLastFarm();

		if (null != lastFarm) {
			if (null == lastFarm.getTime_deal()) {
				result.put("msg", new String[] { "有未完成的排单" });
				return result;
			} // if
		} // if

		// 设置当前排单的上一单id
		farm.setPid(null == lastFarm ? "0" : lastFarm.getId());

		// 当前时间我自己的排单是否与我的上一单接上气儿了
		farm.setFlag_out_self(null == lastFarm ? 1 : lastFarm.checkStatusOut());

		result.put("data", farm);
		return result;
	}

	/**
	 * 买入鸡苗
	 *
	 * 4、更新我的帐户信息--门票数量-1
	 *
	 * 8、预付款直接写入买盘
	 *
	 * 5、插入 w_material_use 一条购买鸡苗使用门票的记录
	 *
	 * 6、插入 w_farm_chick 一条购买鸡苗的记录
	 */
	@Override
	public String[] buy(Farm farm) {
		farm.setCreate_time(new Date());

		/***** *****/

		Map<String, Object> buy_validationParameter = buy_validationParameter(farm);
		if (buy_validationParameter.containsKey("msg")) {
			return (String[]) buy_validationParameter.get("msg");
		} // if
		farm = (Farm) buy_validationParameter.get("data");

		// 获取我的帐户信息（实时）
		User user = userService.getId(1, farm.getUser_id());

		Map<String, Object> buy_validation = buy_validation(farm, user);
		if (buy_validation.containsKey("msg")) {
			return (String[]) buy_validation.get("msg");
		} // if
		farm = (Farm) buy_validation.get("data");

		/***** *****/

		Date tomorrow = null;

		try {
			tomorrow = getTomorrow(farm.getCreate_time());
		} catch (ParseException e) {
			return new String[] { "日期数据异常" };
		} // try

		farm.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		farm.setTime_out(getTimeOut(tomorrow));
		farm.setTime_ripe(getTimeRipe(tomorrow));

		// 我有父级
		if (!"0".equals(user.getPid())) {
			// 获取我上级的帐户信息（实时）
			User p_user = userService.getId(1, user.getPid());

			// 上级的最后一次排单
			Farm p_user_lastFarm = p_user.getLastFarm();

			// 设置此次排单与上级排单的关系
			farm.setPid_higher_ups(null == p_user_lastFarm ? null
					: p_user_lastFarm.getId());
			farm.setFlag_out_p(null == p_user_lastFarm ? 1 : p_user_lastFarm
					.checkStatusOut());
		} // if

		/***** *****/

		saveMaterialRecord(farm, user);
		saveBuy_90(farm);
		saveBuy_10(farm);
		save(farm);
		return null;
	}

	/**
	 * 保存预付款 90%
	 *
	 * @param farm
	 */
	private void saveBuy_90(Farm farm) {
		Buy buy = new Buy();
		buy.setNum_buy(farm.getNum_buy() - (farm.getNum_buy() / 10));
		buy.setW_farm_chick_id(farm.getId());
		buy.setUser_id(farm.getUser_id());
		buy.setTime_deal(null);

		// 第七天开始计算
		Calendar c = Calendar.getInstance();
		c.setTime(farm.getCreate_time());
		c.add(Calendar.DAY_OF_MONTH, (1 == farm.getFlag_out_self()) ? 7 : 8);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		buy.setCalc_time(c.getTime());

		buyService.save(buy);
	}

	/**
	 * 保存预付款 10%
	 *
	 * @param farm
	 */
	private void saveBuy_10(Farm farm) {
		Buy buy = new Buy();
		buy.setNum_buy(farm.getNum_buy() / 10);
		buy.setW_farm_chick_id(farm.getId());
		buy.setUser_id(farm.getUser_id());
		buy.setTime_deal(null);

		// 第二天开始计算
		Calendar c = Calendar.getInstance();
		c.setTime(farm.getCreate_time());
		// 出局前排单24小时，出局后排单48小时
		c.add(Calendar.DAY_OF_MONTH, (1 == farm.getFlag_out_self()) ? 1 : 2);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		buy.setCalc_time(c.getTime());

		// Calendar c = Calendar.getInstance();
		// c.setTime(farm.getCreate_time());
		// // 出局前排单 24 小时，出局后排单 48 小时
		// c.add(Calendar.HOUR_OF_DAY, 24 * (1 == farm.getFlag_out_self() ? 1 :
		// 2));
		// buy.setCalc_time(c.getTime());

		buyService.save(buy);
	}

	/**
	 *
	 * 添加操作记录和用户帐户
	 *
	 * w_material_use 添加一条操作记录
	 *
	 * @param farm
	 * @param user
	 */
	private void saveMaterialRecord(Farm farm, User user) {
		MaterialRecord mr = new MaterialRecord();
		mr.setUser_id(user.getId());

		mr.setNum_use(1.00);
		mr.setStatus(1);
		mr.setType_id(1);

		mr.setComment("买入鸡苗 +" + Double.valueOf(farm.getNum_buy()));

		mr.setTrans_user_id(null);

		mr.setNum_balance(Double.valueOf(user.getNum_ticket() - 1));

		// 0为标识使用了一张门票
		mr.setFlag_plus_minus(0);
		materialRecordService.save(mr);

		// 更新我的门票信息
		User _user = new User();
		_user.setId(user.getId());
		_user.setNum_ticket(mr.getNum_balance().intValue());
		userService.updateNotNull(_user);
	}

	@Override
	public Farm getLastByUserId(String user_id) {
		Example example = new Example(Farm.class);
		example.setOrderByClause("create_time desc");
		// TODO
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);

		PageHelper.startPage(1, 1);
		List<Farm> list = selectByExample(example);
		return (null == list || 0 == list.size()) ? null : list.get(0);
	}

	@Override
	public List<Farm> findFeedByUserId(String user_id) {
		Example example = new Example(Farm.class);
		example.setOrderByClause("create_time asc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);
		criteria.andGreaterThan("num_current", 0);
		// 当前时间 <-- 出局时间
		criteria.andGreaterThan("time_out", new Date());

		List<Farm> list = selectByExample(example);

		if (null == list) {
			return null;
		} // if

		for (int i = 0, j = list.size(); i < j; i++) {
			Farm farm = list.get(i);
			farm.setFarmFeeds(farmFeedService.findByFarmId(farm.getId()));
		} // for

		return list;
	}

	@Override
	public Farm findCanHatch(String key, String user_id) {
		Farm farm = selectByKey(key);
		if (null == farm) {
			return null;
		}

		// 是否是此人的鸡苗批次
		if (!user_id.equals(farm.getUser_id())) {
			return null;
		}

		// 是否已经完全成交过
		if (farm.getNum_buy().intValue() != farm.getNum_deal().intValue()) {
			return null;
		}

		// 是否还有存量
		if (0 == farm.getNum_current()) {
			return null;
		}

		return farm;
	}

	@Override
	public List<Farm> findCanHatch(String user_id) {
		Example example = new Example(Farm.class);
		example.setOrderByClause("create_time asc");
		// TODO
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);
		criteria.andGreaterThan("num_current", 0);
		// 完全成交的时间
		criteria.andIsNotNull("time_deal");

		List<Farm> list = selectByExample(example);
		return list;
	}

	@Override
	public List<Farm> findHatchByUserId(String user_id) {
		Example example = new Example(Farm.class);
		example.setOrderByClause("create_time asc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);
		criteria.andGreaterThan("num_current", 0);
		// 完全成交的时间
		criteria.andIsNotNull("time_deal");

		List<Farm> list = selectByExample(example);

		if (null == list) {
			return null;
		} // if

		for (int i = 0, j = list.size(); i < j; i++) {
			Farm farm = list.get(i);
			farm.setFarmHatchs(farmHatchService.findByFarmId(farm.getId()));
		} // for

		return list;
	}

}
