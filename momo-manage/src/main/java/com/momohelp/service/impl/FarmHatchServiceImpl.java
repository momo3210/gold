package com.momohelp.service.impl;

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
		entity.setCreate_time(new Date());
		return super.save(entity);
	}

	@Override
	public int updateNotNull(FarmHatch entity) {
		entity.setId(null);
		return super.updateNotNull(entity);
	}

	@Override
	public List<FarmHatch> findByFarmId(String farm_id) {
		Example example = new Example(FarmHatch.class);
		example.setOrderByClause("create_time desc");
		// TODO
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("w_farm_chick_id", farm_id);

		return selectByExample(example);
	}

	@Override
	public String[] hatch(FarmHatch farmHatch) {

		farmHatch.setNum_hatch((null == farmHatch.getNum_hatch()) ? 0
				: farmHatch.getNum_hatch());
		if (1 > farmHatch.getNum_hatch()) {
			return new String[] { "孵化数量必须大于 0" };
		}

		Farm farm = farmService.findCanHatch(farmHatch.getW_farm_chick_id(),
				farmHatch.getUser_id());
		if (null == farm) {
			return new String[] { "不能孵化" };
		}

		if (farmHatch.getNum_hatch() > farm.getNum_current()) {
			return new String[] { "待孵化数量不足" };
		}

		Farm _farm = new Farm();
		_farm.setId(farmHatch.getW_farm_chick_id());
		_farm.setNum_current(farm.getNum_current() - farmHatch.getNum_hatch());
		farmService.updateNotNull(_farm);

		save(farmHatch);
		return null;
	}

}
