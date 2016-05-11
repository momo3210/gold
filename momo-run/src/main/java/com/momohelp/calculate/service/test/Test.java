package com.momohelp.calculate.service.test;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.momohelp.model.User;

public class Test {
	private static final BlockingQueue<User> queue = new LinkedBlockingQueue<User>(
			32768);

	public void produce() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for(long i=0;i<100000 ;i++) {
						User user = new User();
						user.setId("" + new Random().nextLong());
						queue.put(user);
						// service.submit(new Task(user));
					}
				} catch (InterruptedException e) {
					// http://www.ibm.com/developerworks/cn/java/j-jtp05236.html
					Thread.currentThread().interrupt();
				}
			}
		}).start();
	}

	/***
	 * 开始消费数据
	 */
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				long start=System.currentTimeMillis();
				queue.parallelStream().forEach(
						(user -> System.err.println(user.getId())));
				System.out.println("---------:"+(System.currentTimeMillis()-start));
			}
		}).start();

	}

	public static void main(String[] args) throws InterruptedException {
		Test test = new Test();
		test.produce();
		//Thread.currentThread().sleep(10000);
		test.start();
	}

}
