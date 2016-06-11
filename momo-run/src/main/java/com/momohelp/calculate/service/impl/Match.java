package com.momohelp.calculate.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.momohelp.calculate.service.IMatch;
import com.momohelp.model.Buy;
import com.momohelp.model.BuySell;
import com.momohelp.model.Sell;
import com.momohelp.service.BuySellService;
import com.momohelp.service.BuyService;
import com.momohelp.service.SellService;

@Service
public class Match implements Serializable, IMatch {

	private static final long serialVersionUID = -7591547109810957383L;
	private static Logger log = Logger.getLogger(Match.class);
	@Resource
	private BuyService buyService;

	@Resource
	private SellService sellService;

	@Resource
	private BuySellService buySellService;

	// 买卖盘自动匹配
	/**
	 * 业务：获取当天 买卖盘中的数据 以及 交易记录中 卖盘id为空或者卖盘id为空且状态为0数据
	 * 
	 * SQL：
	 * 
	 * 1.SELECT * FROM p_buy_sell as p WHERE (p.'p_buy_id' ='null' or
	 * p.'p_sell_id'='null') and p.`status`='0'
	 * 
	 * 2.SELECT * FROM p_buy as p WHERE p.create_time between #{0} and #{1};
	 * 
	 * 3.SELECT * FROM p_sell as p WHERE p.create_time time between #{0} and
	 * #{1}
	 * 
	 * 原则：优先匹配交易记录表中的数据 其次 卖家为主
	 *******************************/
	@Override
	public boolean automatch() {
		log.info("-----------买卖盘自动匹配---------------");
		boolean bool = false;
		// 获取买卖盘中未匹配的单据

		List<BuySell> buyList = buySellService.selectByBuy();
		List<BuySell> sellList = buySellService.selectBySell();
		// List<BuySell> buySells = buySellService.selectBySellAndBuyId();
		Calendar cr = Calendar.getInstance();
		cr.add(Calendar.DAY_OF_MONTH, -25);
		cr.set(Calendar.HOUR_OF_DAY, 0);
		cr.set(Calendar.MINUTE, 0);
		cr.set(Calendar.SECOND, 0);
		// 卖盘数据
		List<Sell> sells = sellService.selectByCycles(cr.getTime(), Calendar
				.getInstance().getTime());
		// 买盘数据
		List<Buy> buys = buyService.selectByCycles(cr.getTime(), Calendar
				.getInstance().getTime());
		for (BuySell sell : sellList) {// 说明是卖盘剩余数据
			sellDataHandle(sell, buys);
		}
		for (BuySell buy : buyList) {// 说明是买盘剩余数据
			buyDataHandle(buy, sells);
		}
		selfMactch(buyList, sellList);
		buyAndSellHandle(sells, buys);

		// 清理剩余数据
		clearBuyOrSellData(sells, buys);

		clearBuyAndSellData(sellList, buyList);
		if (null != sells && sells.size() > 0) {
			updateTime_Second();
		}
		bool = true;
		return bool;
	}

	public void updateTime_Second() {
		Calendar cr = Calendar.getInstance();
		StringBuffer dates = new StringBuffer();
		dates.append(cr.get(Calendar.YEAR));
		dates.append("-");
		int month = cr.get(Calendar.MONTH) + 1;
		if (month > 9) {
			dates.append(month + "");
		} else {
			dates.append("0" + month);
		}
		dates.append("-");
		int day = cr.get(Calendar.DAY_OF_MONTH);
		if (month > 9) {
			dates.append(day + "");
		} else {
			dates.append("0" + day);
		}
		dates.append(" ");
		int hour = cr.get(Calendar.HOUR_OF_DAY);
		if (hour > 9) {
			dates.append(hour + "");
		} else {
			dates.append("0" + hour);
		}
		dates.append(":");
		int minute = cr.get(Calendar.MINUTE);
		if (hour > 9) {
			dates.append(minute + "");
		} else {
			dates.append("0" + minute);
		}
		dates.append("%");
		String date = dates.toString();
		buySellService.updateTimeSecond(date);

	}

	private void clearBuyOrSellData(List<Sell> sells, List<Buy> buys) {

		// 自动匹配结束后 未处理的数据转入买卖交易中
		// 卖盘清理
		// 计算标志设置
		for (Sell sell2 : sells) {
			if (sell2.getNum_sell() > 0) {
				BuySell entity = new BuySell();
				entity.setId(genId());
				entity.setCreate_time(sell2.getCalc_time());
				entity.setP_sell_id(sell2.getId());
				entity.setP_buy_id("null");
				entity.setStatus(0);
				entity.setP_sell_user_id(sell2.getUser_id());
				entity.setP_buy_user_id("null");
				entity.setNum_matching(sell2.getNum_sell());
				buySellService.save(entity);
			}
			sellService.updateFlagCalc(sell2.getId());
		}
		// 买盘清理
		// 计算标志设置
		for (Buy buy2 : buys) {
			if (buy2.getNum_buy() > 0) {
				BuySell entity = new BuySell();
				entity.setId(genId());
				entity.setCreate_time(buy2.getCalc_time());
				entity.setP_sell_id("null");
				entity.setP_buy_id(buy2.getId());
				entity.setStatus(0);
				entity.setP_sell_user_id("null");
				entity.setP_buy_user_id(buy2.getUser_id());
				entity.setNum_matching(buy2.getNum_buy());
				buySellService.save(entity);
			}
			buyService.updateFlagCalc(buy2.getId());
		}

	}

	private void clearBuyAndSellData(List<BuySell> sellList,
			List<BuySell> buyList) {
		// 自动匹配结束后 未处理的数据转入买卖交易中
		// 卖盘中间数据清理
		// 计算标志设置
		for (BuySell sell2 : sellList) {
			if (sell2.getNum_matching() > 0) {
				buySellService.updateNotNull(sell2);
			} else {
				buySellService.deleteByKeys(sell2.getId());
			}
		}
		// 买盘中间数据清理
		// 计算标志设置
		for (BuySell buy2 : buyList) {
			if (buy2.getNum_matching() > 0) {
				buySellService.updateNotNull(buy2);
			} else {
				buySellService.deleteByKeys(buy2.getId());
			}
		}

	}

	private void selfMactch(List<BuySell> buyList, List<BuySell> sellList) {
		if ((sellList == null || sellList.size() == 0)
				|| (buyList == null || buyList.size() == 0)) {
			return;
		}
		for (BuySell sell : sellList) {
			int selltemp = sell.getNum_matching();
			if (selltemp > 0) {
				BuySell entity = null;
				for (BuySell buy : buyList) {
					if (sell.getP_sell_user_id().equals(buy.getP_buy_user_id())) {
						continue;
					}
					int buytemp = buy.getNum_matching();
					entity = new BuySell();
					entity.setId(genId());
					entity.setCreate_time(new Date());
					entity.setP_sell_id(sell.getP_sell_id());
					entity.setP_buy_id(buy.getP_buy_id());
					entity.setStatus(0);
					entity.setP_buy_user_id(buy.getP_buy_user_id());
					entity.setP_sell_user_id(sell.getP_sell_user_id());
					if (buytemp > 0) {
						if (selltemp > buytemp) {
							entity.setNum_matching(buytemp);
							sell.setNum_matching(selltemp - buytemp);
							selltemp = sell.getNum_matching();
							buy.setNum_matching(0);
							buySellService.save(entity);
						} else if (selltemp < buytemp) {
							entity.setNum_matching(selltemp);
							sell.setNum_matching(0);
							buy.setNum_matching(buytemp - selltemp);
							buySellService.save(entity);
							break;
						} else {
							entity.setNum_matching(buytemp);
							sell.setNum_matching(0);
							buy.setNum_matching(0);
							buySellService.save(entity);
							break;
						}
					}
				}
			}
		}
	}

	private void buyAndSellHandle(List<Sell> sells, List<Buy> buys) {

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
				// 自己不能给自己匹配
				if (sell.getUser_id().equals(buy.getUser_id())) {
					continue;
				}
				entity = new BuySell();
				entity.setId(genId());
				entity.setCreate_time(new Date());
				entity.setP_sell_id(sell.getId());
				entity.setP_buy_id(buy.getId());
				entity.setStatus(0);
				entity.setP_buy_user_id(buy.getUser_id());
				entity.setP_sell_user_id(sell.getUser_id());
				if (sellMatchNum > buyMatchNum) {// 卖家大于买家
					entity.setNum_matching(buyMatchNum);
					sell.setNum_sell(sellMatchNum - buyMatchNum);
					sellMatchNum = sell.getNum_sell();
					buy.setNum_buy(0);
					buySellService.save(entity);
				} else if (sellMatchNum < buyMatchNum) {// 卖家小于买家
					entity.setNum_matching(sellMatchNum);
					sell.setNum_sell(0);
					buy.setNum_buy(buyMatchNum - sellMatchNum);
					buySellService.save(entity);
					break;
				} else {// 卖家等于买家
					entity.setNum_matching(buyMatchNum);
					sell.setNum_sell(0);
					buy.setNum_buy(0);
					buySellService.save(entity);
					break;
				}
			}
		}

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
			// 自己不能给自己匹配
			if (buySell.getP_sell_user_id().equals(buy.getUser_id())) {
				continue;
			}
			entity = new BuySell();
			entity.setId(genId());
			entity.setCreate_time(new Date());
			entity.setP_sell_id(buySell.getP_sell_id());
			entity.setP_buy_id(buy.getId());
			entity.setStatus(0);
			entity.setP_buy_user_id(buy.getUser_id());
			entity.setP_sell_user_id(buySell.getP_sell_user_id());
			int buyMatcheNum = buy.getNum_buy();
			int sellMatcheNum = buySell.getNum_matching();
			if (sellMatcheNum > buyMatcheNum) {// 卖家数据大于买家数据
				entity.setNum_matching(buyMatcheNum);
				buySell.setNum_matching(sellMatcheNum - buyMatcheNum);
				buy.setNum_buy(0);
				buySellService.save(entity);
			} else if (sellMatcheNum < buyMatcheNum) {// 卖家数据小于买家数据
				entity.setNum_matching(sellMatcheNum);
				buySell.setNum_matching(0);
				buy.setNum_buy(buyMatcheNum - sellMatcheNum);
				buySellService.save(entity);
				break;
			} else {// 卖家数据等于买家数据
				entity.setNum_matching(sellMatcheNum);
				buySell.setNum_matching(0);
				buy.setNum_buy(0);
				buySellService.save(entity);
				break;
			}
		}

		if (buySell.getNum_matching() > 0) {
			// buySell.setCreate_time(new Date());
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
			// 自己不能给自己匹配
			if (buySell.getP_buy_user_id().equals(sell.getUser_id())) {
				continue;
			}
			entity = new BuySell();
			entity.setId(genId());
			entity.setCreate_time(new Date());
			entity.setP_sell_id(sell.getId());
			entity.setP_buy_id(buySell.getP_buy_id());
			entity.setStatus(0);
			entity.setP_buy_user_id(buySell.getP_buy_user_id());
			entity.setP_sell_user_id(sell.getUser_id());
			int sellMatcheNum = sell.getNum_sell();// 买家数据
			int buySellMatcheNum = buySell.getNum_matching();// 卖家数据
			if (buySellMatcheNum > sellMatcheNum) {// 买家数据大于卖家数据
				entity.setNum_matching(sellMatcheNum);
				buySell.setNum_matching(buySellMatcheNum - sellMatcheNum);
				sell.setNum_sell(0);
				buySellService.save(entity);
			} else if (buySellMatcheNum < sellMatcheNum) {// 买家数据小于卖家数据
				entity.setNum_matching(buySellMatcheNum);
				buySell.setNum_matching(0);
				sell.setNum_sell(sellMatcheNum - buySellMatcheNum);
				buySellService.save(entity);
				break;
			} else {// 卖家数据等于买家数据
				entity.setNum_matching(sellMatcheNum);
				buySell.setNum_matching(0);
				sell.setNum_sell(0);
				buySellService.save(entity);
				break;
			}

		}
		if (buySell.getNum_matching() > 0) {
			// buySell.setCreate_time(new Date());
			buySellService.updateNotNull(buySell);
		} else {
			buySellService.delete(buySell.getId());
		}
		buySell = null;
	}

	/**
	 * 生成主键
	 *
	 * @return
	 */
	private String genId() {
		String id = null;
		BuySell buySell = null;
		do {
			// 算法
			int i = (int) ((Math.random() * 10 + 1) * 100000000);
			id = String.valueOf(i);
			if (9 < id.length()) {
				id = id.substring(0, 9);
			}
			id = "P" + id;
			buySell = buySellService.selectByKey(id);
		} while (null != buySell);
		return id;
	}
}
