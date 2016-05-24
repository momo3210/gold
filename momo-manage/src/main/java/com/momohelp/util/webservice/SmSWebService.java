
package com.momohelp.util.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


@WebServiceClient(name = "SmSWebService", targetNamespace = "http://tempuri.org/", wsdlLocation = "http://116.255.221.40:8888/SmsWebService.asmx?wsdl")
public class SmSWebService
    extends Service
{

    private final static URL SMSWEBSERVICE_WSDL_LOCATION;
    private final static WebServiceException SMSWEBSERVICE_EXCEPTION;
    private final static QName SMSWEBSERVICE_QNAME = new QName("http://tempuri.org/", "SmSWebService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://116.255.221.40:8888/SmsWebService.asmx?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SMSWEBSERVICE_WSDL_LOCATION = url;
        SMSWEBSERVICE_EXCEPTION = e;
    }

    public SmSWebService() {
        super(__getWsdlLocation(), SMSWEBSERVICE_QNAME);
    }

    public SmSWebService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SMSWEBSERVICE_QNAME, features);
    }

    public SmSWebService(URL wsdlLocation) {
        super(wsdlLocation, SMSWEBSERVICE_QNAME);
    }

    public SmSWebService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SMSWEBSERVICE_QNAME, features);
    }

    public SmSWebService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SmSWebService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SmSWebServiceSoap
     */
    @WebEndpoint(name = "SmSWebServiceSoap")
    public SmSWebServiceSoap getSmSWebServiceSoap() {
        return super.getPort(new QName("http://tempuri.org/", "SmSWebServiceSoap"), SmSWebServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SmSWebServiceSoap
     */
    @WebEndpoint(name = "SmSWebServiceSoap")
    public SmSWebServiceSoap getSmSWebServiceSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "SmSWebServiceSoap"), SmSWebServiceSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SMSWEBSERVICE_EXCEPTION!= null) {
            throw SMSWEBSERVICE_EXCEPTION;
        }
        return SMSWEBSERVICE_WSDL_LOCATION;
    }

}
