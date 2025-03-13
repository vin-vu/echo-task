package com.vince.echotask.nlp.depparser.strategy.completedtask;

import com.vince.echotask.nlp.depparser.strategy.TraversalStrategy;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

import java.util.ArrayList;
import java.util.List;

public class CompletedTaskNounTraversalStrategy implements TraversalStrategy {
    @Override
    public List<IndexedWord> traverse(SemanticGraph dependencyParse, IndexedWord currentNode, IndexedWord root) {

        List<String> descriptionWords = new ArrayList<>();


        return List.of();
    }
}
