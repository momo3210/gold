package com.momohelp.calculate.service;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.momohelp.model.User;

public class Cost implements Serializable {

	private static Logger log = Logger.getLogger(Cost.class);

	private static final long serialVersionUID = -6728248502573270438L;
	// 计算会员推荐奖和管理奖线程池
	private static final ExecutorService service = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 32);
	// 存储需要计算会员推荐奖和管理奖
	private static final BlockingQueue<User> queue = new LinkedBlockingQueue<User>(
			8192);
	// 存储需要计算烧伤奖
	private static final BlockingQueue<User> burn = new LinkedBlockingQueue<User>(
			8192);

	public void put(User user) {
		try {
			queue.put(user);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void take(){
		Task task;
		try {
			task = new Task(queue.take());
			service.submit(task);
		} catch (InterruptedException e) {

			
		}
		
	}

	/**
	 * 
	 * @param user
	 *            当前会员
	 * @return boolean true 表示计算成功 false 计算失败
	 */
	public boolean premium(User user) {
		log.info("premium(User user):::::" + user.toString());
		boolean bool = false;
		// 推荐奖计算
		// 管理奖计算
		// 烧伤奖计算 这部分特例

		return bool;

	}

	// 推荐奖计算
	public void recommend(User user) {

	}

	// 管理奖计算
	public void manage(User user) {

	}

	// 烧伤奖计算

	public void burn(User user) {

	}

	// 鸡饲料计算
	public void feed(User user) {

	}

	// 下蛋利息计算
	public void egg() {

	}

	// 鸡苗饲养利息计算
	public void keepe() {

	}

	public static void main(String[] args) {

	}

	private static class Task implements Callable<Boolean> {

		private User user = null;

		public Task(User user) {
			this.user = user;
		}

		@Override
		public Boolean call() throws Exception {
			Boolean bool = false;
			log.info("-------call-------------"+user.toString());
			return bool;
		}

	}

}
