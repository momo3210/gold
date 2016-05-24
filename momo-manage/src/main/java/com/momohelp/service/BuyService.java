package com.momohelp.service;

import java.util.List;

import com.momohelp.model.Buy;

/**
 *
 * @author Administrator
 *
 */
public interface BuyService extends IService<Buy> {

	/**
	 * 获取未完全成交的记录
	 *
	 * @param user_id
	 * @return
	 */
	List<Buy> findUnFinishDeal(String user_id);

	List<Buy> findByFarmId(String farm_id);

	List<Buy> findByFarmId_1(String farm_id);

	List<Buy> findByFarmId_3(String farm_id, int page, int rows);
}
