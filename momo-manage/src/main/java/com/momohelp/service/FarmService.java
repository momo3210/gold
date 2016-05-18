package com.momohelp.service;

import org.springframework.transaction.annotation.Transactional;

import com.momohelp.model.Farm;

/**
 *
 * @author Administrator
 *
 */
public interface FarmService extends IService<Farm> {

	/**
	 * 买入鸡苗
	 *
	 * @param farm
	 * @return
	 */
	@Transactional
	String[] buy(Farm farm);

	/**
	 * 获取用户的最后一单
	 *
	 * @param user_id
	 * @return
	 */
	Farm getLastByUserId(String user_id);

}
