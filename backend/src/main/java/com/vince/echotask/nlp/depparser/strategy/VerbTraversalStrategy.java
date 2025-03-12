package com.vince.echotask.nlp.depparser.strategy;


import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class VerbTraversalStrategy implements TraversalStrategy {

    @Override
    public List<IndexedWord> traverse(SemanticGraph dependencyParse, IndexedWord currentNode, IndexedWord root) {

        List<IndexedWord> descriptionWords = new ArrayList<>();

        for (IndexedWord childNode : dependencyParse.getChildList(currentNode)) {
            String childRelation = dependencyParse.getEdge(currentNode, childNode).getRelation().toString();
            log.info("parent - child: {} - {}", currentNode, childNode);

            // add root verb if intent exists as its nominal subject - see  ex. utterance 7
            if (Objects.equals(childRelation, "nsubj") && currentNode == root) {
                descriptionWords.add(root);
                log.info("adding root verb: {}", descriptionWords);
            } else {
                descriptionWords.add(childNode);
                log.info("added words: {}", descriptionWords);
                descriptionWords.addAll(traverse(dependencyParse, childNode, root));
            }
            log.info("description words children: {}", descriptionWords);
        }
        return descriptionWords;
    }
}
