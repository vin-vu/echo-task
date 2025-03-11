package com.vince.echotask.nlp.depparser.strategy;


import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public class VerbTraversalStrategy implements TraversalStrategy {

    @Override
    public void travserse(SemanticGraph dependencyParse, IndexedWord currentNode, List<IndexedWord> taskDescriptionWords) {
        List<IndexedWord> childrenNodes = dependencyParse.getChildList(currentNode);
        for (IndexedWord childNode : childrenNodes) {
            SemanticGraphEdge edge = dependencyParse.getEdge(currentNode, childNode);
            String relation = edge.getRelation().toString();
            log.info("edge - relation: {}, {}", edge, relation);

            IndexedWord root = dependencyParse.getFirstRoot();

            // add root verb if intent exists as its nominal subject - see  ex. utterance 7
            if (Objects.equals(relation, "nsubj") && currentNode == root) {
                taskDescriptionWords.add(currentNode);
            } else {
                taskDescriptionWords.add(childNode);
                travserse(dependencyParse, childNode, taskDescriptionWords);
            }
        }
    }
}
