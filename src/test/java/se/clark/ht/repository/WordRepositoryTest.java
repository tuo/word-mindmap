package se.clark.ht.repository;


import org.junit.Before;
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
import se.clark.ht.builder.WordBuilder;
import se.clark.ht.domain.Word;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;


/**
 * Exploratory unit-tests for the Spring Data Graph annotated Word entity.
 * <p/>
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
    public void crearDatabase() {
        Neo4jHelper.cleanDb(graphDatabaseContext);
    }

    private Word earth;
    private Word globe;

    @Before
    public void setUp(){
       earth = new WordBuilder()
                .withName("earth")
                .withType("noun")
                .withChineseMeaning("土地")
                .withEnglishMeaning("the planet we live")
                .build();
       globe = new WordBuilder()
                .withName("globe")
                .withType("noun")
                .withChineseMeaning("地球,全球")
                .withEnglishMeaning("the world (used especially to emphasize its size);a thing shaped like a ball")
                .build();

    }

    @Test
    public void shouldRetrieveTheSameWordJustSaved() {
        Word retrievedWord = wordRepository.save(earth);
        assertEquals("retrieved word match persisted one", earth, retrievedWord);
        assertEquals("retrieved word name match ", "earth", retrievedWord.getName());
    }

    @Test
    public void shouldGetSynonymsFromSavingSide() {
        wordRepository.save(earth);
        assertThat("earth's synonyms count", earth.getSynonymsCount(), is(0));
        wordRepository.save(globe);
        //as earth and globe both are under controll of springdata graph, no need to further call earth.save()
        earth.synonymTo(globe, "地球", "the planet we live");
        assertThat("earth's synonyms count", earth.getSynonymsCount(), is(equalTo(1)));
        assertThat("earth's synonym contains globe", earth.getSynonyms(), hasItem(globe));
    }

    @Test
    public void shouldGetSynonymsFromOtherSide() {
        wordRepository.save(earth);
        wordRepository.save(globe);
        assertThat("globe's synonyms count", globe.getSynonymsCount(), is(0));
        //as earth and globe both are under controll of springdata graph, no need to further call earth.save()
        earth.synonymTo(globe, "地球", "the planet we live");
        assertThat("globe's synonyms count", globe.getSynonymsCount(), is(equalTo(1)));
        assertThat("globe's synonyms contain earth", globe.getSynonyms(), hasItem(earth));
    }


}
