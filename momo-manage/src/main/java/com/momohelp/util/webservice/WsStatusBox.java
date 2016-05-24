
package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsStatusBox", propOrder = {
    "mobile",
    "taskID",
    "status",
    "receiveTime",
    "errorCode",
    "extno"
})
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
     * 获取taskID属性的值。
     * 
     */
    public int getTaskID() {
        return taskID;
    }

    /**
     * 设置taskID属性的值。
     * 
     */
    public void setTaskID(int value) {
        this.taskID = value;
    }

    /**
     * 获取status属性的值。
     * 
     */
    public int getStatus() {
        return status;
    }

    /**
     * 设置status属性的值。
     * 
     */
    public void setStatus(int value) {
        this.status = value;
    }

    /**
     * 获取receiveTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiveTime() {
        return receiveTime;
    }

    /**
     * 设置receiveTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiveTime(String value) {
        this.receiveTime = value;
    }

    /**
     * 获取errorCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置errorCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * 获取extno属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtno() {
        return extno;
    }

    /**
     * 设置extno属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtno(String value) {
        this.extno = value;
    }

}
