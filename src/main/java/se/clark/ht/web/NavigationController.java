package se.clark.ht.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.clark.ht.domain.Word;

import java.util.ArrayList;
import java.util.List;

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
