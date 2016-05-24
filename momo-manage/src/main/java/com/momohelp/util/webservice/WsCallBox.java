package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsCallBox", propOrder = { "mobile", "taskID", "content",
		"receiveTime", "extno" })
public class WsCallBox {

	@XmlElement(name = "Mobile")
	protected String mobile;
	@XmlElement(name = "TaskID")
	protected int taskID;
	@XmlElement(name = "Content")
	protected String content;
	@XmlElement(name = "ReceiveTime")
	protected String receiveTime;
	@XmlElement(name = "Extno")
	protected String extno;

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
	 * ��ȡtaskID���Ե�ֵ��
	 *
	 */
	public int getTaskID() {
		return taskID;
	}

	/**
	 * ����taskID���Ե�ֵ��
	 *
	 */
	public void setTaskID(int value) {
		this.taskID = value;
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
	 * ��ȡreceiveTime���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getReceiveTime() {
		return receiveTime;
	}

	/**
	 * ����receiveTime���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setReceiveTime(String value) {
		this.receiveTime = value;
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
