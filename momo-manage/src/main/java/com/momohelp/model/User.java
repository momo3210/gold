package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	 * 父ID
	 */
	private String pid;

	/**
	 * 登陆密码
	 */
	private String user_pass;

	/**
	 * 安全密码
	 */
	private String user_pass_safe;

	/**
	 * 1为后台管理用户 2为会员
	 */
	private Integer role_id;

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
	 * 家族（最顶端的用户id）
	 */
	private String family_id;

	/**
	 * 深度， 从1开始（最顶端为1）
	 */
	private Integer depth;

	/**
	 * level 级别（05贫农、06中农、07富农、08农场主）
	 */
	private String lv;

	private Double num_static;
	private Double num_dynamic;
	private Integer num_ticket;
	private Integer num_food;

	private Double total_static;
	private Double total_dynamic;
	private Integer total_ticket;
	private Integer total_food;

	/**
	 * 短信验证码
	 */
	private String verifycode_sms;

	/**
	 * 父对象
	 */
	@Transient
	private User p_user;

	/**
	 * 直推的下一级用户
	 */
	@Transient
	private List<User> children;

	/**
	 * 用户的最后一次排单（鸡苗批次）
	 */
	@Transient
	private Farm lastFarm;

	/**
	 * 最新的卖盘
	 */
	@Transient
	private Sell lastSell;

	/**
	 * 每月的卖盘
	 */
	@Transient
	private List<Sell> monthSells;

	public Sell getLastSell() {
		return lastSell;
	}

	public void setLastSell(Sell lastSell) {
		this.lastSell = lastSell;
	}

	public List<Sell> getMonthSells() {
		return monthSells;
	}

	public void setMonthSells(List<Sell> monthSells) {
		this.monthSells = monthSells;
	}

	public Farm getLastFarm() {
		return lastFarm;
	}

	public void setLastFarm(Farm lastFarm) {
		this.lastFarm = lastFarm;
	}

	public BuyMo getBuyMo() {
		BuyMo buyMo = new BuyMo();

		buyMo.setMax(5000);

		if ("05".equals(lv)) {
			buyMo.setMin(100);
		} else if ("06".equals(lv)) {
			buyMo.setMin(1000);
		} else if ("07".equals(lv)) {
			buyMo.setMin(2000);
		} else if ("08".equals(lv)) {
			buyMo.setMin(3000);
		} // if

		return buyMo;
	}

	/**
	 * 买入鸡苗的限制
	 *
	 * @author Administrator
	 *
	 */
	public class BuyMo {
		private Integer max;
		private Integer min;

		public Integer getMax() {
			return max;
		}

		public void setMax(Integer max) {
			this.max = max;
		}

		public Integer getMin() {
			return min;
		}

		public void setMin(Integer min) {
			this.min = min;
		}
	}

	public String getVerifycode_sms() {
		return verifycode_sms;
	}

	public void setVerifycode_sms(String verifycode_sms) {
		this.verifycode_sms = verifycode_sms;
	}

	public List<User> getChildren() {
		return children;
	}

	public void setChildren(List<User> children) {
		this.children = children;
	}

	public User getP_user() {
		return p_user;
	}

	public void setP_user(User p_user) {
		this.p_user = p_user;
	}

	public Double getNum_static() {
		return num_static;
	}

	public void setNum_static(Double num_static) {
		this.num_static = num_static;
	}

	public Double getNum_dynamic() {
		return num_dynamic;
	}

	public void setNum_dynamic(Double num_dynamic) {
		this.num_dynamic = num_dynamic;
	}

	public Integer getNum_ticket() {
		return num_ticket;
	}

	public void setNum_ticket(Integer num_ticket) {
		this.num_ticket = num_ticket;
	}

	public Integer getNum_food() {
		return num_food;
	}

	public void setNum_food(Integer num_food) {
		this.num_food = num_food;
	}

	public Double getTotal_static() {
		return total_static;
	}

	public void setTotal_static(Double total_static) {
		this.total_static = total_static;
	}

	public Double getTotal_dynamic() {
		return total_dynamic;
	}

	public void setTotal_dynamic(Double total_dynamic) {
		this.total_dynamic = total_dynamic;
	}

	public Integer getTotal_ticket() {
		return total_ticket;
	}

	public void setTotal_ticket(Integer total_ticket) {
		this.total_ticket = total_ticket;
	}

	public Integer getTotal_food() {
		return total_food;
	}

	public void setTotal_food(Integer total_food) {
		this.total_food = total_food;
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

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

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public String getLv() {
		return lv;
	}

	public void setLv(String lv) {
		this.lv = lv;
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
