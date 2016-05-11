package com.momohelp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

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
		
		return ((FarmService) mapper).getUntreatedFarm();
	}

}
