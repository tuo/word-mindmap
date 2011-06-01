package se.clark.ht.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.graph.neo4j.support.GraphDatabaseContext;
import org.springframework.data.graph.neo4j.support.node.Neo4jHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import se.clark.ht.Entrance;
import se.clark.ht.domain.Word;
import se.clark.ht.exception.WordNotFoundException;
import se.clark.ht.service.WordService;

import java.util.List;

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
        Neo4jHelper.cleanDb(graphDatabaseContext);
        wordService.populateSomeWords();
        logger.info("populating words to neo4j local storage ended..........");

        return new ModelAndView("redirect:startSearching.html");
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
        String result = Entrance.trasaverAll();
        model.addAttribute("result", result == "" ? "No Match" : result);
        return "searchAllWordsResult";
    }


    @ExceptionHandler(WordNotFoundException.class)
    public ModelAndView handleWordNotFoundException(WordNotFoundException ex) {
        ModelAndView model = new ModelAndView("wordNotFound");
        model.addObject("message", ex.getMessage());
        return model;
    }


}