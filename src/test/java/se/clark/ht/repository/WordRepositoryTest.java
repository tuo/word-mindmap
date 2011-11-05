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
import se.clark.ht.builder.WordMother;
import se.clark.ht.domain.Word;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItem;

@ContextConfiguration(locations = "classpath:configuration/word-mindmap-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class WordRepositoryTest {

    @Autowired
    private GraphDatabaseContext graphDatabaseContext;

    @Autowired
    private WordRepositoryExtension repository;

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

        repository.save(earth);
        repository.save(world);
        repository.save(globe);
        repository.save(sky);
        repository.save(onEarth);

        earth.synonymWith(globe, "地球", "the planet we live");
        globe.synonymWith(world, "地球", "the planet we live");
        earth.extendWith(sky, "土地和天空", "earth and sky just intuitive");

    }

    @Test
    public void shouldFindWordsByRelationships(){
        String[] relationshipsLiteral = new String[]{"synonym_with","antonym_with","extension_with","idiom_with"};

        List<Word> result = new ArrayList<Word>();
        for (Word word : repository.findWordsByRelationships(earth,relationshipsLiteral)) {
            result.add(word);
        }
        assertNotNull(result);
        assertThat(result, hasItem(earth));
        assertThat(result, hasItem(globe));
        assertThat(result, hasItem(world));
        assertThat(result, hasItem(sky));
        assertFalse(result.contains(onEarth));
    }

    @Test
    public void findWordsByRelationshipsToDepth(){
        String[] relationshipsLiteral = new String[]{"synonym_with","antonym_with","extension_with","idiom_with"};

        List<Word> result = new ArrayList<Word>();
        for (Word word : repository.findWordsByRelationshipsToDepth(earth, 1, relationshipsLiteral)) {
            System.out.println("word : " + word.getName());
            result.add(word);
        }
        assertNotNull(result);
        assertThat(result, hasItem(earth));
        assertThat(result, hasItem(globe));
        assertFalse("should not contains world, cause it is at depth 2", result.contains(world));
        assertThat(result, hasItem(sky));

    }

    @Test
    public void shouldFindWordByNameExactMatch(){
        assertThat(repository.findWordNamed("earth"), is(earth));
        assertThat(repository.findWordNamed("globe"), is(globe));
        assertThat(repository.findWordNamed("glo"), nullValue());
    }

}
