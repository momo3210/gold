package com.momohelp.service;

import java.util.Date;
import java.util.List;

import com.momohelp.model.Buy;

/**
 *
 * @author Administrator
 *
 */
public interface BuyService extends IService<Buy> {
	
	/**
	 * 获取两个时间之间的数据
	 * @param time
	 * @param time2
	 * @return
	 */
	List<Buy> selectByCycles(Date time, Date time2);
    /***
     * 更新 操作数据状态为 1
     * @param id  主键
     */
	void updateFlagCalc(String id);

}
