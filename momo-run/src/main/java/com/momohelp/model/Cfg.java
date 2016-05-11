package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 *
 */
@Table(name = "s_cfg")
public class Cfg implements Serializable {

	private static final long serialVersionUID = -1784574080098740265L;

	@Id
	private String key_;

	private String value_;

	private Date create_time;

	public String getKey_() {
		return key_;
	}

	public void setKey_(String key_) {
		this.key_ = key_;
	}

	public String getValue_() {
		return value_;
	}

	public void setValue_(String value_) {
		this.value_ = value_;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
