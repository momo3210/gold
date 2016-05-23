package com.momohelp.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

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

	@Override
	public List<FarmFeed> findByFarmId(String farm_id) {
		Example example = new Example(FarmFeed.class);
		example.setOrderByClause("create_time desc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("w_farm_chick_id", farm_id);

		return selectByExample(example);
	}

	@Autowired
	private UserService userService;

	@Autowired
	private FarmService farmService;

	@Autowired
	private MaterialRecordService materialRecordService;

	@Override
	public double calculateInterest(String farm_id) {
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
			return new String[] { "喂养数量必须大于0" };
		} // if

		// 喂的时候也要喂100的倍数
		if (0 != farmFeed.getNum_feed() % 100) {
			return new String[] { "请输入规定的数量" };
		} // if

		// 实时信息
		Farm farm = farmService.getByFarm(1,
				new Farm(farmFeed.getW_farm_chick_id(), farmFeed.getUser_id()));

		if (null == farm) {
			return new String[] { "数据查询异常" };
		} // if

		// 喂鸡的数量大于当前鸡苗批次的数量
		if (farmFeed.getNum_feed() > farm.getNum_current()) {
			return new String[] { "非法操作" };
		} // if

		// 当前时间 <--出局时间（理论）
		if (farmFeed.getCreate_time().after(farm.getTime_out())) {
			return new String[] { "已经出局了" };
		} // if

		// 该批次的鸡都卖完了
		if (0 == farm.getNum_current()) {
			return new String[] { "没有鸡可以喂了" };
		} // if

		// 买入当天不能喂鸡
		if (sdf.format(farm.getCreate_time()).equals(
				sdf.format(farmFeed.getCreate_time()))) {
			return new String[] { "买入当天不能喂鸡" };
		} // if

		// 判断今天是否已经喂过该批次的鸡苗了
		String[] checkTodayFeed = checkTodayFeed(farm.getLastFarmFeed());
		if (null != checkTodayFeed) {
			return checkTodayFeed;
		} // if

		// 我的实时信息
		User user = userService.selectByKey(farm.getUser_id());

		if ((farmFeed.getNum_feed() / 100) > user.getNum_food()) {
			return new String[] { "饲料不足，请购买饲料" };
		} // if

		saveMaterialRecord(farmFeed, user);

		// 1饲料喂100只鸡
		// 计算利息 START 0.5% or 0.9%
		farmFeed.setPrice(Double.valueOf(farmFeed.getNum_feed())
				* (8 > ((null == farm.getFarmFeeds()) ? 1 : (farm
						.getFarmFeeds().size() + 1)) ? 0.007 : 0.012));

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

		materialRecord.setNum_use(Double.valueOf(farmFeed.getNum_feed() / 100));

		materialRecord.setStatus(1);
		materialRecord.setType_id(2);

		materialRecord.setComment("喂养鸡苗 -" + farmFeed.getNum_feed());

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

	@Override
	public String[] checkTodayFeed(FarmFeed farmFeed) {

		if (null == farmFeed) {
			return null;
		} // if

		// 该批次最后一次喂鸡的时间
		String date_1 = sdf.format(farmFeed.getCreate_time());
		// 当前时间
		String date_2 = sdf.format(new Date());

		return date_1.equals(date_2) ? new String[] { "今天已经喂过鸡了" } : null;
	}
}
