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
	public int save(FarmHatch entity) {
		entity.setFlag_calc_bonus(0);
		return super.save(entity);
	}

	@Override
	public int updateNotNull(FarmHatch entity) {
		return super.updateNotNull(entity);
	}

	@Override
	public List<FarmHatch> findByFarmId(String farm_id) {
		Example example = new Example(FarmHatch.class);
		example.setOrderByClause("create_time desc");

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("w_farm_chick_id", farm_id);

		return selectByExample(example);
	}

	@Override
	public String[] hatch(FarmHatch farmHatch) {
		farmHatch.setCreate_time(new Date());

		farmHatch.setNum_hatch((null == farmHatch.getNum_hatch()) ? 0
				: farmHatch.getNum_hatch());
		if (1 > farmHatch.getNum_hatch()) {
			return new String[] { "孵化数量必须大于0" };
		} // if

		// 100的倍数
		if (0 != farmHatch.getNum_hatch() % 100) {
			return new String[] { "请输入规定的数量" };
		} // if

		// 查找鸡苗批次
		Farm farm = farmService
				.getByFarm(1, new Farm(farmHatch.getW_farm_chick_id(),
						farmHatch.getUser_id()));

		if (null == farm) {
			return new String[] { "数据查询异常" };
		} // if

		// 未完全交易成功，所以不能孵化
		if (null == farm.getTime_deal()) {
			return new String[] { "不能孵化" };
		} // if

		if (0 == farm.getNum_current()) {
			return new String[] { "库存不足" };
		} // if

		if (farmHatch.getNum_hatch() > farm.getNum_current()) {
			return new String[] { "待孵化数量不足" };
		} // if

		// 更新鸡苗批次表
		Farm _farm = new Farm();
		_farm.setId(farmHatch.getW_farm_chick_id());
		_farm.setNum_current(farm.getNum_current() - farmHatch.getNum_hatch());
		farmService.updateNotNull(_farm);

		// 最后一笔孵化
		// 需要+利息+奖金
		farmHatch.setFlag_is_last(0 == _farm.getNum_current() ? 1 : 0);

		// 冻结24小时
		Calendar c = Calendar.getInstance();
		c.setTime(farmHatch.getCreate_time());
		c.add(Calendar.HOUR_OF_DAY, 24);
		farmHatch.setFreeze_time(c.getTime());

		save(farmHatch);
		return null;
	}

}
