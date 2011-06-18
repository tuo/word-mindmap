package se.clark.ht.domain;

import org.springframework.data.graph.annotation.NodeEntity;
import org.springframework.data.graph.annotation.RelatedTo;
import org.springframework.data.graph.core.Direction;
import org.springframework.data.graph.neo4j.annotation.Indexed;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Word {

    @Indexed
    private String name;

    //consider using enum for this
    @Indexed
    private String type;

    @Indexed
    private String chineseMeaning;

    @Indexed
    private String englishMeaning;

    @RelatedTo(elementClass = Word.class, type = "SYNONYM_WITH", direction = Direction.BOTH)
    private Set<Word> synonyms;

    @RelatedTo(elementClass = Word.class, type = "EXTENSION_WITH", direction = Direction.BOTH)
    private Set<Word> extensions;
    //
    @RelatedTo(elementClass = Word.class, type = "IDIOM_WITH", direction = Direction.BOTH)
    private Set<Word> idioms;

    @RelatedTo(elementClass = Word.class, type = "ANTONYM_WITH", direction = Direction.BOTH)
    private Set<Word> antonyms;


    public Word() {

    }


    public Word(String name, String type, String chineseMeaning, String englishMeaning) {
        this.name = name;
        this.type = type;
        this.englishMeaning = englishMeaning;
        this.chineseMeaning = chineseMeaning;
    }

    public String getName() {
        return name;

    }

    public String getType() {
        return type;
    }

    public String getEnglishMeaning() {
        return englishMeaning;
    }

    public String getChineseMeaning() {
        return chineseMeaning;
    }

    public Set<Word> getSynonyms() {
        return synonyms;
    }

    public int getSynonymsCount() {
        return synonyms.size();
    }

    public Set<Word> getExtensions() {
        return extensions;
    }

    public int getExtensionsCount() {
        return extensions.size();
    }

    public Set<Word> getIdioms() {
        return idioms;
    }

    public int getIdiomsCount() {
        return idioms.size();
    }

    public Set<Word> getAntonyms() {
        return antonyms;
    }

    public int getAntonymsCount() {
        return antonyms.size();
    }

    @Override
    public String toString() {

        return "Word{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", englishMeaning='" + englishMeaning + '\'' +
                ", chineseMeaning='" + chineseMeaning + '\'' +
                '}';
    }

    public void synonymWith(Word anotherWord, String onChinese, String onEnglish) {
        Relationship relationship = relateTo(anotherWord, Relationship.class, WordRelationshipTypes.SYNONYM_WITH.name());
        relationship.on(onChinese, onEnglish);
    }

    public void extendWith(Word anotherWord, String onChinese, String onEnglish) {
        Relationship relationship = relateTo(anotherWord, Relationship.class, WordRelationshipTypes.EXTENSION_WITH.name());
        relationship.on(onChinese, onEnglish);
    }

    public void idiomWith(Word anotherWord, String onChinese, String onEnglish) {
        Relationship relationship = relateTo(anotherWord, Relationship.class, WordRelationshipTypes.IDIOM_WITH.name());
        relationship.on(onChinese, onEnglish);
    }

    public void antonymWith(Word anotherWord, String onChinese, String onEnglish) {
        Relationship relationship = relateTo(anotherWord, Relationship.class, WordRelationshipTypes.ANTONYM_WITH.name());
        relationship.on(onChinese, onEnglish);
    }

    public Relationship getSynonymRelationshipTo(Word anotherWord) {
        return getRelationshipTo(anotherWord, Relationship.class, "SYNONYM_WITH");
    }

    public Relationship getAntonymRelationshipTo(Word anotherWord) {
        return getRelationshipTo(anotherWord, Relationship.class, "ANTONYM_WITH");
    }

    public Relationship getIdiomRelationshipTo(Word anotherWord) {
        return getRelationshipTo(anotherWord, Relationship.class, "IDIOM_WITH");
    }

    public Relationship getExtensionRelationshipTo(Word anotherWord) {
        return getRelationshipTo(anotherWord, Relationship.class, "EXTENSION_WITH");
    }

    public Set<Relationship> getRelationshipsTo(Word anotherWord) {
        Set<Relationship> relationships = new HashSet<Relationship>();
        if (getSynonymRelationshipTo(anotherWord) != null)
            relationships.add(getSynonymRelationshipTo(anotherWord));
        if (getAntonymRelationshipTo(anotherWord) != null)
            relationships.add(getAntonymRelationshipTo(anotherWord));
        if (getExtensionRelationshipTo(anotherWord) != null)
            relationships.add(getExtensionRelationshipTo(anotherWord));
        if (getIdiomRelationshipTo(anotherWord) != null)
            relationships.add(getIdiomRelationshipTo(anotherWord));
        return relationships;
    }

    public Set<Word> getAllRelatedWords(){
        Set<Word> words = new HashSet<Word>();
        words.addAll(getSynonyms());
        words.addAll(getAntonyms());
        words.addAll(getIdioms());
        words.addAll(getExtensions());
        return words;
    }

}
