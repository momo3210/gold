package com.momohelp.calculate.service.test;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.momohelp.model.User;

//动态奖金计算
public class DynamicCalculation implements Serializable {

	private static Logger log = Logger.getLogger(DynamicCalculation.class);

	private static final long serialVersionUID = -6728248502573270438L;
	// 计算会员推荐奖和管理奖线程池
	private static final ExecutorService service = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 32);
	// 存储需要计算会员推荐奖和管理奖
	private static final BlockingQueue<User> queue = new LinkedBlockingQueue<User>(
			32768);

	// 存储需要计算烧伤奖
	// private static final BlockingQueue<User> burn = new
	// LinkedBlockingQueue<User>(8192);

	public void produce(User user) {
		try {
			queue.put(user);
		} catch (InterruptedException e) {
			// http://www.ibm.com/developerworks/cn/java/j-jtp05236.html
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}

	/***
	 * 开始消费数据
	 */
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					User user = null;
					while (true) {
						user = queue.take();
						service.submit(new Task(user));
					}
				} catch (InterruptedException e) {
					// http://www.ibm.com/developerworks/cn/java/j-jtp05236.html
					Thread.currentThread().interrupt();
					log.info("----------", e);
				}
			}
		}).start();

	}

	public void stop() {
		service.shutdown();
	}

	private class Task implements Callable<Boolean> {
		User user = null;

		public Task(User user) {
			this.user = user;
		}

		@Override
		public Boolean call() throws Exception {
			return premium(this.user);
		}

	}

	private boolean premium(User user) {
		log.info("消费数据:::::" + user.getId());
		boolean bool = false;
		// 推荐奖计算
		// 管理奖计算
		// 烧伤奖计算 这部分特例【出局】 判断是否在上一排单20天之内 再次排单
		/**
		 * 如果没有出局 按照管理奖计算 否则按照 烧伤奖计算 烧伤奖与管理奖 之间区别在于： 基数值不同：
		 * 
		 * 
		 */
		return bool;

	}
	public static void main(String[] args) {
		DynamicCalculation cost = new DynamicCalculation();
		Mytask mytask = new Mytask(cost);
		Thread thread = new Thread(mytask);
		thread.start();
		cost.start();

	}

	// 测试使用到，生产不建议这样使用
	public static class Mytask implements Runnable {
		DynamicCalculation cost = null;

		public Mytask(DynamicCalculation cost) {
			this.cost = cost;
		}

		@Override
		public void run() {
			while (true) {
				User user = new User();
				user.setId("" + new Random().nextLong());
				this.cost.produce(user);
				log.info("生产数据：：：：" + user.getId());
			}

		}

	}
}
