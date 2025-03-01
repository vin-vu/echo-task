package com.vince.echotask.nlp;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PhraseParser {

    private static final StanfordCoreNLP pipeline;

    static {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,pos,depparse");
        props.setProperty("depparse.model", "edu/stanford/nlp/models/parser/nndep/english_UD.gz");
        pipeline = new StanfordCoreNLP(props);
    }

    public SemanticGraph createDependencyParseTree(String phrase) {

        CoreDocument document = pipeline.processToCoreDocument(phrase);
        pipeline.annotate(document);
        SemanticGraph dependencyParse = document.sentences().get(0).dependencyParse();
        log.info("document: {}", dependencyParse);
        return dependencyParse;
    }

    public String extractDescription(SemanticGraph dependencyParse) {

        IndexedWord root = dependencyParse.getFirstRoot();
        log.info("root: {}", root);

        List<IndexedWord> taskDescriptionWords = new ArrayList<>();
        traverseParseTree(dependencyParse, root, taskDescriptionWords);
        taskDescriptionWords.sort(Comparator.comparingInt(IndexedWord::index));
        String taskDescription = taskDescriptionWords.stream().map(IndexedWord::word).collect(Collectors.joining(" "));

        log.info("task description words: {}", taskDescription);
        return taskDescription;
    }

    private void traverseParseTree(SemanticGraph dependencyParse, IndexedWord parentNode, List<IndexedWord> taskDescriptionWords) {

        List<IndexedWord> childrenNodes = dependencyParse.getChildList(parentNode);

        for (IndexedWord childNode : childrenNodes) {
            SemanticGraphEdge edge = dependencyParse.getEdge(parentNode, childNode);
            String relation = edge.getRelation().toString();

            log.info("edge - relation: {}, {}", edge, relation);
            if (!Objects.equals(relation, "nsubj")) {
                taskDescriptionWords.add(childNode);
                traverseParseTree(dependencyParse, childNode, taskDescriptionWords);
            }
        }

    }
}
