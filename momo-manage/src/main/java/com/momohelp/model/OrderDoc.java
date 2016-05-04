package com.momohelp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 排单
 *
 * @author Administrator
 *
 */
@Table(name = "w_orderdoc")
public class OrderDoc implements Serializable {

	private static final long serialVersionUID = -7481892273733454008L;

	@Id
	@Column(name = "id")
	private String id;

	private String user_id;

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
}
