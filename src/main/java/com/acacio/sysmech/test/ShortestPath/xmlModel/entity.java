package com.acacio.sysmech.test.ShortestPath.xmlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class entity {

    @XmlAttribute
    private String key;

    public String getKey(){
        return this.key;
    }



}
