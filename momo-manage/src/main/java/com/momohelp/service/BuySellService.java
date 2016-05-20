package com.momohelp.service;

import java.util.List;

import com.momohelp.model.BuySell;

/**
 *
 * @author Administrator
 *
 */
public interface BuySellService extends IService<BuySell> {

	List<BuySell> findBySellId(String sell_id);

	List<BuySell> findByBuyId(String buy_id);
}
