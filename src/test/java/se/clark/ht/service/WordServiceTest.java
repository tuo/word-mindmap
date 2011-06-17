package se.clark.ht.service;

import net.minidev.json.JSONValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.graph.core.EntityPath;
import org.springframework.data.graph.neo4j.support.GraphDatabaseContext;
import org.springframework.data.graph.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.builder.WordBuilder;
import se.clark.ht.builder.WordMother;
import se.clark.ht.domain.Relationship;
import se.clark.ht.domain.Word;
import se.clark.ht.exception.WordNotFoundException;
import sun.security.krb5.internal.EncAPRepPart;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static se.clark.ht.domain.WordRelationshipTypes.*;


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
    public void shouldSearchSynonymsInAnyDepthFor() throws WordNotFoundException {
        List<Word> result = wordService.searchSynonymsInAnyDepthFor("earth");

        assertThat(result.size(), is(2));
        assertThat(result, equalTo(Arrays.asList(globe, world)));
    }

    @Test
    public void shouldSearchSynonymsInAnyDepthForThatDontHoldSynonym() throws WordNotFoundException {
        List<Word> result = wordService.searchSynonymsInAnyDepthFor("on earth");
        assertThat(result.isEmpty(), is(true));
    }

    @Test(expected = WordNotFoundException.class)
    public void shouldRaiseNotFoundExceptionWhenNamePassedDoesntExistForAll() throws WordNotFoundException {
        wordService.searchSynonymsInAnyDepthFor("some word that doesn't exist");
    }

    @Test(expected = WordNotFoundException.class)
    public void shouldRaiseNotFoundExceptionWhenNamePassedDoesntExistForNearby() throws WordNotFoundException {
        wordService.searchNearBySynonymsFor("some word that doesn't exist");
    }

    static class NodeRelationship {
        String nodeFrom;
        String nodeTo;
        String relateOn;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NodeRelationship that = (NodeRelationship) o;

            if (nodeFrom != null ? !nodeFrom.equals(that.nodeFrom) : that.nodeFrom != null) return false;
            if (nodeTo != null ? !nodeTo.equals(that.nodeTo) : that.nodeTo != null) return false;
            if (relateOn != null ? !relateOn.equals(that.relateOn) : that.relateOn != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = nodeFrom != null ? nodeFrom.hashCode() : 0;
            result = 31 * result + (nodeTo != null ? nodeTo.hashCode() : 0);
            result = 31 * result + (relateOn != null ? relateOn.hashCode() : 0);
            return result;
        }

        public NodeRelationship() {
        }

    }
}


