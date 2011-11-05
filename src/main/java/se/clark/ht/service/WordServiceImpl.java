package se.clark.ht.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.domain.Word;
import se.clark.ht.domain.WordRelationshipTypes;
import se.clark.ht.exception.WordNotFoundException;
import se.clark.ht.repository.WordRepositoryExtension;

import java.util.ArrayList;
import java.util.List;

@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordRepositoryExtension wordRepository;

    @Override
    public Word searchExactWordByName(String wordName) {
        return wordRepository.findWordNamed(wordName);
    }

    @Override
    public List<Word> searchNearBySynonymsFor(String wordName) throws WordNotFoundException {
        Word earth = wordRepository.findWordNamed(wordName);
        if (earth == null) {
            throw new WordNotFoundException(wordName);
        }
        List<Word> result = new ArrayList<Word>();
        for (Word word : wordRepository.findSynonymsFor(earth, 1)) {
            result.add(word);
        }
        return result;
    }

    @Override
    public List<Word> searchSynonymsInAnyDepthFor(String wordName) throws WordNotFoundException {
        Word earth = wordRepository.findWordNamed(wordName);
        if (earth == null) {
            throw new WordNotFoundException(wordName);
        }
        List<Word> result = new ArrayList<Word>();
        for (Word word : wordRepository.findSynonymsInAnyDepthFor(earth)) {
            result.add(word);
        }
        return result;

    }

    @Override
    public List<Word> getAllWords() {
        Word earth = wordRepository.findWordNamed("earth");

        List<Word> result = new ArrayList<Word>();
        for (Word word : wordRepository.getAllWords(earth)) {
            result.add(word);
        }
        return result;
    }


    @Transactional
    public void createWord(Word word) {
        wordRepository.save(word);
    }

    @Override
    @Transactional
    public void createRelationshipBetween(Word word, Word anotherWord, String onChinese, String onEnglish, String whichType) {

        switch (WordRelationshipTypes.valueOf(whichType.toUpperCase())) {
            case SYNONYM_WITH:
                word.synonymWith(anotherWord, onEnglish);
                break;
            case ANTONYM_WITH:
                word.antonymWith(anotherWord, onEnglish);
                break;
            case IDIOM_WITH:
                word.idiomWith(anotherWord, onEnglish);
                break;
            case EXTENSION_WITH:
                word.extendWith(anotherWord, onEnglish);
                break;
            default:
                break;
        }

    }


    @Override
    @Transactional
    public void populateSomeWords() {

        Word earth = new Word("earth", "noun", "the world; the planet that we live on;the substance that plants grow in");

        Word globe = new Word("globe", "noun", "the world (used especially to emphasize its size);a thing shaped like a ball");
        Word world = new Word("world", "noun", "the earth, with all its countries, peoples and natural features");
        Word soil = new Word("soil", "noun", "the top layer of the earth in which plants, trees, etc. grow;a country; an area of land");

        Word ground = new Word("ground", "noun", "the solid surface of the earth; a large area of land or sea that is used for a particular purpose; background that a design is painted or printed on;a good or true reason for saying, doing or believing something");

        Word land = new Word("land", "noun", "the surface of the earth that is not sea");
        Word mud = new Word("mud", "noun", "wet earth that is soft and sticky");
        Word dirt = new Word("dirt", "noun", "any substance that makes something dirty, for example dust, soil or mud;unpleasant or harmful information about somebody that could be used to damage their reputation, career, etc");
        Word dust = new Word("dust", "noun", "fine dust floating in the air");

        Word word = new Word("word", "noun", "a single unit of language which means something and can be spoken or written;a promise or guarantee that you will do something or that something will happen or is true");
        Word in_a_word = new Word("in a word", "phrase", "used for giving a very short, usually negative, answer or comment informal");

        Word sky = new Word("sky", "noun", "plural skies the space above the earth that you can see when you look up, where clouds and the sun, moon and stars appear");
        Word ocean = new Word("ocean", "noun", "the mass of salt water that covers most of the earth's surface");
        Word blue = new Word("blue", "adj", "having the colour of a clear sky or the sea/ocean on a clear day;depressed;(of an area in the US) having more people who vote for the Democratic candidate than the Republican one");
        Word yellow = new Word("yellow", "adj", "positive color");
        Word yellowCard = new Word("yellow card", "noun", "yellow card for foul in football");
        Word football = new Word("football", "noun", "a sport that play on the grass");

        Word happy = new Word("happy", "adj", "feeling or showing pleasure; pleased;satisfied that something is good or right; not anxious");
        Word depressed = new Word("depressed", "adj", "very sad and without hope");

        wordRepository.save(earth);
        wordRepository.save(globe);
        wordRepository.save(world);
        wordRepository.save(soil);
        wordRepository.save(ground);
        wordRepository.save(land);
        wordRepository.save(mud);
        wordRepository.save(dirt);
        wordRepository.save(dust);
        wordRepository.save(word);
        wordRepository.save(in_a_word);
        wordRepository.save(sky);
        wordRepository.save(ocean);
        wordRepository.save(blue);
        wordRepository.save(yellow);
        wordRepository.save(yellowCard);
        wordRepository.save(football);
        wordRepository.save(happy);
        wordRepository.save(depressed);

        String earthSynonymChi = "地球";
        String earthSynonymEng = "the planet we live";

        earth.synonymWith(globe, earthSynonymEng);
        earth.synonymWith(world, earthSynonymEng);

        String earthSynonymAeraChi = "土地";
        String earthSynonymAeraEng = "area on earth";

        earth.synonymWith(soil, earthSynonymAeraEng);
        earth.synonymWith(land, earthSynonymAeraEng);
        earth.synonymWith(ground, earthSynonymAeraEng);
        earth.synonymWith(mud, earthSynonymAeraEng);
        earth.synonymWith(dirt, earthSynonymAeraEng);

        dirt.synonymWith(dust, "little floating in the air");

        earth.extendWith(sky, "from earth to sky");
        earth.extendWith(ocean, "from earth to ocean");
        ocean.extendWith(sky, "from ocean to sky");

        ocean.synonymWith(blue, " ocean is blue");

//        blue.extendWith(ocean, "海洋是蓝色的", " ocean is blue");
        blue.antonymWith(depressed, "not very happy though");
        blue.synonymWith(happy, "happiness vs depressed");
        blue.extendWith(yellow, "color");

        world.extendWith(word, "just have one more letter 'l'");
        word.idiomWith(in_a_word, "compose to a phrase");

        yellow.extendWith(yellowCard, "yellow card");
        yellowCard.extendWith(football, "somebody foul too hard get warning by yellow card in football");

        Word being = new Word("being", "noun", "existence; Living, being alive; The nature or essence of a person");

        Word existence = new Word("existence", "noun", "The fact or state of living or having objective reality");
        Word presence = new Word("presence", "noun", "The state or fact of existing, occurring, or being present in a place or thing");

        Word nonbeing = new Word("nonbeing", "noun", "The state of not being; nonexistence");

        Word organism = new Word("organism", "noun", "An individual animal, plant, or single-celled life form");
        Word life = new Word("life", "noun", "Living things and their activity");
        Word creature = new Word("creature", "noun", "An animal or person");
        Word person = new Word("person", "noun", "A human being regarded as an individual");

        Word soul = new Word("soul", "noun", "The spiritual or immaterial part of a human being or animal, regarded as immortal");
        Word essence = new Word("essence", "noun", "The intrinsic nature or indispensable quality of something, esp. something abstract, that determines its character");
        Word self = new Word("self", "noun", "a person's personality or character that makes them different from other people");
        Word spirit = new Word("spirit", "noun", "The nonphysical part of a person that is the seat of emotions and character; the soul");

        wordRepository.save(being);
        wordRepository.save(nonbeing);
        wordRepository.save(existence);
        wordRepository.save(presence);
        wordRepository.save(organism);
        wordRepository.save(life);
        wordRepository.save(creature);
        wordRepository.save(person);
        wordRepository.save(soul);
        wordRepository.save(essence);
        wordRepository.save(self);
        wordRepository.save(spirit);

        String beingSynonymChi = "存在";
        String beingSynonymEng = "existence";
        being.synonymWith(existence, beingSynonymEng);
        being.synonymWith(presence, beingSynonymEng);
        being.antonymWith(nonbeing, beingSynonymEng);

        String beingSynonymLifeChi = "人,生物";
        String beingSynonymLifeEng = "Living, being alive";
        being.synonymWith(organism, beingSynonymLifeEng);
        being.synonymWith(life, beingSynonymLifeEng);
        being.synonymWith(creature, beingSynonymLifeEng);
        being.synonymWith(person, beingSynonymLifeEng);

        String beingSynonymNatureChi = "本质,思想感情,身心";
        String beingSynonymNatureEng = "The nature or essence of a person";
        being.synonymWith(soul, beingSynonymNatureEng);
        being.synonymWith(essence, beingSynonymNatureEng);
        being.synonymWith(self, beingSynonymNatureEng);
        being.synonymWith(spirit, beingSynonymNatureEng);


        Word economy_noun = new Word("economy", "noun", "Offering the best value for the money");
//        Word economy_adj = new Word("economy", "adj", "经济的,廉价的", "Designed to be economical to use");

        Word thrift = new Word("thrift", "noun", "The quality of using money and other resources carefully and not wastefully");
        Word frugality = new Word("frugality", "noun", "The quality of being economical with money or food; thriftiness");
        Word saving = new Word("saving", "noun", "With the exception of; except");
        Word budget = new Word("budget", "noun", "the money that is available to a person or an organization and a plan of how it will be spent over a period of time");
        Word cost_cutting = new Word("cost-cutting", "noun", "the reduction of the amount of money spent on something, especially because of financial difficulty");
        Word cutback = new Word("cutback", "noun", "An act or instance of reducing something, typically expenditures");
        Word reduction = new Word("reduction", "noun", "The action or fact of making a specified thing smaller or less in amount, degree, or size");
        Word luxury = new Word("luxury", "noun", "The state of great comfort and extravagant living");

        Word cheap = new Word("cheap", "adj", "(of an item for sale) Low in price; worth more than its cost");
        Word reduced = new Word("reduced", "adj", "Make smaller or less in amount, degree, or size");

        wordRepository.save(economy_noun);
//        wordRepository.save(economy_adj);
        wordRepository.save(thrift);
        wordRepository.save(frugality);
        wordRepository.save(saving);
        wordRepository.save(budget);
        wordRepository.save(cost_cutting);
        wordRepository.save(cutback);
        wordRepository.save(reduction);
        wordRepository.save(luxury);
        wordRepository.save(cheap);
        wordRepository.save(reduced);

        String economySynonymNounChi = "经济,节约";
        String economySynonymNounEng = "Offering the best value for the money";
        economy_noun.synonymWith(thrift, economySynonymNounEng);
        economy_noun.synonymWith(frugality, economySynonymNounEng);
        economy_noun.synonymWith(saving, economySynonymNounEng);
        economy_noun.synonymWith(budget, economySynonymNounEng);
        economy_noun.synonymWith(cost_cutting, economySynonymNounEng);
        economy_noun.synonymWith(cutback, economySynonymNounEng);
        economy_noun.synonymWith(reduction, economySynonymNounEng);
        economy_noun.antonymWith(luxury, economySynonymNounEng);

        String economySynonymAdjChi = "经济的,廉价的";
        String economySynonymAdjEng = "Designed to be economical to use";
//        economy_adj.synonymWith(cheap, economySynonymAdjChi, economySynonymAdjEng);
//        economy_adj.synonymWith(reduced, economySynonymAdjChi, economySynonymAdjEng);

        economy_noun.synonymWith(cheap, economySynonymAdjEng);
        economy_noun.synonymWith(reduced, economySynonymAdjEng);

        cheap.extendWith(happy, "cheap things are happy to buy");
        depressed.extendWith(soul, " depressed soul, not happy");

        Word sallow = new Word("sallow", "adj", "having a slightly yellow colour that does not look healthy");
        Word creamy = new Word("creamy", "adj", "pale yellowish-white in colour");
        Word beige = new Word("beige", "adj", "light yellowish-brown in colour");
        Word buttery = new Word("buttery", "adj", "like, containing or covered with butter");
        Word milky = new Word("milky", "adj", "made of milk; containing a lot of milk");
        Word sickly = new Word("sickly", "adj", "of colours, unpleasant to look at");
        Word wan = new Word("wan", "adj", "looking pale and weak");
        Word xanthous = new Word("xanthous", "adj", "黄色的,浅黄色的");
        Word pale = new Word("pale", "adj", "having skin that is almost white; having skin that is whiter than usual because of illness, a strong emotion");
        Word washed_out = new Word("washed-out", "adj", "Pale and tired");

        wordRepository.save(sallow);
        wordRepository.save(creamy);
        wordRepository.save(beige);
        wordRepository.save(buttery);
        wordRepository.save(milky);
        wordRepository.save(wan);
        wordRepository.save(sickly);
        wordRepository.save(xanthous);
        wordRepository.save(pale);
        wordRepository.save(washed_out);

        String yellowSynonymChi = "黄色的";
        String yellowSynonymEng = "positive color";
        yellow.synonymWith(sallow, yellowSynonymEng);
        yellow.synonymWith(creamy, yellowSynonymEng);
        yellow.synonymWith(beige, yellowSynonymEng);
        yellow.synonymWith(buttery, yellowSynonymEng);
        yellow.synonymWith(milky, yellowSynonymEng);
        yellow.synonymWith(wan, yellowSynonymEng);
        yellow.synonymWith(sickly, yellowSynonymEng);
        yellow.synonymWith(xanthous, yellowSynonymEng);
        yellow.synonymWith(pale, yellowSynonymEng);
        yellow.synonymWith(washed_out, yellowSynonymEng);
    }


}
