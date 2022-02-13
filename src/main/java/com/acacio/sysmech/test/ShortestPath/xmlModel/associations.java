package com.acacio.sysmech.test.ShortestPath.xmlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class associations {

    private List<association> association;

    public List<association> getAssociation(){
        return this.association;
    }
}
