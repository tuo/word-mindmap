package se.clark.ht.domain;

import org.springframework.data.graph.annotation.EndNode;
import org.springframework.data.graph.annotation.RelationshipEntity;
import org.springframework.data.graph.annotation.StartNode;

@RelationshipEntity
public class Relationship {

    @StartNode
    private Word word;

    @EndNode
    private Word anotherWord;

    private String onChinese;

    private String onEnglish;

    public Word getWord() {
        return word;
    }

    public Word getAnotherWord() {
        return anotherWord;
    }

    public String getOnChinese() {
        return onChinese;

    }

    public String getOnEnglish() {
        return onEnglish;
    }

    public Relationship on(String onChinese, String onEnglish){
        this.onChinese = onChinese;
        this.onEnglish = onEnglish;
        return this;
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "word=" + word +
                ", anotherWord=" + anotherWord +
                ", onChinese='" + onChinese + '\'' +
                ", onEnglish='" + onEnglish + '\'' +
                '}';
    }


}
