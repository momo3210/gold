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

		if (materialRecord.getUser_id().equals(
				materialRecord.getTrans_user_id())) {
			return new String[] { "接收人不能是自己" };
		}

		User trans_user = userService.selectByKey(materialRecord
				.getTrans_user_id());
		if (null == trans_user) {
			return new String[] { "请选择接收人" };
		}

		// 获取我的余额进行比对
		User my_user = userService.selectByKey(materialRecord.getUser_id());
		int num_current = (1 == materialRecord.getType_id()) ? my_user
				.getNum_ticket() : my_user.getNum_food();

		if (num_current < materialRecord.getNum_use()) {
			return new String[] { "您的帐号余额不足" };
		}

		/***** 增加一条我的 *****/
		materialRecord.setStatus(1);
		materialRecord.setComment(null);
		// 转出
		materialRecord.setFlag_plus_minus(0);
		// 更新我的本次余额
		materialRecord
				.setNum_balance(num_current - materialRecord.getNum_use());
		save(materialRecord);

		/***** 更新我的帐户信息 *****/
		User new_my_user = new User();
		if (1 == materialRecord.getType_id()) {
			new_my_user.setNum_ticket(materialRecord.getNum_balance()
					.intValue());
		} else {
			new_my_user.setNum_food(materialRecord.getNum_balance().intValue());
		}
		new_my_user.setId(materialRecord.getUser_id());
		userService.updateNotNull(new_my_user);

		/***** 增加一条接受人的 *****/
		MaterialRecord new_trans_materialRecord = new MaterialRecord();
		new_trans_materialRecord.setUser_id(materialRecord.getTrans_user_id());
		new_trans_materialRecord.setNum_use(materialRecord.getNum_use());
		new_trans_materialRecord.setStatus(materialRecord.getStatus());
		new_trans_materialRecord.setType_id(materialRecord.getType_id());
		new_trans_materialRecord.setComment(materialRecord.getComment());
		new_trans_materialRecord.setTrans_user_id(materialRecord.getUser_id());

		if (1 == new_trans_materialRecord.getType_id()) {
			new_trans_materialRecord.setNum_balance(materialRecord.getNum_use()
					+ trans_user.getNum_ticket());
		} else {
			new_trans_materialRecord.setNum_balance(materialRecord.getNum_use()
					+ trans_user.getNum_food());
		}
		// 转入
		new_trans_materialRecord.setFlag_plus_minus(1);
		save(new_trans_materialRecord);

		/***** 更新接收人的帐户信息 *****/
		User new_trans_user = new User();
		if (1 == new_trans_materialRecord.getType_id()) {
			new_trans_user.setNum_ticket(new_trans_materialRecord
					.getNum_balance().intValue());
			new_trans_user.setTotal_ticket(trans_user.getTotal_ticket()
					+ new_trans_materialRecord.getNum_use().intValue());
		} else {
			new_trans_user.setNum_food(new_trans_materialRecord
					.getNum_balance().intValue());
			new_trans_user.setTotal_food(trans_user.getTotal_ticket()
					+ new_trans_materialRecord.getNum_use().intValue());
		}
		new_trans_user.setId(new_trans_materialRecord.getUser_id());
		userService.updateNotNull(new_trans_user);

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
		materialRecord.setStatus(0);
		materialRecord.setComment(null);
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
}