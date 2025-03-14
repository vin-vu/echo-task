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

        for (IndexedWord childNode : dependencyParse.getChildList(currentNode)) {
            String childNodeRelation = dependencyParse.getEdge(currentNode, childNode).getRelation().toString();

            if (currentNode == root && Objects.equals(childNodeRelation, "amod")) {
                log.info("adding noun root: {} -  skipped intent word: {}", root, currentNode); // see example C5, c6
                descriptionWords.add(root);
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
