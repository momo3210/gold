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
		// TODO
		return super.save(entity);
	}

	@Override
	public int updateNotNull(MaterialRecord entity) {
		entity.setCreate_time(null);
		// TODO
		return super.updateNotNull(entity);
	}

	@Override
	public String[] saveNew(MaterialRecord materialRecord) {
		materialRecord
				.setNum_use((0 < materialRecord.getNum_use()) ? materialRecord
						.getNum_use() : 1);

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

	@Override
	public List<MaterialRecord> findByTypeId(MaterialRecord materialRecord) {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("user_id", materialRecord.getUser_id());
		map.put("trans_user_id", materialRecord.getTrans_user_id());
		map.put("type_id", materialRecord.getType_id());

		return ((MaterialRecordMapper) getMapper()).findByTypeId(map);
	}
}