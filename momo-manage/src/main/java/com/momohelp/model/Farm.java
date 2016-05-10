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

	private Date out_time;
	private Integer num_current;
	private Integer num_buy;
	private Integer num_deal;

	/**
	 * 计算奖金（0未计算，1已计算）
	 */
	private Integer calc_bonus;

	/**
	 * 实际的出局时间
	 */
	private Date out_time_actual;

	public Date getOut_time_actual() {
		return out_time_actual;
	}

	public void setOut_time_actual(Date out_time_actual) {
		this.out_time_actual = out_time_actual;
	}

	public Integer getCalc_bonus() {
		return calc_bonus;
	}

	public void setCalc_bonus(Integer calc_bonus) {
		this.calc_bonus = calc_bonus;
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

	public Date getOut_time() {
		return out_time;
	}

	public void setOut_time(Date out_time) {
		this.out_time = out_time;
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
