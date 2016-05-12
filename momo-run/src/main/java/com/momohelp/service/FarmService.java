package com.momohelp.service;

import java.util.List;

import com.momohelp.model.Farm;

/**
 *
 * @author Administrator
 *
 */
public interface FarmService extends IService<Farm> {
	/***
	 * 未处理的提成的单据
	 * 
	 * @return
	 */
	List<Farm> getUntreatedFarm();
	/***
	 * 根据用户id 获取最新排单
	 * 
	 * @return
	 */
	List<Farm> selectLastFarmByDate(String key);
}
