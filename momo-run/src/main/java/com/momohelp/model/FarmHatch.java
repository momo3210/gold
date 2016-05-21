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
@Table(name = "w_farm_hatch")
public class FarmHatch implements Serializable {

	private static final long serialVersionUID = 4120592353591992995L;

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	private String id;

	private String user_id;

	/**
	 * 孵化时间
	 */
	private Date create_time;

	/**
	 * 冻结到期时间
	 */
	private Date freeze_time;

	/**
	 * 孵化数量
	 */
	private Integer num_hatch;

	/**
	 * 关联批次
	 */
	private String w_farm_chick_id;

	/**
	 * 最后一笔孵化
	 */
	private Integer flag_is_last;

	/**
	 * 是否计算过 0未计算 1已计算
	 */
	private Integer flag_calc_bonus;

	public Integer getFlag_calc_bonus() {
		return flag_calc_bonus;
	}

	public void setFlag_calc_bonus(Integer flag_calc_bonus) {
		this.flag_calc_bonus = flag_calc_bonus;
	}

	public Date getFreeze_time() {
		return freeze_time;
	}

	public void setFreeze_time(Date freeze_time) {
		this.freeze_time = freeze_time;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Integer getFlag_is_last() {
		return flag_is_last;
	}

	public void setFlag_is_last(Integer flag_is_last) {
		this.flag_is_last = flag_is_last;
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

	public Integer getNum_hatch() {
		return num_hatch;
	}

	public void setNum_hatch(Integer num_hatch) {
		this.num_hatch = num_hatch;
	}

	public String getW_farm_chick_id() {
		return w_farm_chick_id;
	}

	public void setW_farm_chick_id(String w_farm_chick_id) {
		this.w_farm_chick_id = w_farm_chick_id;
	}
}
