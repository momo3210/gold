package com.momohelp.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.momohelp.model.Manager;
import com.momohelp.service.ManagerService;
import com.momohelp.util.encryptUtil.MD5;

/**
 *
 * @author Administrator
 *
 */
@Service("manageService")
public class ManagerServiceImpl extends BaseService<Manager> implements
		ManagerService {

	@Override
	public Map<String, Object> login(String user_name, String user_pass) {

		Map<String, Object> result = new HashMap<String, Object>();

		Manager user = selectByKey(user_name);

		if (null == user) {
			result.put("msg", new String[] { "用户名或密码输入错误" });
			return result;
		}

		if (!MD5.encode(user_pass).equals(user.getUser_pass())) {
			result.put("msg", new String[] { "用户名或密码输入错误" });
			return result;
		}

		result.put("data", user);
		return result;
	}

}
