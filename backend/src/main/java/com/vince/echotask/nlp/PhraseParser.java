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

        // Identify main object
        IndexedWord mainObject = null;
        List<IndexedWord> childrenOfRoot = dependencyParse.getChildList(root);
        List<IndexedWord> taskDescriptionWords = new ArrayList<>();

        for (IndexedWord sibling : childrenOfRoot) {
            SemanticGraphEdge edge = dependencyParse.getEdge(root, sibling);
            String relation = edge.getRelation().toString();
            log.info("edge to main object: {}, {}, {}", sibling, edge, relation);

            if (Objects.equals(relation, "nsubj")) {
                log.info("ignoring nsubj: {}", edge);
                continue;
            } else if (Objects.equals(relation, "obj")) {
                mainObject = sibling;
                taskDescriptionWords.add(mainObject);
                log.info("found main object: {}", mainObject);

                List<IndexedWord> childrenOfMainObject = dependencyParse.getChildList(mainObject);
                taskDescriptionWords.addAll(childrenOfMainObject);
            } else {
                taskDescriptionWords.add(sibling);
            }
        }

        taskDescriptionWords.sort(Comparator.comparingInt(IndexedWord::index));
        String taskDescription = taskDescriptionWords.stream().map(IndexedWord::word).collect(Collectors.joining(" "));
        log.info("task description words: {}", taskDescription);
        return taskDescription;
    }
}
