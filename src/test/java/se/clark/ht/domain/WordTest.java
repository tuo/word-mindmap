package se.clark.ht.domain;


import org.hamcrest.CoreMatchers;
import org.junit.Before;
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
import se.clark.ht.builder.WordBuilder;
import se.clark.ht.repository.WordRepository;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
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
public class WordTest {

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
    private Word sky;
    private Word onEarth;
    private Word happy;
    private Word sad;

    @Before
    public void setUp() {
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
        sky = new WordBuilder()
                .withName("sky")
                .withType("noun")
                .withChineseMeaning("天空")
                .withEnglishMeaning("blue sky")
                .build();
        onEarth = new WordBuilder()
                .withName("on earth")
                .withType("phrase")
                .withChineseMeaning("究竟")
                .withEnglishMeaning("what the hell")
                .build();

        happy = new WordBuilder()
                .withName("happy")
                .withType("noun/adj")
                .withChineseMeaning("高兴")
                .withEnglishMeaning("active emotion,feel good")
                .build();

        sad = new WordBuilder()
                .withName("sad")
                .withType("adj")
                .withChineseMeaning("悲伤的")
                .withEnglishMeaning("not feel good")
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
        earth.synonymWith(globe, "地球", "the planet we live");
        assertThat("earth's synonyms count", earth.getSynonymsCount(), is(equalTo(1)));
        assertThat("earth's synonym contains globe", earth.getSynonyms(), hasItem(globe));
        Relationship relationship = earth.getSynonymRelationshipTo(globe);
        assertThat("relationship should work properly" , relationship.getWord(), is(earth));
        assertThat("relationship should work properly" , relationship.getAnotherWord(), is(globe));
        assertThat("relationship should work properly" , relationship.getOnChinese(), is("地球"));
        assertThat("relationship should work properly" , relationship.getOnEnglish(), is("the planet we live"));

    }




    @Test
    public void shouldGetSynonymsFromOtherSide() {
        wordRepository.save(earth);
        wordRepository.save(globe);
        assertThat("globe's synonyms count", globe.getSynonymsCount(), is(0));
        //as earth and globe both are under controll of springdata graph, no need to further call earth.save()
        earth.synonymWith(globe, "地球", "the planet we live");
        assertThat("globe's synonyms count", globe.getSynonymsCount(), is(equalTo(1)));
        assertThat("globe's synonyms contain earth", globe.getSynonyms(), hasItem(earth));

    }

    @Test
    public void shouldGetExtensionFromSavingSide() {
        wordRepository.save(earth);
        assertThat("earth's extensions count", earth.getExtensionsCount(), is(0));
        wordRepository.save(sky);
        //as earth and sky both are under controll of springdata graph, no need to further call earth.save()
        earth.extendWith(sky, "土地和天空", "earth and sky just intuitive");
        assertThat("earth's extensions count", earth.getExtensionsCount(), is(equalTo(1)));
        assertThat("earth's extensions contains sky", earth.getExtensions(), hasItem(sky));
        Relationship relationship = earth.getExtensionRelationshipTo(sky);
        assertThat("relationship should work properly" , relationship.getWord(), is(earth));
        assertThat("relationship should work properly" , relationship.getAnotherWord(), is(sky));
        assertThat("relationship should work properly" , relationship.getOnChinese(), is("土地和天空"));
        assertThat("relationship should work properly" , relationship.getOnEnglish(), is("earth and sky just intuitive"));
    }

    @Test
    public void shouldGetExtensionFromOtherSide() {
        wordRepository.save(earth);
        wordRepository.save(sky);
        assertThat("sky's extensions count", sky.getExtensionsCount(), is(0));

        //as earth and sky both are under controll of springdata graph, no need to further call earth.save()
        earth.extendWith(sky, "土地和天空", "earth and sky just intuitive");
        assertThat("sky's extensions count", sky.getExtensionsCount(), is(equalTo(1)));
        assertThat("sky's extensions contains sky", sky.getExtensions(), hasItem(earth));
    }

    @Test
    public void shouldGetIdiomFromSavingSide() {
        wordRepository.save(earth);
        assertThat("earth's idioms count", earth.getIdiomsCount(), is(0));
        wordRepository.save(onEarth);
        //as earth and sky both are under controll of springdata graph, no need to further call earth.save()
        earth.idiomWith(onEarth, "加前缀on", "added a prefix 'on'");
        assertThat("earth's idioms count", earth.getIdiomsCount(), is(equalTo(1)));
        assertThat("earth's idioms contains onEarth", earth.getIdioms(), hasItem(onEarth));
        Relationship relationship = earth.getIdiomRelationshipTo(onEarth);
        assertThat("relationship should work properly" , relationship.getWord(), is(earth));
        assertThat("relationship should work properly" , relationship.getAnotherWord(), is(onEarth));
        assertThat("relationship should work properly" , relationship.getOnChinese(), is("加前缀on"));
        assertThat("relationship should work properly" , relationship.getOnEnglish(), is("added a prefix 'on'"));

    }

    @Test
    public void shouldGetIdiomFromOtherSide() {
        wordRepository.save(earth);
        wordRepository.save(onEarth);
        assertThat("onEarth's idiom count", onEarth.getIdiomsCount(), is(0));

        //as earth and sky both are under controll of springdata graph, no need to further call earth.save()
        earth.idiomWith(onEarth, "加前缀on", "added a prefix 'on'");
        assertThat("onEarth's idiom count", onEarth.getIdiomsCount(), is(equalTo(1)));
        assertThat("onEarth's idiom contains earth", onEarth.getIdioms(), hasItem(earth));
    }

    @Test
    public void shouldGetAntonymFromSavingSide() {
        wordRepository.save(happy);
        assertThat("happy's antonym count", happy.getAntonymsCount(), is(0));
        wordRepository.save(sad);
        //as earth and sky both are under controll of springdata graph, no need to further call earth.save()
        happy.antonymWith(sad, "高兴与悲伤", "feel good vs feel bad");
        assertThat("happy's antonym count", happy.getAntonymsCount(), is(equalTo(1)));
        assertThat("happy's antonym contains sad", happy.getAntonyms(), hasItem(sad));
        Relationship relationship = happy.getAntonymRelationshipTo(sad);
        assertThat("relationship should work properly" , relationship.getWord(), is(happy));
        assertThat("relationship should work properly" , relationship.getAnotherWord(), is(sad));
        assertThat("relationship should work properly" , relationship.getOnChinese(), is("高兴与悲伤"));
        assertThat("relationship should work properly" , relationship.getOnEnglish(), is("feel good vs feel bad"));

        Set<Relationship> relationships = happy.getRelationshipsTo(sad);
        assertThat(relationships.size(), is(1));
        assertThat(relationships, hasItem(relationship));
    }

    @Test
    public void shouldGetAntonymFromOtherSide() {
        wordRepository.save(happy);
        wordRepository.save(sad);
        wordRepository.save(earth);
        assertThat("sad's antonym count", sad.getAntonymsCount(), is(0));
        //as earth and sky both are under controll of springdata graph, no need to further call earth.save()
        happy.antonymWith(sad, "高兴与悲伤", "feel good vs feel bad'");
        assertThat("sad's antonym count", sad.getAntonymsCount(), is(equalTo(1)));
        assertThat("sad's antonym contains happy", sad.getAntonyms(), hasItem(happy));
        Relationship relationship = happy.getSynonymRelationshipTo(sad);
        assertThat("relationship should work properly" , relationship, is(CoreMatchers.<Object>nullValue()));

        Set<Relationship> relationships = happy.getRelationshipsTo(earth);
        assertThat(relationships.size(), is(0));

    }


}
