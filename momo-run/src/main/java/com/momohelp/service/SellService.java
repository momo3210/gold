package com.momohelp.service;

import java.util.Date;
import java.util.List;

import com.momohelp.model.Sell;

/**
 *
 * @author Administrator
 *
 */
public interface SellService extends IService<Sell> {
    /**
     * 查询时间周期内所有的数据
     * @param time 开始时间
     * @param date 当前时间
     * @return
     */
	List<Sell> selectByCycles(Date time, Date date);
    /***
     * 更新计算状态
     * @param key  计算状态值
     */
	void updateFlagCalc(String key);

}
