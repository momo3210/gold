package com.momohelp.calculate.quartz;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.momohelp.calculate.service.test2.DynamicCalculation;
@Service
public class AutomatchJob implements Job {

	public static Logger log = LoggerFactory.getLogger(AutomatchJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		log.info("------- Initializing ----------计算自动匹配------------");
		WebApplicationContext webContext = ContextLoaderListener.getCurrentWebApplicationContext();
		webContext.getBean(DynamicCalculation.class).automatch();
	}

	@SuppressWarnings("deprecation")
	public void runJob()throws Exception {
		log.info("------- Initializing ----------------------");

		// First we must get a reference to a scheduler
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		log.info("------- Scheduling Jobs -------------------");
		JobDetail job = JobBuilder.newJob(AutomatchJob.class)
				.withIdentity("job1", "group1").build();

		CronTrigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("trigger1", "group1")
				.withSchedule(
						CronScheduleBuilder.cronSchedule("0/20 * * * * ?"))
				.build();

		// schedule the job and print the first run date
		Date firstRunTime = sched.scheduleJob(job, trigger);
		log.info("------- firstRunTime ----------------"
				+ firstRunTime.toLocaleString());
		log.info("------- Starting Scheduler ----------------");
		sched.start();
		log.info("------- Shutting Down ---------------------");
		// sched.shutdown(true);
		log.info("------- Shutdown Complete -----------------");

		SchedulerMetaData metaData = sched.getMetaData();
		log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
	}

}
