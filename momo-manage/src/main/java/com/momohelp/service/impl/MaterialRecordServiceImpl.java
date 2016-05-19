package com.momohelp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.momohelp.mapper.MaterialRecordMapper;
import com.momohelp.model.MaterialRecord;
import com.momohelp.model.User;
import com.momohelp.service.MaterialRecordService;
import com.momohelp.service.UserService;

/**
 *
 * @author Administrator
 *
 */
@Service("materialRecordService")
public class MaterialRecordServiceImpl extends BaseService<MaterialRecord>
		implements MaterialRecordService {

	@Autowired
	private UserService userService;

	@Override
	public int save(MaterialRecord entity) {
		entity.setCreate_time(new Date());
		entity.setNum_use((0 < entity.getNum_use()) ? entity.getNum_use() : 1);
		return super.save(entity);
	}

	@Override
	public int updateNotNull(MaterialRecord entity) {
		entity.setUser_id(null);
		entity.setCreate_time(null);
		entity.setNum_use(null);
		entity.setType_id(null);
		entity.setTrans_user_id(null);
		entity.setFlag_plus_minus(null);
		return super.updateNotNull(entity);
	}

	/**
	 * 转账（门票、饲料）
	 *
	 * 1、判断接收人是否存在
	 *
	 * 2、判断我的余额是否够用
	 *
	 * 3、增加一条我的转出记录
	 *
	 * 4、更新我的帐户余额
	 *
	 * 5、增加一条接收人的转入记录
	 *
	 * 6、更新接收人的帐户余额
	 */
	@Override
	public String[] virement(MaterialRecord materialRecord) {
		switch (materialRecord.getType_id()) {
		case 1:
		case 2:
			break;
		default:
			return new String[] { "非法操作" };
		}

		if (1 > materialRecord.getNum_use()) {
			return new String[] { "转出数量必须大于 0" };
		}

		if (materialRecord.getUser_id().equals(
				materialRecord.getTrans_user_id())) {
			return new String[] { "接收人不能是自己" };
		}

		User trans_user = userService.selectByKey(materialRecord
				.getTrans_user_id());
		if (null == trans_user) {
			return new String[] { "请选择接收人" };
		}

		// 获取用户实时信息
		User user = userService.selectByKey(materialRecord.getUser_id());

		int num_current = (1 == materialRecord.getType_id()) ? user
				.getNum_ticket() : user.getNum_food();

		if (num_current < materialRecord.getNum_use()) {
			return new String[] { "您的帐号余额不足" };
		}

		/***** 增加一条我的 *****/
		materialRecord.setStatus(1);
		materialRecord.setComment("转出"
				+ ((1 == materialRecord.getType_id()) ? "门票" : "饲料") + " -"
				+ materialRecord.getNum_use() + " 给 M"
				+ materialRecord.getTrans_user_id());
		// 更新我的本次余额
		materialRecord
				.setNum_balance(num_current - materialRecord.getNum_use());
		// 转出
		materialRecord.setFlag_plus_minus(0);
		save(materialRecord);

		/***** 更新我的帐户信息 *****/
		User _user = new User();
		if (1 == materialRecord.getType_id()) {
			_user.setNum_ticket(materialRecord.getNum_balance().intValue());
		} else {
			_user.setNum_food(materialRecord.getNum_balance().intValue());
		}
		_user.setId(materialRecord.getUser_id());
		userService.updateNotNull(_user);

		/***** 增加一条接受人的 *****/
		MaterialRecord _trans_materialRecord = new MaterialRecord();
		_trans_materialRecord.setUser_id(materialRecord.getTrans_user_id());
		_trans_materialRecord.setNum_use(materialRecord.getNum_use());
		_trans_materialRecord.setStatus(materialRecord.getStatus());
		_trans_materialRecord.setType_id(materialRecord.getType_id());
		_trans_materialRecord.setComment("接收"
				+ ((1 == materialRecord.getType_id()) ? "门票" : "饲料") + " +"
				+ materialRecord.getNum_use() + " 来自 M"
				+ materialRecord.getUser_id());
		_trans_materialRecord.setTrans_user_id(materialRecord.getUser_id());

		_trans_materialRecord.setNum_balance(materialRecord.getNum_use()
				+ ((1 == _trans_materialRecord.getType_id()) ? trans_user
						.getNum_ticket() : trans_user.getNum_food()));

		// 转入
		_trans_materialRecord.setFlag_plus_minus(1);
		save(_trans_materialRecord);

		/***** 更新接收人的帐户信息 *****/
		User _trans_user = new User();
		if (1 == _trans_materialRecord.getType_id()) {
			_trans_user.setNum_ticket(_trans_materialRecord.getNum_balance()
					.intValue());
			_trans_user.setTotal_ticket(trans_user.getTotal_ticket()
					+ _trans_materialRecord.getNum_use().intValue());
		} else {
			_trans_user.setNum_food(_trans_materialRecord.getNum_balance()
					.intValue());
			_trans_user.setTotal_food(trans_user.getTotal_food()
					+ _trans_materialRecord.getNum_use().intValue());
		}
		_trans_user.setId(_trans_materialRecord.getUser_id());
		userService.updateNotNull(_trans_user);

		return null;
	}

	/**
	 * 通过类型，获取对账单（门票、饲料、静态、动态）
	 */
	@Override
	public List<MaterialRecord> findByTypeId(MaterialRecord materialRecord) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("user_id", materialRecord.getUser_id());
		map.put("type_id", materialRecord.getType_id());

		return ((MaterialRecordMapper) getMapper()).findByTypeId(map);
	}

	/**
	 * 买入门票（饲料），只插入一条记录
	 */
	@Override
	public String[] buy(MaterialRecord materialRecord) {
		switch (materialRecord.getType_id()) {
		case 1:
		case 2:
			break;
		default:
			return new String[] { "非法操作" };
		}

		if (1 > materialRecord.getNum_use()) {
			return new String[] { "购买数量必须大于 0" };
		}

		materialRecord.setStatus(0);
		materialRecord.setComment("购买数额 +" + materialRecord.getNum_use()
				+ "，等待入账");
		materialRecord.setTrans_user_id(null);

		// 设置余额
		materialRecord.setNum_balance(getBalanceByTypeId(
				materialRecord.getUser_id(), materialRecord.getType_id()));
		materialRecord.setFlag_plus_minus(1);

		save(materialRecord);
		return null;
	}

	/**
	 * 从我当前的帐户中获取此类型的余额
	 *
	 * @param user_id
	 * @param type_id
	 *            门票、饲料、静态、动态
	 * @return
	 */
	private double getBalanceByTypeId(String user_id, int type_id) {
		User my_user = userService.selectByKey(user_id);
		// TODO
		switch (type_id) {
		case 1:
			return my_user.getNum_ticket();
		case 2:
			return my_user.getNum_food();
		case 3:
			return my_user.getNum_static();
		case 4:
			return my_user.getNum_dynamic();
		default:
			return 0;
		}
	}

	/**
	 * 买入后确认
	 *
	 * 1、更新用户帐户信息
	 *
	 * 2、更新转账记录 status 为1
	 */
	@Override
	public String[] buy_establish(String key) {
		MaterialRecord materialRecord = selectByKey(key);

		if (null == materialRecord) {
			return new String[] { "没有找到此记录" };
		}

		User user = userService.selectByKey(materialRecord.getUser_id());

		/***** 更新用户帐户信息 *****/
		User _user = new User();
		_user.setId(user.getId());
		if (1 == materialRecord.getType_id()) {
			_user.setNum_ticket(user.getNum_ticket()
					+ materialRecord.getNum_use().intValue());
			_user.setTotal_ticket(user.getTotal_ticket()
					+ materialRecord.getNum_use().intValue());
		} else {
			_user.setNum_food(user.getNum_food()
					+ materialRecord.getNum_use().intValue());
			_user.setTotal_food(user.getTotal_food()
					+ materialRecord.getNum_use().intValue());
		}
		userService.updateNotNull(_user);

		/***** 更新转账信息 *****/
		MaterialRecord _materialRecord = new MaterialRecord();
		_materialRecord.setId(key);
		_materialRecord.setStatus(1);

		double d = (1 == materialRecord.getType_id()) ? _user.getNum_ticket()
				: _user.getNum_food();
		_materialRecord.setNum_balance(d);

		_materialRecord.setComment("购买数额 +" + materialRecord.getNum_use()
				+ "，已入账");

		updateNotNull(_materialRecord);
		return null;
	}
}