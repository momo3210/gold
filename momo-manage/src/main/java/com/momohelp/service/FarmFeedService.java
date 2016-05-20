package com.momohelp.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.momohelp.model.FarmFeed;

/**
 *
 * @author Administrator
 *
 */
public interface FarmFeedService extends IService<FarmFeed> {

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
	 * 根据批次ID判断今天是否已经喂过鸡
	 *
	 * @param farm_id
	 * @return
	 */
	Map<String, Object> checkTodayFeed(String farm_id);
}
