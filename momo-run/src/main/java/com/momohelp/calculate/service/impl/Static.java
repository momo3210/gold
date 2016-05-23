package com.momohelp.calculate.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.calculate.service.IStatic;
import com.momohelp.model.Farm;
import com.momohelp.model.FarmFeed;
import com.momohelp.model.FarmHatch;
import com.momohelp.model.User;
import com.momohelp.service.FarmFeedService;
import com.momohelp.service.FarmHatchService;
import com.momohelp.service.FarmService;
import com.momohelp.service.UserService;

@Service("myStatic")
public class Static implements IStatic {

	private static final long serialVersionUID = -4917080782460473547L;
	private static Logger log = Logger.getLogger(Static.class);

	@Resource
	private FarmHatchService farmHatchService;

	@Resource
	private UserService userService;

	@Resource
	private FarmFeedService farmFeedService;

	@Resource
	private FarmService farmService;

	@Override
	public boolean calculation() {
		log.info("-------------静态余额孵化计算------------------------");
		Example example = new Example(FarmHatch.class);
		Calendar cr = Calendar.getInstance();
		cr.add(Calendar.DAY_OF_MONTH, -1);
		example.createCriteria()
				.andBetween("freeze_time", cr.getTime(), new Date())
				.andEqualTo("flag_calc_bonus", 0);
		List<FarmHatch> farmHatchs = farmHatchService.selectByExample(example);
		User user = null;
		for (FarmHatch farmHatch : farmHatchs) {
			user = userService.selectByKey(farmHatch.getUser_id());
			if (user == null) {
				continue;
			}
			double temp = 0.00;
			if (farmHatch.getFlag_is_last() == 1) {
				Example example2 = new Example(FarmFeed.class);
				example2.createCriteria().andEqualTo("w_farm_chick_id",
						farmHatch.getW_farm_chick_id());
				List<FarmFeed> farmFeeds = farmFeedService
						.selectByExample(example);
				for (FarmFeed farmFeed : farmFeeds) {
					temp = temp + farmFeed.getPrice();
				}
				Farm farm = farmService.selectByKey(farmHatch
						.getW_farm_chick_id());
				if (farm != null) {
					temp = temp + farm.getNum_reward();
				}
			}
			user.setNum_static(user.getNum_static()
					+ farmHatch.getNum_hatch().intValue() + temp);
			user.setTotal_static(user.getTotal_static()
					+ farmHatch.getNum_hatch().intValue() + temp);
			userService.updateNotNull(user);
			farmHatch.setFlag_calc_bonus(1);
			farmHatchService.updateNotNull(farmHatch);
		}
		return true;
	}
}
