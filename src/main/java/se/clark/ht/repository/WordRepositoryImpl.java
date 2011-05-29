package se.clark.ht.repository;


import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.clark.ht.domain.Word;

import static se.clark.ht.domain.WordRelationshipTypes.SYNONYM_WITH;


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

}
