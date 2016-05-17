package com.momohelp.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.momohelp.model.MaterialRecord;
import com.momohelp.model.Sell;
import com.momohelp.model.User;
import com.momohelp.service.CfgService;
import com.momohelp.service.MaterialRecordService;
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

	@Autowired
	private MaterialRecordService materialRecordService;

	@Override
	public int save(Sell entity) {
		entity.setCreate_time(new Date());
		entity.setId(genId());
		return super.save(entity);
	}

	@Override
	public int updateNotNull(Sell entity) {
		entity.setId(null);
		return super.updateNotNull(entity);
	}

	/**
	 * 生成主键
	 *
	 * @return
	 */
	private String genId() {
		String id = null;
		Sell sell = null;
		do {
			// 算法
			int i = (int) ((Math.random() * 10 + 1) * 100000000);
			id = String.valueOf(i);
			// END
			sell = selectByKey(id);
		} while (null != sell);
		return id;
	}

	/**
	 * 步骤
	 *
	 * 1、验证购买的数量是否是10的倍数
	 *
	 * 2、根据动静调用不同的方法
	 *
	 * 3、判断用户的存款是否够卖
	 */
	@Override
	public String[] sell(Sell sell) {
		sell.setNum_sell((null == sell.getNum_sell()) ? 0 : sell.getNum_sell());
		if (1 > sell.getNum_sell()) {
			return new String[] { "卖出鸡苗数量不能为 0" };
		}

		// 获取我的帐户信息（实时）
		User user = userService.selectByKey(sell.getUser_id());

		String[] checkNum = checkNum(user, sell);
		if (null != checkNum) {
			return checkNum;
		}

		if (1 == sell.getType_id()) {
			return sell_static(user, sell);
		} else { // 动态钱包
			return sell_dynamic(user, sell);
		}
	}

	/**
	 * 卖出动态钱包
	 *
	 * @param user
	 * @param sell
	 * @return
	 */
	private String[] sell_dynamic(User user, Sell sell) {
		if (sell.getNum_sell() > user.getNum_dynamic()) {
			return new String[] { "动态钱包余额不足" };
		}

		sell_dynamic_material(user, sell);
		save(sell);
		return null;
	}

	/**
	 * 存入记录并更新用户的动态余额
	 *
	 * @param user
	 * @param sell
	 */
	private void sell_dynamic_material(User user, Sell sell) {
		MaterialRecord mr = new MaterialRecord();
		mr.setUser_id(user.getId());
		double d = sell.getNum_sell();
		mr.setNum_use(d);
		mr.setStatus(1);
		mr.setType_id(4);
		mr.setComment(null);
		mr.setTrans_user_id(null);
		mr.setNum_balance(user.getNum_dynamic() - sell.getNum_sell());
		mr.setFlag_plus_minus(0);

		materialRecordService.save(mr);

		User new_user = new User();
		new_user.setId(user.getId());
		new_user.setNum_dynamic(mr.getNum_balance());
		userService.updateNotNull(new_user);
	}

	/**
	 * 卖出静态钱包
	 *
	 * @param user
	 * @param sell
	 * @return
	 */
	private String[] sell_static(User user, Sell sell) {
		if (sell.getNum_sell() > user.getNum_static()) {
			return new String[] { "静态钱包余额不足" };
		}
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
