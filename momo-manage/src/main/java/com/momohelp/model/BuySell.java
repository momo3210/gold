package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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

	private String p_buy_user_id;
	private String p_sell_user_id;

	/**
	 * 买家上传的图片（只能上传一张）
	 */
	private String p_buy_user_img;

	/**
	 * 买家的确认内容
	 */
	private String p_buy_user_content;

	/**
	 * 买家的确认时间
	 */
	private Date p_buy_user_time;

	/**
	 * 卖家的确认时间
	 */
	private Date p_sell_user_time;

	/**
	 * 举报人id
	 */
	private String tip_off_user_id;

	/**
	 * 举报处理结果的内容（有则表示管理员已经处理过这条信息）
	 */
	private String tip_off_result;

	/**
	 * 举报时间
	 */
	private Date tip_off_time;

	/**
	 * 举报内容
	 */
	private String tip_off_content;

	/**
	 * 买家对象
	 */
	@Transient
	private User p_buy_user;

	/**
	 * 卖家对象
	 */
	@Transient
	private User p_sell_user;

	public Date getTip_off_time() {
		return tip_off_time;
	}

	public void setTip_off_time(Date tip_off_time) {
		this.tip_off_time = tip_off_time;
	}

	public String getTip_off_content() {
		return tip_off_content;
	}

	public void setTip_off_content(String tip_off_content) {
		this.tip_off_content = tip_off_content;
	}

	public String getTip_off_result() {
		return tip_off_result;
	}

	public void setTip_off_result(String tip_off_result) {
		this.tip_off_result = tip_off_result;
	}

	public Date getP_sell_user_time() {
		return p_sell_user_time;
	}

	public void setP_sell_user_time(Date p_sell_user_time) {
		this.p_sell_user_time = p_sell_user_time;
	}

	public Date getP_buy_user_time() {
		return p_buy_user_time;
	}

	public void setP_buy_user_time(Date p_buy_user_time) {
		this.p_buy_user_time = p_buy_user_time;
	}

	public String getP_buy_user_img() {
		return p_buy_user_img;
	}

	public void setP_buy_user_img(String p_buy_user_img) {
		this.p_buy_user_img = p_buy_user_img;
	}

	public String getP_buy_user_content() {
		return p_buy_user_content;
	}

	public void setP_buy_user_content(String p_buy_user_content) {
		this.p_buy_user_content = p_buy_user_content;
	}

	public String getTip_off_user_id() {
		return tip_off_user_id;
	}

	public void setTip_off_user_id(String tip_off_user_id) {
		this.tip_off_user_id = tip_off_user_id;
	}

	public User getP_buy_user() {
		return p_buy_user;
	}

	public void setP_buy_user(User p_buy_user) {
		this.p_buy_user = p_buy_user;
	}

	public User getP_sell_user() {
		return p_sell_user;
	}

	public void setP_sell_user(User p_sell_user) {
		this.p_sell_user = p_sell_user;
	}

	public String getP_buy_user_id() {
		return p_buy_user_id;
	}

	public void setP_buy_user_id(String p_buy_user_id) {
		this.p_buy_user_id = p_buy_user_id;
	}

	public String getP_sell_user_id() {
		return p_sell_user_id;
	}

	public void setP_sell_user_id(String p_sell_user_id) {
		this.p_sell_user_id = p_sell_user_id;
	}

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
