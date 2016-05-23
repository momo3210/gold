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
import com.momohelp.model.BuySell;
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

	@Override
	public int save(Sell entity) {
		entity.setId(genId());
		return super.save(entity);
	}

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
			} // id
			id = "S" + id;
			sell = selectByKey(id);
		} while (null != sell);
		return id;
	}

	/**
	 * 营业时间 1点 - 23点
	 *
	 * @param date
	 * @return
	 */
	private String[] checkOfficeHours(Date date) {
		Calendar c_1 = Calendar.getInstance();
		c_1.setTime(date);
		c_1.set(Calendar.HOUR_OF_DAY, 1);

		Calendar c_23 = Calendar.getInstance();
		c_23.setTime(date);
		c_23.set(Calendar.HOUR_OF_DAY, 23);

		return (date.after(c_1.getTime()) && date.before(c_23.getTime())) ? null
				: new String[] { "交易时间：凌晨01点至午夜23点" };
	}

	/**
	 * 卖出鸡苗参数验证
	 *
	 * @param sell
	 * @return
	 */
	private Map<String, Object> sell_validationParameter(Sell sell) {

		Map<String, Object> result = new HashMap<String, Object>();

		switch (sell.getType_id()) {
		case 1:
		case 2:
			break;
		default:
			result.put("msg", new String[] { "非法操作" });
			return result;
		} // switch

		sell.setNum_sell((null == sell.getNum_sell()) ? 0 : sell.getNum_sell());
		if (1 > sell.getNum_sell()) {
			result.put("msg", new String[] { "卖出鸡苗数量必须大于0" });
			return result;
		} // if

		if (0 != (sell.getNum_sell() % ((1 == sell.getType_id()) ? 10 : 500))) {
			result.put("msg", new String[] { "请输入规定的数量" });
			return result;
		} // if

		String[] checkOfficeHours = checkOfficeHours(sell.getCreate_time());
		if (null != checkOfficeHours) {
			result.put("msg", checkOfficeHours);
			return result;
		} // if

		if (2 == sell.getType_id()) {
			if (5000 < sell.getNum_sell()) {
				result.put("msg", new String[] { "动态钱包上限不能大于 5000" });
				return result;
			} // if
		} // if

		result.put("data", sell);
		return result;
	}

	/**
	 * 卖出鸡苗的各种验证（操作数据库相关的查询）
	 *
	 * @param sell
	 * @param user
	 * @return
	 */
	private Map<String, Object> sell_validation(Sell sell, User user) {

		Map<String, Object> result = new HashMap<String, Object>();

		if (sell.getNum_sell() > ((1 == sell.getType_id()) ? user
				.getNum_static() : user.getNum_dynamic())) {
			result.put("msg", new String[] { (1 == sell.getType_id() ? "静"
					: "动") + "态钱包余额不足" });
		} // if

		String[] checkTodaySell = checkTodaySell(sell, user);
		if (null != checkTodaySell) {
			result.put("msg", checkTodaySell);
			return result;
		} // if

		String[] checkMonthCeiling = checkMonthCeiling(sell, user);
		if (null != checkMonthCeiling) {
			result.put("msg", checkMonthCeiling);
			return result;
		} // if

		result.put("data", sell);
		return result;
	}

	/**
	 * 步骤
	 *
	 * 1、验证购买的数量是否是10的倍数
	 *
	 * 2、根据动静调用不同的方法
	 *
	 * 3、判断用户的存款是否够卖
	 *
	 * 4、1点到23点可以卖，其他时间都不能卖
	 *
	 * 5、动态钱包每月上限 6 万，动态钱包上限 20 万（假设）
	 *
	 * 6、卖出标记（出局前排单24小时，出局后48小时）
	 */
	@Override
	public String[] sell(Sell sell) {
		sell.setCreate_time(new Date());

		Map<String, Object> sell_validationParameter = sell_validationParameter(sell);
		if (sell_validationParameter.containsKey("msg")) {
			return (String[]) sell_validationParameter.get("msg");
		} // if
		sell = (Sell) sell_validationParameter.get("data");

		// 用户实时信息
		User user = userService.getId(1, sell.getUser_id());

		Map<String, Object> sell_validation = sell_validation(sell, user);
		if (sell_validation.containsKey("msg")) {
			return (String[]) sell_validation.get("msg");
		} // if
		sell = (Sell) sell_validation.get("data");

		sell.setTime_deal(null);

		saveMaterialRecord(sell, user);
		save(sell);
		return null;
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
	private String[] checkMonthCeiling(Sell sell, User user) {

		if (null == user.getMonthSells()) {
			return new String[] { "数据查询异常" };
		} // if

		if (12 == user.getMonthSells().size()) {
			return new String[] { "每月卖出鸡苗上限不能超过12次" };
		} // if

		// 每月卖出的静态总数、动态总数
		int num_static = 0, num_dynamic = 0;

		for (int i = 0, j = user.getMonthSells().size(); i < j; i++) {
			Sell item = user.getMonthSells().get(i);

			if (1 == item.getType_id()) {
				num_static += item.getNum_sell();
			} else {
				num_dynamic += item.getNum_sell();
			} // if
		} // for

		if (60000 < (num_dynamic + sell.getNum_sell())) {
			return new String[] { "动态钱包每月卖出不能超过60000" };
		} // if

		return null;
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
	private String[] checkTodaySell(Sell sell, User user) {
		Sell lastSell = user.getLastSell();
		if (null == lastSell) {
			return null;
		}

		// 取当前的时间
		String date_1 = sdf.format(sell.getCreate_time());
		// 取最后一次卖盘的创建时间
		String date_2 = sdf.format(lastSell.getCreate_time());

		return (date_1.equals(date_2)) ? new String[] { "今天已经卖出过鸡苗了" } : null;
	}

	/**
	 * 存入记录并更新用户的动态余额
	 *
	 * @param sell
	 * @param user
	 */
	private void saveMaterialRecord(Sell sell, User user) {
		// 添加操作记录
		MaterialRecord materialRecord = new MaterialRecord();
		materialRecord.setUser_id(sell.getUser_id());

		materialRecord.setNum_use(Double.valueOf(sell.getNum_sell()));

		materialRecord.setStatus(1);
		materialRecord.setType_id(sell.getType_id() + 2);

		materialRecord.setComment("卖出鸡苗 -" + materialRecord.getNum_use());

		materialRecord.setTrans_user_id(null);

		materialRecord.setNum_balance(((1 == sell.getType_id()) ? user
				.getNum_static() : user.getNum_dynamic())
				- materialRecord.getNum_use());

		materialRecord.setFlag_plus_minus(0);
		materialRecordService.save(materialRecord);

		// 更新用户信息表
		User _user = new User();
		_user.setId(user.getId());
		if (1 == sell.getType_id()) {
			_user.setNum_static(materialRecord.getNum_balance());
		} else {
			_user.setNum_dynamic(materialRecord.getNum_balance());
		} // if
		userService.updateNotNull(_user);
	}

	@Override
	public List<Sell> findUnFinishDeal(String user_id) {
		Example example = new Example(Sell.class);
		example.setOrderByClause("create_time desc");

		// 显示24小时内的
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, -24);

		example.or().andEqualTo("user_id", user_id)
				.andGreaterThan("time_deal", c.getTime());
		example.or().andEqualTo("user_id", user_id).andIsNull("time_deal");

		List<Sell> list_sell = selectByExample(example);

		if (null == list_sell) {
			return list_sell;
		}

		for (int i = 0, j = list_sell.size(); i < j; i++) {
			Sell sell = list_sell.get(i);
			sell.setBuySells(buySellService.findBySellId(sell.getId()));

			List<BuySell> list_buySell = sell.getBuySells();
			if (null == list_buySell) {
				continue;
			}

			// 获取用户对象和其父对象
			for (int m = 0, n = list_buySell.size(); m < n; m++) {
				BuySell buySell = list_buySell.get(m);

				// 买盘
				User buy_user = userService.selectByKey(buySell
						.getP_buy_user_id());
				buySell.setP_buy_user(buy_user);

				if (!"0".equals(buy_user.getPid())) {
					User p_buy_user = userService
							.selectByKey(buy_user.getPid());
					buy_user.setP_user(p_buy_user);
				}
			}
		}

		return list_sell;
	}

	@Override
	public List<Sell> findMonthSellByUserId(String user_id) {

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
	public Sell getLastSellByUserId(String user_id) {

		Example example = new Example(Sell.class);
		example.setOrderByClause("create_time desc");
		// TODO
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);

		PageHelper.startPage(1, 1);
		List<Sell> list = selectByExample(example);
		return (null == list || 0 == list.size()) ? null : list.get(0);
	}

	@Override
	public List<Sell> findByUnDeal_1(String user_id) {

		Example example = new Example(Sell.class);
		example.setOrderByClause("create_time desc");

		// 24小时之内的卖盘可以查看
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, -24);

		example.or().andEqualTo("user_id", user_id)
				.andGreaterThan("time_deal", c.getTime());
		example.or().andEqualTo("user_id", user_id).andIsNull("time_deal");

		List<Sell> list = selectByExample(example);

		if (null == list) {
			return null;
		} // if

		for (int i = 0, j = list.size(); i < j; i++) {
			Sell sell = list.get(i);
			sell.setBuySells(buySellService.findBySellId_1(sell.getId()));
		} // for

		return list;
	}

}
