package com.momohelp.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.momohelp.model.MaterialRecord;

/**
 *
 * @author Administrator
 *
 */
public interface MaterialRecordService extends IService<MaterialRecord> {

	/**
	 * 买入
	 *
	 * @param materialRecord
	 * @return
	 */
	String[] buy(MaterialRecord materialRecord);

	/**
	 * 转账
	 *
	 * @param materialRecord
	 * @return
	 */
	@Transactional
	String[] virement(MaterialRecord materialRecord);

	List<MaterialRecord> findByTypeId(MaterialRecord materialRecord);
}
