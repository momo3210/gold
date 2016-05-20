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
import com.momohelp.model.Cfg;
import com.momohelp.model.Farm;
import com.momohelp.model.FarmHatch;
import com.momohelp.model.MaterialRecord;
import com.momohelp.model.User;
import com.momohelp.service.BuyService;
import com.momohelp.service.CfgService;
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
	private CfgService cfgService;

	@Autowired
	private MaterialRecordService materialRecordService;

	@Autowired
	private BuyService buyService;

	@Autowired
	private FarmHatchService farmHatchService;

	@Override
	public int save(Farm entity) {
		entity.setFlag_calc_bonus(0);
		entity.setNum_current(entity.getNum_buy());
		entity.setNum_deal(0);
		entity.setNum_reward(0);
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
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DAY_OF_MONTH, 1);
		return sdf.parse(sdf.format(ca.getTime()));
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
		}

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
			result.put("msg", new String[] { "请购买门票" });
			return result;
		}

		String[] checkBuyNum = checkBuyNum(user.getLv(), farm.getNum_buy());
		if (null != checkBuyNum) {
			result.put("msg", checkBuyNum);
			return result;
		}

		// 获取我的最后一单
		Farm my_last_farm = getLastByUserId(farm.getUser_id());

		String[] checkDealDone = checkDealDone(my_last_farm);
		if (null != checkDealDone) {
			result.put("msg", checkDealDone);
			return result;
		}

		farm.setPid((null == my_last_farm) ? "0" : my_last_farm.getId());

		// 当前排单是否接上气儿了
		farm.setFlag_out_self(checkFarmOut(my_last_farm, farm.getCreate_time()));

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

		// TODO
		Map<String, Object> buy_validationParameter = buy_validationParameter(farm);
		if (buy_validationParameter.containsKey("msg")) {
			return (String[]) buy_validationParameter.get("msg");
		}
		farm = (Farm) buy_validationParameter.get("data");

		// 获取我的帐户信息（实时）
		User user = userService.selectByKey(farm.getUser_id());

		// TODO
		Map<String, Object> buy_validation = buy_validation(farm, user);
		if (buy_validation.containsKey("msg")) {
			return (String[]) buy_validation.get("msg");
		}
		farm = (Farm) buy_validation.get("data");

		/***** 整理数据 *****/

		Date tomorrow = null;

		try {
			tomorrow = getTomorrow(farm.getCreate_time());
		} catch (ParseException e) {
			return new String[] { "日期异常" };
		}

		farm.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		farm.setTime_out(getTimeOut(tomorrow));
		farm.setTime_ripe(getTimeRipe(tomorrow));
		farm.setNum_current(farm.getNum_buy());

		farm.setNum_deal(0);
		farm.setFlag_calc_bonus(0);
		farm.setTime_deal(null);

		// 我有父级
		if (!"0".equals(user.getPid())) {
			Farm my_parent_last_farm = getLastByUserId(user.getPid());
			farm.setPid_higher_ups(my_parent_last_farm.getId());
			farm.setFlag_out_p(checkFarmOut(my_parent_last_farm,
					farm.getCreate_time()));
		}

		/***** 开始保存数据 *****/

		saveMaterialRecord(farm, user);
		saveBuy(farm);
		save(farm);
		return null;
	}

	/**
	 * 保存预付款 10%
	 *
	 * @param farm
	 */
	private void saveBuy(Farm farm) {
		Buy buy = new Buy();
		buy.setNum_buy(farm.getNum_buy() / 10);
		buy.setW_farm_chick_id(farm.getId());
		buy.setCreate_time(farm.getCreate_time());
		buy.setUser_id(farm.getUser_id());
		buy.setTime_deal(null);

		Calendar c = Calendar.getInstance();
		c.setTime(farm.getCreate_time());
		c.add(Calendar.DAY_OF_MONTH, 1);
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
	 * 判断排单是否完全交易完成
	 *
	 * @param farm
	 * @return
	 */
	private String[] checkDealDone(Farm farm) {
		if (null == farm) {
			return null;
		}
		return farm.getNum_buy().intValue() == farm.getNum_deal().intValue() ? null
				: new String[] { "有未完成的排单" };
	}

	/**
	 *
	 * 添加操作记录
	 *
	 * w_material_use 添加一条操作记录
	 *
	 * @param farm
	 * @param user
	 */
	private void saveMaterialRecord(Farm farm, User user) {
		MaterialRecord materialRecord = new MaterialRecord();
		materialRecord.setUser_id(farm.getUser_id());

		materialRecord.setNum_use(1.0);
		materialRecord.setStatus(1);
		materialRecord.setType_id(1);

		materialRecord.setComment("买入鸡苗 +" + Double.valueOf(farm.getNum_buy()));

		materialRecord.setTrans_user_id(null);

		materialRecord.setNum_balance(Double.valueOf(user.getNum_ticket() - 1));

		materialRecord.setFlag_plus_minus(0);
		materialRecordService.save(materialRecord);

		// 更新我的门票信息
		User _user = new User();
		_user.setId(user.getId());
		_user.setNum_ticket(materialRecord.getNum_balance().intValue());
		userService.updateNotNull(_user);
	}

	/**
	 * 检测购买的鸡苗数量是否合法
	 *
	 * @param user_lv
	 * @param num_buy
	 * @return
	 */
	private String[] checkBuyNum(String user_lv, int num_buy) {
		// 100 的倍数
		Cfg cfg = cfgService.selectByKey("0106");

		if (0 != num_buy % Integer.valueOf(cfg.getValue_())) {
			return new String[] { "输入的数量不正确" };
		}

		// 获取上下限
		Integer[] range = getRangeByUserLv(user_lv);

		return (range[0] <= num_buy && num_buy <= range[1]) ? null
				: new String[] { "请输入正确的数量范围" };
	}

	/**
	 * 根据用户级别获取可以买入鸡苗的范围
	 *
	 * @param user_lv
	 * @return
	 */
	private Integer[] getRangeByUserLv(String user_lv) {
		String min = null, max = null;

		if ("05".equals(user_lv)) {
			min = "2001";
			max = "2002";
		} else if ("06".equals(user_lv)) {
			min = "2003";
			max = "2004";
		} else if ("07".equals(user_lv)) {
			min = "2005";
			max = "2006";
		} else if ("08".equals(user_lv)) {
			min = "2007";
			max = "2008";
		}

		Cfg minObj = cfgService.selectByKey(min);
		Cfg maxObj = cfgService.selectByKey(max);

		return new Integer[] { Integer.valueOf(minObj.getValue_()),
				Integer.valueOf(maxObj.getValue_()) };
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

	/**
	 *
	 * 判断排单在当前时间是否出局
	 *
	 * 1未出局（接上气儿）
	 *
	 * 2主动出局
	 *
	 * 3自然出局
	 *
	 * @param farm
	 * @param current_time
	 * @return
	 */
	private int checkFarmOut(Farm farm, Date current_time) {
		if (null == farm) {
			return 1;
		}

		List<FarmHatch> list = farmHatchService.findByFarmId(farm.getId());

		if (null == list || 0 < list.size()) {
			return 2;
		}

		if (current_time.after(farm.getTime_out())) {
			return 3;
		}

		return 1;
	}

	@Override
	public List<Farm> findCanFeed(String user_id) {
		Example example = new Example(Farm.class);
		example.setOrderByClause("create_time asc");
		// TODO
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);
		criteria.andGreaterThan("num_current", 0);
		criteria.andLessThan("time_out", new Date());

		List<Farm> list = selectByExample(example);
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

}
