package se.clark.ht.service;

import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.domain.Word;
import se.clark.ht.exception.WordNotFoundException;

import java.util.Collection;
import java.util.List;

public interface WordService {

    @Transactional
    public void populateSomeWords();

    Word searchExactWordByName(String wordName);

    List<Word> searchNearBySynonymsFor(String wordName) throws WordNotFoundException;

    List<Word> searchSynonymsInAnyDepthFor(String wordName) throws WordNotFoundException;

    List<Word> getAllWords();

    @Transactional
    void createWord(Word word);


}
