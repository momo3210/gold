package com.momohelp.calculate.service.impl;

import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.momohelp.calculate.service.IUpdateTime;
import com.momohelp.service.BuySellService;
@Service
public class UpdateTime implements IUpdateTime {

	
	private static final long serialVersionUID = 6408957395420079857L;
	
	@Resource
	private BuySellService buySellService;

	@Override
	public void updateTime() {
		Calendar  cr=Calendar.getInstance();
		StringBuffer  dates=new StringBuffer();
		dates.append(cr.get(Calendar.YEAR));
		dates.append("-");
		int month=cr.get(Calendar.MONTH)+1;
		if (month>9) {
			dates.append(month+"");
		}else{
			dates.append("0"+month);
		}
		dates.append("-");
		int day=cr.get(Calendar.DAY_OF_MONTH);
		if (month>9) {
			dates.append(day+"");
		}else{
			dates.append("0"+day);
		}
		dates.append("%");
		String date=dates.toString();
		buySellService.updateTimeHour(date);
		buySellService.updateTimeMinute(date);
		buySellService.updateTimeSecond(date);
	}

}
