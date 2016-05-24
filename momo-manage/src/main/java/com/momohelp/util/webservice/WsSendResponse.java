
package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsSendResponse", propOrder = {
    "returnStatus",
    "message",
    "remainPoint",
    "taskID",
    "successCounts"
})
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

 
    public String getReturnStatus() {
        return returnStatus;
    }

    
    public void setReturnStatus(String value) {
        this.returnStatus = value;
    }

   
    public String getMessage() {
        return message;
    }

  
    public void setMessage(String value) {
        this.message = value;
    }

  
    public int getRemainPoint() {
        return remainPoint;
    }

  
    public void setRemainPoint(int value) {
        this.remainPoint = value;
    }

  
    public int getTaskID() {
        return taskID;
    }

 
    public void setTaskID(int value) {
        this.taskID = value;
    }

   
    public int getSuccessCounts() {
        return successCounts;
    }

 
    public void setSuccessCounts(int value) {
        this.successCounts = value;
    }

}
