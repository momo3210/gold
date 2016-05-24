package com.momohelp.service;

import java.util.List;

import com.momohelp.model.Buy;

/**
 *
 * @author Administrator
 *
 */
public interface BuyService extends IService<Buy> {

	Buy getId(String id);

	/**
	 * 获取未完全成交的记录
	 *
	 * 可以删除了
	 *
	 * @param user_id
	 * @return
	 */
	List<Buy> findUnFinishDeal(String user_id);

	List<Buy> findByFarmId__4(String farm_id);

	void updateNum_deal(String id, int num_deal);
}
