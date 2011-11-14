package se.clark.ht.web;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.graph.neo4j.support.GraphDatabaseContext;
import org.springframework.data.graph.neo4j.support.node.Neo4jHelper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerAdapter;
import se.clark.ht.builder.WordMother;
import se.clark.ht.domain.Word;
import se.clark.ht.repository.WordRepositoryExtension;
import se.clark.ht.service.WordService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

@ContextConfiguration({"classpath:configuration/word-mindmap-context.xml",
        "classpath:configuration/word-mindmap-servlet.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchControllerTest {

    @Autowired
    private ApplicationContext applicationContext;


    private SearchController controller;
    private HandlerAdapter handlerAdapter;
    private MockHttpServletResponse response;
    private MockHttpServletRequest request;
    private WordService service;
    private WordRepositoryExtension repository;

    @Before
    public void setUp(){
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        service = mock(WordService.class);
        repository = mock(WordRepositoryExtension.class);
        handlerAdapter = applicationContext.getBean(HandlerAdapter.class);
       // I could get the controller from the context here
        controller = new SearchController(service, repository);
    }
		
		
    @Ignore
    @Test
    public void shouldReturnJsonForSearchWordNearTo() throws Exception {
        Word earth = WordMother.getEarth();
        Word globe = WordMother.getGlobe();
        Word sky = WordMother.getSky();
        stub(service.searchExactWordByName("earth")).toReturn(earth);
        List<Word> result = new ArrayList();
        result.add(earth);
        result.add(globe);
        result.add(sky);
        stub(repository.findWordsToDepth(earth, 1)).toReturn(result);
        request.setRequestURI("/getWordsNearTo.html");
        request.setContentType("application/json");
        request.addParameter("name", "earth");

        handlerAdapter.handle(request, response, controller);
        assertEquals(response.getStatus(), 200);
        assertThat(response.getContentAsString().replace("\\\\","").replace("\"", "").replace("\\", ""),
             equalTo("{" +
                        "startWord:{name:earth,type:noun,meaning:the planet we live}," +
                        "relatedWords:[{name:globe,type:noun,meaning:the world (used especially to emphasize its size);a thing shaped like a ball}," +
                                 "{name:sky,type:noun,meaning:blue sky}]}"));
    }

}
