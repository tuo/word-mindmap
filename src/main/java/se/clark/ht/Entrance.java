package se.clark.ht;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.io.File;

import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.WildcardQuery;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.index.RelationshipIndex;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.helpers.collection.MapUtil;


import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;
import se.clark.ht.domain.WordRelationshipTypes;
import se.clark.ht.util.FileHelper;

public class Entrance {

    //get log4j handler
    private static final Logger logger = Logger.getLogger(Entrance.class);

    private static final String DB_PATH = "data/neo4j-db";
    private static GraphDatabaseService graphDb;

    private static final String NAME_KEY = "name";
    private static final String TYPE_KEY = "type";
    private static final String CHINESE_MEANING_KEY = "chinese_meaning";
    private static final String ENGLISH_MEANING_KEY = "english_meaning";


    private static final String ON_CHI_REL_KEY = "on_chinese";
    private static final String ON_ENG_REL_KEY = "on_english";

    private static final String WORDS_INDEX_KEY = "words-fulltext";

    /**
     * @param args about index:
     *             http://docs.neo4j.org/chunked/snapshot/indexing-create.html
     *             http://wiki.neo4j.org/content/Indexing_with_IndexService#
     *             Updating_values_in_the_index
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        populateData();
    }

    public static void populateData() {
        // deleteWordNodeSpace();
        FileHelper.deleteDir(new File(DB_PATH));

        System.out.println("Creating word node space ...");
        graphDb = new EmbeddedGraphDatabase(DB_PATH);
        Map<String, Node> savedNodes = new Hashtable<String, Node>();
        Transaction tx = graphDb.beginTx();
        try {
            Node referenceNode = graphDb.getReferenceNode();

            IndexManager indexManger = graphDb.index();
            // create a fulltext index

            Index<Node> wordsIndex = indexManger
                    .forNodes(WORDS_INDEX_KEY, MapUtil.stringMap("provider",
                            "lucene", "type", "fulltext"));

            RelationshipIndex synonyms = indexManger
                    .forRelationships(WordRelationshipTypes.SYNONYM_WITH
                            .toString());
            RelationshipIndex extentions = indexManger
                    .forRelationships(WordRelationshipTypes.EXTENSION_WITH
                            .toString());
            RelationshipIndex idioms = indexManger
                    .forRelationships(WordRelationshipTypes.IDIOM_TO.toString());

            // RelationshipIndex synonyms =
            // indexManger.forRelationships(WordRelationshipTypes.SYNONYM_WITH.toString()
            // +"-fulltext",
            // MapUtil.stringMap( "provider", "lucene", "type", "fulltext" ) );
            // RelationshipIndex extentions =
            // indexManger.forRelationships(WordRelationshipTypes.EXTENSION_WITH.toString()
            // +"-fulltext",
            // MapUtil.stringMap( "provider", "lucene", "type", "fulltext" ) );
            // RelationshipIndex idioms =
            // indexManger.forRelationships(WordRelationshipTypes.IDIOM_TO.toString()+"-fulltext",
            // MapUtil.stringMap( "provider", "lucene", "type", "fulltext" ) );

            Node earth = graphDb.createNode();

            earth.setProperty(NAME_KEY, "earth");
            earth.setProperty(TYPE_KEY, "noun");
            earth.setProperty(CHINESE_MEANING_KEY, "土地,泥土;地球");
            earth.setProperty(ENGLISH_MEANING_KEY,
                    "the world; the planet that we live on;the substance that plants grow in");

            wordsIndex.add(earth, NAME_KEY, "earth");
            wordsIndex.add(earth, TYPE_KEY, "noun");
            wordsIndex.add(earth, CHINESE_MEANING_KEY, "土地,泥土;地球");
            wordsIndex
                    .add(earth, ENGLISH_MEANING_KEY,
                            "the world; the planet that we live on;the substance that plants grow in");

            savedNodes.put("earth", earth);
            // create a reference root as aggregation root
            referenceNode.createRelationshipTo(earth,
                    WordRelationshipTypes.ROOT);

            // globe and world

            Meaning meanings = new Meaning();
            meanings.putMeaning("globe", "地球,全球",
                    "the world (used especially to emphasize its size);a thing shaped like a ball");
            meanings.putMeaning("world", "地球,世界",
                    "the earth, with all its countries, peoples and natural features;");

            insertNodeAndCreateRelAsGroup(earth, "地球", "the planet we live",
                    meanings, savedNodes, wordsIndex, synonyms);

            // for ground,soild
            meanings = new Meaning();
            meanings.putMeaning(
                    "soil",
                    "土地,土壤",
                    "the top layer of the earth in which plants, trees, etc. grow;a country; an area of land");
            meanings.putMeaning(
                    "ground",
                    "大地,操场,背景,主题,立场",
                    "the solid surface of the earth; a large area of land or sea that is used for a particular purpose; background that a design is painted or printed on;a good or true reason for saying, doing or believing something");
            meanings.putMeaning("land", "大地",
                    "the surface of the earth that is not sea");
            meanings.putMeaning("mud", "湿地,泥潭",
                    "wet earth that is soft and sticky");
            meanings.putMeaning(
                    "dirt",
                    "污垢, 泥土, 灰尘;污秽的言行, 粪便, 卑鄙的人, 堕落, 矿渣",
                    "any substance that makes something dirty, for example dust, soil or mud;unpleasant or harmful information about somebody that could be used to damage their reputation, career, etc");

            insertNodeAndCreateRelAsGroup(earth, "土地", "area on earth",
                    meanings, savedNodes, wordsIndex, synonyms);

            // idiom word and in a word
            Node word = graphDb.createNode();
            word.setProperty(NAME_KEY, "word");
            word.setProperty(TYPE_KEY, "noun");
            word.setProperty(CHINESE_MEANING_KEY, "单词,承诺");
            word.setProperty(
                    ENGLISH_MEANING_KEY,
                    "a single unit of language which means something and can be spoken or written;a promise or guarantee that you will do something or that something will happen or is true");

            wordsIndex.add(word, NAME_KEY, "word");
            wordsIndex.add(word, TYPE_KEY, "noun");
            wordsIndex.add(word, CHINESE_MEANING_KEY, "单词,承诺");
            wordsIndex
                    .add(word,
                            ENGLISH_MEANING_KEY,
                            "a single unit of language which means something and can be spoken or written;a promise or guarantee that you will do something or that something will happen or is true");

            // word extend to world
            Relationship rel = savedNodes.get("world").createRelationshipTo(
                    word, WordRelationshipTypes.EXTENSION_WITH);
            rel.setProperty(ON_CHI_REL_KEY, "世界--单词");
            rel.setProperty(ON_ENG_REL_KEY, "world-word");
            synonyms.add(rel, ON_CHI_REL_KEY, "世界--单词");
            synonyms.add(rel, ON_ENG_REL_KEY, "world-word");

            savedNodes.put("word", word);

            meanings = new Meaning();
            meanings.putMeaning("in a word", "phrase", "总而言之",
                    "used for giving a very short, usually negative, answer or comment");
            insertNodeAndCreateRelAsGroup(word, WordRelationshipTypes.IDIOM_TO,
                    "总之", "in a word", meanings, savedNodes, wordsIndex, idioms);
            tx.success();
        } finally {
            tx.finish();
        }

        // search by the word (full text search)
//		while (true) {
//			String userInput = interactionWithConsole();
//			searchCorrespondingWords(userInput);
//		}
//		trasaverAllSynonyms();

    }

    static final TraversalDescription SYNONYMS_TRAVERSAL = Traversal.description()
            .relationships(WordRelationshipTypes.SYNONYM_WITH)
            .depthFirst()
            .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL);


    public static String trasaverAll() {
        //http://wiki.neo4j.org/content/Traversal_Framework
        Node earth = graphDb.getReferenceNode().getSingleRelationship(WordRelationshipTypes.ROOT, Direction.OUTGOING).getEndNode();

        logger.info("traverse synonyms from node 'earth':");
        String result = "";
        for (Path position : SYNONYMS_TRAVERSAL
                .relationships(WordRelationshipTypes.EXTENSION_WITH)
                .relationships(WordRelationshipTypes.IDIOM_TO)
                .filter(Traversal.returnAllButStartNode())
                .traverse(earth)) {
            result += "Path from start node to current position is: " + position + ", to node: " + position.endNode().getProperty(NAME_KEY);
            logger.info(result);
        }
        return result;
    }

    public static String searchCorrespondingWords(String wordToSearch) {
        IndexManager indexManager = graphDb.index();
        logger.info("search started.........." + indexManager.existsForNodes(WORDS_INDEX_KEY));
        String result = "";
        if (indexManager.existsForNodes(WORDS_INDEX_KEY)) {
            Index<Node> indexService = indexManager.forNodes(WORDS_INDEX_KEY);
//			IndexHits<Node> hits = indexService.get(NAME_KEY, wordToSearch);
//			In order to search for tokenized words, the query method has to be used. The get method will only match the full string value, not the tokens
            IndexHits<Node> hits = indexService.query(new WildcardQuery(new Term(NAME_KEY, "*" + wordToSearch + "*")));
//			IndexHits<Node> hits = indexService.get(NAME_KEY, "*" +wordToSearch +"*");
            // fulltext uses a white-space tokenizer in its analyzer.
            logger.info("Those are the words that match '" + wordToSearch + "': ");

            for (Node node : hits) {
                String matchOne = "name: '" + node.getProperty(NAME_KEY)
                        + "', type: " + node.getProperty(TYPE_KEY)
                        + ", chinese_meaning: " + node.getProperty(CHINESE_MEANING_KEY)
                        + ", english_meaning: " + node.getProperty(ENGLISH_MEANING_KEY);
                result += matchOne;
                logger.info(matchOne);
            }
        }
        logger.info("search ended.");
        return result;
    }

    private static String interactionWithConsole() {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the name of word to search (No way to quit ^_^): ");
        String input = null;
        try {
            input = stdin.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("your input is : " + input);
        return input;
    }


    private static void insertNodeAndCreateRelAsGroup(Node earth, WordRelationshipTypes relationshipType,
                                                      String on_chinese_mean, String on_english_mean, Meaning meanings, Map<String, Node> savedNodes, Index<Node> wordsIndex, RelationshipIndex relIndex) {
        Relationship rel;
        Iterator<Map.Entry<String, Pair>> it = meanings.getMeanings()
                .entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Pair> entry = it.next();

            String wordName = entry.getKey();
            String type = entry.getValue().type;
            String chiMean = entry.getValue().chiMean;
            String engMean = entry.getValue().engMean;

            Node someWord = graphDb.createNode();
            someWord.setProperty(NAME_KEY, wordName);
            someWord.setProperty(TYPE_KEY, type);
            someWord.setProperty(CHINESE_MEANING_KEY, chiMean);
            someWord.setProperty(ENGLISH_MEANING_KEY, engMean);

            wordsIndex.add(someWord, NAME_KEY, wordName);
            wordsIndex.add(someWord, TYPE_KEY, type);
            wordsIndex.add(someWord, CHINESE_MEANING_KEY, chiMean);
            wordsIndex.add(someWord, ENGLISH_MEANING_KEY, engMean);

            savedNodes.put(wordName, someWord);
            // :on_chinese => "地球", :on_english => "the planet we live"

            rel = earth.createRelationshipTo(someWord, relationshipType);

            rel.setProperty(ON_CHI_REL_KEY, on_chinese_mean);
            rel.setProperty(ON_ENG_REL_KEY, on_english_mean);
            relIndex.add(rel, ON_CHI_REL_KEY, on_chinese_mean);
            relIndex.add(rel, ON_ENG_REL_KEY, on_english_mean);
        }

    }


    private static void insertNodeAndCreateRelAsGroup(Node earth,
                                                      String on_chinese_mean, String on_english_mean, Meaning meanings, Map<String, Node> savedNodes, Index<Node> wordsIndex, RelationshipIndex relIndex) {
        insertNodeAndCreateRelAsGroup(earth, WordRelationshipTypes.SYNONYM_WITH, on_chinese_mean, on_english_mean, meanings, savedNodes, wordsIndex, relIndex);
    }


    private static void deleteWordNodeSpace() {

        System.out.println("Delete word node space ..........");
        Transaction tx = graphDb.beginTx();
        try {
            Relationship rootRel = graphDb.getReferenceNode()
                    .getSingleRelationship(WordRelationshipTypes.ROOT,
                            Direction.OUTGOING);
            if (rootRel == null) {
                System.out.println("Nothing to delete,brand new node space...");
                return;
            }
            Node wordReferenceNode = rootRel.getEndNode();

            for (Relationship rel : wordReferenceNode.getRelationships(Direction.OUTGOING)) {
                Node word = rel.getEndNode();
                if (word == null) {
                    continue;
                }
//				System.out.println("delete node name: "+ word.getProperty(NAME_KEY));
//				System.out.println("Start node name:" +rel.getStartNode().getProperty(NAME_KEY));
                word.delete();
                rel.delete();
            }
            wordReferenceNode.getSingleRelationship(WordRelationshipTypes.ROOT,
                    Direction.INCOMING).delete();
            wordReferenceNode.delete();
        } finally {
            tx.finish();
        }
    }

    private static class Meaning {
        Map<String, Pair> meanings = new Hashtable<String, Pair>();

        public void putMeaning(String wordName, String chiMean, String engMean) {
            putMeaning(wordName, "noun", chiMean, engMean);
        }

        public void putMeaning(String wordName, String type, String chiMean, String engMean) {
            meanings.put(wordName, new Pair(type, chiMean, engMean));

        }

        public Map<String, Pair> getMeanings() {
            return meanings;
        }


    }

    private static class Pair {
        String chiMean;
        String engMean;
        String type;

        Pair(String type, String chiMean, String engMean) {
            this.type = type;
            this.chiMean = chiMean;
            this.engMean = engMean;
        }
    }
}

