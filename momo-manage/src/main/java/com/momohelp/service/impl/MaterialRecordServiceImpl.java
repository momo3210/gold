package com.momohelp.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.momohelp.model.MaterialRecord;
import com.momohelp.service.MaterialRecordService;

/**
 *
 * @author Administrator
 *
 */
@Service("materialRecordService")
public class MaterialRecordServiceImpl extends BaseService<MaterialRecord>
		implements MaterialRecordService {

	@Override
	public String[] saveNew(MaterialRecord materialRecord) {
		materialRecord.setCreate_time(new Date());

		materialRecord
				.setNum_use((0 < materialRecord.getNum_use()) ? materialRecord
						.getNum_use() : 1);

		// TODO
		save(materialRecord);
		return null;
	}
}