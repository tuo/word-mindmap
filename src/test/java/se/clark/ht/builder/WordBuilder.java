package se.clark.ht.builder;

import se.clark.ht.domain.Word;

/**
 * Test Data Builder
 *
 *    Writing Test Data Builders: Make It Easy on Yourself
 *    http://www.natpryce.com/articles/000769.html
 *
 *  */
public class WordBuilder {

    private String name;
    private String type;
    private String englishMeaning;
    private String chineseMeaning;

    public WordBuilder() {

    }

    public WordBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public WordBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public WordBuilder withEnglishMeaning(String englishMeaning) {
        this.englishMeaning = englishMeaning;
        return this;
    }

    public WordBuilder withChineseMeaning(String chineseMeaning) {
        this.chineseMeaning = chineseMeaning;
        return this;
    }

    public Word build(){
        return new Word(name, type, englishMeaning);
    }


}
