package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsOverageResponse", propOrder = { "returnStatus", "message",
		"payInfo", "overage", "sendTotal" })
public class WsOverageResponse {

	@XmlElement(name = "ReturnStatus")
	protected String returnStatus;
	@XmlElement(name = "Message")
	protected String message;
	@XmlElement(name = "PayInfo")
	protected String payInfo;
	@XmlElement(name = "Overage")
	protected int overage;
	@XmlElement(name = "SendTotal")
	protected int sendTotal;

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
	 * ��ȡpayInfo���Ե�ֵ��
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getPayInfo() {
		return payInfo;
	}

	/**
	 * ����payInfo���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setPayInfo(String value) {
		this.payInfo = value;
	}

	/**
	 * ��ȡoverage���Ե�ֵ��
	 *
	 */
	public int getOverage() {
		return overage;
	}

	/**
	 * ����overage���Ե�ֵ��
	 *
	 */
	public void setOverage(int value) {
		this.overage = value;
	}

	/**
	 * ��ȡsendTotal���Ե�ֵ��
	 *
	 */
	public int getSendTotal() {
		return sendTotal;
	}

	/**
	 * ����sendTotal���Ե�ֵ��
	 *
	 */
	public void setSendTotal(int value) {
		this.sendTotal = value;
	}

}
