package com.acacio.sysmech.test.ShortestPath.xmlModel;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class entities {

    @XmlElement(name = "class")
    private List<_class> _class;

    public List<_class> get_class(){
        return this._class;
    }

}
