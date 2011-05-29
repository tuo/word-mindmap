package se.clark.ht.repository;


import org.hamcrest.CoreMatchers;
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
import se.clark.ht.service.WordService;

import javax.validation.constraints.Null;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
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
    private WordRepositoryExtension wordRepository;

    @Rollback(false)
    @BeforeTransaction
    public void crearDatabase() {
        Neo4jHelper.cleanDb(graphDatabaseContext);
    }

    @Test
    public void shouldFindWordByNameExactMatch(){
        Word earth = new WordBuilder()
                .withName("earth")
                .withType("noun")
                .withChineseMeaning("土地")
                .withEnglishMeaning("the planet we live")
                .build();
        Word globe = new WordBuilder()
                .withName("globe")
                .withType("noun")
                .withChineseMeaning("地球,全球")
                .withEnglishMeaning("the world (used especially to emphasize its size);a thing shaped like a ball")
                .build();

        wordRepository.save(earth);
        wordRepository.save(globe);

        assertThat(wordRepository.findWordNamed("earth"), is(earth));
        assertThat(wordRepository.findWordNamed("globe"), is(globe));
        assertThat(wordRepository.findWordNamed("glo"), nullValue());
    }

}
