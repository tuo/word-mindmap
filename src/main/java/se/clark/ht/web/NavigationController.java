package se.clark.ht.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NavigationController {

    @RequestMapping(value = "openSearchSpecificWord.html")
    public String openSearchSpecificWord(){
        return "searchSpecificWord";
    }

    @RequestMapping(value = "openSearchNearbySynonyms.html")
    public String openSearchNearbySynonyms(){
        return "searchNearbySynonyms";
    }

    @RequestMapping(value = "openSearchSynonymsInAnyDepth.html")
    public String openSearchSynonymsInAnyDepth(){
        return "searchSynonymsInAnyDepth";
    }

}
