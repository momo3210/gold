package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 网站公告
 *
 * @author Administrator
 *
 */
@Table(name = "w_notice")
public class Notice implements Serializable {

	private static final long serialVersionUID = 325747742798753510L;

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	private String id;

	/**
	 * 公告标题
	 */
	private String title;

	/**
	 * 公告内容
	 */
	private String content;

	/**
	 * 发布时间
	 */
	private Date create_time;

	/**
	 * 发布人
	 */
	private String user_id;

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
}