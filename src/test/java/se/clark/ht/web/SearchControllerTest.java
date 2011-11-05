package se.clark.ht.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.graph.neo4j.support.GraphDatabaseContext;
import org.springframework.data.graph.neo4j.support.node.Neo4jHelper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import se.clark.ht.builder.WordMother;
import se.clark.ht.domain.Word;
import se.clark.ht.service.WordService;

@ContextConfiguration({"classpath:configuration/word-mindmap-context.xml",
        "classpath:configuration/word-mindmap-servlet.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SearchControllerTest {

    @Autowired
    private SearchController controller;

    private AnnotationMethodHandlerAdapter adapter;
    private MockHttpServletResponse response;
    private MockHttpServletRequest request;


    @Autowired
    private GraphDatabaseContext graphDatabaseContext;

    @Autowired
    private WordService wordService;


    @Rollback(false)
    @BeforeTransaction
    public void crearDatabase() {
        Neo4jHelper.cleanDb(graphDatabaseContext);
    }

    private Word earth;
    private Word globe;
    private Word world;
    private Word sky;
    private Word onEarth;


    @Before
    public void setUp(){
        adapter = new AnnotationMethodHandlerAdapter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        earth = WordMother.getEarth();
        globe = WordMother.getGlobe();
        world = WordMother.getWorld();
        sky = WordMother.getSky();

        wordService.createWord(earth);
        wordService.createWord(world);
        wordService.createWord(globe);
        wordService.createWord(sky);
        wordService.createWord(onEarth);

        earth.synonymWith(globe, "地球", "the planet we live");
        globe.synonymWith(world, "地球", "the planet we live");
        earth.extendWith(sky, "土地和天空", "earth and sky just intuitive");

    }

    @Test
    public void shouldReturnJsonForSearchWordNearTo() throws Exception {
        request.setRequestURI("/getWordsNearTo.html");
        request.addParameter("name", "earth");

        ModelAndView view = adapter.handle(request, response, controller);
        Assert.assertNotNull(view);
    }
}
