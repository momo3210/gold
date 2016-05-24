package com.momohelp.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.model.Farm;
import com.momohelp.model.FarmHatch;
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

	@Override
	public String[] hatch(FarmHatch farmHatch) {

		FarmHatch _farmHatch = new FarmHatch();
		_farmHatch.setUser_id(farmHatch.getUser_id());
		_farmHatch.setCreate_time(new Date());

		// 冻结24小时
		Calendar c = Calendar.getInstance();
		c.setTime(_farmHatch.getCreate_time());
		c.add(Calendar.HOUR_OF_DAY, 24);
		_farmHatch.setFreeze_time(c.getTime());

		_farmHatch.setNum_hatch((null == farmHatch.getNum_hatch()) ? 0
				: farmHatch.getNum_hatch());

		_farmHatch.setW_farm_chick_id(farmHatch.getW_farm_chick_id());
		_farmHatch.setFlag_calc_bonus(0);

		/**/

		if (1 > _farmHatch.getNum_hatch()) {
			return new String[] { "孵化数量必须大于0" };
		}

		// 100的倍数
		if (0 != _farmHatch.getNum_hatch() % 100) {
			return new String[] { "请输入规定的数量" };
		}

		/**/

		// 查找鸡苗批次
		Farm farm = farmService.getByUserId(_farmHatch.getW_farm_chick_id(),
				_farmHatch.getUser_id());

		if (null == farm) {
			return new String[] { "数据查询异常" };
		}

		/**/

		// 未完全交易成功，所以不能孵化
		if (farm.getNum_buy() != farm.getNum_deal()) {
			return new String[] { "不能孵化" };
		}

		if (0 == farm.getNum_current()) {
			return new String[] { "库存不足" };
		}

		if (_farmHatch.getNum_hatch() > farm.getNum_current()) {
			return new String[] { "待孵化数量不足" };
		}

		// 更新鸡苗孵化之后的剩余数量
		Farm _farm = new Farm();
		_farm.setId(_farmHatch.getW_farm_chick_id());
		_farm.setNum_current(farm.getNum_current() - _farmHatch.getNum_hatch());
		farmService.updateNotNull(_farm);

		// 最后一笔孵化
		_farmHatch.setFlag_is_last(0 == _farm.getNum_current() ? 1 : 0);

		save(_farmHatch);
		return null;
	}

	@Override
	public List<FarmHatch> findByFarmId(String farm_id) {

		Example example = new Example(FarmHatch.class);
		example.setOrderByClause("create_time desc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("w_farm_chick_id", farm_id);

		return selectByExample(example);
	}

}
