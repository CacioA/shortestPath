package com.acacio.sysmech.test.ShortestPath.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
public class ShortestPathResult {

    private LinkedHashMap<String,String> nodesWithClassification;
    private int totalLengthInNodes;


    public ShortestPathResult(){
        this.nodesWithClassification = new LinkedHashMap<>();
        this.totalLengthInNodes = 0;
    }
}
