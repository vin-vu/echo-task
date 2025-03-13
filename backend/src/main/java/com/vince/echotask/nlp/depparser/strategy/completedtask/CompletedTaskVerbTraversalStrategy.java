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
            log.info("adding root: {}", root);
        }

        for (IndexedWord childNode : dependencyParse.getChildList(currentNode)) {
            String partOfSpeechChildNode = childNode.tag();

            if (Objects.equals(partOfSpeechChildNode, "VBN")) {
                log.info("skipping VBN intent: {}", childNode);
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
