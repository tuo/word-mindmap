package se.clark.ht.service;

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
import se.clark.ht.builder.WordMother;
import se.clark.ht.domain.Word;
import se.clark.ht.exception.WordNotFoundException;

import java.util.Arrays;
import java.util.List;

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
public class WordServiceTest {

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
    public void setUp() {
        earth = WordMother.getEarth();
        globe = WordMother.getGlobe();
        world = WordMother.getWorld();
        sky = WordMother.getSky();
        onEarth = WordMother.getOnEarth();

        wordService.createWord(earth);
        wordService.createWord(world);
        wordService.createWord(globe);
        wordService.createWord(sky);
        wordService.createWord(onEarth);

        earth.synonymWith(globe, "地球", "the planet we live");
        globe.synonymWith(world, "地球", "the planet we live");
        earth.extendWith(sky, "土地和天空", "earth and sky just intuitive");
        earth.idiomWith(onEarth, "加前缀on", "added a prefix 'on'");

    }


    @Test
    public void shouldSearchNearBySynonymsFor() throws WordNotFoundException {
        List<Word> result = wordService.searchNearBySynonymsFor("earth");

        assertThat(result.size(), is(1));
        assertThat(result, hasItem(globe));
    }

    @Test
    public void shouldSearchSynonymsForAtDepthTwo() throws WordNotFoundException {
        List<Word> result = wordService.searchSynonymsFor("earth", 2);

        assertThat(result.size(), is(2));
        assertThat(result, equalTo(Arrays.asList(globe, world)));
    }

    @Test
    public void shouldSearchNearBySynonymsThatDontHoldSynonym() throws WordNotFoundException {
        List<Word> result = wordService.searchSynonymsFor("on earth", 2);
        assertThat(result.isEmpty(), is(true));
    }

    @Test(expected = WordNotFoundException.class)
    public void shouldRaiseNotFoundExceptionWhenNamePassedDoesntExist() throws WordNotFoundException {
        wordService.searchSynonymsFor("some word that doesn't exist", 2);
    }
}
