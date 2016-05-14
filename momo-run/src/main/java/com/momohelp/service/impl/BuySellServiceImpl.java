package com.momohelp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

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
		
		return ((BuySellService) mapper).selectBySellAndBuyId();
	}

}
