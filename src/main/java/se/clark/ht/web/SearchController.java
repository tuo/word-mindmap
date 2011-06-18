package se.clark.ht.web;

import net.minidev.json.JSONValue;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.graph.core.EntityPath;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import se.clark.ht.domain.Relationship;
import se.clark.ht.domain.Word;
import se.clark.ht.domain.WordRelationshipTypes;
import se.clark.ht.exception.WordNotFoundException;
import se.clark.ht.service.WordService;

import java.util.*;

@Controller
public class SearchController {

    //get log4j handler
    private static final Logger logger = Logger.getLogger(SearchController.class);

    @Autowired
    private WordService wordService;
    @ExceptionHandler(WordNotFoundException.class)
    public ModelAndView handleWordNotFoundException(WordNotFoundException ex) {
        ModelAndView model = new ModelAndView("wordNotFound");
        model.addObject("message", ex.getMessage());
        return model;
    }

//    static final  TraversalDescription WORDS_TRAVERSAL = Traversal.description()
//                    .breadthFirst();


    @RequestMapping(value = "/searchWords.html")
    public @ResponseBody String searchWords(@RequestParam(value = "name", required = false) String wordName, @RequestParam(value = "relationships", required = false) String splitRelationships) {
        logger.error("---------------"  + wordName + ", ,,,,,,,,,"  + splitRelationships);
        String[] relationshipsLiteral = splitRelationships.split(",");



        TraversalDescription WORDS_TRAVERSAL = Traversal.description();
        for(String rel : relationshipsLiteral){
            WordRelationshipTypes type = WordRelationshipTypes.valueOf(rel.toUpperCase());
            logger.error("==========" + type);
            WORDS_TRAVERSAL = WORDS_TRAVERSAL.relationships(type);
        }


        WORDS_TRAVERSAL.breadthFirst()
                    .evaluator(Evaluators.excludeStartPosition());

        Word startWord = wordService.searchExactWordByName(wordName);
        Iterable<EntityPath<Word, Word>> paths = startWord.findAllPathsByTraversal(WORDS_TRAVERSAL);
        Map<Word, Set<Relationship>> words = new HashMap<Word, Set<Relationship>>();
        for (EntityPath<Word, Word> path : paths) {


            Iterator<Relationship> relaIter = path.relationshipEntities(Relationship.class).iterator();

            while (relaIter.hasNext()) {
                Relationship relationship = relaIter.next();
                if (words.containsKey(relationship.getWord())) {
                    words.get(relationship.getWord()).add(relationship);
                } else {
                    Set<Relationship> relationships = new HashSet<Relationship>();
                    relationships.add(relationship);
                    words.put(relationship.getWord(), relationships);
                }

            }
        }

        List root = new LinkedList();

        for (Word key : words.keySet()) {
//            System.out.println("Key: " + key + ", Value: " + words.get(key));

            Map adjacencies = new LinkedHashMap();
            List nodes = new LinkedList();
            Set<Relationship> relationships = words.get(key);
            for (Relationship relationship : relationships) {
                LinkedHashMap innerNode = new LinkedHashMap();
                innerNode.put("nodeTo", relationship.getAnotherWord().getName());
                innerNode.put("nodeFrom", relationship.getWord().getName());
                LinkedHashMap innerData = new LinkedHashMap();
                innerData.put("$color", "#dd99dd");
                innerData.put("relatedOn", relationship.getOnEnglish());
                innerNode.put("data", innerData);
                nodes.add(innerNode);
            }
            adjacencies.put("adjacencies", nodes);
            adjacencies.put("id", key.getName());
            adjacencies.put("name", key.getName());
            LinkedHashMap nodeData = new LinkedHashMap();
            nodeData.put("$color", "#83548B");
            if(key.getName().equals(startWord.getName())){
                nodeData.put("$type", "star");
                nodeData.put("$color", "red");
            }else{
                nodeData.put("$type", "circle");
                nodeData.put("$color", "#83548B");
            }
            adjacencies.put("data", nodeData);
            root.add(adjacencies);
        }
        return JSONValue.toJSONString(root);

    }




}