package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 *
 */
@Table(name = "s_user")
public class User implements Serializable {
	private static final long serialVersionUID = -1453141126844138706L;

	/**
	 * 会员编号 M08351506
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 登陆密码
	 */
	private String user_pass;

	/**
	 * 安全密码
	 */
	private String user_pass_safe;

	/**
	 * 姓名
	 */
	private String real_name;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 电子邮箱
	 */
	private String email;

	/**
	 * 支付宝帐号
	 */
	private String alipay_account;

	/**
	 * 微信号
	 */
	private String wx_account;

	private Date create_time;
	private Integer status;

	private String apikey;
	private String seckey;

	/**
	 * 开户银行
	 */
	private String bank;

	/**
	 * 开户分行
	 */
	private String bank_name;

	/**
	 * 银行帐号
	 */
	private String bank_account;

	/**
	 * 门票数量
	 */
	private Integer ticket_num;

	/**
	 * 食物数量
	 */
	private Integer food_num;

	/**
	 * 父ID
	 */
	private String pid;

	/**
	 * 家族（最顶端的用户id）
	 */
	private String family_id;

	/**
	 * 深度， 从1开始（最顶端为1）
	 */
	private Integer deepth;

	/**
	 * level 级别（4贫农、3中农、2富农、1农场主）
	 */
	private Integer lv;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getFamily_id() {
		return family_id;
	}

	public void setFamily_id(String family_id) {
		this.family_id = family_id;
	}

	public Integer getDeepth() {
		return deepth;
	}

	public void setDeepth(Integer deepth) {
		this.deepth = deepth;
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public Integer getFood_num() {
		return food_num;
	}

	public void setFood_num(Integer food_num) {
		this.food_num = food_num;
	}

	public Integer getTicket_num() {
		return ticket_num;
	}

	public void setTicket_num(Integer ticket_num) {
		this.ticket_num = ticket_num;
	}

	public String getUser_pass_safe() {
		return user_pass_safe;
	}

	public void setUser_pass_safe(String user_pass_safe) {
		this.user_pass_safe = user_pass_safe;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getWx_account() {
		return wx_account;
	}

	public void setWx_account(String wx_account) {
		this.wx_account = wx_account;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_account() {
		return bank_account;
	}

	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}

	public String getAlipay_account() {
		return alipay_account;
	}

	public void setAlipay_account(String alipay_account) {
		this.alipay_account = alipay_account;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getSeckey() {
		return seckey;
	}

	public void setSeckey(String seckey) {
		this.seckey = seckey;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_pass() {
		return user_pass;
	}

	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
