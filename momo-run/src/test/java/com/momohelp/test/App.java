package com.momohelp.test;

import java.util.Calendar;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
//		AbstractApplicationContext context = 
//		new ClassPathXmlApplicationContext("applicationContext.xml");
//		SchedulerFactoryBean bean=null;
//		try {
//			 bean=context.getBean(SchedulerFactoryBean.class);
//			bean.start();
//		} catch (Exception e) {
//			bean.stop();
//			context.close();
//		}
		
		//ICalculation calculation=context.getBean(ICalculation.class);
		//calculation.automatch();
		// calculation.calculateForceLogout();//通过测试
		// calculation.base();
		// System.err.println("-------------------App------------------\n");
		// AutomatchJob job=context.getBean(AutomatchJob.class);
		// job.runJob();
		//org.apache.ibatis.type.JdbcType.INTEGER
		//context.close();
		
		Calendar cr=Calendar.getInstance();
		cr.add(Calendar.DAY_OF_MONTH, -1);
		System.err.println(cr.getTime());
		
	}
}
