package com.momohelp.service;

import java.util.List;

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

	/**
	 * 查找库存
	 *
	 * @param user_id
	 * @return
	 */
	List<Farm> findInventory(String user_id);

	/**
	 * 获取可以孵化的鸡苗批次
	 *
	 * @param key
	 * @param user_id
	 * @return
	 */
	Farm findCanHatch(String key, String user_id);
}
