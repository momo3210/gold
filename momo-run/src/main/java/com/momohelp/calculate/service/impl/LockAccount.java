package com.momohelp.calculate.service.impl;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.calculate.service.ILockAccount;
import com.momohelp.model.BuySell;
import com.momohelp.model.User;
import com.momohelp.service.BuySellService;
import com.momohelp.service.CfgService;
import com.momohelp.service.SellService;
import com.momohelp.service.UserService;

@Service
public class LockAccount implements ILockAccount {

	private static final long serialVersionUID = 4255951746782373554L;
	private static Logger log = Logger.getLogger(LockAccount.class);

	@Resource
	private BuySellService buySellService;

	@Resource
	private UserService userService;

	@Resource
	private SellService sellService;

	@Resource
	private CfgService cfgService;

	@Override
	public void lockAccount() {
		log.info("----------------锁定账号并且重新排卖家数据----------------------");
		int data = Integer.parseInt(cfgService.selectByKey("4001").getValue_());
		int sellData = Integer.parseInt(cfgService.selectByKey("4002")
				.getValue_());
		List<BuySell> buySells = buySellService.lockAccount(data);
		for (BuySell buySell : buySells) {
			upateBuySellStatus(buySell,sellData);// 更新当前用户对应单据的状态为0单据 设置为问题单==3
			updateUserStatus(buySell.getP_buy_user_id());// 更新当前用户状态
		}
	}
	private void upateBuySellStatus(BuySell buySell,int sellData) {
		if (null != buySell) {
			Example example = new Example(BuySell.class);
			example.createCriteria().andEqualTo("status", 0)
					.andEqualTo("p_buy_user_id", buySell.getP_buy_user_id());
			List<BuySell> buySells= buySellService.selectByExample(example);
			for (BuySell buySell2 : buySells) {
				buySell2.setStatus(4);// 标志为匹配48小时不打款
				buySellService.updateNotNull(buySell2);
			}
			for (BuySell buySell2 : buySells) {
				insertIntoSell(buySell2, sellData);// 重新生成卖单数据 重新排单开始时间==当前时间延迟24小时
			}
		}
	}
	private void insertIntoSell(BuySell buySell, int sellData) {
		Calendar cr3 = Calendar.getInstance();
		if (sellData!=0) {
			cr3.add(Calendar.HOUR_OF_DAY, sellData);//
		}
		buySell.setId(genId());
		buySell.setP_buy_id("null");
		buySell.setP_buy_user_id("null");
		buySell.setCreate_time(cr3.getTime()); 
		buySell.setStatus(0);
		buySellService.save(buySell);
//		Sell sell = sellService.selectByKey(buySell.getP_sell_id());
//		sell.setCreate_time(new Date());
//		sell.setId(genId());
//		sell.setNum_sell(buySell.getNum_matching());
//		sell.setNum_deal(0);
//		sell.setCalc_time(cr3.getTime());
//		sell.setFlag_calc_bonus(0);
//		sellService.save(sell);

	}
	private void updateUserStatus(String p_buy_user_id) {
		if (null != p_buy_user_id && p_buy_user_id.trim().length() > 0) {
			User user = userService.selectByKey(p_buy_user_id);
			if (null != user) {
				user.setStatus(2);// 冻结账号
				userService.updateNotNull(user);// 冻结用户 但是可以登录申诉中心
			}
		}

	}
	/**
	 * 生成主键
	 *
	 * @return
	 */
	private String genId() {
		String id = null;
		BuySell buySell = null;
		do {
			// 算法
			int i = (int) ((Math.random() * 10 + 1) * 100000000);
			id = String.valueOf(i);
			if (9 < id.length()) {
				id = id.substring(0, 9);
			}
			id = "P" + id;
			buySell = buySellService.selectByKey(id);
		} while (null != buySell);
		return id;
	}
}
