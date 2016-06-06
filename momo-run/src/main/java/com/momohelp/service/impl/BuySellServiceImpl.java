package com.momohelp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.momohelp.mapper.BuySellMapper;
import com.momohelp.model.BuySell;
import com.momohelp.service.BuySellService;

/**
 *
 * @author Administrator
 *
 */
@Service("buySellService")
public class BuySellServiceImpl extends BaseService<BuySell> implements
		BuySellService {

	@Override
	public List<BuySell> selectBySellAndBuyId() {
		return ((BuySellMapper) mapper).selectBySellAndBuyId();
	}

	@Override
	public List<BuySell> selectBySell() {
		
		return ((BuySellMapper) mapper).selectBySell();
	}

	@Override
	public List<BuySell> selectByBuy() {
		return ((BuySellMapper) mapper).selectByBuy();
	}

	@Override
	public List<BuySell> lockAccount(int hour) {
		return ((BuySellMapper) mapper).lockAccount( hour);
	}

}
