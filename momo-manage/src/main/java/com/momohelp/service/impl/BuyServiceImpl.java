package com.momohelp.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.momohelp.model.Buy;
import com.momohelp.service.BuyService;

/**
 *
 * @author Administrator
 *
 */
@Service("buyService")
public class BuyServiceImpl extends BaseService<Buy> implements BuyService {

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
}
