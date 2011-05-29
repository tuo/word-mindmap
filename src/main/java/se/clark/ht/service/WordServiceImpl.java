package se.clark.ht.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.domain.Word;
import se.clark.ht.repository.WordRepository;

@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordRepository wordRepository;

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

        Word word = new Word("word", "noun", "单词,承诺", "a single unit of language which means something and can be spoken or written;a promise or guarantee that you will do something or that something will happen or is true");
        Word in_a_word = new Word("in a word", "phrase", "总而言之", "used for giving a very short, usually negative, answer or comment informal");

        Word sky = new Word("sky", "noun", "天空", "plural skies the space above the earth that you can see when you look up, where clouds and the sun, moon and stars appear");
        Word ocean = new Word("ocean", "noun", "海洋", "the mass of salt water that covers most of the earth's surface");
        Word blue = new Word("blue", "adj", "蓝色的(颜色),沮丧的,支持共和党的", "having the colour of a clear sky or the sea/ocean on a clear day;depressed;(of an area in the US) having more people who vote for the Democratic candidate than the Republican one");

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
        wordRepository.save(word);
        wordRepository.save(in_a_word);
        wordRepository.save(sky);
        wordRepository.save(ocean);
        wordRepository.save(blue);
        wordRepository.save(happy);
        wordRepository.save(depressed);

        String earthSynonymChi = "地球";
        String earthSynonymEng = "the planet we live";

        earth.synonymTo(globe, earthSynonymChi, earthSynonymEng);
        earth.synonymTo(world, earthSynonymChi, earthSynonymEng);

        String earthSynonymAeraChi = "土地";
        String earthSynonymAeraEng = "area on earth";

        earth.synonymTo(soil, earthSynonymAeraChi, earthSynonymAeraEng);
        earth.synonymTo(ground, earthSynonymAeraChi, earthSynonymAeraEng);
        earth.synonymTo(mud, earthSynonymAeraChi, earthSynonymAeraEng);
        earth.synonymTo(dirt, earthSynonymAeraChi, earthSynonymAeraEng);

        earth.extendTo(sky, "土地 <--> 天空", "from earth to sky");
        earth.extendTo(ocean, "土地 <--> 海洋", "from earth to ocean");
        ocean.extendTo(sky, "天空 <--> 海洋", "from ocean to sky");

        ocean.synonymTo(blue, "海洋是蓝色的", " ocean is blue");

        blue.antonymTo(depressed, "忧郁的", "not very happy though");
        blue.synonymTo(happy, "高兴和忧郁", "happiness vs depressed");

        world.extendTo(word, "两个单词只差一个字母", "just have one more letter 'l'");
        word.idiomTo(in_a_word, "扩展,组成短语", "compose to a phrase");

    }
}
