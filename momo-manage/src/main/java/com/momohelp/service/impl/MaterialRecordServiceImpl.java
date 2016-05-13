package com.momohelp.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public String[] saveNew(MaterialRecord materialRecord) {
		materialRecord
				.setNum_use((0 < materialRecord.getNum_use()) ? materialRecord
						.getNum_use() : 1);
		materialRecord.setCreate_time(new Date());
		save(materialRecord);
		return null;
	}

	@Override
	public String[] virement(MaterialRecord materialRecord) {
		// 判断
		if (null != materialRecord.getTrans_user_id()) {
			User user = userService.selectByKey(materialRecord
					.getTrans_user_id());
			if (null == user) {
				return new String[] { "没有找到接收人" };
			}
		} // IF

		// 获取我的余额进行比对
		User myUser = userService.selectByKey(materialRecord.getUser_id());
		int num_curr = (1 == materialRecord.getType_id()) ? myUser
				.getNum_ticket() : myUser.getNum_food();

		materialRecord
				.setNum_use((0 < materialRecord.getNum_use()) ? materialRecord
						.getNum_use() : 1);

		if (num_curr < materialRecord.getNum_use()) {
			return new String[] { "您的帐号余额不足" };
		}

		return saveNew(materialRecord);
	}
}