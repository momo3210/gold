package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Administrator
 *
 */
@Table(name = "p_sell")
public class Sell implements Serializable {

	private static final long serialVersionUID = 1687652136137493371L;

	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 卖出数量
	 */
	private Integer num_sell;

	/**
	 * 成交时间
	 */
	private Date time_deal;

	/**
	 * 卖出时间
	 */
	private Date create_time;
	private String user_id;

	/**
	 * 1静态钱包
	 *
	 * 2动态钱包
	 */
	private Integer type_id;

	@Transient
	private List<BuySell> buySells;

	public List<BuySell> getBuySells() {
		return buySells;
	}

	public void setBuySells(List<BuySell> buySells) {
		this.buySells = buySells;
	}

	public Date getTime_deal() {
		return time_deal;
	}

	public void setTime_deal(Date time_deal) {
		this.time_deal = time_deal;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getNum_sell() {
		return num_sell;
	}

	public void setNum_sell(Integer num_sell) {
		this.num_sell = num_sell;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Integer getType_id() {
		return type_id;
	}

	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}
}
