package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 在线工单
 *
 * @author Administrator
 *
 */
@Table(name = "w_work_order")
public class Message implements Serializable {
	private static final long serialVersionUID = 6286684205702978092L;

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	private String id;

	/**
	 * 工单主题
	 */
	private String title;

	/**
	 * 具体内容
	 */
	private String content;

	/**
	 * 发起时间
	 */
	private Date create_time;

	/**
	 * 工单类型
	 */
	private String type_id;

	private String user_id;

	/**
	 * 收件人
	 */
	private String reply_user_id;

	/**
	 * 回复时间
	 */
	private String reply_time;

	/**
	 * 回复内容
	 */
	private String reply_content;

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getReply_user_id() {
		return reply_user_id;
	}

	public void setReply_user_id(String reply_user_id) {
		this.reply_user_id = reply_user_id;
	}

	public String getReply_time() {
		return reply_time;
	}

	public void setReply_time(String reply_time) {
		this.reply_time = reply_time;
	}

	public String getReply_content() {
		return reply_content;
	}

	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
