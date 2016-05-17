package com.momohelp.mapper;

import java.util.Date;
import java.util.List;

import com.momohelp.model.Farm;
import com.momohelp.util.MyMapper;

/**
 *
 * @author Administrator
 *
 */
public interface FarmMapper extends MyMapper<Farm> {
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
	List<Farm> selectLastFarmByDate(String id, Date create_time);
	
	/**
	 * 获取自动到期 ，出局排单
	 * @return
	 */
	List<Farm> selectForceLogout(Date date);
	
}