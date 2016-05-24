
package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "userid",
    "account",
    "password",
    "mobile",
    "content",
    "sendTime",
    "extno"
})
@XmlRootElement(name = "SendSms")
public class SendSms {

    protected String userid;
    protected String account;
    protected String password;
    protected String mobile;
    protected String content;
    protected String sendTime;
    protected String extno;

 
    public String getUserid() {
        return userid;
    }

   
    public void setUserid(String value) {
        this.userid = value;
    }

   
    public String getAccount() {
        return account;
    }

  
    public void setAccount(String value) {
        this.account = value;
    }

  
    public String getPassword() {
        return password;
    }

  
    public void setPassword(String value) {
        this.password = value;
    }

  
    public String getMobile() {
        return mobile;
    }

  
    public void setMobile(String value) {
        this.mobile = value;
    }

  
    public String getContent() {
        return content;
    }

  
    public void setContent(String value) {
        this.content = value;
    }

 
    public String getSendTime() {
        return sendTime;
    }

  
    public void setSendTime(String value) {
        this.sendTime = value;
    }

  
    public String getExtno() {
        return extno;
    }

  
    public void setExtno(String value) {
        this.extno = value;
    }

}
