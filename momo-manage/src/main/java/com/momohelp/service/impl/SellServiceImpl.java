package com.momohelp.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.mapper.SellMapper;
import com.momohelp.model.MaterialRecord;
import com.momohelp.model.Sell;
import com.momohelp.model.User;
import com.momohelp.service.BuySellService;
import com.momohelp.service.MaterialRecordService;
import com.momohelp.service.SellService;
import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Service("sellService")
public class SellServiceImpl extends BaseService<Sell> implements SellService {

	@Autowired
	private UserService userService;

	@Autowired
	private MaterialRecordService materialRecordService;

	@Autowired
	private BuySellService buySellService;

	/**
	 * 生成主键
	 *
	 * @return
	 */
	private String genId() {
		String id = null;
		Sell sell = null;
		do {
			// 算法
			int i = (int) ((Math.random() * 10 + 1) * 100000000);
			id = String.valueOf(i);
			if (9 < id.length()) {
				id = id.substring(0, 9);
			}
			id = "S" + id;
			sell = selectByKey(id);
		} while (null != sell);
		return id;
	}

	@Override
	public List<Sell> findMonthSellByUserId____4(String user_id) {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		// 查询用户当月从2016-05-01到现在的卖盘列表
		Example example = new Example(Sell.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andGreaterThan("create_time", c.getTime());
		criteria.andEqualTo("user_id", user_id);

		return selectByExample(example);
	}

	@Override
	public Sell getLastByUserId___4(String user_id) {

		Example example = new Example(Sell.class);
		example.setOrderByClause("create_time desc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);

		PageHelper.startPage(1, 1);
		List<Sell> list = selectByExample(example);
		return (null == list || 0 == list.size()) ? null : list.get(0);
	}

	@Override
	public String[] sell(Sell sell) {

		switch (sell.getType_id()) {
		case 1:
		case 2:
			break;
		default:
			return new String[] { "非法操作" };
		}

		/**/

		Sell _sell = new Sell();
		_sell.setNum_sell((null == sell.getNum_sell()) ? 0 : sell.getNum_sell());
		_sell.setCreate_time(new Date());
		_sell.setUser_id(sell.getUser_id());
		_sell.setType_id(sell.getType_id());
		_sell.setNum_deal(0);

		/**/

		if (1 > _sell.getNum_sell()) {
			return new String[] { "卖出鸡苗数量必须大于0" };
		}

		if (0 != (_sell.getNum_sell() % ((1 == _sell.getType_id()) ? 10 : 500))) {
			return new String[] { "请输入规定的数量" };
		}

		String[] checkDealTime = checkDealTime___4();
		if (null != checkDealTime) {
			return checkDealTime;
		}

		if (2 == _sell.getType_id()) {
			if (5000 < _sell.getNum_sell()) {
				return new String[] { "动态钱包上限不能大于 5000" };
			}
		}

		/**/

		// 实时
		User user = userService.sellTime___4(_sell.getUser_id());

		if (null == user) {
			return new String[] { "非法操作" };
		}

		/**/

		if (_sell.getNum_sell() > ((1 == _sell.getType_id()) ? user
				.getNum_static() : user.getNum_dynamic())) {
			return new String[] { (1 == _sell.getType_id() ? "静" : "动")
					+ "态钱包余额不足" };
		}

		String[] checkTodaySell = checkTodaySell____4(_sell, user.getLastSell());
		if (null != checkTodaySell) {
			return checkTodaySell;
		}

		String[] checkMonthCeiling = checkMonthCeiling___4(_sell,
				user.getMonthSells());
		if (null != checkMonthCeiling) {
			return checkMonthCeiling;
		}

		saveMaterialRecord___4(_sell, user);
		_sell.setId(genId());
		save(_sell);
		return null;
	}

	/**
	 * 存入记录并更新用户的余额
	 *
	 * @param sell
	 * @param user
	 */
	private void saveMaterialRecord___4(Sell sell, User user) {
		// 添加操作记录
		MaterialRecord mr = new MaterialRecord();
		mr.setUser_id(sell.getUser_id());

		mr.setNum_use(Double.valueOf(sell.getNum_sell()));

		mr.setStatus(1);
		mr.setType_id(sell.getType_id() + 2);

		mr.setComment("卖出鸡苗 -" + mr.getNum_use());

		mr.setTrans_user_id(null);

		mr.setNum_balance(((1 == sell.getType_id()) ? user.getNum_static()
				: user.getNum_dynamic()) - mr.getNum_use());

		mr.setFlag_plus_minus(0);
		materialRecordService.save(mr);

		// 更新用户信息表
		User _user = new User();
		_user.setId(user.getId());
		if (1 == sell.getType_id()) {
			_user.setNum_static(mr.getNum_balance());
		} else {
			_user.setNum_dynamic(mr.getNum_balance());
		}
		userService.updateNotNull(_user);
	}

	/**
	 *
	 * 判断每月的卖出上限不能超过12次
	 *
	 * 动态钱包每月上限6万，动态钱包上限度20万（假设）
	 *
	 * @param sell
	 * @param user
	 * @return
	 */
	private String[] checkMonthCeiling___4(Sell sell, List<Sell> monthSells) {

		if (null == monthSells) {
			return new String[] { "数据查询异常" };
		}

		if (12 == monthSells.size()) {
			return new String[] { "每月卖出鸡苗上限不能超过12次" };
		}

		// 每月卖出的静态总数、动态总数
		int num_static = 0, num_dynamic = 0;

		for (int i = 0, j = monthSells.size(); i < j; i++) {
			Sell item = monthSells.get(i);

			if (1 == item.getType_id()) {
				num_static += item.getNum_sell();
			} else {
				num_dynamic += item.getNum_sell();
			}
		}

		if (60000 < (num_dynamic + sell.getNum_sell())) {
			return new String[] { "动态钱包每月卖出不能超过60000" };
		}

		return null;
	}

	/**
	 * 交易时间
	 *
	 * @return
	 */
	private String[] checkDealTime___4() {
		Date date = new Date();

		Calendar c_1 = Calendar.getInstance();
		c_1.setTime(date);
		c_1.set(Calendar.HOUR_OF_DAY, 1);

		Calendar c_23 = Calendar.getInstance();
		c_23.setTime(date);
		c_23.set(Calendar.HOUR_OF_DAY, 23);

		return (date.after(c_1.getTime()) && date.before(c_23.getTime())) ? null
				: new String[] { "交易时间：凌晨01点至午夜23点" };
	}

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd 00:00:00");

	/**
	 *
	 * 判断今天是否已经卖出过鸡苗了（不管是动态还是静态）
	 *
	 * 比较最后一次的卖出记录（年月日）和当前（年月日）进行比对
	 *
	 * @param sell
	 * @param user
	 * @return
	 */
	private String[] checkTodaySell____4(Sell sell, Sell last_sell) {
		if (null == last_sell) {
			return null;
		}

		// 取当前的时间
		String date_1 = sdf.format(sell.getCreate_time());
		// 取最后一次卖盘的创建时间
		String date_2 = sdf.format(last_sell.getCreate_time());

		return (date_1.equals(date_2)) ? new String[] { "今天已经卖出过鸡苗了" } : null;
	}

	@Override
	public List<Sell> findUnDealByUserId__4(String user_id) {

		// Example example = new Example(Sell.class);
		// example.setOrderByClause("create_time desc");
		//
		// // 24小时之内的卖盘可以查看
		// Calendar c = Calendar.getInstance();
		// c.add(Calendar.HOUR_OF_DAY, -24);
		//
		// example.or().andEqualTo("user_id", user_id)
		// .andGreaterThan("time_deal", c.getTime());
		// example.or().andEqualTo("user_id", user_id).andIsNull("time_deal");
		//
		// List<Sell> list = selectByExample(example);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		List<Sell> list = ((SellMapper) getMapper()).findUnDealByUserId(map);

		if (null == list) {
			return null;
		}

		for (int i = 0, j = list.size(); i < j; i++) {
			Sell sell = list.get(i);
			sell.setBuySells(buySellService.findBySellId__4(sell.getId()));
		}

		return list;
	}

	@Override
	public void updateNum_deal___4(String id, int num_deal) {
		Sell sell = new Sell();
		sell.setId(id);
		sell.setNum_deal(num_deal);
		((SellMapper) getMapper()).updateNum_deal(sell);
	}

	@Override
	public List<Sell> findBySell__4(Sell sell, int page, int rows) {

		Example example = new Example(Sell.class);
		example.setOrderByClause("create_time desc");

		PageHelper.startPage(page, rows);
		return selectByExample(example);
	}
}
