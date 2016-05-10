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

	private Date create_time;
	private Integer num_hatch;
	private String w_farm_chick_id;

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
