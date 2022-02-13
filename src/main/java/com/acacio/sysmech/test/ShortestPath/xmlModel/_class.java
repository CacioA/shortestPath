package com.acacio.sysmech.test.ShortestPath.xmlModel;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name ="class")
@XmlAccessorType(XmlAccessType.FIELD)
public class _class {

    @XmlAttribute()
    private String key;

    private List<entity> entity;

    public List<entity> getEntity(){
        return this.entity;
    }

    public String getKey(){
        return this.key;
    }

    public boolean existsInEntityList(String path){
        boolean exists=false;
        for(entity e: entity){
            if(e.getKey().equals(path)) exists=true;
        }
        return exists;
    }


}
