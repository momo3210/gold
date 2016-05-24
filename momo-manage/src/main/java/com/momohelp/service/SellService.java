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
	 * 获取未成交的卖盘
	 *
	 * @param user_id
	 * @return
	 */
	List<Sell> findUnDealByUserId__4(String user_id);

	/**
	 * 获取当前月的卖出记录
	 *
	 * @param user_id
	 * @return
	 */
	List<Sell> findMonthSellByUserId____4(String user_id);

	/**
	 * 获取用户的最后一次卖出记录
	 *
	 * YES
	 *
	 * @param user_id
	 * @return
	 */
	Sell getLastByUserId___4(String user_id);

	/**
	 * 卖出鸡苗
	 *
	 * @param sell
	 * @return
	 */
	@Transactional
	String[] sell(Sell sell);

	void updateNum_deal___4(String id, int num_deal);
}
