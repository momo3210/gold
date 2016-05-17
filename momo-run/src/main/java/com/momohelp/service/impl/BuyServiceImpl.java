package com.momohelp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.momohelp.mapper.BuyMapper;
import com.momohelp.model.Buy;
import com.momohelp.service.BuyService;

/**
 *
 * @author Administrator
 *
 */
@Service("buyService")
public class BuyServiceImpl extends BaseService<Buy> implements BuyService {

	@Override
	public List<Buy> selectByCycles(Date time, Date time2) {
		
		return ((BuyMapper)mapper).selectByCycles(time, time2);
	}

}
