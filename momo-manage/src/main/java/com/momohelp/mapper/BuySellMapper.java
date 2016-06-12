package com.momohelp.mapper;

import java.util.List;
import java.util.Map;

import com.momohelp.model.BuySell;
import com.momohelp.util.MyMapper;

/**
 *
 * @author Administrator
 *
 */
public interface BuySellMapper extends MyMapper<BuySell> {

	List<BuySell> findByFarmId(Map<String, Object> map);
}