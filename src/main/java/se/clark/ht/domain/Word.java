package se.clark.ht.domain;

import org.springframework.data.graph.annotation.NodeEntity;
import org.springframework.data.graph.annotation.RelatedTo;
import org.springframework.data.graph.core.Direction;
import org.springframework.data.graph.core.GraphBacked;
import org.springframework.data.graph.neo4j.annotation.Indexed;

import java.util.Set;

@NodeEntity
public class Word {

    @Indexed
    private String name;

    //consider using enum for this
    @Indexed
    private String type;

    @Indexed
    private String englishMeaning;

    @Indexed
    private String chineseMeaning;

    @RelatedTo(elementClass = Word.class, type = "SYNONYM_TO", direction = Direction.BOTH)
    private Set<Word> synonyms;
//
//    private Set<Word> extentions;
//
//    private Set<Word> idioms;


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

    @Override
    public String toString() {

        return "Word{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", englishMeaning='" + englishMeaning + '\'' +
                ", chineseMeaning='" + chineseMeaning + '\'' +
                '}';
    }
}
