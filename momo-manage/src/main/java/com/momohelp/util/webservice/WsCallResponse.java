
package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsCallResponse", propOrder = {
    "returnStatus",
    "message",
    "callList"
})
public class WsCallResponse {

    @XmlElement(name = "ReturnStatus")
    protected int returnStatus;
    @XmlElement(name = "Message")
    protected String message;
    @XmlElement(name = "CallList")
    protected ArrayOfWsCallBox callList;

    /**
     * 获取returnStatus属性的值。
     * 
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * 设置returnStatus属性的值。
     * 
     */
    public void setReturnStatus(int value) {
        this.returnStatus = value;
    }

    /**
     * 获取message属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置message属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * 获取callList属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfWsCallBox }
     *     
     */
    public ArrayOfWsCallBox getCallList() {
        return callList;
    }

    /**
     * 设置callList属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfWsCallBox }
     *     
     */
    public void setCallList(ArrayOfWsCallBox value) {
        this.callList = value;
    }

}
