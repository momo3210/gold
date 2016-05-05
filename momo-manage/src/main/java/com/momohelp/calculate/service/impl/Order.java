package com.momohelp.calculate.service.impl;

import java.io.Serializable;

public class Order implements Serializable {

	private static final long serialVersionUID = -7481892273733454008L;

	
	private String id;

	private String user_id;
	
	private int money;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Order [id=");
		builder.append(id);
		builder.append(", money=");
		builder.append(money);
		builder.append("]");
		return builder.toString();
	}
}