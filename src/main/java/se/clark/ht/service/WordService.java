package se.clark.ht.service;

import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.domain.Word;

import java.util.Collection;

public interface WordService {

    @Transactional
    public void populateSomeWords();

    Word searchExactWordByName(String wordName);

    Iterable<Word> searchSynonymsFor(String wordName);
}
