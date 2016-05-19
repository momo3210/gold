package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 *
 */
@Table(name = "w_farm_feed")
public class FarmFeed implements Serializable {

	private static final long serialVersionUID = -6448206516414282722L;

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	private String id;

	private String user_id;

	/**
	 * 喂食时间
	 */
	private Date create_time;

	/**
	 * 喂食数量
	 */
	private Integer num_feed;

	/**
	 * 关联批次
	 */
	private String w_farm_chick_id;

	/**
	 * 此次的利息金额
	 */
	private Double price;

	/**
	 * 喂鸡顺序
	 *
	 * 第一次、第二次等等
	 */
	private Integer order_feed;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Integer getOrder_feed() {
		return order_feed;
	}

	public void setOrder_feed(Integer order_feed) {
		this.order_feed = order_feed;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getNum_feed() {
		return num_feed;
	}

	public void setNum_feed(Integer num_feed) {
		this.num_feed = num_feed;
	}

	public String getW_farm_chick_id() {
		return w_farm_chick_id;
	}

	public void setW_farm_chick_id(String w_farm_chick_id) {
		this.w_farm_chick_id = w_farm_chick_id;
	}
}
