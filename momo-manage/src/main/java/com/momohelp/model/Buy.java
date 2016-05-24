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
@Table(name = "p_buy")
public class Buy implements Serializable {

	private static final long serialVersionUID = -8880872222905009779L;

	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 关联批次
	 */
	private String w_farm_chick_id;

	/**
	 * 购买数量
	 */
	private Integer num_buy;

	/**
	 * 购买时间
	 */
	private Date create_time;

	/**
	 * 计算时间
	 */
	private Date calc_time;

	private String user_id;

	/**
	 * 真实的成交时间（后台计算后，更新此字段）
	 */
	private Date time_deal;

	/**
	 * 是否是订金（预付款）
	 *
	 * 1是
	 *
	 * 0不是
	 */
	private Integer is_deposit;

	private Integer num_deal;

	@Transient
	private List<BuySell> buySells;

	@Transient
	private Farm farm;

	public Integer getNum_deal() {
		return num_deal;
	}

	public void setNum_deal(Integer num_deal) {
		this.num_deal = num_deal;
	}

	public Farm getFarm() {
		return farm;
	}

	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	public Integer getIs_deposit() {
		return is_deposit;
	}

	public void setIs_deposit(Integer is_deposit) {
		this.is_deposit = is_deposit;
	}

	public List<BuySell> getBuySells() {
		return buySells;
	}

	public void setBuySells(List<BuySell> buySells) {
		this.buySells = buySells;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Date getTime_deal() {
		return time_deal;
	}

	public void setTime_deal(Date time_deal) {
		this.time_deal = time_deal;
	}

	public Date getCalc_time() {
		return calc_time;
	}

	public void setCalc_time(Date calc_time) {
		this.calc_time = calc_time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getW_farm_chick_id() {
		return w_farm_chick_id;
	}

	public void setW_farm_chick_id(String w_farm_chick_id) {
		this.w_farm_chick_id = w_farm_chick_id;
	}

	public Integer getNum_buy() {
		return num_buy;
	}

	public void setNum_buy(Integer num_buy) {
		this.num_buy = num_buy;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
