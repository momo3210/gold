package com.momohelp.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.model.BuySell;
import com.momohelp.model.Farm;
import com.momohelp.model.FarmHatch;
import com.momohelp.service.BuySellService;
import com.momohelp.service.FarmHatchService;
import com.momohelp.service.FarmService;

/**
 *
 * @author Administrator
 *
 */
@Service("farmHatchService")
public class FarmHatchServiceImpl extends BaseService<FarmHatch> implements
		FarmHatchService {

	@Autowired
	private FarmService farmService;

	@Autowired
	private BuySellService buySellService;

	@Override
	public String[] hatch(FarmHatch farmHatch) {

		FarmHatch _farmHatch = new FarmHatch();
		_farmHatch.setUser_id(farmHatch.getUser_id());
		_farmHatch.setCreate_time(new Date());

		// 冻结24小时
		Calendar c = Calendar.getInstance();
		c.setTime(_farmHatch.getCreate_time());
		c.add(Calendar.HOUR_OF_DAY, 48);
		_farmHatch.setFreeze_time(c.getTime());

		_farmHatch.setNum_hatch((null == farmHatch.getNum_hatch()) ? 0
				: farmHatch.getNum_hatch());

		_farmHatch.setW_farm_chick_id(farmHatch.getW_farm_chick_id());
		_farmHatch.setFlag_calc_bonus(0);

		/**/

		if (1 > _farmHatch.getNum_hatch()) {
			return new String[] { "孵化数量必须大于0", "401" };
		}

		// 100的倍数
		if (0 != _farmHatch.getNum_hatch() % 100) {
			return new String[] { "请输入规定的数量", "402" };
		}

		/**/

		// 查找鸡苗批次
		Farm farm = farmService.hatchMo_farm_hatch_list___4(
				_farmHatch.getW_farm_chick_id(), _farmHatch.getUser_id());

		if (null == farm) {
			return new String[] { "数据查询异常", "403" };
		}

		/**/

		// 未完全交易成功，所以不能孵化
		if (farm.getNum_buy().intValue() != farm.getNum_deal().intValue()) {
			return new String[] { "不能孵化", "404" };
		}

		if (0 == farm.getNum_current()) {
			return new String[] { "库存不足", "405" };
		}

		if (_farmHatch.getNum_hatch() > farm.getNum_current()) {
			return new String[] { "待孵化数量不足", "406" };
		}

		// 更新鸡苗孵化之后的剩余数量
		Farm _farm = new Farm();
		_farm.setId(_farmHatch.getW_farm_chick_id());
		_farm.setNum_current(farm.getNum_current() - _farmHatch.getNum_hatch());
		farmService.updateNotNull(_farm);

		// 最后一笔孵化
		_farmHatch.setFlag_is_last(0 == _farm.getNum_current() ? 1 : 0);

		// 计算奖金
		calcReward(_farmHatch.getW_farm_chick_id());

		save(_farmHatch);
		return null;
	}

	/**
	 * 计算奖金（买方3小时内打款则有1%的奖金，在最后一笔孵化的时候产生）
	 */
	private void calcReward(String farm_id) {

		// 根据 farm_id 获取所有匹配记录
		List<BuySell> list_buySell = buySellService.findByFarmId__4(farm_id);

		for (int i = 0, j = list_buySell.size(); i < j; i++) {
			BuySell buySell = list_buySell.get(i);

			Calendar c = Calendar.getInstance();
			c.setTime(buySell.getCreate_time());
			// 创建时间加3小时
			c.add(Calendar.HOUR_OF_DAY, 3);

			// 加上3小时的时间在买家打款之前，说明买家在3小时之后打的款
			if (c.getTime().before(buySell.getP_buy_user_time())) {
				return;
			}
		}

		Farm _farm = farmService.selectByKey(farm_id);

		Farm farm = new Farm();
		farm.setId(farm_id);
		farm.setNum_reward(_farm.getNum_buy() / 100);

		farmService.updateNotNull(farm);
	}

	/**
	 * 动态奖金清零（根据最后一笔孵化才执行，判断是否有新的排单）
	 */
	private void clearZeroByDynamic(String farm_id, String user_id) {
		Example example = new Example(FarmHatch.class);
		example.setOrderByClause("create_time desc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("w_farm_chick_id", farm_id);
	}

	@Override
	public List<FarmHatch> findByFarmId___4(String farm_id) {

		Example example = new Example(FarmHatch.class);
		example.setOrderByClause("create_time desc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("w_farm_chick_id", farm_id);

		return selectByExample(example);
	}

	@Override
	public List<FarmHatch> findFlagByUserId___4(String user_id) {

		Example example = new Example(FarmHatch.class);

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);
		criteria.andEqualTo("flag_is_last", 1);
		criteria.andEqualTo("flag_calc_bonus", 0);

		return selectByExample(example);

	}

}
