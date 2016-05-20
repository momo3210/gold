package com.momohelp.calculate.service;

import java.util.List;

import com.momohelp.model.Farm;


public interface ICalculation {

    //奖金基数计算
	public abstract double base();
	//买卖盘自动匹配
	public abstract  boolean automatch();
	//等级计算
	public abstract  boolean calculateLevel(List<Farm> farms);
	//用户强制出局计算
	public abstract  boolean calculateForceLogout();
    //自动转换到期奖金金额
	public abstract  boolean triggerConversion();

}