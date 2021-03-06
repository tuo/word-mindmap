package se.clark.ht.repository;


import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.domain.Word;
import se.clark.ht.domain.WordRelationshipTypes;

import static se.clark.ht.domain.WordRelationshipTypes.*;


@Repository
public class WordRepositoryImpl implements WordRepositoryExtension {

    // optionally inject default repository, or use DirectGraphRepositoryFactory
    @Autowired
    private WordRepository wordRepository;

    @Transactional
    public void save(Word word) {
        wordRepository.save(word);
    }

    @Override
    public Word findWordNamed(String name) {
       return wordRepository.findByPropertyValue("name", name);
    }

    @Override
    public Iterable<Word> findSynonymsFor(Word word, int depth) {
        //you can using Evaluators.includeDepth to limit depth of search in future
        TraversalDescription traversal = Traversal.description().relationships(SYNONYM_WITH).evaluator(Evaluators.includingDepths(1,depth));
        return wordRepository.findAllByTraversal(word, traversal);
    }

    @Override
    public Iterable<Word> findSynonymsInAnyDepthFor(Word word) {
        TraversalDescription traversal = Traversal.description().relationships(SYNONYM_WITH).evaluator(Evaluators.excludeStartPosition());
        return wordRepository.findAllByTraversal(word, traversal);
    }

    @Override
    public Iterable<Word> getAllWords(Word fromWord) {
       TraversalDescription traversal = Traversal.description()
               .relationships(SYNONYM_WITH)
               .relationships(EXTENSION_WITH)
               .relationships(IDIOM_WITH)
               .relationships(ANTONYM_WITH)
               .evaluator(Evaluators.excludeStartPosition());

       return wordRepository.findAllByTraversal(fromWord, traversal);
    }

    @Override
    public Iterable<Word> getAllWords() {
        return wordRepository.findAll();
    }

    @Override
    public Iterable<Word> findWordsByRelationships(Word startWord, String... relationships) {
        TraversalDescription WORDS_TRAVERSAL = Traversal.description();
        for(String relationship : relationships){
            WordRelationshipTypes type = WordRelationshipTypes.valueOf(relationship.toUpperCase().trim());
            WORDS_TRAVERSAL = WORDS_TRAVERSAL.relationships(type, Direction.BOTH);
        }
        WORDS_TRAVERSAL.breadthFirst()
                    .evaluator(Evaluators.all());
        return wordRepository.findAllByTraversal(startWord, WORDS_TRAVERSAL);
    }

    @Override
    public Iterable<Word> findWordsByRelationshipsToDepth(Word startWord, int depth, String... relationships) {
        TraversalDescription WORDS_TRAVERSAL = Traversal.description();
        for(String relationship : relationships){
            WordRelationshipTypes type = WordRelationshipTypes.valueOf(relationship.toUpperCase().trim());
            WORDS_TRAVERSAL = WORDS_TRAVERSAL.relationships(type, Direction.BOTH);
        }
        WORDS_TRAVERSAL = WORDS_TRAVERSAL.evaluator(Evaluators.toDepth(depth)).breadthFirst();
        return wordRepository.findAllByTraversal(startWord, WORDS_TRAVERSAL);
    }

    @Override
    public Iterable<Word> findWordsToDepth(Word startWord, int depth) {
        String[] relationshipsLiteral = new String[]{"synonym_with","antonym_with","extension_with","idiom_with"};
        return findWordsByRelationshipsToDepth(startWord, depth,relationshipsLiteral);
    }

}
