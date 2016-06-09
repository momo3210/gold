package com.momohelp.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 *
 */
public class UserCount implements Serializable {

	private static final long serialVersionUID = -4140168530823538678L;

	/**
	 * 直推数量全部
	 */
	private Integer ztslqb;

	/**
	 * 直推数量合格
	 */
	private Integer ztslhg;

	/**
	 * 团队数量全部
	 */
	private Integer tdslqb;

	/**
	 * 团队数量合格
	 */
	private Integer tdslhg;

	public Integer getZtslqb() {
		return ztslqb;
	}

	public void setZtslqb(Integer ztslqb) {
		this.ztslqb = ztslqb;
	}

	public Integer getZtslhg() {
		return ztslhg;
	}

	public void setZtslhg(Integer ztslhg) {
		this.ztslhg = ztslhg;
	}

	public Integer getTdslqb() {
		return tdslqb;
	}

	public void setTdslqb(Integer tdslqb) {
		this.tdslqb = tdslqb;
	}

	public Integer getTdslhg() {
		return tdslhg;
	}

	public void setTdslhg(Integer tdslhg) {
		this.tdslhg = tdslhg;
	}

}
