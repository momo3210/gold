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
	 * 显示某一个批次下的全部喂食记录
	 *
	 * @param farm_id
	 *            鸡苗批次主键
	 * @return
	 */
	List<FarmFeed> findByFarmId___4(String farm_id);

	/**
	 * 判断今天是否已经喂过鸡了
	 *
	 * @param farmFeed
	 * @return
	 */
	String[] checkTodayFeed__1(FarmFeed farmFeed);

	/**
	 * 判断今天是否已经喂过鸡了
	 *
	 * @param feedDate
	 *            喂鸡时间
	 * @return
	 */
	String[] checkTodayFeed___4(FarmFeed farmFeed);

}
