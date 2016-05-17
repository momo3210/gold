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
}