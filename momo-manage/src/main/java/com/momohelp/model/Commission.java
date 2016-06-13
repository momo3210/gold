package com.momohelp.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Administrator
 *
 */
public class Commission implements Serializable {

	private static final long serialVersionUID = -6747888613393179682L;

	private String id;

	private Double total_dynamic;
	private Integer flag;
	private Integer depth;
	private String relation_id;
	private Date trigger_time;

	private String relation_user_id;

	private Double money;

	public String getRelation_user_id() {
		return relation_user_id;
	}

	public void setRelation_user_id(String relation_user_id) {
		this.relation_user_id = relation_user_id;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getTotal_dynamic() {
		return total_dynamic;
	}

	public void setTotal_dynamic(Double total_dynamic) {
		this.total_dynamic = total_dynamic;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public String getRelation_id() {
		return relation_id;
	}

	public void setRelation_id(String relation_id) {
		this.relation_id = relation_id;
	}

	public Date getTrigger_time() {
		return trigger_time;
	}

	public void setTrigger_time(Date trigger_time) {
		this.trigger_time = trigger_time;
	}

}
