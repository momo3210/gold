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
@Table(name = "p_buy_sell")
public class BuySell implements Serializable {

	private static final long serialVersionUID = 1109628436450077556L;

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	private String id;

	/**
	 * 匹配数量（双方）
	 */
	private Integer num_matching;

	/**
	 * 匹配时间
	 */
	private Date create_time;

	/**
	 * 卖盘ID
	 */
	private String p_buy_id;

	/**
	 * 卖盘ID
	 */
	private String p_sell_id;

	/**
	 * 0初始化
	 *
	 * 1A打款B
	 *
	 * 2B确认
	 *
	 * 3问题单（双方谁不确认都会变成问题单）
	 */
	private Integer status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getNum_matching() {
		return num_matching;
	}

	public void setNum_matching(Integer num_matching) {
		this.num_matching = num_matching;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getP_buy_id() {
		return p_buy_id;
	}

	public void setP_buy_id(String p_buy_id) {
		this.p_buy_id = p_buy_id;
	}

	public String getP_sell_id() {
		return p_sell_id;
	}

	public void setP_sell_id(String p_sell_id) {
		this.p_sell_id = p_sell_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
