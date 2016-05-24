
package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsStatusResponse", propOrder = {
    "returnStatus",
    "message",
    "statusList"
})
public class WsStatusResponse {

    @XmlElement(name = "ReturnStatus")
    protected int returnStatus;
    @XmlElement(name = "Message")
    protected String message;
    @XmlElement(name = "StatusList")
    protected ArrayOfWsStatusBox statusList;

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
    public ArrayOfWsStatusBox getStatusList() {
        return statusList;
    }
    public void setStatusList(ArrayOfWsStatusBox value) {
        this.statusList = value;
    }

}
