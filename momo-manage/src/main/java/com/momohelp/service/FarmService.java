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
	 * 孵化时使用
	 *
	 * @param id
	 * @param user_id
	 * @return
	 */
	Farm getHatchByFarmId(String id, String user_id);

	/**
	 * 喂鸡时使用
	 *
	 * @param id
	 * @param user_id
	 * @return
	 */
	Farm getFeedByFarmId(String id, String user_id);

	/*************************/

	/**
	 * 买入鸡苗
	 *
	 * @param farm
	 * @return
	 */
	@Transactional
	String[] buy(Farm farm);

	/**
	 * 查找未完全交易的鸡苗批次
	 *
	 * @param user_id
	 * @return
	 */
	List<Farm> findUnDealByUserId__1(String user_id);

	/**
	 * 查找目前可以孵化的批次
	 *
	 * @param user_id
	 * @return
	 */
	List<Farm> findHatchByUserId(String user_id);

	/**
	 * 查找目前可以喂的批次
	 *
	 * @param user_id
	 * @return
	 */
	List<Farm> findFeedByUserId__3(String user_id);

	/**
	 * 查找目前可以喂的批次（展示使用）
	 *
	 * @param user_id
	 * @return
	 */
	List<Farm> feedMo_list___4(String user_id);

	/**
	 * 查找目前可以孵化的批次
	 *
	 * @param user_id
	 * @return
	 */
	List<Farm> hatchMo_list__4(String user_id);

	/**
	 * 获取用户的排单（鸡苗批次）
	 *
	 * YES
	 *
	 * @param id
	 *            批次id
	 * @param user_id
	 *            用户id
	 * @return
	 */
	Farm getFeedById__2(String id, String user_id);

	/**
	 * 鸡苗（单个）及其喂食记录
	 *
	 * @param id
	 * @param user_id
	 * @return
	 */
	Farm feedMo_farm_feed_list___4(String id, String user_id);

	/**
	 * 鸡苗（单个）及其孵化记录
	 *
	 * @param id
	 * @param user_id
	 * @return
	 */
	Farm hatchMo_farm_hatch_list___4(String id, String user_id);

	/**
	 * 获取用户的最后一条买入记录（鸡苗批次）
	 *
	 * YES
	 *
	 * @param user_id
	 * @return
	 */
	Farm getLastByUserId__4(String user_id);

	void updateNum_deal(String id, int num_deal);

}
