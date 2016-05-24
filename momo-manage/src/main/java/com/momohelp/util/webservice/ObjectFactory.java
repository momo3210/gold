
package com.momohelp.util.webservice;

import javax.xml.bind.annotation.XmlRegistry;


@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }
    public QueryStatusResponse createQueryStatusResponse() {
        return new QueryStatusResponse();
    }
    public WsStatusResponse createWsStatusResponse() {
        return new WsStatusResponse();
    }
    public QueryCall createQueryCall() {
        return new QueryCall();
    }

    public QueryCallResponse createQueryCallResponse() {
        return new QueryCallResponse();
    }
    public WsCallResponse createWsCallResponse() {
        return new WsCallResponse();
    }

    public SendSms createSendSms() {
        return new SendSms();
    }

    public QueryOverage createQueryOverage() {
        return new QueryOverage();
    }
    public SendSmsResponse createSendSmsResponse() {
        return new SendSmsResponse();
    }
    public WsSendResponse createWsSendResponse() {
        return new WsSendResponse();
    }
    public QueryStatus createQueryStatus() {
        return new QueryStatus();
    }
    public QueryOverageResponse createQueryOverageResponse() {
        return new QueryOverageResponse();
    }
    public WsOverageResponse createWsOverageResponse() {
        return new WsOverageResponse();
    }
    public WsCallBox createWsCallBox() {
        return new WsCallBox();
    }

    public ArrayOfWsCallBox createArrayOfWsCallBox() {
        return new ArrayOfWsCallBox();
    }

    public ArrayOfWsStatusBox createArrayOfWsStatusBox() {
        return new ArrayOfWsStatusBox();
    }

    public WsStatusBox createWsStatusBox() {
        return new WsStatusBox();
    }

}
