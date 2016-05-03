package com.momohelp.model;

import java.io.Serializable;

import javax.persistence.Table;

/**
 * 用户间关系
 *
 * @author Administrator
 *
 */
@Table(name = "s_user_relation")
public class UserRelation implements Serializable {

	private static final long serialVersionUID = 7577812725789962277L;

	private String parent_id;
	private String child_id;

	/**
	 * 家族（最顶端的用户id）
	 */
	private String family_id;

	/**
	 * 深度， 从1开始（最顶端为1）
	 */
	private Integer deepth;

	/**
	 * level 级别（4贫农、3中农、2富农、1农场主）
	 */
	private Integer lv;

	public Integer getDeepth() {
		return deepth;
	}

	public void setDeepth(Integer deepth) {
		this.deepth = deepth;
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public String getFamily_id() {
		return family_id;
	}

	public void setFamily_id(String family_id) {
		this.family_id = family_id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getChild_id() {
		return child_id;
	}

	public void setChild_id(String child_id) {
		this.child_id = child_id;
	}
}