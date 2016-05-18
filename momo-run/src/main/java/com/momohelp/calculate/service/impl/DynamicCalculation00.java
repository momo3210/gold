package com.momohelp.calculate.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.calculate.service.ICalculation;
import com.momohelp.model.Buy;
import com.momohelp.model.BuySell;
import com.momohelp.model.Cfg;
import com.momohelp.model.Farm;
import com.momohelp.model.ModelLV;
import com.momohelp.model.Prize;
import com.momohelp.model.Sell;
import com.momohelp.model.User;
import com.momohelp.service.BuySellService;
import com.momohelp.service.BuyService;
import com.momohelp.service.CfgService;
import com.momohelp.service.FarmService;
import com.momohelp.service.PrizeService;
import com.momohelp.service.SellService;
import com.momohelp.service.UserService;

//动态奖金计算
@Component
public class DynamicCalculation00 implements Serializable, ICalculation {

	private static final long serialVersionUID = -817967291317652692L;
	private static Logger log = Logger.getLogger(DynamicCalculation00.class);
	@Resource
	private CfgService cfgService;
	@Resource
	private FarmService farmService;
	@Resource
	private UserService userService;

	@Resource
	private BuyService buyService;

	@Resource
	private SellService sellService;

	@Resource
	private BuySellService buySellService;

	@Resource
	private PrizeService prizeService;

	// 自动转换到期奖金金额
	public boolean triggerConversion() {
		Example example = new Example(Prize.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("flag", 0);
		Calendar cr = Calendar.getInstance();
		cr.add(Calendar.DAY_OF_MONTH, -1);
		criteria.andGreaterThan("trigger_time", cr.getTime());
		criteria.andLessThan("trigger_time", Calendar.getInstance().getTime());
		List<Prize> prizes = prizeService.selectByExample(example);
		User userTemp = null;
		for (Prize prize : prizes) {
			userTemp = userService.selectByKey(prize.getUser_id());
			userTemp.setNum_dynamic(userTemp.getNum_dynamic()
					+ prize.getMoney());
			userTemp.setTotal_dynamic(userTemp.getTotal_dynamic()
					+ prize.getMoney());
			userService.updateNotNull(userTemp);
			prize.setFlag(1);
			prizeService.updateNotNull(prize);
		}
		return true;
	}

	// 用户强制出局计算
	@Override
	public boolean calculateForceLogout() {
		Calendar cr = Calendar.getInstance();
		cr.add(Calendar.DAY_OF_MONTH, -2);
		List<Farm> farms = farmService.selectForceLogout(cr.getTime());
		for (Farm farm : farms) {
			//farm.setFlag_out(3);
			farm.setFlag_out_p(3);
			farmService.updateNotNull(farm);
		}
		return true;
	}

	// 用户等级计算
	@Override
	public boolean calculateLevel(List<Farm> farms) {
		boolean bool = false;
		for (Farm farm : farms) {
			User user = userService.selectByKey(farm.getUser_id());// 当前用户id
			if (user == null) {
				break;
			}
			String pid = user.getPid();// 获取当前用户的领导
			List<ModelLV> childsList = (List<ModelLV>) userService
					.countMemberNOAndlevel(pid);
			/**
			 * level 级别（05贫农、06中农、07富农、08农场主）
			 */
			List<String> list = new ArrayList<String>(8);
			list.add(Parameter.POOR_PEASANT);
			for (ModelLV modelLV : childsList) {
				// System.err.println(modelLV.getLv() + "---" +
				// modelLV.getNo());
				switch (modelLV.getLv()) {
				case Parameter.POOR_PEASANT:
					if (modelLV.getNo() >= 4
							&& userService.countLvNO(pid,
									Parameter.POOR_PEASANT) >= 4) {
						list.add(Parameter.MIDDLE_PEASANT);
					}
					break;
				case Parameter.MIDDLE_PEASANT:
					if (modelLV.getNo() >= 8) {
						list.add(Parameter.RICH_PEASANT);
					}
					break;
				case Parameter.RICH_PEASANT:
					if (modelLV.getNo() >= 12) {
						list.add(Parameter.FARMERS);
					}
					break;
				case Parameter.FARMERS:
					if (modelLV.getNo() >= 1) {
						list.add(Parameter.FARMERS);
					}
					break;
				default:
					break;
				}
			}
			user = new User();
			user.setId(pid);
			user.setLv(Collections.max(list));
			userService.updateNotNull(user);
		}
		bool = true;
		return bool;
	}

	// 奖金基数计算
	/****
	 * 注意事项： 计算后的基数如果小于0 返回0 前置条件：买卖盘匹配结束，且没有遗留问题 当前单购买数量和成交数量相等
	 * 业务描述：判断是否出局条件【w_farm_chick表中是否出局标志】： 1.正常 0 出局 出局需要计算烧伤奖 正常排单基数不变 即购买金额
	 * 计算公式：小端金额- 推荐人最近获取被推荐人一单的推荐奖-被推荐人上一单所得利息 SQL： 被推荐人上一单推荐奖 SELECT w.* FROM
	 * w_farm_chick as w WHERE w.user_id=? ORDER BY w.create_time DESC LIMIT 0,2
	 * 中的第二条数据：来计算推荐奖 和等级相关 所得利息： 幼鸡利息=幼鸡数量* 喂养时长+成年鸡利息=成年鸡数量*喂养时长
	 * 
	 * 成年鸡数量： 可能在喂养过程中卖掉成年鸡 ，需要每日计算
	 */
	@Override
	public double base() {
		double base = 0.0000;
		log.info("-------------------奖金基数计算------------------------");
		List<Farm> farms = farmService.getUntreatedFarm();
		// 用户等级计算
		if (calculateLevel(farms)) {
			// 计算奖金基数
			for (Farm farm : farms) {
				// farm.getUser_id();// 用户id
				// farm.getNum_buy(); // 购买数量
				// 获取当前用户的领导【用户】
				User user = userService.selectByKey(farm.getUser_id());// 获取当前排单的用户
				User leader = userService.selectByKey(user.getPid());// 获取当前排单的用户的领导
				// 获取当前用户排单对应的领导用户对应的最近排单 且小于当前用户排单的创建日期
				List<Farm> leaderLastFarm = farmService.selectLastFarmByDate(
						leader.getId(), farm.getCreate_time());
				double tempBase = farm.getNum_buy();
				if (leaderLastFarm == null || leaderLastFarm.size() == 0) {
					break;
				}
				Farm f = leaderLastFarm.get(0);// 领导最近一单
				if (farm.getNum_buy() >= f.getNum_buy()) {
					tempBase = f.getNum_buy();
				} else {
					tempBase = farm.getNum_buy();
				}
				// 判断当前用户对应的领导是否出局
				// if (farm.getTime_out().after(f.getTime_out())) {//
				// 这里判断还是有问题！！！！！！！！！！
				//if (f.getFlag_out() == 2) {// 领导排单已经出局 f.getFlag_out()==3时
											// 计算基数为0 也就不用计算了
				if (f.getFlag_out_p() == 2){// 领导排单已经出局 f.getFlag_out()==3时
					                        // 计算基数为0 也就不用计算了
					// 提成基数计算
					// 计算公式：小端金额- 推荐人获取被推荐人最近一单的推荐奖-被推荐人当前单所得利息
					// tempBase=leader.
					// 小端金额
					
					// 推荐人获取被推荐人最近一单的推荐奖
					/***
					 * 获取当前单的用户排单时间【create_time】 查询当前用户小于create_time最近一单 select
					 * w.* from w_farm_chick as w where w.user_id=#{当前单据用户id}
					 * and w.create_time<#{当前单据排单时间} order by w.create_time DESC
					 * limit 0,1
					 */
					List<Farm> userLastFarm = farmService.selectLastFarmByDate(
							farm.getUser_id(), farm.getCreate_time());
					// 被推荐人上一单所得利息
					/***
					 * 利息包含： 1.幼鸡饲养利息：获取配置表中幼鸡饲养利率 * 幼鸡饲养周期 2.成年下蛋利息：获取这一批次 * 时间
					 * 3.select sum(money) from (select (w.num_feed)* 注意这里的$用法
					 * ${参数：下蛋利率} as money from w_farm_feed as w where
					 * w.w_farm_chick_id=#{批次})
					 * 
					 * 版本二： select sum(w.price) from w_farm_feed as w where
					 * w.w_farm_chick_id=#{批次})
					 */
					// 获得当前用户的上一单
					Farm beforeFarm = farmService.selectByKey(farm.getPid());
					// 获得当前用户的上一单饲养成年鸡利息 --还没有计算
					beforeFarm.getId();// 当前用户上一单的批次
					double chicken = 0.0;//成鸡饲养利息计算----还没有做
					// 获得当前单饲养利息总和
					double deposit = beforeFarm.getNum_buy()
							* Integer.parseInt(cfgService.selectByKey("0205")
									.getValue_()) + chicken;
					tempBase = farm.getNum_buy()
							- userLastFarm.get(0).getNum_buy()
							* Double.parseDouble(cfgService.selectByKey(
									getCoefficientNO(leader.getLv(), 1))
									.getValue_()) - deposit;
				}
				if (tempBase <= 0.0) {
					break;
				}
				int temp = user.getDepth();
				User userTemp = user;
				Prize entity = null;
				while (true) {// 这样可能数据不统一：
					userTemp = userService.selectByKey(userTemp.getPid());// 2
					if (null == userTemp) {// 死循环终止条件
						break;
					}
					if (userTemp.getId().trim().length() == 0) {
						continue;
					}
					int depth = temp - userTemp.getDepth();
					double number = calculateRoyalty(tempBase,
							userTemp.getLv(), depth);
					// userTemp.setNum_dynamic(userTemp.getNum_dynamic() +
					// number);
					// userTemp.setTotal_dynamic(userTemp.getTotal_dynamic()+
					// number);
					// userService.updateNotNull(userTemp);
					entity = new Prize();
					entity.setId(UUID.randomUUID().toString().replace("-", ""));
					entity.setCreate_time(new Date());
					entity.setDepth(depth);
					entity.setMoney(number);
					entity.setFlag(0);
					entity.setUser_id(userTemp.getId());
					Calendar cr = Calendar.getInstance();
					if (depth == 1) {
						cr.add(Calendar.DAY_OF_MONTH, 7);
					} else {
						cr.add(Calendar.DAY_OF_MONTH, 15);
					}
					entity.setTrigger_time(cr.getTime());
					prizeService.save(entity);
				}
				farm.setFlag_calc_bonus(1);
				farmService.updateNotNull(farm);
			}

		} else {
			// 计算用户等级失败
			return -1;
		}

		return base;
	}

	// 根据用户代数 计算代数字符串编号
	private String getCoefficientNO(int generation) {
		String generationNO = null;
		switch (generation) {
		case 1:
			generationNO = "01";
			break;
		case 2:
			generationNO = "02";
			break;
		case 3:
			generationNO = "03";
			break;
		case 4:
			generationNO = "04";
			break;
		case 5:
			generationNO = "05";
			break;
		case 6:
			generationNO = "06";
			break;
		default:
			generationNO = "06";
			break;
		}
		return generationNO;
	}

	// 根据用户等级和代数 计算提成系数编号
	private String getCoefficientNO(String lv, int generation) {

		StringBuffer base = new StringBuffer(8);
		base.append(lv);
		base.append(getCoefficientNO(generation));
		return base.toString();
	}

	// 计算提成
	/***
	 * 
	 * 
	 * @param base
	 *            提成基数
	 * @param uuid
	 *            当前等级lv编号
	 * @param generation
	 *            直系几代
	 * @return
	 */
	private double calculateRoyalty(double base, String lv, int generation) {
		Cfg cfg = cfgService.selectByKey(getCoefficientNO(lv, generation));
		return Double.parseDouble(cfg.getValue_()) * base;
	}

	// 买卖盘自动匹配
	/**
	 * 业务：获取当天 买卖盘中的数据 以及 交易记录中 卖盘id为空或者卖盘id为空且状态为0数据 SQL： 1.SELECT * FROM
	 * p_buy_sell as p WHERE (p.'p_buy_id' ='null' or p.'p_sell_id'='null') and
	 * p.`status`='0' 2.SELECT * FROM p_buy as p WHERE p.create_time between
	 * #{0} and #{1}; 3.SELECT * FROM p_sell as p WHERE p.create_time time
	 * between #{0} and #{1} 原则：优先匹配交易记录表中的数据 其次 卖家为主
	 *******************************/
	@Override
	public boolean automatch() {
		boolean bool = false;
		// 获取买卖盘中未匹配的单据
		List<BuySell> buySells = buySellService.selectBySellAndBuyId();
		// 获取卖盘中数据
		Calendar cr = Calendar.getInstance();
		cr.add(Calendar.DATE, -2);
		// 卖盘数据
		List<Sell> sells = sellService.selectByCycles(cr.getTime(), Calendar
				.getInstance().getTime());
		// 卖盘数据
		List<Buy> buys = buyService.selectByCycles(cr.getTime(), Calendar
				.getInstance().getTime());
		for (BuySell buySell : buySells) {
			if (!buySell.getP_buy_id().equals("null")) {// 说明是买盘剩余数据
				buyDataHandle(buySell, sells);
			} else {// 说明是卖盘剩余数据
				sellDataHandle(buySell, buys);
			}
		}
		buyAndSellHandle(sells, buys);
		bool = true;
		return bool;
	}

	private void buyAndSellHandle(List<Sell> sells, List<Buy> buys) {
		if (sells == null || sells.size() == 0) {
			return;
		}
		if (buys == null || buys.size() == 0) {
			return;
		}
		// 以卖家为主
		for (Sell sell : sells) {
			int sellMatchNum = sell.getNum_sell();//
			if (sellMatchNum <= 0) {
				continue;
			}
			BuySell entity = null;
			for (Buy buy : buys) {
				int buyMatchNum = buy.getNum_buy();
				if (buyMatchNum <= 0) {
					continue;
				}
				entity = new BuySell();
				entity.setId(UUID.randomUUID().toString().replace("-", ""));
				entity.setCreate_time(new Date());
				entity.setP_sell_id(sell.getId());
				entity.setP_buy_id(buy.getId());
				entity.setStatus(0);
				if (sellMatchNum > buyMatchNum) {// 卖家大于买家
					entity.setNum_matching(buyMatchNum);
					sell.setNum_sell(sellMatchNum - buyMatchNum);
					buy.setNum_buy(0);
				} else if (sellMatchNum < buyMatchNum) {// 卖家小于买家
					entity.setNum_matching(sellMatchNum);
					sell.setNum_sell(0);
					buy.setNum_buy(buyMatchNum - sellMatchNum);
					break;
				} else {// 卖家等于买家
					entity.setNum_matching(buyMatchNum);
					sell.setNum_sell(0);
					buy.setNum_buy(0);
					break;
				}
				buySellService.save(entity);
			}
		}

		// 自动匹配结束后 未处理的数据转入买卖交易中
		// 卖盘清理
		sells.parallelStream()
				.filter(sell -> sell.getNum_sell() > 0)
				.forEach(
						sell -> {
							BuySell entity = new BuySell();
							entity.setId(UUID.randomUUID().toString()
									.replace("-", ""));
							entity.setCreate_time(new Date());
							entity.setP_sell_id(sell.getId());
							entity.setP_buy_id("null");
							entity.setStatus(0);
							entity.setNum_matching(sell.getNum_sell());
						});
		// 买盘清理
		buys.parallelStream()
				.filter(buy -> buy.getNum_buy() > 0)
				.forEach(
						buy -> {
							BuySell entity = new BuySell();
							entity.setId(UUID.randomUUID().toString()
									.replace("-", ""));
							entity.setCreate_time(new Date());
							entity.setP_sell_id("null");
							entity.setP_buy_id(buy.getId());
							entity.setStatus(0);
							entity.setNum_matching(buy.getNum_buy());
						});
	}

	private void sellDataHandle(BuySell buySell, List<Buy> buys) {
		if (null == buySell || buySell.getNum_matching() <= 0) {
			return;
		}
		BuySell entity = null;
		for (Buy buy : buys) {
			if (buy.getNum_buy() <= 0) {
				continue;
			}
			entity = new BuySell();
			entity.setId(UUID.randomUUID().toString().replace("-", ""));
			entity.setCreate_time(new Date());
			entity.setP_sell_id(buySell.getP_sell_id());
			entity.setP_buy_id(buy.getId());
			entity.setStatus(0);
			int buyMatcheNum = buy.getNum_buy();
			int sellMatcheNum = buySell.getNum_matching();
			if (sellMatcheNum > buyMatcheNum) {// 卖家数据大于买家数据
				entity.setNum_matching(buyMatcheNum);
				buySell.setNum_matching(sellMatcheNum - buyMatcheNum);
				buy.setNum_buy(0);
			} else if (sellMatcheNum < buyMatcheNum) {// 卖家数据小于买家数据
				entity.setNum_matching(sellMatcheNum);
				buySell.setNum_matching(0);
				buy.setNum_buy(buyMatcheNum - sellMatcheNum);
				break;
			} else {// 卖家数据等于买家数据
				entity.setNum_matching(sellMatcheNum);
				buySell.setNum_matching(0);
				buy.setNum_buy(0);
				break;
			}
			buySellService.save(entity);
		}
		if (buySell.getNum_matching() > 0) {
			// buySellService.delete(buySell.getId());
			buySell.setCreate_time(new Date());
			// buySellService.save(buySell);
			buySellService.updateNotNull(buySell);
		} else {
			buySellService.delete(buySell.getId());
		}
		buySell = null;
	}

	private void buyDataHandle(BuySell buySell, List<Sell> sells) {
		if (null == buySell || buySell.getNum_matching() <= 0) {
			return;
		}
		BuySell entity = null;
		for (Sell sell : sells) {
			if (sell.getNum_sell() <= 0) {
				continue;
			}
			entity = new BuySell();
			entity.setId(UUID.randomUUID().toString().replace("-", ""));
			entity.setCreate_time(new Date());
			entity.setP_sell_id(sell.getId());
			entity.setP_buy_id(buySell.getP_buy_id());
			entity.setStatus(0);
			int sellMatcheNum = sell.getNum_sell();// 买家数据
			int buySellMatcheNum = buySell.getNum_matching();// 卖家数据
			if (buySellMatcheNum > sellMatcheNum) {// 买家数据大于卖家数据
				entity.setNum_matching(sellMatcheNum);
				buySell.setNum_matching(buySellMatcheNum - sellMatcheNum);
				sell.setNum_sell(0);
			} else if (buySellMatcheNum < sellMatcheNum) {// 买家数据小于卖家数据
				entity.setNum_matching(buySellMatcheNum);
				buySell.setNum_matching(0);
				sell.setNum_sell(sellMatcheNum - buySellMatcheNum);
				break;
			} else {// 卖家数据等于买家数据
				entity.setNum_matching(sellMatcheNum);
				buySell.setNum_matching(0);
				sell.setNum_sell(0);
				break;
			}
			buySellService.save(entity);
		}
		if (buySell.getNum_matching() > 0) {
			// buySellService.delete(buySell.getId());
			buySell.setCreate_time(new Date());
			// buySellService.save(buySell);
			buySellService.updateNotNull(buySell);
		} else {
			buySellService.delete(buySell.getId());
		}

		buySell = null;
	}

	public static void main(String[] args) {

	}
}
