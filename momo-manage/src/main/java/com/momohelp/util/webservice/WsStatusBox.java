
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

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int value) {
        this.taskID = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int value) {
        this.status = value;
    }

    public String getReceiveTime() {
        return receiveTime;
    }


    public void setReceiveTime(String value) {
        this.receiveTime = value;
    }

    public String getErrorCode() {
        return errorCode;
    }


    public void setErrorCode(String value) {
        this.errorCode = value;
    }

   
    public String getExtno() {
        return extno;
    }

   
    public void setExtno(String value) {
        this.extno = value;
    }

}
