
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

  
    public int getReturnStatus() {
        return returnStatus;
    }

    
    public void setReturnStatus(int value) {
        this.returnStatus = value;
    }

   
    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public ArrayOfWsCallBox getCallList() {
        return callList;
    }

 
    public void setCallList(ArrayOfWsCallBox value) {
        this.callList = value;
    }

}
