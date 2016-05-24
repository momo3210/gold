
package com.momohelp.util.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfWsStatusBox", propOrder = {
    "wsStatusBox"
})
public class ArrayOfWsStatusBox {

    @XmlElement(name = "WsStatusBox", nillable = true)
    protected List<WsStatusBox> wsStatusBox;
    public List<WsStatusBox> getWsStatusBox() {
        if (wsStatusBox == null) {
            wsStatusBox = new ArrayList<WsStatusBox>();
        }
        return this.wsStatusBox;
    }

}
