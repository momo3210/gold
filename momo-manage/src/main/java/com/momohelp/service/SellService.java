package com.momohelp.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.momohelp.model.Sell;

/**
 *
 * @author Administrator
 *
 */
public interface SellService extends IService<Sell> {

	/**
	 * 卖出鸡苗
	 *
	 * @param sell
	 * @return
	 */
	@Transactional
	String[] sell(Sell sell);

	/**
	 * 获取每月卖出
	 *
	 * @param user_id
	 * @return
	 */
	List<Sell> findMonthSellByUserId(String user_id);

	/**
	 * 获取最后一次的卖盘
	 *
	 * @param user_id
	 * @return
	 */
	Sell getLastSellByUserId(String user_id);

	/**
	 * 获取未完全成交的记录
	 *
	 * @param user_id
	 * @return
	 */
	List<Sell> findUnFinishDeal(String user_id);
}
