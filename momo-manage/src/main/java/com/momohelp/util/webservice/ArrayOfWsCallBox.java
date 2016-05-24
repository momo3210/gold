
package com.momohelp.util.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfWsCallBox", propOrder = {
    "wsCallBox"
})
public class ArrayOfWsCallBox {

    @XmlElement(name = "WsCallBox", nillable = true)
    protected List<WsCallBox> wsCallBox;

    public List<WsCallBox> getWsCallBox() {
        if (wsCallBox == null) {
            wsCallBox = new ArrayList<WsCallBox>();
        }
        return this.wsCallBox;
    }

}
