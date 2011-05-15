package se.clark.ht.web;

/**
 * Created by IntelliJ IDEA.
 * User: Tuo
 * Date: 5/15/11
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */


import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WordController {

    //get log4j handler
	private static final Logger logger = Logger.getLogger(WordController.class);

    @RequestMapping(value = "populate_data.html")
    public String populateWords(ModelMap model) {
        model.addAttribute("name", "hello world");
        logger.info("logging something in log4j...........");
        return "words";
    }
}