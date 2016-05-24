
package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sendSmsResult"
})
@XmlRootElement(name = "SendSmsResponse")
public class SendSmsResponse {

    @XmlElement(name = "SendSmsResult")
    protected WsSendResponse sendSmsResult;

    /**
     * 获取sendSmsResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link WsSendResponse }
     *     
     */
    public WsSendResponse getSendSmsResult() {
        return sendSmsResult;
    }

    /**
     * 设置sendSmsResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link WsSendResponse }
     *     
     */
    public void setSendSmsResult(WsSendResponse value) {
        this.sendSmsResult = value;
    }

}
