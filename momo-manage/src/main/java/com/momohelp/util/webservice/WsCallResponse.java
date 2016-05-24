package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsCallResponse", propOrder = { "returnStatus", "message",
		"callList" })
public class WsCallResponse {

	@XmlElement(name = "ReturnStatus")
	protected int returnStatus;
	@XmlElement(name = "Message")
	protected String message;
	@XmlElement(name = "CallList")
	protected ArrayOfWsCallBox callList;

	/**
	 * ��ȡreturnStatus���Ե�ֵ��
	 *
	 */
	public int getReturnStatus() {
		return returnStatus;
	}

	/**
	 * ����returnStatus���Ե�ֵ��
	 *
	 */
	public void setReturnStatus(int value) {
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
	 * ��ȡcallList���Ե�ֵ��
	 *
	 * @return possible object is {@link ArrayOfWsCallBox }
	 *
	 */
	public ArrayOfWsCallBox getCallList() {
		return callList;
	}

	/**
	 * ����callList���Ե�ֵ��
	 *
	 * @param value
	 *            allowed object is {@link ArrayOfWsCallBox }
	 *
	 */
	public void setCallList(ArrayOfWsCallBox value) {
		this.callList = value;
	}

}
