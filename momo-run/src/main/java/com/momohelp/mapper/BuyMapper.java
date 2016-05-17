package com.momohelp.mapper;

import java.util.Date;
import java.util.List;

import com.momohelp.model.Buy;
import com.momohelp.util.MyMapper;

/**
 *
 * @author Administrator
 *
 */
public interface BuyMapper extends MyMapper<Buy> {
	/**
	 * 获取两个时间之间的数据
	 * @param time
	 * @param time2
	 * @return
	 */
	List<Buy> selectByCycles(Date time, Date time2);
}