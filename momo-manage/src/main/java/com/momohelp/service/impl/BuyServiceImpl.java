package com.momohelp.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.model.Buy;
import com.momohelp.model.BuySell;
import com.momohelp.model.User;
import com.momohelp.service.BuySellService;
import com.momohelp.service.BuyService;
import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Service("buyService")
public class BuyServiceImpl extends BaseService<Buy> implements BuyService {

	@Autowired
	private BuySellService buySellService;

	@Autowired
	private UserService userService;

	@Override
	public int save(Buy entity) {
		entity.setId(genId());
		entity.setCreate_time(new Date());
		return super.save(entity);
	}

	@Override
	public int updateNotNull(Buy entity) {
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
		Buy buy = null;
		do {
			// 算法
			int i = (int) ((Math.random() * 10 + 1) * 100000000);
			id = String.valueOf(i);
			if (9 < id.length()) {
				id.substring(0, 9);
			}
			// END
			buy = selectByKey(id);
		} while (null != buy);
		return id;
	}

	@Override
	public List<Buy> findUnFinishDeal(String user_id) {
		Example example = new Example(Buy.class);
		example.setOrderByClause("create_time desc");

		// 显示24小时内的
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, -24);

		example.or().andEqualTo("user_id", user_id)
				.andGreaterThan("time_deal", c.getTime());
		example.or().andEqualTo("user_id", user_id).andIsNull("time_deal");

		List<Buy> list_buy = selectByExample(example);

		if (null == list_buy) {
			return list_buy;
		}

		for (int i = 0, j = list_buy.size(); i < j; i++) {
			Buy buy = list_buy.get(i);
			buy.setBuySells(buySellService.findByBuyId(buy.getId()));

			List<BuySell> list_buySell = buy.getBuySells();
			if (null == list_buySell) {
				continue;
			}

			// 获取用户对象和其父对象
			for (int m = 0, n = list_buySell.size(); m < n; m++) {
				BuySell buySell = list_buySell.get(m);

				// 卖盘
				User sell_user = userService.selectByKey(buySell
						.getP_sell_user_id());
				buySell.setP_sell_user(sell_user);

				if (!"0".equals(sell_user.getPid())) {
					User p_sell_user = userService.selectByKey(sell_user
							.getPid());
					sell_user.setP_user(p_sell_user);
				}
			}
		}

		return list_buy;
	}
}
