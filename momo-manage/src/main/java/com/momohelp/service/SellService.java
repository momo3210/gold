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
	 * 获取未完全成交的记录
	 *
	 * @param user_id
	 * @return
	 */
	List<Sell> findUnFinishDeal(String user_id);
}
