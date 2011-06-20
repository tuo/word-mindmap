package se.clark.ht.repository;

import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.domain.Relationship;
import se.clark.ht.domain.Word;

public interface WordRepositoryExtension {

    @Transactional
    public void save(Word word);

    public Word findWordNamed(String name);

    public Iterable<Word> findSynonymsFor(Word word, int depth);

    public Iterable<Word> findSynonymsInAnyDepthFor(Word word);

    public Iterable<Word> getAllWords(Word fromWord);

    public Iterable<Word> getAllWords();

    public Iterable<Word> findWordsByRelationships(Word startWord, String... relationships);

    public Iterable<Word> findWordsByRelationshipsAtDepth(Word startWord, int depth, String... relationships);
}
