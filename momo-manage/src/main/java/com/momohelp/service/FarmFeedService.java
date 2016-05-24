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
	 * 喂饲料
	 *
	 * @param farmFeed
	 * @return
	 */
	@Transactional
	String[] feed(FarmFeed farmFeed);

	/**
	 * 获取鸡苗批次关联的喂食记录
	 *
	 * YES
	 *
	 * @param farm_id
	 * @return
	 */
	List<FarmFeed> findByFarmId__1(String farm_id);

	/**
	 * 判断今天是否已经喂过鸡了
	 *
	 * @param farmFeed
	 * @return
	 */
	String[] checkTodayFeed__1(FarmFeed farmFeed);

}
