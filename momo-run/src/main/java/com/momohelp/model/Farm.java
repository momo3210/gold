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
@Table(name = "w_farm_chick")
public class Farm implements Serializable {

	private static final long serialVersionUID = -9018511068175944699L;

	@Id
	@Column(name = "id")
	private String id;

	private String user_id;
	private Date create_time;

	/**
	 * 出局时间（理论）
	 */
	private Date time_out;

	/**
	 * 实际出局时间（当第一次孵化时更新此字段）
	 */
	private Date time_out_real;

	/**
	 * 成熟时间（理论）
	 */
	private Date time_ripe;

	/**
	 * 当前鸡数量（还有多少可以卖出，应在已经成交后才能操作）
	 */
	private Integer num_current;

	/**
	 * 购买时数量
	 */
	private Integer num_buy;

	/**
	 * 已成交数量
	 */
	private Integer num_deal;

	/**
	 * 计算奖金
	 *
	 * 0未计算
	 *
	 * 1已计算
	 */
	private Integer flag_calc_bonus;

	/**
	 * 实际成交时间（当最后一笔钱卖家确认后，更新此字段）
	 */
	private Date time_deal;

	/**
	 * 相对于上级领导
	 *
	 * 1未出局
	 *
	 * 2主动出局
	 *
	 * 3自然出局
	 */
	private Integer flag_out_p;

	/**
	 * 上一单的ID
	 */
	private String pid;

	/**
	 * 相对于上级领导的最近一单
	 */
	private String pid_higher_ups;

	/**
	 * 当前单的上一单是否出局
	 *
	 * 对于自己来说，是重新开始的一次排单
	 *
	 * 1未出局（接上气儿）
	 *
	 * 2主动出局
	 *
	 * 3自然出局
	 */
	private Integer flag_out_self;

	/**
	 * 最后90%打款在3小时内，则奖励 1% 只鸡，最后一次孵化时加入这笔钱
	 */
	private Integer num_reward;

	@Transient
	private List<Buy> buys;

	@Transient
	private List<FarmFeed> farmFeeds;

	@Transient
	private FarmFeed lastFarmFeed;

	@Transient
	private List<FarmHatch> farmHatchs;

	@Transient
	private FarmFeed farmFeed;

	@Transient
	private FarmHatch farmHatch;

	public FarmHatch getFarmHatch() {
		return farmHatch;
	}

	public void setFarmHatch(FarmHatch farmHatch) {
		this.farmHatch = farmHatch;
	}

	public FarmFeed getFarmFeed() {
		return farmFeed;
	}

	public void setFarmFeed(FarmFeed farmFeed) {
		this.farmFeed = farmFeed;
	}

	public FarmFeed getLastFarmFeed() {
		return lastFarmFeed;
	}

	public void setLastFarmFeed(FarmFeed lastFarmFeed) {
		this.lastFarmFeed = lastFarmFeed;
	}

	/**
	 *
	 * 判断排单在当前时间是否出局
	 *
	 * 1未出局（接上气儿）
	 *
	 * 2主动出局
	 *
	 * 3自然出局
	 *
	 * @return
	 */
	public int checkStatusOut() {

		if (null == this.getTime_out_real()) {
			return 1;
		}

		// 实际出局时间在理论出局时间之后
		return this.getTime_out_real().after(this.getTime_out()) ? 3 : 2;
	}

	public List<Buy> getBuys() {
		return buys;
	}

	public void setBuys(List<Buy> buys) {
		this.buys = buys;
	}

	public List<FarmFeed> getFarmFeeds() {
		return farmFeeds;
	}

	public void setFarmFeeds(List<FarmFeed> farmFeeds) {
		this.farmFeeds = farmFeeds;
	}

	public List<FarmHatch> getFarmHatchs() {
		return farmHatchs;
	}

	public void setFarmHatchs(List<FarmHatch> farmHatchs) {
		this.farmHatchs = farmHatchs;
	}

	public Date getTime_out_real() {
		return time_out_real;
	}

	public void setTime_out_real(Date time_out_real) {
		this.time_out_real = time_out_real;
	}

	public Integer getNum_reward() {
		return num_reward;
	}

	public void setNum_reward(Integer num_reward) {
		this.num_reward = num_reward;
	}

	public Integer getFlag_out_self() {
		return flag_out_self;
	}

	public void setFlag_out_self(Integer flag_out_self) {
		this.flag_out_self = flag_out_self;
	}

	public Integer getFlag_out_p() {
		return flag_out_p;
	}

	public void setFlag_out_p(Integer flag_out_p) {
		this.flag_out_p = flag_out_p;
	}

	public String getPid_higher_ups() {
		return pid_higher_ups;
	}

	public void setPid_higher_ups(String pid_higher_ups) {
		this.pid_higher_ups = pid_higher_ups;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Date getTime_deal() {
		return time_deal;
	}

	public void setTime_deal(Date time_deal) {
		this.time_deal = time_deal;
	}

	public Integer getFlag_calc_bonus() {
		return flag_calc_bonus;
	}

	public void setFlag_calc_bonus(Integer flag_calc_bonus) {
		this.flag_calc_bonus = flag_calc_bonus;
	}

	public Date getTime_out() {
		return time_out;
	}

	public void setTime_out(Date time_out) {
		this.time_out = time_out;
	}

	public Date getTime_ripe() {
		return time_ripe;
	}

	public void setTime_ripe(Date time_ripe) {
		this.time_ripe = time_ripe;
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

	public Integer getNum_current() {
		return num_current;
	}

	public void setNum_current(Integer num_current) {
		this.num_current = num_current;
	}

	public Integer getNum_buy() {
		return num_buy;
	}

	public void setNum_buy(Integer num_buy) {
		this.num_buy = num_buy;
	}

	public Integer getNum_deal() {
		return num_deal;
	}

	public void setNum_deal(Integer num_deal) {
		this.num_deal = num_deal;
	}

}
