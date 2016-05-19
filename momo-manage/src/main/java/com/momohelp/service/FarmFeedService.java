package com.momohelp.service;

import org.springframework.transaction.annotation.Transactional;

import com.momohelp.model.FarmFeed;

/**
 *
 * @author Administrator
 *
 */
public interface FarmFeedService extends IService<FarmFeed> {

	/**
	 * 计算利息
	 *
	 * @param farm_id
	 * @return
	 */
	double dividend(String farm_id);

	/**
	 * 喂饲料
	 *
	 * @param farm_id
	 * @return
	 */
	@Transactional
	String[] feed(FarmFeed farmFeed);
}
