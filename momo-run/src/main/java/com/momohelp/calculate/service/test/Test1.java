package com.momohelp.calculate.service.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Test1 {
    //延迟队列
	private static final BlockingQueue<DelayUser> queue = new DelayQueue<DelayUser>();
	//工作窃取
	private static final ExecutorService  workSteal= Executors
			.newWorkStealingPool(Runtime.getRuntime().availableProcessors());
    //定时执行任务
	private static final ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
	
	//使用 spring定时作业
	public static void main(String[] args) throws InterruptedException {

		queue.put(null);
		queue.take();
		workSteal.execute(null);
		scheduledExecutorService.scheduleWithFixedDelay(null, 0, 1, TimeUnit.DAYS);
		
		LockSupport.parkNanos(0);
		LockSupport.parkUntil(0);
		System.out.println(1L << 32);
	}

}
