package se.clark.ht.web;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.clark.ht.Entrance;

@Controller
public class WordController {

    //get log4j handler
	private static final Logger logger = Logger.getLogger(WordController.class);

    //this is not very good way to use GET
    @RequestMapping(value = "populate_data.html",method = RequestMethod.GET)
    public String populateWords(ModelMap model) {
        logger.info("start populating words to neo4j local storage..........");
        Entrance.populateData();
        logger.info("populating words to neo4j local storage ended..........");
        model.addAttribute("name", "hello world");
        return "words";
    }
}