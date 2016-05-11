package com.momohelp.calculate.service.test.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SpringJob  extends QuartzJobBean{

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		  System.err.println("----------------------------"+context.getJobInstance());
	}

}
