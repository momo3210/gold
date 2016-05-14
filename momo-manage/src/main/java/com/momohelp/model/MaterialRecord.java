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

	/**
	 * 本次使用的数额
	 */
	private Double num_use;

	/**
	 * 0未打款 1打款成功（购买时使用）
	 */
	private Integer status;

	/**
	 * 1门票 2饲料
	 */
	private Integer type_id;
	private String comment;

	/**
	 * 转给某某的用户ID
	 */
	private String trans_user_id;

	/**
	 * 当前余额
	 */
	private Integer num_balance;

	/**
	 * 正负标记（0负 1正）
	 */
	private Integer flag_plus_minus;

	public Integer getNum_balance() {
		return num_balance;
	}

	public void setNum_balance(Integer num_balance) {
		this.num_balance = num_balance;
	}

	public Integer getFlag_plus_minus() {
		return flag_plus_minus;
	}

	public void setFlag_plus_minus(Integer flag_plus_minus) {
		this.flag_plus_minus = flag_plus_minus;
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

	public Double getNum_use() {
		return num_use;
	}

	public void setNum_use(Double num_use) {
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
