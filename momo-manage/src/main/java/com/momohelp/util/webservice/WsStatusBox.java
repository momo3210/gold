package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsStatusBox", propOrder = { "mobile", "taskID", "status",
		"receiveTime", "errorCode", "extno" })
public class WsStatusBox {

	@XmlElement(name = "Mobile")
	protected String mobile;
	@XmlElement(name = "TaskID")
	protected int taskID;
	@XmlElement(name = "Status")
	protected int status;
	@XmlElement(name = "ReceiveTime")
	protected String receiveTime;
	@XmlElement(name = "ErrorCode")
	protected String errorCode;
	@XmlElement(name = "Extno")
	protected String extno;

	public String getMobile() {
		return mobile;
	}

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
	 * ��ȡstatus���Ե�ֵ��
	 *
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * ����status���Ե�ֵ��
	 *
	 */
	public void setStatus(int value) {
		this.status = value;
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
	 * ��ȡerrorCode���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * ����errorCode���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setErrorCode(String value) {
		this.errorCode = value;
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
