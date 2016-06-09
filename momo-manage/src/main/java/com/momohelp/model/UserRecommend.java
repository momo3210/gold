package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 用户推荐清单
 *
 * @author Administrator
 *
 */
public class UserRecommend implements Serializable {

	private static final long serialVersionUID = -4419962051641298704L;

	private String id;

	private String nickname;
	private String real_name;
	private String mobile;
	private Integer moCount;
	private Integer teamCount;

	private Double total_static;

	public Double getTotal_static() {
		return total_static;
	}

	public void setTotal_static(Double total_static) {
		this.total_static = total_static;
	}

	private Date create_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getMoCount() {
		return moCount;
	}

	public void setMoCount(Integer moCount) {
		this.moCount = moCount;
	}

	public Integer getTeamCount() {
		return teamCount;
	}

	public void setTeamCount(Integer teamCount) {
		this.teamCount = teamCount;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
