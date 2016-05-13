package com.momohelp.mapper;

import java.util.List;
import java.util.Map;

import com.momohelp.model.MaterialRecord;
import com.momohelp.util.MyMapper;

/**
 *
 * @author Administrator
 *
 */
public interface MaterialRecordMapper extends MyMapper<MaterialRecord> {

	List<MaterialRecord> findByTypeId(Map<String, Object> map);
}