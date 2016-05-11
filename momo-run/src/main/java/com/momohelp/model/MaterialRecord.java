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
@Table(name = "w_material_use")
public class MaterialRecord implements Serializable {

	private static final long serialVersionUID = -271456547236518576L;

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	private String id;

	private String user_id;
	private Date create_time;

	private Integer num_use;
	private Integer status;
	private Integer type_id;
	private String comment;
	private String trans_user_id;

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

	public Integer getNum_use() {
		return num_use;
	}

	public void setNum_use(Integer num_use) {
		this.num_use = num_use;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType_id() {
		return type_id;
	}

	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTrans_user_id() {
		return trans_user_id;
	}

	public void setTrans_user_id(String trans_user_id) {
		this.trans_user_id = trans_user_id;
	}
}
