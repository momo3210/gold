
package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsCallBox", propOrder = {
    "mobile",
    "taskID",
    "content",
    "receiveTime",
    "extno"
})
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
     * 获取mobile属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置mobile属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
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
     * 获取content属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置content属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
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
