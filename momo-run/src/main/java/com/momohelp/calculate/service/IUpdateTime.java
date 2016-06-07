package com.momohelp.calculate.service;

import java.io.Serializable;

public interface IUpdateTime extends Serializable {
	/***
	 * 更新准确买卖时间
	 * 
	 * @param date
	 */
	void updateTime();
}
