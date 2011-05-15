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
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloWorldController {

    //get log4j handler
	private static final Logger logger = Logger.getLogger(HelloWorldController.class);

    @RequestMapping(value = "hello.html")
    public String sayHelloWithSpringMVC(ModelMap model) {
        model.addAttribute("name", "hello world");
        logger.info("logging something in log4j...........");
        return "hello";
    }
}