package com.momohelp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.momohelp.mapper.SellMapper;
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
		return ((SellMapper)mapper).selectByCycles(time, date);
	}

	@Override
	public void updateFlagCalc(String key) {
		((SellMapper)mapper).updateFlagCalc(key);
		
	}

}
