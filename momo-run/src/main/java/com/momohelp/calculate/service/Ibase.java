package com.momohelp.calculate.service;

import java.util.List;

import com.momohelp.model.Farm;

public interface Ibase {
	// 奖金基数计算
	public abstract double base();

	// 等级计算
	public abstract boolean calculateLevel(List<Farm> farms);
}
