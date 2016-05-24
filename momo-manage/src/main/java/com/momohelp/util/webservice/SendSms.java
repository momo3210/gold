package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "userid", "account", "password", "mobile",
		"content", "sendTime", "extno" })
@XmlRootElement(name = "SendSms")
public class SendSms {

	protected String userid;
	protected String account;
	protected String password;
	protected String mobile;
	protected String content;
	protected String sendTime;
	protected String extno;

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
	 * ��ȡmobile���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * ����mobile���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setMobile(String value) {
		this.mobile = value;
	}

	/**
	 * ��ȡcontent���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getContent() {
		return content;
	}

	/**
	 * ����content���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setContent(String value) {
		this.content = value;
	}

	/**
	 * ��ȡsendTime���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getSendTime() {
		return sendTime;
	}

	/**
	 * ����sendTime���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setSendTime(String value) {
		this.sendTime = value;
	}

	/**
	 * ��ȡextno���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getExtno() {
		return extno;
	}

	/**
	 * ����extno���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setExtno(String value) {
		this.extno = value;
	}

}
