package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsSendResponse", propOrder = { "returnStatus", "message",
		"remainPoint", "taskID", "successCounts" })
public class WsSendResponse {

	@XmlElement(name = "ReturnStatus")
	protected String returnStatus;
	@XmlElement(name = "Message")
	protected String message;
	@XmlElement(name = "RemainPoint")
	protected int remainPoint;
	@XmlElement(name = "TaskID")
	protected int taskID;
	@XmlElement(name = "SuccessCounts")
	protected int successCounts;

	/**
	 * ��ȡreturnStatus���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getReturnStatus() {
		return returnStatus;
	}

	/**
	 * ����returnStatus���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setReturnStatus(String value) {
		this.returnStatus = value;
	}

	/**
	 * ��ȡmessage���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * ����message���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setMessage(String value) {
		this.message = value;
	}

	/**
	 * ��ȡremainPoint���Ե�ֵ��
	 *
	 */
	public int getRemainPoint() {
		return remainPoint;
	}

	/**
	 * ����remainPoint���Ե�ֵ��
	 *
	 */
	public void setRemainPoint(int value) {
		this.remainPoint = value;
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
	 * ��ȡsuccessCounts���Ե�ֵ��
	 *
	 */
	public int getSuccessCounts() {
		return successCounts;
	}

	/**
	 * ����successCounts���Ե�ֵ��
	 *
	 */
	public void setSuccessCounts(int value) {
		this.successCounts = value;
	}

}
