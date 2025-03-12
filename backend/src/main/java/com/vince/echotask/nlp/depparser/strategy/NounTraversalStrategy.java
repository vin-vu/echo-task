package com.vince.echotask.nlp.depparser.strategy;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class NounTraversalStrategy implements TraversalStrategy {

    @Override
    public List<IndexedWord> traverse(SemanticGraph dependencyParse, IndexedWord currentNode, IndexedWord root) {

        List<IndexedWord> descriptionWords = new ArrayList<>();
        List<IndexedWord> childrenNodes = dependencyParse.getChildList(currentNode);

        for (IndexedWord childNode : childrenNodes) {
            String childNodeRelation = dependencyParse.getEdge(currentNode, childNode).getRelation().toString();
            String pos = childNode.tag();
            log.info("parent - child: {}, {}", currentNode, childNode);

            // add root noun
            if (Objects.equals(pos, "VB") && (Objects.equals(childNodeRelation, "csubj") ||
                    Objects.equals(childNodeRelation, "dep"))) {
                descriptionWords.add(root);
                log.info("adding root noun: {}", descriptionWords);
                descriptionWords.addAll(traverse(dependencyParse, childNode, root));

                // ignore JJ amod intent ex. see utterance 10
            } else if (Objects.equals(pos, "JJ") && Objects.equals(childNodeRelation, "amod")) {
                descriptionWords.addAll(traverse(dependencyParse, childNode, root));

            } else {
                descriptionWords.add(childNode);
                log.info("added word: {}", childNode);
                descriptionWords.addAll(traverse(dependencyParse, childNode, root));
            }
            log.info("description words children: {}", descriptionWords);
        }
        return descriptionWords;
    }
}
