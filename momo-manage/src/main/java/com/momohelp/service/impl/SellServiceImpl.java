package com.momohelp.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.momohelp.model.MaterialRecord;
import com.momohelp.model.Sell;
import com.momohelp.model.User;
import com.momohelp.service.CfgService;
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
	private CfgService cfgService;

	@Autowired
	private MaterialRecordService materialRecordService;

	@Override
	public int save(Sell entity) {
		entity.setCreate_time(new Date());
		entity.setId(genId());
		return super.save(entity);
	}

	@Override
	public int updateNotNull(Sell entity) {
		entity.setId(null);
		return super.updateNotNull(entity);
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
			// END
			sell = selectByKey(id);
		} while (null != sell);
		return id;
	}

	/**
	 * 营业时间 1点 - 23点
	 *
	 * @return
	 */
	private String[] checkShopHour() {
		Calendar ca_1 = Calendar.getInstance();
		ca_1.set(Calendar.HOUR_OF_DAY, 1);

		Calendar ca_23 = Calendar.getInstance();
		ca_23.set(Calendar.HOUR_OF_DAY, 23);

		// 当前时间
		Date date = new Date();

		return (date.after(ca_1.getTime()) && date.before(ca_23.getTime())) ? null
				: new String[] { "营业时间 1点 - 23点" };
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
		switch (sell.getType_id()) {
		case 1:
		case 2:
			break;
		default:
			return new String[] { "非法操作" };
		}

		String[] checkShopHours = checkShopHour();
		if (null != checkShopHours) {
			return checkShopHours;
		}

		sell.setNum_sell((null == sell.getNum_sell()) ? 0 : sell.getNum_sell());
		if (1 > sell.getNum_sell()) {
			return new String[] { "卖出鸡苗数量不能为 0" };
		}

		if (2 == sell.getType_id()) {
			if (5000 < sell.getNum_sell()) {
				return new String[] { "动态钱包金额上限不能大于 5000" };
			}
		}

		String[] checkCeilingMonth = checkCeilingMonth(sell.getUser_id(), sell);
		if (null != checkCeilingMonth) {
			return checkCeilingMonth;
		}

		// 获取我的帐户信息（实时）
		User user = userService.selectByKey(sell.getUser_id());

		String[] checkNum = checkNum(user, sell);
		if (null != checkNum) {
			return checkNum;
		}

		String[] checkTodaySell = checkTodaySell(user);
		if (null != checkTodaySell) {
			return checkTodaySell;
		}

		if (1 == sell.getType_id()) {
			return sell_static(user, sell);
		} else { // 动态钱包
			return sell_dynamic(user, sell);
		}
	}

	/**
	 *
	 * 判断每月的卖出上限不能超过12次
	 *
	 * 动态钱包每月上限6万，动态钱包上限度20万（假设）
	 *
	 * @param user_id
	 * @param sell
	 * @return
	 */
	private String[] checkCeilingMonth(String user_id, Sell sell) {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, 1);
		Date first_day = ca.getTime();

		// TODO
		Example example = new Example(Sell.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andGreaterThan("create_time", first_day);
		criteria.andEqualTo("user_id", user_id);
		List<Sell> list = selectByExample(example);

		if (null == list) {
			return new String[] { "读取数据异常" };
		}

		if (12 == list.size()) {
			return new String[] { "每月卖出鸡苗上线不能超过 12 次" };
		}

		// 汇总每月的静态、动态
		int num_static = 0, num_dynamic = 0;

		for (int i = 0, j = list.size(); i < j; i++) {
			Sell item = list.get(i);
			if (1 == item.getType_id()) {
				num_static += item.getNum_sell();
			} else {
				num_dynamic += item.getNum_sell();
			}
		}

		if (60000 < (num_dynamic + sell.getNum_sell())) {
			return new String[] { "动态钱包每月不能超过 60000" };
		}

		return null;
	}

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * 判断今天是否已经卖出过鸡苗了（不管是动态还是静态）
	 *
	 * 比较最后一次的卖出记录（年月日）和当前（年月日）进行比对
	 *
	 * @param user
	 * @return
	 */
	private String[] checkTodaySell(User user) {
		Sell sell = findLast(user.getId());
		if (null == sell) {
			return null;
		}

		String date_1 = sdf.format(new Date());
		String date_2 = sdf.format(sell.getCreate_time());

		return (date_1.equals(date_2)) ? new String[] { "今天已经卖出过鸡苗了" } : null;
	}

	/**
	 * 卖出动态钱包
	 *
	 * @param user
	 * @param sell
	 * @return
	 */
	private String[] sell_dynamic(User user, Sell sell) {
		if (sell.getNum_sell() > user.getNum_dynamic()) {
			return new String[] { "动态钱包余额不足" };
		}

		sell_dynamic_material(user, sell);
		save(sell);
		return null;
	}

	/**
	 * 存入记录并更新用户的动态余额
	 *
	 * @param user
	 * @param sell
	 */
	private void sell_dynamic_material(User user, Sell sell) {
		MaterialRecord mr = new MaterialRecord();
		mr.setUser_id(user.getId());
		double d = sell.getNum_sell();
		mr.setNum_use(d);
		mr.setStatus(1);
		mr.setType_id(4);
		mr.setComment(null);
		mr.setTrans_user_id(null);
		mr.setNum_balance(user.getNum_dynamic() - sell.getNum_sell());
		mr.setFlag_plus_minus(0);

		materialRecordService.save(mr);

		User new_user = new User();
		new_user.setId(user.getId());
		new_user.setNum_dynamic(mr.getNum_balance());
		userService.updateNotNull(new_user);
	}

	/**
	 * 卖出静态钱包
	 *
	 * @param user
	 * @param sell
	 * @return
	 */
	private String[] sell_static(User user, Sell sell) {
		if (sell.getNum_sell() > user.getNum_static()) {
			return new String[] { "静态钱包余额不足" };
		}
		return null;
	}

	/**
	 * 检测购买的鸡苗数量是否合法
	 *
	 * @param user
	 * @param sell
	 * @return
	 */
	private String[] checkNum(User user, Sell sell) {
		// 静态10倍 静态500
		int radix = (1 == sell.getType_id()) ? 10 : 500;
		return (0 == sell.getNum_sell() % Integer.valueOf(radix)) ? null
				: new String[] { "输入的数量不正确" };
	}

	/**
	 * 获取用户最后一次的卖出记录
	 *
	 * @param user_id
	 * @return
	 */
	private Sell findLast(String user_id) {
		Example example = new Example(Sell.class);
		example.setOrderByClause("create_time desc");
		// TODO
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);

		PageHelper.startPage(1, 1);
		List<Sell> list = selectByExample(example);
		return (null == list || 0 == list.size()) ? null : list.get(0);
	}

}
