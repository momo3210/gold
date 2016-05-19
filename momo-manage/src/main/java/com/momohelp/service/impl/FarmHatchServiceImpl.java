package com.momohelp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.model.FarmHatch;
import com.momohelp.service.FarmHatchService;

/**
 *
 * @author Administrator
 *
 */
@Service("farmHatchService")
public class FarmHatchServiceImpl extends BaseService<FarmHatch> implements
		FarmHatchService {

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

}
