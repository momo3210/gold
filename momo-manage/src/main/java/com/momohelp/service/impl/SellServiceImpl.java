package com.momohelp.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.momohelp.model.Sell;
import com.momohelp.service.CfgService;
import com.momohelp.service.SellService;
import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Service("sellService")
public class SellServiceImpl extends BaseService<Sell> implements SellService {

	@Autowired
	private UserService userService;

	@Autowired
	private CfgService cfgService;

	@Override
	public int save(Sell entity) {
		entity.setCreate_time(new Date());
		return super.save(entity);
	}

	@Override
	public int updateNotNull(Sell entity) {
		entity.setUser_id(null);
		entity.setNum_sell(null);
		entity.setCreate_time(null);
		entity.setType_id(null);
		return super.updateNotNull(entity);
	}

	/**
	 * 步骤
	 */
	@Override
	public String[] sell(Sell sell) {

		save(sell);
		return null;
	}

}
