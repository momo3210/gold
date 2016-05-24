
package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "queryCallResult"
})
@XmlRootElement(name = "QueryCallResponse")
public class QueryCallResponse {

    @XmlElement(name = "QueryCallResult")
    protected WsCallResponse queryCallResult;
    public WsCallResponse getQueryCallResult() {
        return queryCallResult;
    }
    public void setQueryCallResult(WsCallResponse value) {
        this.queryCallResult = value;
    }

}
