package com.momohelp.calculate.service;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class App {

	public static void main(String[] args) throws Exception {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		SchedulerFactoryBean bean=null;
		try {
			 bean=context.getBean(SchedulerFactoryBean.class);
			bean.start();
		} catch (Exception e) {
			bean.stop();
			context.close();
		}
		
		//ICalculation calculation=context.getBean(ICalculation.class);
		//calculation.automatch();
		// calculation.calculateForceLogout();//通过测试
		// calculation.base();
		// System.err.println("-------------------App------------------\n");
		// AutomatchJob job=context.getBean(AutomatchJob.class);
		// job.runJob();
		//org.apache.ibatis.type.JdbcType.INTEGER
		//context.close();
		
	}
}
