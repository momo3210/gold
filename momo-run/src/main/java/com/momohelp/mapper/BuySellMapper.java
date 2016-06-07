package com.momohelp.mapper;

import java.util.List;

import com.momohelp.model.BuySell;
import com.momohelp.util.MyMapper;


/**
 *
 * @author Administrator
 *
 */
public interface BuySellMapper extends MyMapper<BuySell> {
	/***
	 * 获取当前买卖明细中，还没有通过匹配记录
	 * @return
	 */
	List<BuySell> selectBySellAndBuyId();
	
	/***
	 * 获取当前卖明细中，还没有通过匹配记录
	 * @return
	 */
	List<BuySell> selectBySell();
	
	/***
	 * 获取当前买明细中，还没有通过匹配记录
	 * @return
	 */
	List<BuySell> selectByBuy();
	
	/***
	 * 获取当前买明细中，已经匹配但是超过48小时 还没有打款的数据
	 * @return
	 */
	List<BuySell> lockAccount(int hour);
	
	/***
	 * 更新准确买卖时间--小时
	 * @param date
	 */
	void updateTimeHour(String  date);
	/***
	 * 更新准确买卖时间--秒
	 * @param date
	 */
	void updateTimeSecond(String  date);
	/***
	 * 更新准确买卖时间--分钟
	 * @param date
	 */
	void updateTimeMinute(String  date);
}