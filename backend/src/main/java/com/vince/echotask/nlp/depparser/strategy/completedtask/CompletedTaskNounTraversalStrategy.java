package com.vince.echotask.nlp.depparser.strategy.completedtask;

import com.vince.echotask.nlp.depparser.strategy.TraversalStrategy;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CompletedTaskNounTraversalStrategy implements TraversalStrategy {
    @Override
    public List<IndexedWord> traverse(SemanticGraph dependencyParse, IndexedWord currentNode, IndexedWord root) {

        List<IndexedWord> descriptionWords = new ArrayList<>();

        if (currentNode == root) {
            descriptionWords.add(root);
            log.info("adding  root: {}", root);
        }

        for (IndexedWord childNode : dependencyParse.getChildList(currentNode)) {
            String childNodeRelation = dependencyParse.getEdge(currentNode, childNode).getRelation().toString();

            if (Objects.equals(childNodeRelation, "amod") && dependencyParse.getChildList(childNode).isEmpty()) {
                log.info("skipping amod intent: {}", childNode); // see example C5, C7
                continue;
            }
            log.info("adding word: {}", childNode);
            descriptionWords.add(childNode);
            descriptionWords.addAll(traverse(dependencyParse, childNode, root));

            log.info("description words children: {}", descriptionWords);
        }
        return descriptionWords;
    }
}
