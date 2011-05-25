package se.clark.ht.repository;

import org.springframework.data.graph.neo4j.repository.GraphRepository;
import org.springframework.data.graph.neo4j.repository.NamedIndexRepository;
import se.clark.ht.domain.Word;

public interface WordRepository extends GraphRepository<Word>, NamedIndexRepository<Word>{


}
