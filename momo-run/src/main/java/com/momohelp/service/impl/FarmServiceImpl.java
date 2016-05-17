package com.momohelp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.momohelp.mapper.FarmMapper;
import com.momohelp.model.Farm;
import com.momohelp.service.FarmService;

/**
 *
 * @author Administrator
 *
 */
@Service("farmService")
public class FarmServiceImpl extends BaseService<Farm> implements FarmService {

	@Override
	public List<Farm> getUntreatedFarm() {

		return ((FarmMapper) mapper).getUntreatedFarm();
	}

	@Override
	public List<Farm> selectLastFarmByDate(String id, Date create_time) {

		return ((FarmMapper) mapper).selectLastFarmByDate(id, create_time);
	}

	@Override
	public List<Farm> selectForceLogout(Date date) {
		return ((FarmMapper) mapper).selectForceLogout(date);
	}

}
