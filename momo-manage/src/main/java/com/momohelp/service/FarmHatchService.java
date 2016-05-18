package com.momohelp.service;

import java.util.List;

import com.momohelp.model.FarmHatch;

/**
 *
 * @author Administrator
 *
 */
public interface FarmHatchService extends IService<FarmHatch> {

	List<FarmHatch> findByFarmId(String farm_id);
}
