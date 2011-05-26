package se.clark.ht.repository;


import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.graph.neo4j.support.GraphDatabaseContext;
import org.springframework.data.graph.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.domain.Word;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.hasItem;


/**
 * Exploratory unit-tests for the Spring Data Graph annotated Word entity.
 *
 * Since the Word is a @NodeEntity, the SpringDataGraph must
 * be setup before you can even create instances of the POJO.
 */
@ContextConfiguration(locations = "classpath:configuration/word-mindmap-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class WordRepositoryTest {

    @Autowired
    private GraphDatabaseContext graphDatabaseContext;

    @Autowired
    private WordRepository wordRepository;


    @Rollback(false)
    @BeforeTransaction
    public void crearDatabase(){
        Neo4jHelper.cleanDb(graphDatabaseContext);
    }

    @Test
    public void shouldSaveWord(){
        Word earth = new Word("earth", "noun", "土地", "the planet we live");
        Word retrievedWord = wordRepository.save(earth);
        assertEquals("retrieved word match persisted one", earth, retrievedWord);
        assertEquals("retrieved word name match ", "earth", retrievedWord.getName());
    }

    @Test
    public void shouldSaveWordWithSynonyms(){
        Word earth = new Word("earth", "noun", "土地", "the planet we live");
        Word globe = new Word("globe", "noun", "地球,全球", "the world (used especially to emphasize its size);a thing shaped like a ball");
        wordRepository.save(earth);
        assertThat("earth's synonyms count", earth.getSynonymsCount(), is(0));
        wordRepository.save(globe);
        //as earth and globe both are under controll of springdata graph, no need to further call earth.save()
        earth.synonymTo(globe, "地球", "the planet we live");
        assertThat("earth's synonyms count", earth.getSynonymsCount(), is(equalTo(1)));
        assertThat("earth's synonym contains globe", earth.getSynonyms(), hasItem(globe));
    }

}
