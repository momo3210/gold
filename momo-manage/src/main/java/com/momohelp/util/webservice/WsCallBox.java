
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

  
    public String getContent() {
        return content;
    }

    
    public void setContent(String value) {
        this.content = value;
    }

  
    public String getReceiveTime() {
        return receiveTime;
    }

    
    public void setReceiveTime(String value) {
        this.receiveTime = value;
    }


    public String getExtno() {
        return extno;
    }

   
    public void setExtno(String value) {
        this.extno = value;
    }

}
