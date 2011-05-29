package se.clark.ht.domain;

import org.neo4j.graphdb.RelationshipType;


public enum WordRelationshipTypes implements RelationshipType {

    ROOT, SYNONYM_WITH, EXTENSION_WITH, ANTONYM_TO, IDIOM_TO;
}
