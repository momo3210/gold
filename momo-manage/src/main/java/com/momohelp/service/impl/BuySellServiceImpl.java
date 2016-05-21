package com.momohelp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.model.BuySell;
import com.momohelp.service.BuySellService;

/**
 *
 * @author Administrator
 *
 */
@Service("buySellService")
public class BuySellServiceImpl extends BaseService<BuySell> implements
		BuySellService {

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
		return list;
	}

	/**
	 * 买盘
	 */
	@Override
	public List<BuySell> findByBuyId(String buy_id) {

		Example example = new Example(BuySell.class);
		example.setOrderByClause("create_time asc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("p_buy_id", buy_id);

		List<BuySell> list = selectByExample(example);
		return list;
	}

	@Override
	public String[] confirm(BuySell buySell, String user_id) {

		BuySell __buySell = selectByKey(buySell.getId());
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

		// 重新创建新对象
		BuySell _buySell = new BuySell();
		_buySell.setId(buySell.getId());

		// 买家确认
		if (user_id.equals(__buySell.getP_buy_user_id())) {

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
		} else if (user_id.equals(__buySell.getP_sell_user_id())) {

			if (1 != __buySell.getStatus()) {
				return new String[] { "非法操作" };
			}

			_buySell.setP_sell_user_time(new Date());
			_buySell.setStatus(2);
		} else {
			return new String[] { "非法操作" };
		}

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

}
