package com.momohelp.calculate.service.test;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Test17 {
	
	public static void main(String[] args) throws Exception {
		Test17 t = new Test17();
		t.count1();
		t.count2();
	}
	
	public static final ThreadLocalRandom rand = ThreadLocalRandom.current();
	// 得到一个任务
	public Callable<Integer> getTask() {
		
		Random rand = new Random();
		Callable<Integer> task = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				int i = rand.nextInt(10);
				int j = rand.nextInt(10);
				int sum = i * j;
				System.out.println(sum + "\t");
				return sum;
			}
		};
		return task;
	}

	// 使用阻塞容器保存每次Executor处理的结果，在后面进行统一处理
	public void count1() throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool();
		BlockingQueue<Future<Integer>> queue = new LinkedBlockingQueue<Future<Integer>>();
		for (int i = 0; i < 10; i++) {
			Future<Integer> future = exec.submit(getTask());
			queue.add(future);
		}
		int sum = 0;
		int queueSize = queue.size();
		for (int i = 0; i < queueSize; i++) {
			sum += queue.take().get();
		}
		System.out.println("总数为：" + sum);
		exec.shutdown();
	}

	// 使用CompletionService(完成服务)保持Executor处理的结果
	public void count2() throws InterruptedException, ExecutionException {
		ExecutorService exec = Executors.newCachedThreadPool();
		CompletionService<Integer> execcomp = new ExecutorCompletionService<Integer>(
				exec);
		for (int i = 0; i < 10; i++) {
			execcomp.submit(getTask());
		}
		int sum = 0;
		for (int i = 0; i < 10; i++) {
			// 检索并移除表示下一个已完成任务的 Future，如果目前不存在这样的任务，则等待。
			Future<Integer> future = execcomp.take();
			sum += future.get();
		}
		System.out.println("总数为：" + sum);
		exec.shutdown();
	}

}