package com.momohelp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.model.Prize;
import com.momohelp.service.PrizeService;

@Service("prizeService")
public class PrizeServiceImpl extends BaseService<Prize> implements
		PrizeService {

	@Override
	public List<Prize> findByUserId(String user_id) {
		Example example = new Example(Prize.class);
		// TODO
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("user_id", user_id);

		List<Prize> list = selectByExample(example);
		return list;
	}

}
