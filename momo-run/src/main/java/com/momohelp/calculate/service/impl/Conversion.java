package com.momohelp.calculate.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.calculate.service.IConversion;
import com.momohelp.model.Prize;
import com.momohelp.model.User;
import com.momohelp.service.PrizeService;
import com.momohelp.service.UserService;
@Service
public class Conversion implements Serializable, IConversion {

	private static final long serialVersionUID = 1374594730910928041L;
	
	private static Logger log = Logger.getLogger(Conversion.class);
	
	@Resource
	private UserService userService;

	@Resource
	private PrizeService prizeService;


	// 自动转换到期奖金金额--需要定时作业
	@Override
	public boolean triggerConversion() {
		log.info("------------自动转换到期奖金金额--需要定时作业-----------------");
		Example example = new Example(Prize.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("flag", 0);
		Calendar cr = Calendar.getInstance();
		cr.add(Calendar.DAY_OF_MONTH, -1);
		criteria.andGreaterThan("trigger_time", cr.getTime());
		criteria.andLessThan("trigger_time", Calendar.getInstance().getTime());
		List<Prize> prizes = prizeService.selectByExample(example);
		User userTemp = null;
		for (Prize prize : prizes) {
			userTemp = userService.selectByKey(prize.getUser_id());
			if (null==userTemp) {
				continue;
			}
			userTemp.setNum_dynamic(userTemp.getNum_dynamic()
					+ prize.getMoney());
			userTemp.setTotal_dynamic(userTemp.getTotal_dynamic()
					+ prize.getMoney());
			userService.updateNotNull(userTemp);
			prize.setFlag(1);
			prizeService.updateNotNull(prize);
		}
		return true;
	}

}
