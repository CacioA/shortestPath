package com.acacio.sysmech.test.ShortestPath.xmlModel;

import javax.xml.bind.annotation.*;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class topology {

    @XmlElement
    private entities entities;
    @XmlElement
    private associations associations;


    public entities getEntities(){
        return this.entities;
    }
    public associations getAssociations(){
        return this.associations;
    }



}
