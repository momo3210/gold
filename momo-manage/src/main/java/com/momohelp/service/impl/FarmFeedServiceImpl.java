package com.momohelp.service.impl;

import java.util.List;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.model.FarmFeed;
import com.momohelp.service.FarmFeedService;

/**
 *
 * @author Administrator
 *
 */
public class FarmFeedServiceImpl extends BaseService<FarmFeed> implements
		FarmFeedService {

	@Override
	public double dividend(String farm_id) {
		Example example = new Example(FarmFeed.class);
		// TODO
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("w_farm_chick_id", farm_id);

		List<FarmFeed> list = selectByExample(example);

		double count = 0.00;

		if (null == list) {
			return count;
		}

		for (int i = 0, j = list.size(); i < j; i++) {
			FarmFeed item = list.get(i);
			count += item.getPrice();
		}

		return count;
	}

}
