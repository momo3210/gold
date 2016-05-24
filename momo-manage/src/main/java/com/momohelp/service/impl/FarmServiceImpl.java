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
import com.momohelp.mapper.FarmMapper;
import com.momohelp.model.Buy;
import com.momohelp.model.Farm;
import com.momohelp.model.FarmFeed;
import com.momohelp.model.FarmHatch;
import com.momohelp.model.MaterialRecord;
import com.momohelp.model.User;
import com.momohelp.service.BuyService;
import com.momohelp.service.FarmFeedService;
import com.momohelp.service.FarmHatchService;
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
	private MaterialRecordService materialRecordService;

	@Autowired
	private BuyService buyService;

	@Autowired
	private FarmFeedService farmFeedService;

	@Autowired
	private FarmHatchService farmHatchService;

	@Override
	public String[] buy(Farm farm) {

		Farm _farm = new Farm();
		_farm.setCreate_time(new Date());
		_farm.setUser_id(farm.getUser_id());
		_farm.setNum_buy((null == farm.getNum_buy()) ? 0 : farm.getNum_buy());
		_farm.setNum_current(_farm.getNum_buy());
		_farm.setNum_deal(0);
		_farm.setFlag_calc_bonus(0);
		_farm.setNum_reward(0);

		/**/

		if (1 > _farm.getNum_buy()) {
			return new String[] { "买入鸡苗数量必须大于 0" };
		}

		// 100的倍数
		if (0 != _farm.getNum_buy() % 100) {
			return new String[] { "买入数量请输入100的倍数" };
		}

		/**/

		// 实时
		User user = userService.buyTime___4(_farm.getUser_id());

		if (null == user) {
			return new String[] { "非法操作" };
		}

		/**/

		if (1 > user.getNum_ticket()) {
			return new String[] { "门票数量不足，请购买" };
		}

		if (_farm.getNum_buy() < user.getBuyMo().getMin()
				|| _farm.getNum_buy() > user.getBuyMo().getMax()) {
			return new String[] { "请输入正确的数量范围" };
		}

		// 用户的最后一次排单（鸡苗批次）
		Farm last_farm = user.getLastFarm();

		if (null != last_farm) {
			if (last_farm.getNum_buy().intValue() != last_farm.getNum_deal()
					.intValue()) {
				return new String[] { "有未完成的排单" };
			}
		}

		// 设置当前排单的上一单id
		_farm.setPid(null == last_farm ? "0" : last_farm.getId());
		// 当前时间我自己的排单是否与我的上一单接上气儿了
		_farm.setFlag_out_self(null == last_farm ? 1 : last_farm
				.checkStatusOut());

		/**/

		Date tomorrow = null;

		try {
			tomorrow = getTomorrow__4(_farm.getCreate_time());
		} catch (ParseException e) {
			return new String[] { "日期数据异常" };
		}

		_farm.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		_farm.setTime_out(getTimeOut__4(tomorrow));
		_farm.setTime_ripe(getTimeRipe__4(tomorrow));

		// 我有父级
		if (!"0".equals(user.getPid())) {
			// 获取我上级的帐户信息（实时）
			User p_user = userService.buyTime___4(user.getPid());

			if (null != p_user) {
				// 上级的最后一次排单
				Farm p_user_last_farm = p_user.getLastFarm();

				// 设置此次排单与上级排单的关系
				_farm.setPid_higher_ups(null == p_user_last_farm ? null
						: p_user_last_farm.getId());
				_farm.setFlag_out_p(null == p_user_last_farm ? 1
						: p_user_last_farm.checkStatusOut());
			}
		}

		/**/

		saveMaterialRecord__4(_farm, user);
		saveBuy_90__4(_farm);
		saveBuy_10__4(_farm);
		save(_farm);
		return null;
	}

	/**
	 * 保存预付款 90%
	 *
	 * @param farm
	 */
	private void saveBuy_90__4(Farm farm) {
		Buy buy = new Buy();
		buy.setNum_buy(farm.getNum_buy() - (farm.getNum_buy() / 10));
		buy.setW_farm_chick_id(farm.getId());
		buy.setUser_id(farm.getUser_id());
		buy.setIs_deposit(0);
		buy.setNum_deal(0);

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
	private void saveBuy_10__4(Farm farm) {
		Buy buy = new Buy();
		buy.setNum_buy(farm.getNum_buy() / 10);
		buy.setW_farm_chick_id(farm.getId());
		buy.setUser_id(farm.getUser_id());
		buy.setIs_deposit(1);
		buy.setNum_deal(0);

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
	private void saveMaterialRecord__4(Farm farm, User user) {
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
	public Farm getLastByUserId__4(String user_id) {

		Example example = new Example(Farm.class);
		example.setOrderByClause("create_time desc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);

		PageHelper.startPage(1, 1);
		List<Farm> list = selectByExample(example);
		return (null == list || 0 == list.size()) ? null : list.get(0);
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
	private Date getTomorrow__4(Date date) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);
		return sdf.parse(sdf.format(c.getTime()));
	}

	/**
	 * 获取出局时间 20天
	 *
	 * @param date
	 * @return
	 */
	private Date getTimeOut__4(Date date) {
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
	private Date getTimeRipe__4(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, 24 * 7);
		return c.getTime();
	}

	@Override
	public Farm getFeedById__2(String farm_id, String user_id) {

		Farm farm = selectByKey(farm_id);

		if (null == farm) {
			return null;
		}

		if (!user_id.equals(farm.getUser_id())) {
			return null;
		}

		// 孵化记录
		farm.setFarmHatchs(farmHatchService.findByFarmId___4(farm.getId()));

		// 喂食记录
		farm.setFarmFeeds(farmFeedService.findByFarmId___4(farm.getId()));

		if (null != farm.getFarmFeeds()) {
			// 最后一次喂食记录
			if (0 < farm.getFarmFeeds().size()) {
				farm.setLastFarmFeed(farm.getFarmFeeds().get(0));
			}
		}

		return farm;
	}

	@Override
	public List<Farm> findFeedByUserId__3(String user_id) {

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
		}

		for (int i = 0, j = list.size(); i < j; i++) {
			Farm farm = list.get(i);
			farm.setFarmFeeds(farmFeedService.findByFarmId___4(farm.getId()));
		}

		return list;
	}

	@Override
	public List<Farm> findHatchByUserId(String user_id) {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("user_id", user_id);
		map.put("num_current", 0);

		List<Farm> list = ((FarmMapper) getMapper()).findHatchByUserId(map);

		if (null == list) {
			return null;
		}

		for (int i = 0, j = list.size(); i < j; i++) {
			Farm farm = list.get(i);
			farm.setFarmHatchs(farmHatchService.findByFarmId___4(farm.getId()));
		}

		return list;
	}

	@Override
	public List<Farm> findUnDealByUserId___4(String user_id) {

		// Example example = new Example(Farm.class);
		// example.setOrderByClause("create_time desc");
		//
		// // 24小时之内的买盘可以查看
		// Calendar c = Calendar.getInstance();
		// c.add(Calendar.HOUR_OF_DAY, -24);
		//
		// example.or().andEqualTo("user_id", user_id)
		// .andGreaterThan("time_deal", c.getTime());
		// example.or().andEqualTo("user_id", user_id).andIsNull("time_deal");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		List<Farm> list = ((FarmMapper) getMapper()).findUnDealByUserId(map);

		if (null == list) {
			return null;
		}

		for (int i = 0, j = list.size(); i < j; i++) {
			Farm farm = list.get(i);
			farm.setBuys(buyService.findByFarmId__4(farm.getId()));
		}

		return list;
	}

	@Override
	public void updateNum_deal(String id, int num_deal) {
		Farm farm = new Farm();
		farm.setId(id);
		farm.setNum_deal(num_deal);
		((FarmMapper) getMapper()).updateNum_deal(farm);
	}

	@Override
	public Farm getFeedByFarmId(String id, String user_id) {

		Farm farm = selectByKey(id);

		if (null == farm) {
			return null;
		}

		// 判断是否是我的排单
		if (!user_id.equals(farm.getUser_id())) {
			return null;
		}

		// 获取鸡苗批次的喂鸡记录（全部）
		farm.setFarmFeeds(farmFeedService.findByFarmId___4(id));

		List<FarmFeed> list = farm.getFarmFeeds();

		if (null != list) {
			if (0 < list.size()) {
				// 获取最后一次的喂鸡记录
				farm.setFarmFeed(list.get(0));
			}
		}

		return null;
	}

	@Override
	public Farm getHatchByFarmId(String id, String user_id) {

		Farm farm = selectByKey(id);

		if (null == farm) {
			return null;
		}

		// 判断是否是我的排单
		if (!user_id.equals(farm.getUser_id())) {
			return null;
		}

		// 获取鸡苗批次的孵化记录（全部）
		farm.setFarmHatchs(farmHatchService.findByFarmId___4(id));

		List<FarmHatch> list = farm.getFarmHatchs();

		if (null != list) {
			if (0 < list.size()) {
				// 获取最后一次的孵化记录
				farm.setFarmHatch(list.get(0));
			}
		}

		return null;
	}

	@Override
	public List<Farm> feedMo_list___4(String user_id) {

		Example example = new Example(Farm.class);
		example.setOrderByClause("create_time asc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);
		criteria.andGreaterThan("num_current", 0);
		// 当前时间 <-- 出局时间
		criteria.andGreaterThan("time_out", new Date());

		List<Farm> list = selectByExample(example);

		// if (null == list) {
		// return null;
		// }
		//
		// for (int i = 0, j = list.size(); i < j; i++) {
		// Farm farm = list.get(i);
		// farm.setFarmFeeds(farmFeedService.findByFarmId__1(farm.getId()));
		// }

		return list;
	}

	@Override
	public Farm feedMo_farm_feed_list___4(String id, String user_id) {

		Farm farm = selectByKey(id);

		if (null == farm) {
			return null;
		}

		if (!user_id.equals(farm.getUser_id())) {
			return null;
		}

		// 喂食记录
		farm.setFarmFeeds(farmFeedService.findByFarmId___4(farm.getId()));

		List<FarmFeed> list = farm.getFarmFeeds();

		if (null != list) {
			if (0 < list.size()) {
				// 最后一次的喂鸡时间
				farm.setLastFarmFeed(list.get(0));
			}
		}

		return farm;
	}

	@Override
	public List<Farm> hatchMo_list__4(String user_id) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("num_current", 0);

		List<Farm> list = ((FarmMapper) getMapper()).findHatchByUserId(map);

		// if (null == list) {
		// return null;
		// }
		//
		// for (int i = 0, j = list.size(); i < j; i++) {
		// Farm farm = list.get(i);
		// farm.setFarmHatchs(farmHatchService.findByFarmId___4(farm.getId()));
		// }

		return list;
	}

	@Override
	public Farm hatchMo_farm_hatch_list___4(String id, String user_id) {

		Farm farm = selectByKey(id);

		if (null == farm) {
			return null;
		}

		if (!user_id.equals(farm.getUser_id())) {
			return null;
		}

		// 孵化记录
		farm.setFarmHatchs(farmHatchService.findByFarmId___4(farm.getId()));

		return farm;
	}

}
