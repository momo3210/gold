package com.momohelp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.momohelp.model.Farm;
import com.momohelp.service.CfgService;
import com.momohelp.service.FarmService;
import com.momohelp.service.MaterialRecordService;
import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Service("farmService")
public class FarmServiceImpl extends BaseService<Farm> implements FarmService {

	@Autowired
	private UserService userService;

	@Autowired
	private CfgService cfgService;

	@Autowired
	private MaterialRecordService materialRecordService;

	/**
	 * 买入鸡苗
	 *
	 * 1、查看门票是否小于1，如果是则返回
	 *
	 * 2、验证购买的数量是否是100的倍数
	 *
	 * 3、验证购买的数量是否符合我的身份
	 *
	 * 4、更新我的帐户信息--门票数量-1
	 *
	 * 5、插入 w_material_use 一条购买鸡苗使用门票的记录
	 *
	 * 6、插入 w_farm_chick 一条购买鸡苗的记录
	 */
	@Override
	public String[] buy(Farm farm) {
		return null;
	}

}
