package com.momohelp.service;

import java.util.List;

import com.momohelp.model.Farm;

/**
 *
 * @author Administrator
 *
 */
public interface FarmService extends IService<Farm> {
	/***
	 * δ�������ɵĵ���
	 * 
	 * @return
	 */
	List<Farm> getUntreatedFarm();
}
