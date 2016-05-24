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

	@Autowired
	private UserService userService;

	@Autowired
	private FarmService farmService;

	@Autowired
	private MaterialRecordService materialRecordService;

	public int updateNotNull(FarmFeed entity) {
		entity.setId(null);
		return super.updateNotNull(entity);
	}

	@Override
	public String[] feed(FarmFeed farmFeed) {

		FarmFeed _farmFeed = new FarmFeed();
		_farmFeed.setUser_id(farmFeed.getUser_id());
		_farmFeed.setCreate_time(new Date());
		_farmFeed.setNum_feed((null == farmFeed.getNum_feed()) ? 0 : farmFeed
				.getNum_feed());
		_farmFeed.setW_farm_chick_id(farmFeed.getW_farm_chick_id());

		/**/

		if (1 > _farmFeed.getNum_feed()) {
			return new String[] { "喂养数量必须大于0" };
		}

		// 100的倍数
		if (0 != _farmFeed.getNum_feed() % 100) {
			return new String[] { "请输入规定的数量" };
		}

		/**/

		// 实时
		Farm farm = farmService.getFeedById__2(_farmFeed.getW_farm_chick_id(),
				_farmFeed.getUser_id());

		if (null == farm) {
			return new String[] { "数据查询异常" };
		}

		/**/

		// 喂鸡的数量大于当前鸡苗批次的数量
		if (_farmFeed.getNum_feed() > farm.getNum_current()) {
			return new String[] { "非法操作" };
		}

		// 当前时间 <--出局时间（理论）
		if (_farmFeed.getCreate_time().after(farm.getTime_out())) {
			return new String[] { "已经出局了" };
		}

		// 该批次的鸡都卖完了
		if (0 == farm.getNum_current()) {
			return new String[] { "没有鸡可以喂了" };
		}

		// 买入当天不能喂鸡
		if (sdf.format(farm.getCreate_time()).equals(
				sdf.format(_farmFeed.getCreate_time()))) {
			return new String[] { "买入当天不能喂鸡" };
		}

		// 判断今天是否已经喂过该批次的鸡苗了
		String[] checkTodayFeed = checkTodayFeed__1(farm.getLastFarmFeed());
		if (null != checkTodayFeed) {
			return checkTodayFeed;
		}

		/**/

		// 我的实时信息
		User user = userService.selectByKey(_farmFeed.getUser_id());

		if ((_farmFeed.getNum_feed() / 100) > user.getNum_food()) {
			return new String[] { "饲料不足，请购买饲料" };
		}

		// 1饲料喂100只鸡
		// 计算利息 START 0.5% or 0.9%
		_farmFeed.setPrice(Double.valueOf(_farmFeed.getNum_feed())
				* (8 > ((null == farm.getFarmFeeds()) ? 1 : (1 + farm
						.getFarmFeeds().size())) ? 0.007 : 0.012));

		saveMaterialRecord(_farmFeed, user);
		save(_farmFeed);
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

		materialRecord.setComment("喂食鸡苗 -" + farmFeed.getNum_feed());

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
	public String[] checkTodayFeed__1(FarmFeed farmFeed) {

		if (null == farmFeed) {
			return null;
		}

		// 该批次最后一次喂鸡的时间
		String date_1 = sdf.format(farmFeed.getCreate_time());
		// 当前时间
		String date_2 = sdf.format(new Date());

		return date_1.equals(date_2) ? new String[] { "今天已经喂过鸡了" } : null;
	}

	@Override
	public List<FarmFeed> findByFarmId__1(String farm_id) {

		Example example = new Example(FarmFeed.class);
		example.setOrderByClause("create_time desc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("w_farm_chick_id", farm_id);

		return selectByExample(example);
	}

}
