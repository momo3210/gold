package com.momohelp.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 *
 */
public class SysCount implements Serializable {

	private static final long serialVersionUID = 7513713053933039852L;

	/**
	 * 会员总数
	 */
	private Integer hyzs;

	/**
	 * 今日喂鸡数
	 */
	private Integer jrwjs;

	/**
	 * 今日孵化数
	 */
	private Integer jrfhs;

	/**
	 * 累计门票数
	 */
	private Integer ljmps;

	/**
	 * 累计饲料数
	 */
	private Integer ljsls;

	/**
	 * 静态钱包
	 */
	private Integer jtqb;

	/**
	 * 排单量
	 */
	private Integer pdl;

	/**
	 * 卖盘量
	 */
	private Integer mpl;

	/**
	 * 匹配量
	 */
	private Integer ppl;

	public Integer getHyzs() {
		return hyzs;
	}

	public void setHyzs(Integer hyzs) {
		this.hyzs = hyzs;
	}

	public Integer getJrwjs() {
		return jrwjs;
	}

	public void setJrwjs(Integer jrwjs) {
		this.jrwjs = jrwjs;
	}

	public Integer getJrfhs() {
		return jrfhs;
	}

	public void setJrfhs(Integer jrfhs) {
		this.jrfhs = jrfhs;
	}

	public Integer getLjmps() {
		return ljmps;
	}

	public void setLjmps(Integer ljmps) {
		this.ljmps = ljmps;
	}

	public Integer getLjsls() {
		return ljsls;
	}

	public void setLjsls(Integer ljsls) {
		this.ljsls = ljsls;
	}

	public Integer getJtqb() {
		return jtqb;
	}

	public void setJtqb(Integer jtqb) {
		this.jtqb = jtqb;
	}

	public Integer getPdl() {
		return pdl;
	}

	public void setPdl(Integer pdl) {
		this.pdl = pdl;
	}

	public Integer getMpl() {
		return mpl;
	}

	public void setMpl(Integer mpl) {
		this.mpl = mpl;
	}

	public Integer getPpl() {
		return ppl;
	}

	public void setPpl(Integer ppl) {
		this.ppl = ppl;
	}

}
