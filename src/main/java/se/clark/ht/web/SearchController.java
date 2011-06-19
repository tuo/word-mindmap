package se.clark.ht.web;

import net.minidev.json.JSONValue;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.graph.core.EntityPath;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import se.clark.ht.domain.Relationship;
import se.clark.ht.domain.Word;
import se.clark.ht.domain.WordRelationshipTypes;
import se.clark.ht.exception.WordNotFoundException;
import se.clark.ht.repository.WordRepositoryExtension;
import se.clark.ht.service.WordService;

import java.util.*;

@Controller
public class SearchController {

    //get log4j handler
    private static final Logger logger = Logger.getLogger(SearchController.class);

    @Autowired
    private WordService wordService;

    @Autowired
    private WordRepositoryExtension wordRepository;


    @ExceptionHandler(WordNotFoundException.class)
    public ModelAndView handleWordNotFoundException(WordNotFoundException ex) {
        ModelAndView model = new ModelAndView("wordNotFound");
        model.addObject("message", ex.getMessage());
        return model;
    }

//    static String[] colors = {"#99FFFF", "#99FF00", "#990000", "#9933CC",
//            "#999900", "#99CC00", "#FFCCCC", "#FFFF00",
//            "#0000CC", "#"};
    String[] colors = {
        "Yellow","Teal","Green ","#8B5742",
        "Lime","Navy","Gray",
        "Red","Silver","Maroon","White",
        "#FFCC33","Purple", "#9F79EE"};

    private String getColorBy(Relationship relationship, Map<String, String> relToColorMap) {
//        logger.error("------------: size: " + relToColorMap.size());
        String onEnglish = relationship.getOnEnglish();
        if(!relToColorMap.containsKey(onEnglish)){
              relToColorMap.put(onEnglish, colors[relToColorMap.size()]);
        }

        return  relToColorMap.get(onEnglish);
    }



    @RequestMapping(value = "/searchWords.html")
    public @ResponseBody String searchWords(@RequestParam(value = "name", required = false) String wordName, @RequestParam(value = "relationships", required = false) String splitRelationships) {

        String[] relationshipsLiteral = splitRelationships.split(",");

        Word startWord = wordService.searchExactWordByName(wordName);

        List<Word> wantedWords = new ArrayList<Word>();

        for(Word word : wordRepository.findWordsByRelationships(startWord, relationshipsLiteral)){
            wantedWords.add(word);
        }


        List<Word> allWords = new ArrayList<Word>();
        for(Word word : wordRepository.getAllWords()){
            allWords.add(word);
        }

        List root = new LinkedList();
        Map<String,String> relToColorMap = new HashMap<String,String>();
        for (Word word : allWords){
            Map adjacencies = new LinkedHashMap();
            List nodes = new LinkedList();

            Set<Word> allRelatedWords = word.getAllRelatedWords();
            for (Word relatedWord : allRelatedWords){


                LinkedHashMap innerNode = new LinkedHashMap();
                innerNode.put("nodeTo", relatedWord.getName());
                innerNode.put("nodeFrom", word.getName());
                LinkedHashMap innerData = new LinkedHashMap();
//                logger.error("for relationshiP: " + relationship.getOnEnglish() + " color: " + getColorBy(relationship, relToColorMap));
                //only start node and end node of relationship both are wanted, then add highlight color
                if(wantedWords.contains(relatedWord) && wantedWords.contains(word)){
                    Set<Relationship> relationshipsTo = word.getRelationshipsTo(relatedWord);
                    innerData.put("$color", getColorBy(((Relationship)(relationshipsTo.toArray()[0])), relToColorMap));
                    innerData.put("$lineWidth", "3");
                }else{
                    //for other nodes that are not wanted
                    innerData.put("$color", "grey");
                }
//                innerData.put("relatedOn", relationship.getOnEnglish());
                innerNode.put("data", innerData);
                nodes.add(innerNode);

            }
            adjacencies.put("adjacencies", nodes);
            adjacencies.put("id", word.getName());
            adjacencies.put("name", word.getName());
            LinkedHashMap nodeData = new LinkedHashMap();
            nodeData.put("$color", "#83548B");
            if(word.getName().equals(startWord.getName())){
                //if it is start word
                nodeData.put("$type", "star");
                nodeData.put("$color", "red");
                nodeData.put("$dim", "25");
            }else if(wantedWords.contains(word)){
                // for those words are wanted
                nodeData.put("$type", "circle");
                nodeData.put("$color", "#D2691E");
            }else{
                // for those words are not in wanted
                nodeData.put("$type", "triangle");
                nodeData.put("$color", "Gray");

            }
            adjacencies.put("data", nodeData);
            root.add(adjacencies);

        }

        LinkedHashMap result = new LinkedHashMap();
        result.put("data", root);
        List colorRelJson = new LinkedList();

        for (Map.Entry<String, String> entry : relToColorMap.entrySet()) {
               LinkedHashMap color = new LinkedHashMap();
               color.put("color", entry.getValue());
               color.put("meaning", entry.getKey());
               colorRelJson.add(color);
        }


        result.put("colorToRelMap", colorRelJson);

        return JSONValue.toJSONString(result);

    }


}