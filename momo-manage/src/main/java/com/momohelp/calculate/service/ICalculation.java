package com.momohelp.calculate.service;

import com.momohelp.model.User;

public interface ICalculation {

	// 推荐奖计算
	public abstract void recommend(User user);

	// 管理奖计算
	public abstract void manage(User user);
    //烧伤奖计算
	public abstract void burn(User user);

	// 鸡饲料计算
	public abstract void feed(User user);

	// 下蛋利息计算
	public abstract void egg();

	// 鸡苗饲养利息计算
	public abstract void keepe();

}