package com.vince.echotask.nlp.depparser.strategy;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class NounTraversalStrategy implements TraversalStrategy {

    @Override
    public List<IndexedWord> traverse(SemanticGraph dependencyParse, IndexedWord currentNode, IndexedWord root) {

        List<IndexedWord> descriptionWords = new ArrayList<>();
        descriptionWords.add(currentNode);

        List<IndexedWord> descriptionWordsChildren = new ArrayList<>();
        List<IndexedWord> childrenNodes = dependencyParse.getChildList(currentNode);
        log.info("parent - children: {}, {}", currentNode, childrenNodes);

        for (IndexedWord childNode : childrenNodes) {
            SemanticGraphEdge edge = dependencyParse.getEdge(currentNode, childNode);
            String relation = edge.getRelation().toString();

            String pos = childNode.tag();

            // skip intent verb
            if (Objects.equals(pos, "VB") && Objects.equals(relation, "csubj")) {
                continue;
                // handle NN root with JJ intent amod see ex. utterance9
            } else if (root == currentNode && Objects.equals(pos, "JJ") && Objects.equals(relation, "amod")) {
                continue;
            }
            descriptionWordsChildren.addAll(traverse(dependencyParse, childNode, root));
        }
        descriptionWords.addAll(descriptionWordsChildren);
        return descriptionWords;
    }
}
