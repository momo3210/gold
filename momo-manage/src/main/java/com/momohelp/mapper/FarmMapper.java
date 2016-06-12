package com.momohelp.mapper;

import java.util.List;
import java.util.Map;

import com.momohelp.model.Farm;
import com.momohelp.util.MyMapper;

/**
 *
 * @author Administrator
 *
 */
public interface FarmMapper extends MyMapper<Farm> {

	/**
	 * 查找用户是否有新的排单
	 *
	 * @param map
	 * @return
	 */
	List<Farm> findNewByUserId(Map<String, Object> map);

	List<Farm> findRewardByUserId(Map<String, Object> map);

	List<Farm> findUnDealByUserId(Map<String, Object> map);

	List<Farm> findHatchByUserId(Map<String, Object> map);

	void updateNum_deal(Farm farm);
}