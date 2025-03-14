package com.vince.echotask.nlp.depparser.strategy.completedtask;

import com.vince.echotask.nlp.depparser.strategy.TraversalStrategy;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CompletedTaskVerbTraversalStrategy implements TraversalStrategy {

    @Override
    public List<IndexedWord> traverse(SemanticGraph dependencyParse, IndexedWord currentNode, IndexedWord root) {

        List<IndexedWord> descriptionWords = new ArrayList<>();

        String partOfSpeechCurrentNode = currentNode.tag();
        if (currentNode == root && !Objects.equals(partOfSpeechCurrentNode, "VBN")) {
            descriptionWords.add(root);
            log.info("adding root: {}", root); // see example C2
        }

        for (IndexedWord childNode : dependencyParse.getChildList(currentNode)) {
            String partOfSpeechChildNode = childNode.tag();
            String childNodeRelation = dependencyParse.getEdge(currentNode, childNode).getRelation().toString();

            if (currentNode == root && Objects.equals(partOfSpeechChildNode, "VBN")) {
                log.info("skipping VBN intent: {}", childNode); // see example C2
                descriptionWords.addAll(traverse(dependencyParse, childNode, root));

            } else if (currentNode == root && Objects.equals(childNodeRelation, "advmod")) {
                log.info("skipping advmod intent: {}", childNode);  // see example C3
                descriptionWords.addAll(traverse(dependencyParse, childNode, root));

            } else if (currentNode == root && Objects.equals(childNodeRelation, "nsubj")) {
                log.info("skipping nsubj intent: {}", childNode);  // see example C4
                descriptionWords.addAll(traverse(dependencyParse, childNode, root));

            } else {
                log.info("adding word: {}", childNode);
                descriptionWords.add(childNode);
                descriptionWords.addAll(traverse(dependencyParse, childNode, root));
            }
            log.info("description words children: {}", descriptionWords);
        }
        return descriptionWords;
    }
}
