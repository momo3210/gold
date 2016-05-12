package com.momohelp.calculate.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Automatch implements Serializable {

	private static final long serialVersionUID = -1717054057417431945L;

	private static final BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(
			32768);
	// 十进制 基数 与分解存储
	private static final List<String[]> list = new ArrayList<String[]>(9);
	static {
		for (int i = 0; i < list.size(); i++) {
			switch (i) {
			case 0:
				list.add(0, new String[] { "0" });
				break;
			case 1:
				list.add(1, new String[] { "1" });
				break;
			case 2:
				list.add(2, new String[] { "2", "1,1" });
				break;
			case 3:
				list.add(3, new String[] { "3", "2,1" });
				break;
			case 4:
				list.add(4, new String[] { "4", "3,1", "2,2" });
				break;
			case 5:
				list.add(5, new String[] { "5", "4,1", "3,2" });
				break;
			case 6:
				list.add(6, new String[] { "6", "5,1", "4,2", "3,3" });
				break;
			case 7:
				list.add(7, new String[] { "7", "6,1", "5,2", "4,3" });
				break;
			case 8:
				list.add(8, new String[] { "8", "7,1", "6,2", "5,3", "4,4" });
				break;
			case 9:
				list.add(9, new String[] { "9", "8,1", "7,2", "6,3", "5,4" });
				break;
			default:
				new RuntimeException("只能处理 1-10之间的进制数据 ");
				break;
			}
		}

	}

	public void produce(Integer user) {
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
			while (true) {
				Integer user = queue.take();

				queue.remove(user);
			}
		} catch (InterruptedException e) {
			// http://www.ibm.com/developerworks/cn/java/j-jtp05236.html
			Thread.currentThread().interrupt();
			// log.info("----------", e);
		}

	}

	/**
	 * @param user
	 *            需要待匹配清单
	 * 
	 */
	public void match2(Order order) {
		int length = order.getMoney();
		// 查询最先待匹配的前十单
		// 模拟10单
		System.out.println(length);

	}

	public void match(Integer user) {
		int length = user.toString().length();
		switch (length) {
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;

		case 8:
			break;
		case 9:
			break;
		default:
			break;

		}
	}

	public void parse() {

	}

	public static void main(String[] args) {
		
		Order[] orders = new Order[1000];
		for (int i = 0; i < 1000; i++) {
			Order temp = new Order();
			temp.setId("" + System.currentTimeMillis());
			temp.setMoney(((int) (1 + Math.random() * (50 - 1 + 1))) * 100);
			orders[i] = temp;
		}
		int base = 4500;
		int j = 0;
		int middle=0;
		for (int i = 0; i < orders.length; i++) {
			int temp = orders[i].getMoney();
			if (temp == base) {
				System.err.println(temp + "---id---" + orders[i].getId());
				System.err.println(Arrays.deepToString(orders));
				return;
			} else {
				
				if(temp>base){
					continue;
				}else{
					j = temp + j;
					middle=base-j;
				}
				
				
				System.out.println(j + "=====iii=======" + i);
				System.err.println(Arrays.deepToString(orders));
				return;
			}
		}
		System.out.println(middle+ "=====middle=======");
	}

}
