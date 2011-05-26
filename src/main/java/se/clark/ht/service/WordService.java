package se.clark.ht.service;

import org.springframework.transaction.annotation.Transactional;

public interface WordService {

    @Transactional
    public void populateSomeWords();
}
