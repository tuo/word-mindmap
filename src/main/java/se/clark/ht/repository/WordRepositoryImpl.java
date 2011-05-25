package se.clark.ht.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.domain.Word;

@Repository
public class WordRepositoryImpl implements WordRepositoryExtension {

    // optionally inject default repository, or use DirectGraphRepositoryFactory
    @Autowired
    private WordRepository wordRepository;

    @Override
    public Word findWordNamed(String name) {
       return wordRepository.findByPropertyValue("name", name);
    }


}
