package com.momohelp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.momohelp.model.Sell;
import com.momohelp.service.SellService;

/**
 *
 * @author Administrator
 *
 */
@Service("sellService")
public class SellServiceImpl extends BaseService<Sell> implements SellService {

	@Override
	public List<Sell> selectByCycles(Date time, Date date) {
		return ((SellService) mapper).selectByCycles(time, date);
	}

}
