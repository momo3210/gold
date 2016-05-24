package com.momohelp.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.momohelp.model.BuySell;

/**
 *
 * @author Administrator
 *
 */
public interface BuySellService extends IService<BuySell> {

	BuySell getId(String id);

	/**
	 * 卖盘
	 *
	 * @param sell_id
	 * @return
	 */
	List<BuySell> findBySellId__4(String sell_id);

	/**
	 * 买盘
	 *
	 * @param buy_id
	 * @return
	 */
	List<BuySell> findByBuyId__4(String buy_id);

	/**
	 * 确认打款（买家确认打款，卖家确认打款）
	 *
	 * @param buySell
	 * @param user_id
	 * @return
	 */
	@Transactional
	String[] confirm(BuySell buySell, String user_id);

	/**
	 * 举报（买家确认打款，卖家确认打款）
	 *
	 * @param buySell
	 * @return
	 */
	@Transactional
	String[] tip_off(BuySell buySell);
}
