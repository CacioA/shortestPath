package com.acacio.sysmech.test.ShortestPath.controller;

import com.acacio.sysmech.test.ShortestPath.ShortestPathApplication;
import com.acacio.sysmech.test.ShortestPath.model.*;

import com.acacio.sysmech.test.ShortestPath.xmlModel.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;


@RestController
public class mainController {
    private final Logger logger = LoggerFactory.getLogger(mainController.class);
    @GetMapping("/shortestDistance")
    public ShortestPathResult shortestPathBetweenTwoNodesWithPath(
                 @RequestParam String startNode,
                 @RequestParam String finishNode
    ) throws JAXBException, URISyntaxException {

        return shortestPathBetweenTwoNodes(startNode,finishNode,"/data.xml");
    }


    @GetMapping("/shortestDistanceWithPath")
    public ShortestPathResult shortestPathBetweenTwoNodes(@RequestParam String startNode,
                                                                 @RequestParam String finishNode, @RequestParam String fileName
                  ) throws JAXBException, URISyntaxException{

        JAXBContext jaxbContext = null;
        jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                .createContext(new Class[]{topology.class}, null);
        // retrieve file named data.xml from resources
        URL urlResource = ShortestPathApplication.class.getResource(fileName);

        File file = Paths.get(urlResource.toURI()).toFile();
        logger.info("Reading file from {}", file.getAbsoluteFile());
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        // xml file to topology model
        topology top = (topology) jaxbUnmarshaller.unmarshal(file);

        List<String> shortestPath = new ArrayList<>();

        // list of lists of paths
        // reasoning for using list of list:
        // when a path (list of paths) is created, add it to the list if:
        // 1) list is empty
        // 2) it is shorter than the rest. If true, remove the rest.
        List<List<String>> listOfPaths = calculateShortestPath(top,startNode,finishNode);

        if(listOfPaths.size()>0) {
            // listOfPaths could be 0 when no full path is found
            shortestPath = listOfPaths.get(0);
            return shortestPathWithElementClassification(shortestPath,top);

        }
        logger.info("No complete path between {} and {} has been found",startNode,finishNode);
        return new ShortestPathResult();

    }


    public List<List<String>> calculateShortestPath(topology _topology, String startNode, String finishNode){

        // retrieve associations
        associations _associations = _topology.getAssociations();
        Multimap<String,String> links = ArrayListMultimap.create();
        // map them to a multimap (to preserve order)
        for(association _association:_associations.getAssociation()){
            links.put(_association.getPrimary(),_association.getSecondary());
        }

        List<List<String>> result= new ArrayList<>();
        // if the startNode or finishNode do not exist in the topology model (file)
        // return - there will be no path
        if(links.containsKey(startNode) && links.values().contains(finishNode)) {

            // custom class to connect the nodes
            Tree rootTree = new Tree(startNode);

            // recursively add the links to the rootTree
            recursiveNodeAdd(links, startNode, rootTree);

             result = shortestPathBetweenTwoNodes(rootTree, startNode, finishNode, new ArrayList<>(), new ArrayList<>(), false);
        }
        return result;

    }

    public List<List<String>> shortestPathBetweenTwoNodes(Tree tree,String startNode, String finishNode, List<List<String>> shortestPathSteps,  List<String> currentTrail, boolean found)  {

        // checks children nodes of current node

        for(Tree firstTree: tree.getNodes()) {

                if (firstTree.getValue().equals(finishNode)) {

                    // add currentTrail to shortestPath list if the finishNode has been found
                    if(currentTrail.isEmpty()) currentTrail.add(tree.getValue());
                    currentTrail.add(firstTree.getValue());
                    logger.info("Finish Node has been found and added currentTrail");
                    if(!shortestPathSteps.isEmpty()) {
                        for (List<String> list : shortestPathSteps) {
                            if (list.size()>currentTrail.size()){
                                // if the currentTrail is shorter than the rest, remove them and only keep the shortest
                                shortestPathSteps.remove(list);
                                shortestPathSteps.add(currentTrail);
                                }
                        }
                    }
                    // add currentTrail if no other paths exist
                    if(shortestPathSteps.isEmpty()) shortestPathSteps.add(currentTrail);
                    currentTrail = new ArrayList<>();

                    // flag to help reset the currentTrail
                    // due to recursion, nodes would continue to be added
                    found= true;
                    break;
                }
        }

        // once a tree was checked and finishNode was not found, check inner trees recursively
        for(Tree innerTree: tree.getNodes()){
            if(found) {
                if (currentTrail.isEmpty()) currentTrail.add(startNode);
                else currentTrail.clear();
                found=false;
                break;
            }
            if(currentTrail.isEmpty()) {
                currentTrail.add(startNode);
            }

            if(!currentTrail.contains(tree.getValue()))
                currentTrail.add(tree.getValue());

            // due to break within the recursive function, a second check for the finishNode is required
            if(currentTrail.contains(finishNode)) break;

            // otherwise continue adding nodes to currentTrail
            currentTrail.add(innerTree.getValue());

            shortestPathBetweenTwoNodes(innerTree,startNode, finishNode,shortestPathSteps,currentTrail, found);
            if(found) {
                if (currentTrail.isEmpty()) currentTrail.add(startNode);
                else currentTrail.clear();
                found=false;

            }
        }

        return shortestPathSteps;
    }

    public void recursiveNodeAdd(Multimap<String,String> map, String startNode, Tree tree){

        // map node links to tree
        for(Map.Entry<String, String> entry:map.entries()){

            if(entry.getKey().equals(startNode)) {
                String value = entry.getValue();
                Tree treeToAdd = new Tree(value);
                tree.add(treeToAdd);
                recursiveNodeAdd(map, value, treeToAdd);
            }
        }

    }

    public ShortestPathResult shortestPathWithElementClassification(List<String> shortestPath, topology top){

        ShortestPathResult shortestPathResult = new ShortestPathResult();
        LinkedHashMap<String,String> nodesWithClassification = new LinkedHashMap<>();
        entities entities = top.getEntities();

        List<_class> classification_class = entities.get_class();

        // get classification for each node and add to map
        for(int i=0;i<shortestPath.size();i++) {
            for(_class c:classification_class){
                if (c.existsInEntityList(shortestPath.get(i))) {
                    nodesWithClassification.put(shortestPath.get(i), c.getKey());
                    logger.info("Node {} with classification {} has been added to complete path", shortestPath.get(i), c.getKey());
                }
            }
        }
        //  add map of nodes->classification and length to model;
        shortestPathResult.setNodesWithClassification(nodesWithClassification);
        shortestPathResult.setTotalLengthInNodes(shortestPath.size());
        return shortestPathResult;
    }
}
