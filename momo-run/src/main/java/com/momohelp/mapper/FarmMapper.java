package com.momohelp.mapper;

import java.util.List;

import com.momohelp.model.Farm;
import com.momohelp.util.MyMapper;

/**
 *
 * @author Administrator
 *
 */
public interface FarmMapper extends MyMapper<Farm> {
	/***
	 * δ�������ɵĵ���
	 * 
	 * @return
	 */
	List<Farm> getUntreatedFarm();
}