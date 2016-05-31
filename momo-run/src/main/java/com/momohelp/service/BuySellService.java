package com.momohelp.service;

import java.util.List;

import com.momohelp.model.BuySell;

/**
 *
 * @author Administrator
 *
 */
public interface BuySellService extends IService<BuySell> {

	/***
	 * 获取当前买卖明细中，还没有通过匹配记录
	 * @return
	 */
	List<BuySell> selectBySellAndBuyId();
	
	/***
	 * 获取当前卖明细中，还没有通过匹配记录
	 * @return
	 */
	List<BuySell> selectBySell();
	
	/***
	 * 获取当前买明细中，还没有通过匹配记录
	 * @return
	 */
	List<BuySell> selectByBuy();

}
