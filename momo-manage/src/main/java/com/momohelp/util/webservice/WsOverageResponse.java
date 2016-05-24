
package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsOverageResponse", propOrder = {
    "returnStatus",
    "message",
    "payInfo",
    "overage",
    "sendTotal"
})
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
     * 获取returnStatus属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnStatus() {
        return returnStatus;
    }

    /**
     * 设置returnStatus属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnStatus(String value) {
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
     * 获取payInfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayInfo() {
        return payInfo;
    }

    /**
     * 设置payInfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayInfo(String value) {
        this.payInfo = value;
    }

    /**
     * 获取overage属性的值。
     * 
     */
    public int getOverage() {
        return overage;
    }

    /**
     * 设置overage属性的值。
     * 
     */
    public void setOverage(int value) {
        this.overage = value;
    }

    /**
     * 获取sendTotal属性的值。
     * 
     */
    public int getSendTotal() {
        return sendTotal;
    }

    /**
     * 设置sendTotal属性的值。
     * 
     */
    public void setSendTotal(int value) {
        this.sendTotal = value;
    }

}
