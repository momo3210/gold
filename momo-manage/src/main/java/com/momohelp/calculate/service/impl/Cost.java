package com.momohelp.calculate.service.impl;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.momohelp.calculate.service.ICost;
import com.momohelp.model.User;

public class Cost implements Serializable, ICost {

	private static Logger log = Logger.getLogger(Cost.class);

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

	public void start() {
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

	@Override
	public boolean premium(User user) {
		log.info("premium(User user):::::" + user.toString());
		boolean bool = false;
		// 推荐奖计算
		// 管理奖计算
		// 烧伤奖计算 这部分特例 判断是否在上一排单20天之内 再次排单

		return bool;

	}

	// 推荐奖计算
	@Override
	public void recommend(User user) {

	}

	// 管理奖计算
	@Override
	public void manage(User user) {

	}

	// 烧伤奖计算
	@Override
	public void burn(User user) {

	}

	// 鸡饲料计算
	@Override
	public void feed(User user) {

	}

	// 下蛋利息计算
	@Override
	public void egg() {

	}

	// 鸡苗饲养利息计算
	@Override
	public void keepe() {

	}

	public static void main(String[] args) {

	}

}
