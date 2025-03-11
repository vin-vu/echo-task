package com.vince.echotask.nlp.depparser.strategy;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

import java.util.List;

public interface TraversalStrategy {
    void traverse(SemanticGraph dependencyParse, IndexedWord currentNode, List<IndexedWord> taskDescriptionWords);
}
