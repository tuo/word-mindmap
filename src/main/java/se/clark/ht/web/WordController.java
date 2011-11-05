package se.clark.ht.web;

import net.minidev.json.JSONValue;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.graph.core.EntityPath;
import org.springframework.data.graph.neo4j.support.GraphDatabaseContext;
import org.springframework.data.graph.neo4j.support.node.Neo4jHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import se.clark.ht.domain.Relationship;
import se.clark.ht.domain.Word;
import se.clark.ht.exception.WordNotFoundException;
import se.clark.ht.service.WordService;

import java.io.File;
import java.util.*;

import static se.clark.ht.domain.WordRelationshipTypes.SYNONYM_WITH;
import static se.clark.ht.domain.WordRelationshipTypes.ANTONYM_WITH;
import static se.clark.ht.domain.WordRelationshipTypes.EXTENSION_WITH;
import static se.clark.ht.domain.WordRelationshipTypes.IDIOM_WITH;

@Controller
public class WordController {

    //get log4j handler
    private static final Logger logger = Logger.getLogger(WordController.class);

    @Autowired
    private WordService wordService;

    @Autowired
    private GraphDatabaseContext graphDatabaseContext;


    //this is not very good way to use GET
    @RequestMapping(value = "populateData.html")
    public ModelAndView populateWords(ModelMap model) {
        logger.info("start populating words to neo4j local storage..........");

        createOrSkipNeo4jDataStore();

        return new ModelAndView("redirect:openSearchSynonymsInAnyDepth.html");
    }


    @RequestMapping(value = "openSearchSynonymsInAnyDepth.html")
    public String openSearchSynonymsInAnyDepth(){
        return "searchSynonymsInAnyDepth";
    }

    @RequestMapping(value = "startSearching.html")
    public String startSearching(ModelMap model) {
        return "searchEntrance";
    }

    @RequestMapping(value = "searchSpecificWord.html")
    public String searchSpecificWord(@RequestParam(value = "word", required = false) String word, ModelMap model) {
        model.addAttribute("wordToSearch", word);
        Word result = wordService.searchExactWordByName(word);
        model.addAttribute("result", result == null ? "No Match" : result);
        return "searchSpecificWordResult";
    }

    @RequestMapping(value = "searchNearbySynonyms.html")
    public String searchNearbySynonyms(@RequestParam(value = "name", required = false) String wordName, ModelMap model) throws WordNotFoundException {
        model.addAttribute("wordToSearch", wordName);
        List<Word> result = wordService.searchNearBySynonymsFor(wordName);
        model.addAttribute("result", result);
        return "searchNearbySynonymsResult";
    }

    @RequestMapping(value = "searchSynonymsInAnyDepth.html")
    public String searchSynonymsInAnyDepth(@RequestParam(value = "name", required = false) String wordName, ModelMap model) throws WordNotFoundException {
        model.addAttribute("wordToSearch", wordName);
        List<Word> result = wordService.searchSynonymsInAnyDepthFor(wordName);
        model.addAttribute("result", result);
        return "searchSynonymsInAnyDepthResult";
    }


    @RequestMapping(value = "searchAllWords.html")
    public String searchAllWords(ModelMap model) {
        List<Word> result = wordService.getAllWords();
        model.addAttribute("result", result);
        return "searchAllWordsResult";
    }






    @ExceptionHandler(WordNotFoundException.class)
    public ModelAndView handleWordNotFoundException(WordNotFoundException ex) {
        ModelAndView model = new ModelAndView("wordNotFound");
        model.addObject("message", ex.getMessage());
        return model;
    }

    @RequestMapping(value = "/words.html", method = RequestMethod.GET)
    public @ResponseBody String getWordsJson() {

        createOrSkipNeo4jDataStore();

        TraversalDescription traversal = Traversal.description()
                .relationships(SYNONYM_WITH)
                .relationships(EXTENSION_WITH)
                .relationships(IDIOM_WITH)
                .relationships(ANTONYM_WITH)
                .breadthFirst()
                .evaluator(Evaluators.excludeStartPosition());

        Word earth = wordService.searchExactWordByName("earth");
        Iterable<EntityPath<Word, Word>> paths = earth.findAllPathsByTraversal(traversal);
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
            if(key.getName().equals(earth.getName())){
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


    private void createOrSkipNeo4jDataStore() {

        //using the relationship file size to determine whether neo4j db is there
        //need a better way to handle it
        File neo4jRelDb = new File("data/neo4j-db/neostore.relationshipstore.db");
        long length = neo4jRelDb.length();

        if(length == 0){
            Neo4jHelper.cleanDb(graphDatabaseContext);
            wordService.populateSomeWords();
            logger.warn("no neo4j exists, populating words to neo4j local storage ended..........");
        }else{
            logger.warn("neo4j db already exist, skip it ...............");
        }
    }


    @RequestMapping(value = "/createWord.html")
    public ModelAndView createWord(@RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "chineseMeaning", required = false) String chineseMeaning,
            @RequestParam(value = "englishMeaning", required = false) String englishMeaning,
            ModelMap model) {

        wordService.createWord(new Word(name,type, englishMeaning));
        return new ModelAndView("redirect:openSearchSynonymsInAnyDepth.html");
    }

    @RequestMapping(value = "/createRelationship.html")
    public ModelAndView createRelationship(@RequestParam(value = "wordName", required = false) String wordName,
            @RequestParam(value = "anotherWordName", required = false) String anotherWordName,
            @RequestParam(value = "onChinese", required = false) String onChinese,
            @RequestParam(value = "onEnglish", required = false) String onEnglish,
            @RequestParam(value = "whichType", required = false) String whichType,
            ModelMap model) {

        Word word = wordService.searchExactWordByName(wordName);
        Word anotherWord = wordService.searchExactWordByName(anotherWordName);

        wordService.createRelationshipBetween(word, anotherWord, onChinese, onEnglish, whichType);
        return new ModelAndView("redirect:openSearchSynonymsInAnyDepth.html");
    }

}