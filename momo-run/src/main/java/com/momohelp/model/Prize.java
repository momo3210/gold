package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 冻结金额以及时间
 *
 * @author
 *
 */
@Table(name = "t_prize")
public class Prize implements Serializable {

	private static final long serialVersionUID = 7414892125201156966L;

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	private String id;

	/***
	 * 推荐代数
	 */
	private Integer depth;
	/***
	 * 到期时间
	 */
	private Date trigger_time;
	/***
	 * 创建时间
	 */
	private Date create_time;
	/***
	 * 动态奖金金额
	 */
	private double money;

	/***
	 * 计算标志 0： 初始数据 1：已经计算
	 */
	private int flag;
	/**
	 * 拿提成的用户
	 */
	private String user_id;
	/**
	 * 被提成的排单id
	 */
	private String relation_id;

	public String getRelation_id() {
		return relation_id;
	}

	public void setRelation_id(String relation_id) {
		this.relation_id = relation_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Date getTrigger_time() {
		return trigger_time;
	}

	public void setTrigger_time(Date trigger_time) {
		this.trigger_time = trigger_time;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
}
