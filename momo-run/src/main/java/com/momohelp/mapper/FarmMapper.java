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
	 * 未处理的提成的单据
	 * 
	 * @return
	 */
	List<Farm> getUntreatedFarm();
}