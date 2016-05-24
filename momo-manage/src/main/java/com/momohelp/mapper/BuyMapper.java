package com.momohelp.mapper;

import java.util.List;
import java.util.Map;

import com.momohelp.model.Buy;
import com.momohelp.util.MyMapper;

/**
 *
 * @author Administrator
 *
 */
public interface BuyMapper extends MyMapper<Buy> {

	List<Buy> findUnDealByUserId(Map<String, Object> map);

	void updateNum_deal(Buy buy);
}