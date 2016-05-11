package com.momohelp.calculate.service;

import java.util.List;

import com.momohelp.model.Farm;


public interface ICalculation {

    //奖金基数计算
	public abstract int base();

	//买卖盘自动匹配
	public abstract  boolean automatch();
	
	//等级计算
	public abstract  boolean calculateLevel(List<Farm> farms);

}