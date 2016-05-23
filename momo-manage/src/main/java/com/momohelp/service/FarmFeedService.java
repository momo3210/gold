package com.momohelp.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.momohelp.model.FarmFeed;

/**
 *
 * @author Administrator
 *
 */
public interface FarmFeedService extends IService<FarmFeed> {

	/**
	 * 获取鸡苗批次的喂食记录
	 *
	 * @param farm_id
	 *            鸡苗批次id
	 * @return
	 */
	List<FarmFeed> findByFarmId(String farm_id);

	/**
	 * 计算利息（只计算了喂饲料产生的利息，未加批次表中的奖金字段 num_reward）
	 *
	 * @param farm_id
	 * @return
	 */
	double calculateInterest(String farm_id);

	/**
	 * 喂饲料
	 *
	 * @param farm_id
	 * @return
	 */
	@Transactional
	String[] feed(FarmFeed farmFeed);

	/**
	 * 判断今天是否已经喂过鸡
	 *
	 * false今天没喂过
	 *
	 * true今天喂过
	 *
	 * @param farmFeeds
	 * @return
	 */
	boolean checkTodayFeed(List<FarmFeed> farmFeeds);
}
