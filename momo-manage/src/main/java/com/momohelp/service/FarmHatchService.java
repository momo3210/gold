package com.momohelp.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.momohelp.model.FarmHatch;

/**
 *
 * @author Administrator
 *
 */
public interface FarmHatchService extends IService<FarmHatch> {

	/**
	 * 获取鸡苗批次的孵化记录
	 *
	 * @param farm_id
	 *            鸡苗批次id
	 * @return
	 */
	List<FarmHatch> findByFarmId(String farm_id);

	/**
	 * 孵化
	 *
	 * @param farmHatch
	 * @return
	 */
	@Transactional
	String[] hatch(FarmHatch farmHatch);
}
