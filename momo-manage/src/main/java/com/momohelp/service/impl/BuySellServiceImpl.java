package com.momohelp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.model.BuySell;
import com.momohelp.service.BuySellService;
import com.momohelp.service.BuyService;
import com.momohelp.service.FarmService;
import com.momohelp.service.SellService;
import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Service("buySellService")
public class BuySellServiceImpl extends BaseService<BuySell> implements
		BuySellService {

	@Autowired
	private BuyService buyService;

	@Autowired
	private SellService sellService;

	@Autowired
	private FarmService farmService;

	@Autowired
	private UserService userService;

	/**
	 * 卖盘
	 */
	@Override
	public List<BuySell> findBySellId(String sell_id) {

		Example example = new Example(BuySell.class);
		example.setOrderByClause("create_time asc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("p_sell_id", sell_id);

		List<BuySell> list = selectByExample(example);

		if (null == list) {
			return null;
		}

		for (int i = 0, j = list.size(); i < j; i++) {
			BuySell buySell = list.get(i);
			buySell.setP_buy_user(userService.getId(0,
					buySell.getP_buy_user_id()));
		}

		return list;
	}

	/**
	 * 买卖盘
	 */
	@Override
	public List<BuySell> findByBuyId(String buy_id) {

		Example example = new Example(BuySell.class);
		example.setOrderByClause("create_time asc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("p_buy_id", buy_id);

		List<BuySell> list = selectByExample(example);

		if (null == list) {
			return null;
		}

		for (int i = 0, j = list.size(); i < j; i++) {
			BuySell buySell = list.get(i);
			buySell.setP_sell_user(userService.getId(0,
					buySell.getP_sell_user_id()));
		}

		return list;
	}

	@Override
	public String[] confirm(BuySell buySell, String user_id) {

		BuySell __buySell = getId(buySell.getId());
		if (null == __buySell) {
			return new String[] { "非法操作" };
		}

		switch (__buySell.getStatus()) {
		case 0:
		case 1:
			break;
		default:
			return new String[] { "非法操作" };
		}

		/**/

		BuySell _buySell = new BuySell();
		_buySell.setId(buySell.getId());

		// 判断是买家确认还是卖家确认（既不属于买家也不属于卖家）
		if (user_id.equals(__buySell.getP_buy_user_id())) { // 买家确认

			if (0 != __buySell.getStatus()) {
				return new String[] { "非法操作" };
			}

			_buySell.setP_buy_user_img(buySell.getP_buy_user_img());
			_buySell.setP_buy_user_content((null == buySell
					.getP_buy_user_content() || "".equals(buySell
					.getP_buy_user_content().trim())) ? "这家伙很懒" : buySell
					.getP_buy_user_content().trim());

			_buySell.setP_buy_user_time(new Date());
			_buySell.setStatus(1);

		} else if (user_id.equals(__buySell.getP_sell_user_id())) { // 卖家确认

			if (1 != __buySell.getStatus()) {
				return new String[] { "非法操作" };
			}

			_buySell.setP_sell_user_time(new Date());
			_buySell.setStatus(2);

			sellService.updateNum_deal(__buySell.getP_sell_id(),
					__buySell.getNum_matching());
			buyService.updateNum_deal(__buySell.getP_buy_id(),
					__buySell.getNum_matching());
			farmService.updateNum_deal(__buySell.getBuy().getW_farm_chick_id(),
					__buySell.getNum_matching());

		} else {
			return new String[] { "非法操作" };
		}

		// // 买家确认
		// if (user_id.equals(__buySell.getP_buy_user_id())) {
		//
		// if (0 != __buySell.getStatus()) {
		// return new String[] { "非法操作" };
		// }
		//
		// _buySell.setP_buy_user_img(buySell.getP_buy_user_img());
		// _buySell.setP_buy_user_content((null == buySell
		// .getP_buy_user_content() || "".equals(buySell
		// .getP_buy_user_content().trim())) ? "这家伙很懒" : buySell
		// .getP_buy_user_content().trim());
		//
		// _buySell.setP_buy_user_time(new Date());
		// _buySell.setStatus(1);
		// } else if (user_id.equals(__buySell.getP_sell_user_id())) {
		//
		// if (1 != __buySell.getStatus()) {
		// return new String[] { "非法操作" };
		// }
		//
		// // 查当前笔是否是此次买盘的最后一笔或买入鸡苗的最后一笔（相对于买家）
		// // 查当前笔是否是卖家的最后一笔（相对于卖家）
		// // 买家更新两个表，卖家更新一个表的 成交时间
		//
		// // 买家
		// Buy buy = buyService.selectByKey(__buySell.getP_buy_id());
		//
		// List<BuySell> list_buy = findByBuyId(__buySell.getP_buy_id());
		//
		// int count_buySell = 0;
		//
		// for (int i = 0; i < list_buy.size(); i++) {
		// BuySell item = list_buy.get(i);
		// if (2 == item.getStatus()) {
		// count_buySell += item.getNum_matching();
		// }
		// }
		//
		// if (buy.getNum_buy() == (count_buySell + __buySell
		// .getNum_matching())) {
		// Buy _buy = new Buy();
		// _buy.setId(buy.getId());
		// _buy.setTime_deal(new Date());
		// buyService.updateNotNull(_buy);
		// }
		//
		// // 卖家
		// Sell sell = sellService.selectByKey(__buySell.getP_sell_id());
		//
		// List<BuySell> list_sell = findBySellId(__buySell.getP_sell_id());
		//
		// int count_sell = 0;
		//
		// for (int i = 0; i < list_sell.size(); i++) {
		// BuySell item = list_sell.get(i);
		// if (2 == item.getStatus()) {
		// count_sell += item.getNum_matching();
		// }
		// }
		//
		// if (sell.getNum_sell() == (count_sell + __buySell.getNum_matching()))
		// {
		// Sell _sell = new Sell();
		// _sell.setId(sell.getId());
		// _sell.setTime_deal(new Date());
		// sellService.updateNotNull(_sell);
		// }
		//
		// // 更新Farm表
		// Farm farm = farmService.selectByKey(buy.getW_farm_chick_id());
		//
		// List<Buy> list = buyService.findByFarmId(farm.getId());
		//
		// int count_buybuy = 0;
		//
		// for (int j = 0; j < list.size(); j++) {
		// Buy buy2 = list.get(j);
		//
		// List<BuySell> list2 = findByBuyId(buy2.getId());
		//
		// for (int i = 0; i < list2.size(); i++) {
		// BuySell item = list2.get(i);
		// if (2 == item.getStatus()) {
		// count_buybuy += item.getNum_matching();
		// }
		// }
		// }
		//
		// if (farm.getNum_buy() == (count_buybuy + __buySell
		// .getNum_matching())) {
		// Farm _farm = new Farm();
		// _farm.setId(farm.getId());
		// _farm.setNum_deal(farm.getNum_buy());
		// _farm.setTime_deal(new Date());
		// // 划拨奖金
		// _farm.setNum_reward(farm.getNum_buy() / 100);
		// farmService.updateNotNull(_farm);
		// }
		//
		// // TODO
		// _buySell.setP_sell_user_time(new Date());
		// _buySell.setStatus(2);
		// } else {
		// return new String[] { "非法操作" };
		// }

		updateNotNull(_buySell);
		return null;
	}

	/**
	 * 买卖双方都可以举报
	 */
	@Override
	public String[] tip_off(BuySell buySell) {

		BuySell __buySell = selectByKey(buySell.getId());

		if (null == __buySell) {
			return new String[] { "非法操作" };
		}

		switch (__buySell.getStatus()) {
		case 0:
		case 1:
			break;
		default:
			return new String[] { "不能举报" };
		}

		BuySell _buySell = new BuySell();
		_buySell.setId(buySell.getId());
		_buySell.setTip_off_user_id(buySell.getTip_off_user_id());
		_buySell.setTip_off_time(new Date());
		_buySell.setStatus(3);
		_buySell.setTip_off_content(buySell.getTip_off_content());

		updateNotNull(_buySell);
		return null;
	}

	@Override
	public BuySell getId(String id) {

		BuySell buySell = selectByKey(id);

		if (null == buySell) {
			return null;
		}

		buySell.setBuy(buyService.selectByKey(buySell.getP_buy_id()));

		return buySell;
	}
}
