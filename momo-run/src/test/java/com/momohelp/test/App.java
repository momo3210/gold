package com.momohelp.test;

import java.util.Calendar;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.momohelp.calculate.service.IMatch;
import com.momohelp.calculate.service.IStatic;


public class App {

	public static void main(String[] args) throws Exception {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		
		// IConversion conversion=context.getBean(IConversion.class);
		// conversion.triggerConversion();
		// IForceLogout conversion=context.getBean(IForceLogout.class);
		// conversion.calculateForceLogout();
		// IStatic conversion=context.getBean(IStatic.class);
		// conversion.calculation();
		IMatch match= context.getBean(IMatch.class);
		
		match.automatch();
	}
}
