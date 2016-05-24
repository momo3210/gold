package com.momohelp.mapper;

import java.util.List;
import java.util.Map;

import com.momohelp.model.Sell;
import com.momohelp.util.MyMapper;

/**
 *
 * @author Administrator
 *
 */
public interface SellMapper extends MyMapper<Sell> {

	List<Sell> findUnDealByUserId(Map<String, Object> map);

	void updateNum_deal(Sell sell);
}