package com.momohelp.service;

import java.util.List;

import com.momohelp.model.Prize;

public interface PrizeService extends IService<Prize> {

	List<Prize> findByUserId(String user_id);
}
