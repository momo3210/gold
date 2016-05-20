package com.momohelp.service;

import java.util.List;

import com.momohelp.model.BuySell;

/**
 *
 * @author Administrator
 *
 */
public interface BuySellService extends IService<BuySell> {

	/**
	 * 卖盘
	 *
	 * @param sell_id
	 * @return
	 */
	List<BuySell> findBySellId(String sell_id);

	/**
	 * 买盘
	 *
	 * @param buy_id
	 * @return
	 */
	List<BuySell> findByBuyId(String buy_id);
}
