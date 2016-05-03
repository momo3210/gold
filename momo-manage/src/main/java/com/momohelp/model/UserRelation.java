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