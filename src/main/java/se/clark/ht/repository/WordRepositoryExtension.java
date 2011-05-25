package se.clark.ht.repository;

import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.domain.Word;

public interface WordRepositoryExtension {

    public Word findWordNamed(String name);

}
