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
            String childPartOfSpeech = childNode.tag();
            String childNodeRelation = dependencyParse.getEdge(currentNode, childNode).getRelation().toString();

            if (Objects.equals(childPartOfSpeech, "JJ") && Objects.equals(childNodeRelation, "amod")) {
                descriptionWords.add(root);
                log.info("adding noun root: {} -  skipped intent word: {}", root, currentNode);
                descriptionWords.addAll(traverse(dependencyParse, childNode, root));
            } else {
                descriptionWords.add(childNode);
                log.info("adding word: {}", childNode);
                descriptionWords.addAll(traverse(dependencyParse, childNode, root));
            }
            log.info("description words children: {}", descriptionWords);
        }
        return descriptionWords;
    }
}
