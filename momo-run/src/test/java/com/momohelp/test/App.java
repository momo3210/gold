package com.momohelp.test;

import java.util.Calendar;

public class App {

	public static void main(String[] args) throws Exception {
		// AbstractApplicationContext context = new
		// ClassPathXmlApplicationContext(
		// "applicationContext.xml");
		// Ibase match=context.getBean(Ibase.class);
		// match.base();
		// SchedulerFactoryBean bean=null;
		// try {
		// bean=context.getBean(SchedulerFactoryBean.class);
		// bean.start();
		// } catch (Exception e) {
		// bean.stop();
		// context.close();
		// }

		// ICalculation calculation=context.getBean(ICalculation.class);
		// calculation.automatch();
		// calculation.calculateForceLogout();//通过测试
		// calculation.base();
		// System.err.println("-------------------App------------------\n");
		// AutomatchJob job=context.getBean(AutomatchJob.class);
		// job.runJob();
		// org.apache.ibatis.type.JdbcType.INTEGER
		// context.close();

		Calendar cr = Calendar.getInstance();
		cr.add(Calendar.DAY_OF_MONTH, -1);
		System.err.println(cr.getTime());

	}
}
