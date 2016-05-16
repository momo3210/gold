package com.momohelp.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.momohelp.model.Sell;
import com.momohelp.model.User;
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
	 *
	 * 2、验证购买的数量是否是10的倍数
	 */
	@Override
	public String[] sell(Sell sell) {

		// 获取我的帐户信息（实时）
		User user = userService.selectByKey(sell.getUser_id());

		String[] checkNum = checkNum(user, sell);
		if (null != checkNum) {
			return checkNum;
		}

		save(sell);
		return null;
	}

	/**
	 * 检测购买的鸡苗数量是否合法
	 *
	 * @param user
	 * @param sell
	 * @return
	 */
	private String[] checkNum(User user, Sell sell) {
		// 静态10倍 静态500
		int radix = (1 == sell.getType_id()) ? 10 : 500;
		return (0 == sell.getNum_sell() % Integer.valueOf(radix)) ? null
				: new String[] { "输入的数量不正确" };
	}

}
