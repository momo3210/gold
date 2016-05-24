package com.momohelp.calculate.service.test;

import java.util.Calendar;

import javax.annotation.Resource;

import com.momohelp.model.Cfg;
import com.momohelp.service.CfgService;

public class A {
	@Resource
	private CfgService cfgService = null;

	// 计算提成
	/***
	 * 
	 * @param base
	 *            提成基数
	 * @param uuid
	 *            当前等级lv编号
	 * @return
	 */
	public double test(int base, String uuid) {
		Cfg cfg = cfgService.selectByKey(uuid);
		double royalty = 0.0d;
		switch (cfg.getKey_()) {
		case "0501":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		case "0601":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		case "0602":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		case "0701":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		case "0702":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		case "0703":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		case "0801":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		case "0802":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		case "0803":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		case "0804":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		case "0805":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		case "0806":
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		default:
			royalty = Double.parseDouble(cfg.getValue_()) * base;
			break;
		}
		return royalty;
	}
	
	public static void main(String[] args) {
		Calendar cr=Calendar.getInstance();
		cr.set(Calendar.HOUR, 0);
		cr.set(Calendar.MINUTE, 0);
		cr.set(Calendar.SECOND, 0);
		Calendar cr2=Calendar.getInstance();
		cr2.add(Calendar.DAY_OF_MONTH, 1);
		cr2.set(Calendar.HOUR, 0);
		cr2.set(Calendar.MINUTE, 0);
		cr2.set(Calendar.SECOND, 0);
//		System.out.println(cr.getTime().toLocaleString());
//		System.out.println(cr2.getTime().toLocaleString());
	}
}
