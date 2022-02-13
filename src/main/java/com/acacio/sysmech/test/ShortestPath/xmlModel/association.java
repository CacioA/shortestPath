package com.acacio.sysmech.test.ShortestPath.xmlModel;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class association {

    @XmlAttribute
    private String primary;

    @XmlAttribute
    private String secondary;

    public String getPrimary(){
        return this.primary;
    }
    public String getSecondary(){
        return this.secondary;
    }
}
