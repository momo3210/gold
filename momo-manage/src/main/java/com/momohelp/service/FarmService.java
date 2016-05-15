package com.momohelp.service;

import com.momohelp.model.Farm;

/**
 *
 * @author Administrator
 *
 */
public interface FarmService extends IService<Farm> {

	/**
	 * 买入鸡苗
	 *
	 * @param farm
	 * @return
	 */
	String[] buy(Farm farm);
}
