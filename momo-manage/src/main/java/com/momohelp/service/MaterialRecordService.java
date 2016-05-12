package com.momohelp.service;

import org.springframework.transaction.annotation.Transactional;

import com.momohelp.model.MaterialRecord;

/**
 *
 * @author Administrator
 *
 */
public interface MaterialRecordService extends IService<MaterialRecord> {

	String[] saveNew(MaterialRecord materialRecord);

	/**
	 * 转账
	 *
	 * @param materialRecord
	 * @return
	 */
	@Transactional
	String[] virement(MaterialRecord materialRecord);
}
