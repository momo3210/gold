package com.momohelp.calculate.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.momohelp.calculate.service.IForceLogout;
import com.momohelp.model.Farm;
import com.momohelp.service.FarmService;
@Service
public class ForceLogout implements Serializable, IForceLogout {

	private static final long serialVersionUID = -583545930103637435L;
	private static Logger log = Logger.getLogger(ForceLogout.class);
	
	@Resource
	private FarmService farmService;

	// 用户强制出局计算
	@Override
	public boolean calculateForceLogout() {
		log.info("----------用户强制出局计算----------");
		Calendar cr = Calendar.getInstance();
		cr.set(Calendar.HOUR, 0);
		cr.set(Calendar.MINUTE, 0);
		cr.set(Calendar.SECOND, 0);
		List<Farm> farms = farmService.selectForceLogout(cr.getTime());
		for (Farm farm : farms) {
			farm.setFlag_out_p(3);
			farmService.updateNotNull(farm);
		}
		return true;
	}
}
