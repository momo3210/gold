
package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "queryStatusResult"
})
@XmlRootElement(name = "QueryStatusResponse")
public class QueryStatusResponse {

    @XmlElement(name = "QueryStatusResult")
    protected WsStatusResponse queryStatusResult;

    public WsStatusResponse getQueryStatusResult() {
        return queryStatusResult;
    }

   
    public void setQueryStatusResult(WsStatusResponse value) {
        this.queryStatusResult = value;
    }

}
