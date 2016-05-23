
package com.momohelp.calculate.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "queryOverageResult"
})
@XmlRootElement(name = "QueryOverageResponse")
public class QueryOverageResponse {

    @XmlElement(name = "QueryOverageResult")
    protected WsOverageResponse queryOverageResult;

    /**
     * ��ȡqueryOverageResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link WsOverageResponse }
     *     
     */
    public WsOverageResponse getQueryOverageResult() {
        return queryOverageResult;
    }

    /**
     * ����queryOverageResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link WsOverageResponse }
     *     
     */
    public void setQueryOverageResult(WsOverageResponse value) {
        this.queryOverageResult = value;
    }

}
