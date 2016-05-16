package com.momohelp.service;

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
}
