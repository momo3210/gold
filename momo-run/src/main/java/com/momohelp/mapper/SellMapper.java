package com.momohelp.mapper;

import java.util.Date;

import com.momohelp.model.Sell;
import com.momohelp.util.MyMapper;

/**
 *
 * @author Administrator
 *
 */
public interface SellMapper extends MyMapper<Sell> {
	 /**
     * 查询时间周期内所有的数据
     * @param time 开始时间
     * @param date 当前时间
     * @return
     */
	Sell selectByCycles(Date time, Date date);
}