package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

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
