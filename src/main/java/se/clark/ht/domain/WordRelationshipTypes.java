package se.clark.ht.domain;

import org.neo4j.graphdb.RelationshipType;


public enum WordRelationshipTypes implements RelationshipType {

    ROOT, SYNONYM_TO, EXTENTION_TO, ANTONYM_TO, IDIOM_TO;
}
