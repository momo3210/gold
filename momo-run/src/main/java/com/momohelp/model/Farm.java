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
@Table(name = "w_farm_chick")
public class Farm implements Serializable {

	private static final long serialVersionUID = -9018511068175944699L;

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	private String id;

	private String user_id;
	private Date create_time;

	/**
	 * 出局时间（理论）
	 */
	private Date time_out;
	/**
	 * 成熟时间
	 */
	private Date time_ripe;

	/**
	 * 当前鸡数量
	 */
	private Integer num_current;

	/**
	 * 购买时数量
	 */
	private Integer num_buy;

	/**
	 * 已成交数量
	 */
	private Integer num_deal;

	/**
	 * 计算奖金（0未计算，1已计算）
	 */
	private Integer flag_calc_bonus;

	/**
	 * 实际成交时间
	 */
	private Date time_deal;

	/**
	 * 0初始化 1未出局 2已出局 3强制出局
	 */
	private Integer flag_out;

	/**
	 * 上一单的ID
	 */
	private String pid;

	public Integer getFlag_out() {
		return flag_out;
	}

	public void setFlag_out(Integer flag_out) {
		this.flag_out = flag_out;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Date getTime_deal() {
		return time_deal;
	}

	public void setTime_deal(Date time_deal) {
		this.time_deal = time_deal;
	}

	public Integer getFlag_calc_bonus() {
		return flag_calc_bonus;
	}

	public void setFlag_calc_bonus(Integer flag_calc_bonus) {
		this.flag_calc_bonus = flag_calc_bonus;
	}

	public Date getTime_out() {
		return time_out;
	}

	public void setTime_out(Date time_out) {
		this.time_out = time_out;
	}

	public Date getTime_ripe() {
		return time_ripe;
	}

	public void setTime_ripe(Date time_ripe) {
		this.time_ripe = time_ripe;
	}

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

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getNum_current() {
		return num_current;
	}

	public void setNum_current(Integer num_current) {
		this.num_current = num_current;
	}

	public Integer getNum_buy() {
		return num_buy;
	}

	public void setNum_buy(Integer num_buy) {
		this.num_buy = num_buy;
	}

	public Integer getNum_deal() {
		return num_deal;
	}

	public void setNum_deal(Integer num_deal) {
		this.num_deal = num_deal;
	}

}
