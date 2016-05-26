package com.momohelp.service;

import java.util.Map;

import com.momohelp.model.Manager;

/**
 *
 * @author Administrator
 *
 */
public interface ManagerService extends IService<Manager> {

	Map<String, Object> login(String user_name, String user_pass);

}
