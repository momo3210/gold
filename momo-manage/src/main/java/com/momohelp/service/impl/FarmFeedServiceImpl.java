package com.momohelp.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.model.Farm;
import com.momohelp.model.FarmFeed;
import com.momohelp.model.MaterialRecord;
import com.momohelp.model.User;
import com.momohelp.service.FarmFeedService;
import com.momohelp.service.FarmService;
import com.momohelp.service.MaterialRecordService;
import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Service("farmFeedService")
public class FarmFeedServiceImpl extends BaseService<FarmFeed> implements
		FarmFeedService {

	@Autowired
	private UserService userService;

	@Autowired
	private FarmService farmService;

	@Autowired
	private MaterialRecordService materialRecordService;

	@Override
	public double dividend(String farm_id) {
		Example example = new Example(FarmFeed.class);
		// TODO
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("w_farm_chick_id", farm_id);

		List<FarmFeed> list = selectByExample(example);

		double count = 0.00;

		if (null == list) {
			return count;
		}

		for (int i = 0, j = list.size(); i < j; i++) {
			FarmFeed item = list.get(i);
			count += item.getPrice();
		}

		return count;
	}

	@Override
	public String[] feed(FarmFeed farmFeed) {
		farmFeed.setCreate_time(new Date());

		farmFeed.setNum_feed((null == farmFeed.getNum_feed()) ? 0 : farmFeed
				.getNum_feed());
		if (1 > farmFeed.getNum_feed()) {
			return new String[] { "喂食数量必须大于 0" };
		}

		// 实时信息
		Farm farm = farmService.selectByKey(farmFeed.getW_farm_chick_id());

		if (null == farm) {
			return new String[] { "数据查询异常" };
		}

		if (farm.getUser_id().equals(farmFeed.getUser_id())) {
			return new String[] { "非法操作" };
		}

		if (0 == farm.getNum_current()) {
			return new String[] { "没有鸡可以喂了" };
		}

		// 买入当天不能喂鸡
		String date_1 = sdf.format(farm.getCreate_time());
		String date_2 = sdf.format(farmFeed.getCreate_time());
		if (date_1.equals(date_2)) {
			return new String[] { "买入当天不能喂鸡" };
		}

		// 我的实时信息
		User user = userService.selectByKey(farm.getUser_id());

		if (farmFeed.getNum_feed() > user.getNum_food()) {
			return new String[] { "请购买饲料" };
		}

		// 最后一次喂鸡记录
		FarmFeed last_farmfeed = getLastByFarmId(farmFeed.getW_farm_chick_id());

		String[] checkTodayFeed = checkTodayFeed(last_farmfeed, farmFeed);
		if (null != checkTodayFeed) {
			return checkTodayFeed;
		}

		// 判断是否已经20次了
		if (20 == last_farmfeed.getOrder_feed()) {
			return new String[] { "已经出局了" };
		}

		saveMaterialRecord(farmFeed, user);

		// 计算利息 START 0.5% or 0.9%
		farmFeed.setPrice(Double.valueOf(farmFeed.getNum_feed())
				* (7 > last_farmfeed.getOrder_feed() ? 0.005 : 0.009));

		farmFeed.setOrder_feed((null == last_farmfeed) ? 1 : (1 + last_farmfeed
				.getOrder_feed()));
		save(farmFeed);
		return null;
	}

	/**
	 * 存入记录并更新用户的饲料余额
	 *
	 * @param farmFeed
	 * @param user
	 */
	private void saveMaterialRecord(FarmFeed farmFeed, User user) {
		// 添加操作记录
		MaterialRecord materialRecord = new MaterialRecord();
		materialRecord.setUser_id(farmFeed.getUser_id());

		materialRecord.setNum_use(Double.valueOf(farmFeed.getNum_feed()));

		materialRecord.setStatus(1);
		materialRecord.setType_id(2);

		materialRecord.setComment("喂养鸡苗 -" + materialRecord.getNum_use());

		materialRecord.setTrans_user_id(null);

		materialRecord.setNum_balance(Double.valueOf(user.getNum_food())
				- materialRecord.getNum_use());

		materialRecord.setFlag_plus_minus(0);
		materialRecordService.save(materialRecord);

		// 更新用户信息表
		User _user = new User();
		_user.setId(user.getId());
		_user.setNum_food(materialRecord.getNum_balance().intValue());
		userService.updateNotNull(_user);
	}

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd 00:00:00");

	/**
	 * 判断今天是否已经喂过鸡了
	 *
	 * @param last_farmfeed
	 * @param farmFeed
	 * @return
	 */
	private String[] checkTodayFeed(FarmFeed last_farmfeed, FarmFeed farmFeed) {
		if (null == last_farmfeed) {
			return null;
		}

		String date_1 = sdf.format(farmFeed.getCreate_time());
		String date_2 = sdf.format(last_farmfeed.getCreate_time());

		return (date_1.equals(date_2)) ? new String[] { "今天已经喂过鸡了" } : null;
	}

	/**
	 * 获取用户最后一次的喂鸡记录
	 *
	 * @param farm_id
	 * @return
	 */
	private FarmFeed getLastByFarmId(String farm_id) {
		Example example = new Example(FarmFeed.class);
		example.setOrderByClause("create_time desc");
		// TODO
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("w_farm_chick_id", farm_id);

		PageHelper.startPage(1, 1);
		List<FarmFeed> list = selectByExample(example);
		return (null == list || 0 == list.size()) ? null : list.get(0);
	}
}
