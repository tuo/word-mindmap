package se.clark.ht.repository;

import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.domain.Word;

public interface WordRepositoryExtension {

    @Transactional
    public void save(Word word);

    public Word findWordNamed(String name);

}
