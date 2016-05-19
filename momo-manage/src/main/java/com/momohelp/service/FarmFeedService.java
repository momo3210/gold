package com.momohelp.service;

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
}
