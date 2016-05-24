
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

  
    public String getPayInfo() {
        return payInfo;
    }

  
    public void setPayInfo(String value) {
        this.payInfo = value;
    }

    
    public int getOverage() {
        return overage;
    }

   
    public void setOverage(int value) {
        this.overage = value;
    }

   
    public int getSendTotal() {
        return sendTotal;
    }

  
    public void setSendTotal(int value) {
        this.sendTotal = value;
    }

}
