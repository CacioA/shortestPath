package com.acacio.sysmech.test.ShortestPath.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Tree {

    String value;
    List<Tree> nodes;

    public Tree(String value){
        this.value=value;
        nodes = new ArrayList<>();
    }

    public void add(Tree node){
        this.nodes.add(node);
    }


}
