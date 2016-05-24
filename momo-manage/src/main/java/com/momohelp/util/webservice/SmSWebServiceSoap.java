
package com.momohelp.util.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


@WebService(name = "SmSWebServiceSoap", targetNamespace = "http://tempuri.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SmSWebServiceSoap {


    /**
     * 
     * @param password
     * @param extno
     * @param mobile
     * @param userid
     * @param account
     * @param content
     * @param sendTime
     * @return
     *     returns com.momohelp.calculate.webservice.WsSendResponse
     */
    @WebMethod(operationName = "SendSms", action = "http://tempuri.org/SendSms")
    @WebResult(name = "SendSmsResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "SendSms", targetNamespace = "http://tempuri.org/", className = "com.momohelp.calculate.webservice.SendSms")
    @ResponseWrapper(localName = "SendSmsResponse", targetNamespace = "http://tempuri.org/", className = "com.momohelp.calculate.webservice.SendSmsResponse")
    public WsSendResponse sendSms(
        @WebParam(name = "userid", targetNamespace = "http://tempuri.org/")
        String userid,
        @WebParam(name = "account", targetNamespace = "http://tempuri.org/")
        String account,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        String password,
        @WebParam(name = "mobile", targetNamespace = "http://tempuri.org/")
        String mobile,
        @WebParam(name = "content", targetNamespace = "http://tempuri.org/")
        String content,
        @WebParam(name = "sendTime", targetNamespace = "http://tempuri.org/")
        String sendTime,
        @WebParam(name = "extno", targetNamespace = "http://tempuri.org/")
        String extno);

    /**
     * 
     * @param password
     * @param userid
     * @param account
     * @return
     *     returns com.momohelp.calculate.webservice.WsOverageResponse
     */
    @WebMethod(operationName = "QueryOverage", action = "http://tempuri.org/QueryOverage")
    @WebResult(name = "QueryOverageResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "QueryOverage", targetNamespace = "http://tempuri.org/", className = "com.momohelp.calculate.webservice.QueryOverage")
    @ResponseWrapper(localName = "QueryOverageResponse", targetNamespace = "http://tempuri.org/", className = "com.momohelp.calculate.webservice.QueryOverageResponse")
    public WsOverageResponse queryOverage(
        @WebParam(name = "userid", targetNamespace = "http://tempuri.org/")
        String userid,
        @WebParam(name = "account", targetNamespace = "http://tempuri.org/")
        String account,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        String password);

    /**
     * 
     * @param password
     * @param userid
     * @param account
     * @param statusNum
     * @return
     *     returns com.momohelp.calculate.webservice.WsStatusResponse
     */
    @WebMethod(operationName = "QueryStatus", action = "http://tempuri.org/QueryStatus")
    @WebResult(name = "QueryStatusResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "QueryStatus", targetNamespace = "http://tempuri.org/", className = "com.momohelp.calculate.webservice.QueryStatus")
    @ResponseWrapper(localName = "QueryStatusResponse", targetNamespace = "http://tempuri.org/", className = "com.momohelp.calculate.webservice.QueryStatusResponse")
    public WsStatusResponse queryStatus(
        @WebParam(name = "userid", targetNamespace = "http://tempuri.org/")
        String userid,
        @WebParam(name = "account", targetNamespace = "http://tempuri.org/")
        String account,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        String password,
        @WebParam(name = "statusNum", targetNamespace = "http://tempuri.org/")
        String statusNum);

    /**
     * 
     * @param password
     * @param callNum
     * @param userid
     * @param account
     * @return
     *     returns com.momohelp.calculate.webservice.WsCallResponse
     */
    @WebMethod(operationName = "QueryCall", action = "http://tempuri.org/QueryCall")
    @WebResult(name = "QueryCallResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "QueryCall", targetNamespace = "http://tempuri.org/", className = "com.momohelp.calculate.webservice.QueryCall")
    @ResponseWrapper(localName = "QueryCallResponse", targetNamespace = "http://tempuri.org/", className = "com.momohelp.calculate.webservice.QueryCallResponse")
    public WsCallResponse queryCall(
        @WebParam(name = "userid", targetNamespace = "http://tempuri.org/")
        String userid,
        @WebParam(name = "account", targetNamespace = "http://tempuri.org/")
        String account,
        @WebParam(name = "password", targetNamespace = "http://tempuri.org/")
        String password,
        @WebParam(name = "callNum", targetNamespace = "http://tempuri.org/")
        String callNum);

}
