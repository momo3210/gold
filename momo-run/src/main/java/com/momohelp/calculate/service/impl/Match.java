package com.momohelp.calculate.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
		if (null != buys && buys.size() > 0) {
			if (buySell.getNum_matching() > 0) {
				// buySellService.delete(buySell.getId());
				buySell.setCreate_time(new Date());
				// buySellService.save(buySell);
				buySellService.updateNotNull(buySell);
			} else {
				buySellService.delete(buySell.getId());
			}
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
		if (null != sells && sells.size() > 0) {
			if (buySell.getNum_matching() > 0) {
				// buySellService.delete(buySell.getId());
				buySell.setCreate_time(new Date());
				// buySellService.save(buySell);
				buySellService.updateNotNull(buySell);
			} else {
				buySellService.delete(buySell.getId());
			}
		}
		buySell = null;
	}

}
