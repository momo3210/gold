package com.momohelp.calculate.service.impl;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.calculate.service.ILockAccount;
import com.momohelp.model.BuySell;
import com.momohelp.model.Sell;
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
		Example example = new Example(BuySell.class);
		Calendar cr = Calendar.getInstance();
		cr.add(Calendar.HOUR_OF_DAY, -(data));//
		// cr.add(Calendar.HOUR_OF_DAY, -(96));//
		cr.set(Calendar.MINUTE, 0);
		cr.set(Calendar.SECOND, 0);
		Calendar cr2 = Calendar.getInstance();
		cr2.add(Calendar.HOUR_OF_DAY, -(data));
		example.createCriteria().andNotEqualTo("p_buy_id", "null")
				.andNotEqualTo("p_sell_id", "null").andEqualTo("status", 0)
				.andBetween("create_time", cr.getTime(), cr2.getTime());
		List<BuySell> buySells = buySellService.selectByExample(example);
		for (BuySell buySell : buySells) {
			buySell.setStatus(3);// 标志为问题单
			buySellService.updateNotNull(buySell);
			User user = userService.selectByKey(buySell.getP_buy_user_id());
			user.setStatus(2);// 冻结账号
			userService.updateNotNull(user);// 冻结用户 但是可以登录申诉中心
			Calendar cr3 = Calendar.getInstance();
			cr3.add(Calendar.HOUR_OF_DAY, sellData);//
			Sell sell = sellService.selectByKey(buySell.getP_sell_id());
			sell.setCreate_time(cr3.getTime());
			sell.setId(genId());
			sell.setNum_sell(buySell.getNum_matching());
			sell.setNum_deal(0);
			sell.setFlag_calc_bonus(0);
			sellService.save(sell);
		}
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
			if (9 < id.length()) {
				id = id.substring(0, 9);
			}
			id = "S" + id;
			sell = sellService.selectByKey(id);
		} while (null != sell);
		return id;
	}
}
