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

	/**
	 * 孵化时间
	 */
	private Date create_time;

	/**
	 * 孵化数量
	 */
	private Integer num_hatch;

	/**
	 * 关联批次
	 */
	private String w_farm_chick_id;

	/**
	 * 最后一笔卖出
	 */
	private Integer flag_is_last;

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
