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
                word.synonymWith(anotherWord, onChinese, onEnglish);
                break;
            case ANTONYM_WITH:
                word.antonymWith(anotherWord, onChinese, onEnglish);
                break;
            case IDIOM_WITH:
                word.idiomWith(anotherWord, onChinese, onEnglish);
                break;
            case EXTENSION_WITH:
                word.extendWith(anotherWord, onChinese, onEnglish);
                break;
            default:
                break;
        }

    }


    @Override
    @Transactional
    public void populateSomeWords() {

        Word earth = new Word("earth", "noun", "土地,呢;地球", "the world; the planet that we live on;the substance that plants grow in");

        Word globe = new Word("globe", "noun", "地球,全球", "the world (used especially to emphasize its size);a thing shaped like a ball");
        Word world = new Word("world", "noun", "地球,世界", "the earth, with all its countries, peoples and natural features");
        Word soil = new Word("soil", "noun", "土地,土壤", "the top layer of the earth in which plants, trees, etc. grow;a country; an area of land");

        Word ground = new Word("ground", "noun", "大地,操场,背景,主题,立场", "the solid surface of the earth; a large area of land or sea that is used for a particular purpose; background that a design is painted or printed on;a good or true reason for saying, doing or believing something");

        Word land = new Word("land", "noun", "大地", "the surface of the earth that is not sea");
        Word mud = new Word("mud", "noun", "湿地,泥潭", "wet earth that is soft and sticky");
        Word dirt = new Word("dirt", "noun", "污垢, 泥土, 灰尘;污秽的言行, 粪便, 卑鄙的人, 堕落, 矿渣", "any substance that makes something dirty, for example dust, soil or mud;unpleasant or harmful information about somebody that could be used to damage their reputation, career, etc");
        Word dust = new Word("dust", "noun", "污垢, 灰尘", "fine dust floating in the air");

        Word word = new Word("word", "noun", "单词,承诺", "a single unit of language which means something and can be spoken or written;a promise or guarantee that you will do something or that something will happen or is true");
        Word in_a_word = new Word("in a word", "phrase", "总而言之", "used for giving a very short, usually negative, answer or comment informal");

        Word sky = new Word("sky", "noun", "天空", "plural skies the space above the earth that you can see when you look up, where clouds and the sun, moon and stars appear");
        Word ocean = new Word("ocean", "noun", "海洋", "the mass of salt water that covers most of the earth's surface");
        Word blue = new Word("blue", "adj", "蓝色的(颜色),沮丧的,支持共和党的", "having the colour of a clear sky or the sea/ocean on a clear day;depressed;(of an area in the US) having more people who vote for the Democratic candidate than the Republican one");
        Word yellow = new Word("yellow", "adj", "黄色的", "positive color");
        Word yellowCard = new Word("yellow card", "noun", "黄牌", "yellow card for foul in football");
        Word football = new Word("football", "noun", "足球", "a sport that play on the grass");

        Word happy = new Word("happy", "adj", "高兴的;满足的", "feeling or showing pleasure; pleased;satisfied that something is good or right; not anxious");
        Word depressed = new Word("depressed", "adj", "沮丧的", "very sad and without hope");

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

        earth.synonymWith(globe, earthSynonymChi, earthSynonymEng);
        earth.synonymWith(world, earthSynonymChi, earthSynonymEng);

        String earthSynonymAeraChi = "土地";
        String earthSynonymAeraEng = "area on earth";

        earth.synonymWith(soil, earthSynonymAeraChi, earthSynonymAeraEng);
        earth.synonymWith(land, earthSynonymAeraChi, earthSynonymAeraEng);
        earth.synonymWith(ground, earthSynonymAeraChi, earthSynonymAeraEng);
        earth.synonymWith(mud, earthSynonymAeraChi, earthSynonymAeraEng);
        earth.synonymWith(dirt, earthSynonymAeraChi, earthSynonymAeraEng);

        dirt.synonymWith(dust, "灰尘", "little floating in the air");

        earth.extendWith(sky, "土地 <--> 天空", "from earth to sky");
        earth.extendWith(ocean, "土地 <--> 海洋", "from earth to ocean");
        ocean.extendWith(sky, "天空 <--> 海洋", "from ocean to sky");

        ocean.synonymWith(blue, "海洋是蓝色的", " ocean is blue");

//        blue.extendWith(ocean, "海洋是蓝色的", " ocean is blue");
        blue.antonymWith(depressed, "忧郁的", "not very happy though");
        blue.synonymWith(happy, "高兴和忧郁", "happiness vs depressed");
        blue.extendWith(yellow, "颜色", "color");

        world.extendWith(word, "两个单词只差一个字母", "just have one more letter 'l'");
        word.idiomWith(in_a_word, "扩展,组成短语", "compose to a phrase");

        yellow.extendWith(yellowCard, "黄色的牌", "yellow card");
        yellowCard.extendWith(football, "足球中有黄牌", "somebody foul too hard get warning by yellow card in football");

        Word being = new Word("being", "noun", "存在;人,生物;本质,思想感情,身心", "existence; Living, being alive; The nature or essence of a person");

        Word existence = new Word("existence", "noun", "存在,生存", "The fact or state of living or having objective reality");
        Word presence = new Word("presence", "noun", "出席,存在,到场", "The state or fact of existing, occurring, or being present in a place or thing");

        Word nonbeing = new Word("nonbeing", "noun", "不存在的事, 无", "The state of not being; nonexistence");

        Word organism = new Word("organism", "noun", "有机体,生物体", "An individual animal, plant, or single-celled life form");
        Word life = new Word("life", "noun", "一生,生命,生活", "Living things and their activity");
        Word creature = new Word("creature", "noun", "生物,动物,人", "An animal or person");
        Word person = new Word("person", "noun", "人,本人,身体", "A human being regarded as an individual");

        Word soul = new Word("soul", "noun", "灵魂,心灵,精神", "The spiritual or immaterial part of a human being or animal, regarded as immortal");
        Word essence = new Word("essence", "noun", "精髓,本质,要素", "The intrinsic nature or indispensable quality of something, esp. something abstract, that determines its character");
        Word self = new Word("self", "noun", "自己, 本性", "a person's personality or character that makes them different from other people");
        Word spirit = new Word("spirit", "noun", "精神,心灵,幽灵", "The nonphysical part of a person that is the seat of emotions and character; the soul");

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
        being.synonymWith(existence, beingSynonymChi, beingSynonymEng);
        being.synonymWith(presence, beingSynonymChi, beingSynonymEng);
        being.antonymWith(nonbeing, beingSynonymChi, beingSynonymEng);

        String beingSynonymLifeChi = "人,生物";
        String beingSynonymLifeEng = "Living, being alive";
        being.synonymWith(organism, beingSynonymLifeChi, beingSynonymLifeEng);
        being.synonymWith(life, beingSynonymLifeChi, beingSynonymLifeEng);
        being.synonymWith(creature, beingSynonymLifeChi, beingSynonymLifeEng);
        being.synonymWith(person, beingSynonymLifeChi, beingSynonymLifeEng);

        String beingSynonymNatureChi = "本质,思想感情,身心";
        String beingSynonymNatureEng = "The nature or essence of a person";
        being.synonymWith(soul, beingSynonymNatureChi, beingSynonymNatureEng);
        being.synonymWith(essence, beingSynonymNatureChi, beingSynonymNatureEng);
        being.synonymWith(self, beingSynonymNatureChi, beingSynonymNatureEng);
        being.synonymWith(spirit, beingSynonymNatureChi, beingSynonymNatureEng);


        Word economy_noun = new Word("economy", "noun", "经济,节约", "Offering the best value for the money");
//        Word economy_adj = new Word("economy", "adj", "经济的,廉价的", "Designed to be economical to use");

        Word thrift = new Word("thrift", "noun", "节俭, 节约", "The quality of using money and other resources carefully and not wastefully");
        Word frugality = new Word("frugality", "noun", "节俭, 俭省", "The quality of being economical with money or food; thriftiness");
        Word saving = new Word("saving", "noun", "节约, 挽救", "With the exception of; except");
        Word budget = new Word("budget", "noun", "预算", "the money that is available to a person or an organization and a plan of how it will be spent over a period of time");
        Word cost_cutting = new Word("cost-cutting", "noun", "削减成本", "the reduction of the amount of money spent on something, especially because of financial difficulty");
        Word cutback = new Word("cutback", "noun", "消减, 剪修新芽", "An act or instance of reducing something, typically expenditures");
        Word reduction = new Word("reduction", "noun", "减少, 降低, 减价", "The action or fact of making a specified thing smaller or less in amount, degree, or size");
        Word luxury = new Word("luxury", "noun", "奢侈品, 奢侈, 豪华", "The state of great comfort and extravagant living");

        Word cheap = new Word("cheap", "adj", "便宜的, 廉价的", "(of an item for sale) Low in price; worth more than its cost");
        Word reduced = new Word("reduced", "adj", "减少的, 简化的", "Make smaller or less in amount, degree, or size");

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
        economy_noun.synonymWith(thrift, economySynonymNounChi, economySynonymNounEng);
        economy_noun.synonymWith(frugality, economySynonymNounChi, economySynonymNounEng);
        economy_noun.synonymWith(saving, economySynonymNounChi, economySynonymNounEng);
        economy_noun.synonymWith(budget, economySynonymNounChi, economySynonymNounEng);
        economy_noun.synonymWith(cost_cutting, economySynonymNounChi, economySynonymNounEng);
        economy_noun.synonymWith(cutback, economySynonymNounChi, economySynonymNounEng);
        economy_noun.synonymWith(reduction, economySynonymNounChi, economySynonymNounEng);
        economy_noun.antonymWith(luxury, economySynonymNounChi, economySynonymNounEng);

        String economySynonymAdjChi = "经济的,廉价的";
        String economySynonymAdjEng = "Designed to be economical to use";
//        economy_adj.synonymWith(cheap, economySynonymAdjChi, economySynonymAdjEng);
//        economy_adj.synonymWith(reduced, economySynonymAdjChi, economySynonymAdjEng);

        economy_noun.synonymWith(cheap, economySynonymAdjChi, economySynonymAdjEng);
        economy_noun.synonymWith(reduced, economySynonymAdjChi, economySynonymAdjEng);

        cheap.extendWith(happy, "遇到便宜的东西都很高兴", "cheap things are happy to buy");
        depressed.extendWith(soul, " 忧郁的心灵", " depressed soul, not happy");

        Word sallow = new Word("sallow", "adj", "灰黄的；蜡黄的", "having a slightly yellow colour that does not look healthy");
        Word creamy = new Word("creamy", "adj", "奶油色的；淡黄色的；米色的", "pale yellowish-white in colour");
        Word beige = new Word("beige", "adj", "浅褐色的；米黄色的", "light yellowish-brown in colour");
        Word buttery = new Word("buttery", "adj", "黄油般的；含黄油的；以黄油覆盖的", "like, containing or covered with butter");
        Word milky = new Word("milky", "adj", "奶制的；含奶多的；奶的", "made of milk; containing a lot of milk");
        Word sickly = new Word("sickly", "adj", "难看的；看着不舒服的", "of colours, unpleasant to look at");
        Word wan = new Word("wan", "adj", "苍白无力的；无血色的；憔悴的", "looking pale and weak");
        Word xanthous = new Word("xanthous", "adj", "yellow", "黄色的,浅黄色的");
        Word pale = new Word("pale", "adj", "灰白的；苍白的；白皙的", "having skin that is almost white; having skin that is whiter than usual because of illness, a strong emotion");
        Word washed_out = new Word("washed-out", "adj", "筋疲力尽的;面色苍白的", "Pale and tired");

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
        yellow.synonymWith(sallow, yellowSynonymChi, yellowSynonymEng);
        yellow.synonymWith(creamy, yellowSynonymChi, yellowSynonymEng);
        yellow.synonymWith(beige, yellowSynonymChi, yellowSynonymEng);
        yellow.synonymWith(buttery, yellowSynonymChi, yellowSynonymEng);
        yellow.synonymWith(milky, yellowSynonymChi, yellowSynonymEng);
        yellow.synonymWith(wan, yellowSynonymChi, yellowSynonymEng);
        yellow.synonymWith(sickly, yellowSynonymChi, yellowSynonymEng);
        yellow.synonymWith(xanthous, yellowSynonymChi, yellowSynonymEng);
        yellow.synonymWith(pale, yellowSynonymChi, yellowSynonymEng);
        yellow.synonymWith(washed_out, yellowSynonymChi, yellowSynonymEng);
    }


}
