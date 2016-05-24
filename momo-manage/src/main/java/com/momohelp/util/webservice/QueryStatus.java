
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
    "statusNum"
})
@XmlRootElement(name = "QueryStatus")
public class QueryStatus {

    protected String userid;
    protected String account;
    protected String password;
    protected String statusNum;

    
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

   
    public String getStatusNum() {
        return statusNum;
    }

   
    public void setStatusNum(String value) {
        this.statusNum = value;
    }

}
