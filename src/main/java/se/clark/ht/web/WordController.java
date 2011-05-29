package se.clark.ht.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import se.clark.ht.Entrance;
import se.clark.ht.domain.Word;
import se.clark.ht.service.WordService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
public class WordController {

    //get log4j handler
    private static final Logger logger = Logger.getLogger(WordController.class);

    @Autowired
    private WordService wordService;


    //this is not very good way to use GET
    @RequestMapping(value = "populateData.html")
    public ModelAndView populateWords(ModelMap model) {
        logger.info("start populating words to neo4j local storage..........");
        wordService.populateSomeWords();
        logger.info("populating words to neo4j local storage ended..........");

        return new ModelAndView("redirect:populateSuccess.html");
    }

    @RequestMapping(value = "populateSuccess.html")
    public String populateSuccess(ModelMap model) {
        return "searchEntrance";
    }

    @RequestMapping(value = "searchSimilarWords.html")
    public String searchSimilarWords(@RequestParam(value = "word", required = false) String word, ModelMap model) {
        model.addAttribute("wordToSearch", word);
        String result = Entrance.searchCorrespondingWords(word);
        model.addAttribute("result", result == "" ? "No Match" : result);
        return "searchSimilarResult";
    }


    @RequestMapping(value = "searchSpecificWord.html")
    public String searchSpecificWord(@RequestParam(value = "word", required = false) String word, ModelMap model) {
        model.addAttribute("wordToSearch", word);
        Word result = wordService.searchExactWordByName(word);
        model.addAttribute("result", result == null ? "No Match" : result);
        return "searchSpecificWordResult";
    }

    @RequestMapping(value = "searchSynonyms.html")
    public String searchSynonyms(@RequestParam(value = "name", required = false) String wordName, ModelMap model) {
        model.addAttribute("wordToSearch", wordName);
        Iterable<Word> returned = wordService.searchSynonymsFor(wordName);

        List<Word> result = new ArrayList<Word>();
        for(Word word : returned){
            result.add(word);
        }

        model.addAttribute("result", result.isEmpty() ? "No Match" : result);
        return "searchSynonymsResult";
    }


    @RequestMapping(value = "searchAllWords.html")
    public String searchAllWords(ModelMap model){
        String result = Entrance.trasaverAll();
        model.addAttribute("result", result == "" ? "No Match" : result);
        return "searchAllWordsResult";
    }


}