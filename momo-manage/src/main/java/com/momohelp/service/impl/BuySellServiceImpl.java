package com.momohelp.service.impl;

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

}
