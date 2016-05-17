package com.momohelp.model;

import java.io.Serializable;

public class ModelLV implements Serializable {

	private static final long serialVersionUID = 6303405734594521950L;
	private String lv;
	private Integer no;
	public String getLv() {
		return lv;
	}
	public void setLv(String lv) {
		this.lv = lv;
	}
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}

}
