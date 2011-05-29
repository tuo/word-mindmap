package se.clark.ht.builder;

import se.clark.ht.domain.Word;

public class WordMother {
    public static Word getEarth() {
        return new WordBuilder().withName("earth")
                .withType("noun")
                .withChineseMeaning("土地")
                .withEnglishMeaning("the planet we live")
                .build();
    }

    public static Word getGlobe() {
        return new WordBuilder()
                .withName("globe")
                .withType("noun")
                .withChineseMeaning("地球,全球")
                .withEnglishMeaning("the world (used especially to emphasize its size);a thing shaped like a ball")
                .build();
    }

    public static Word getWorld() {
        return new WordBuilder()
                .withName("world")
                .withType("noun")
                .withChineseMeaning("地球,全球")
                .withEnglishMeaning("the earth, with all its countries, peoples and natural features")
                .build();
    }

    public static Word getSky() {
        return new WordBuilder()
                .withName("sky")
                .withType("noun")
                .withChineseMeaning("天空")
                .withEnglishMeaning("blue sky")
                .build();
    }

    public static Word getOnEarth() {
        return new WordBuilder()
                .withName("on earth")
                .withType("phrase")
                .withChineseMeaning("究竟")
                .withEnglishMeaning("what the hell")
                .build();
    }
}
