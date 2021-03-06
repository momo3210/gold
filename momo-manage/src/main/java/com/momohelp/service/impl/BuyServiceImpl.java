package com.momohelp.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.mapper.BuyMapper;
import com.momohelp.model.Buy;
import com.momohelp.model.BuySell;
import com.momohelp.model.User;
import com.momohelp.service.BuySellService;
import com.momohelp.service.BuyService;
import com.momohelp.service.FarmService;
import com.momohelp.service.UserService;
import com.momohelp.util.StringUtil;

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

	@Autowired
	private FarmService farmService;

	@Override
	public int save(Buy entity) {
		entity.setId(genId());
		entity.setCreate_time(new Date());
		return super.save(entity);
	}

	@Override
	public int updateNotNull(Buy entity) {
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
				id = id.substring(0, 9);
			}
			id = "B" + id;
			buy = selectByKey(id);
		} while (null != buy);
		return id;
	}

	@Override
	public List<Buy> findUnFinishDeal(String user_id) {
		// Example example = new Example(Buy.class);
		// example.setOrderByClause("create_time desc");
		//
		// // 显示24小时内的
		// Calendar c = Calendar.getInstance();
		// c.add(Calendar.HOUR_OF_DAY, -24);
		//
		// example.or().andEqualTo("user_id", user_id)
		// .andGreaterThan("time_deal", c.getTime());
		// example.or().andEqualTo("user_id", user_id).andIsNull("time_deal");
		//
		// List<Buy> list_buy = selectByExample(example);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("user_id", user_id);

		List<Buy> list_buy = ((BuyMapper) getMapper()).findUnDealByUserId(map);

		if (null == list_buy) {
			return list_buy;
		}

		for (int i = 0, j = list_buy.size(); i < j; i++) {
			Buy buy = list_buy.get(i);
			buy.setBuySells(buySellService.findByBuyId__4(buy.getId()));

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

	@Override
	public List<Buy> findByFarmId__4(String farm_id) {
		Example example = new Example(Buy.class);

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("w_farm_chick_id", farm_id);

		List<Buy> list = selectByExample(example);

		if (null == list) {
			return null;
		}

		for (int i = 0, j = list.size(); i < j; i++) {
			Buy buy = list.get(i);
			buy.setBuySells(buySellService.findByBuyId__4(buy.getId()));
		}

		return list;
	}

	@Override
	public Buy getId(String id) {

		Buy buy = selectByKey(id);

		if (null == buy) {
			return null;
		}

		buy.setFarm(farmService.selectByKey(buy.getW_farm_chick_id()));

		return buy;
	}

	@Override
	public void updateNum_deal__4(String id, int num_deal) {
		Buy buy = new Buy();
		buy.setId(id);
		buy.setNum_deal(num_deal);
		((BuyMapper) getMapper()).updateNum_deal(buy);
	}

	private static final SimpleDateFormat sdf_2 = new SimpleDateFormat(
			"yyyy-MM-dd");

	@Override
	public List<Buy> findByBuy__4(Buy buy, int page, int rows) {

		Example example = new Example(Buy.class);
		example.setOrderByClause("create_time DESC, is_deposit DESC");

		if (null != buy) {
			Example.Criteria criteria = example.createCriteria();

			String user_id = StringUtil.isEmpty(buy.getUser_id());
			if (null != user_id) {
				criteria.andEqualTo("user_id", user_id);
			}

			criteria.andLike("create_time", sdf_2.format(buy.getCreate_time())
					+ "%");
		}

		// PageHelper.startPage(page, rows);

		List<Buy> list = selectByExample(example);

		for (int i = 0, j = list.size(); i < j; i++) {
			Buy item = list.get(i);
			item.setUser(userService.selectByKey(item.getUser_id()));
		}

		return list;
	}
}
