package com.momohelp.service;

import com.momohelp.model.MaterialRecord;

/**
 *
 * @author Administrator
 *
 */
public interface MaterialRecordService extends IService<MaterialRecord> {

	String[] saveNew(MaterialRecord materialRecord);
}
