package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "userid", "account", "password", "statusNum" })
@XmlRootElement(name = "QueryStatus")
public class QueryStatus {

	protected String userid;
	protected String account;
	protected String password;
	protected String statusNum;

	/**
	 * ��ȡuserid���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * ����userid���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setUserid(String value) {
		this.userid = value;
	}

	/**
	 * ��ȡaccount���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * ����account���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setAccount(String value) {
		this.account = value;
	}

	/**
	 * ��ȡpassword���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * ����password���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setPassword(String value) {
		this.password = value;
	}

	/**
	 * ��ȡstatusNum���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getStatusNum() {
		return statusNum;
	}

	/**
	 * ����statusNum���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setStatusNum(String value) {
		this.statusNum = value;
	}

}
