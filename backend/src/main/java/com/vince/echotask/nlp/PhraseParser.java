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

        String rootPOS = root.tag();
        log.info("tag: {}", rootPOS);

        determineTraversalMethod(rootPOS, dependencyParse, root, taskDescriptionWords);

        taskDescriptionWords.sort(Comparator.comparingInt(IndexedWord::index));
        String taskDescription = taskDescriptionWords.stream().map(IndexedWord::word).collect(Collectors.joining(" "));

        log.info("task description words: {}", taskDescription);
        return taskDescription;
    }

    private void determineTraversalMethod(String tag, SemanticGraph dependencyParse, IndexedWord root, List<IndexedWord> taskDescriptionWords) {
        if (Objects.equals(tag, "VB") || Objects.equals(tag, "VBP")) {
            traverseVerbRootTree(dependencyParse, root, taskDescriptionWords);
        } else if (Objects.equals(tag, "NN")) {
            traverseNounRootTree(dependencyParse, root, taskDescriptionWords);
        }
    }

    private void traverseVerbRootTree(SemanticGraph dependencyParse, IndexedWord currentNode, List<IndexedWord> taskDescriptionWords) {

        List<IndexedWord> childrenNodes = dependencyParse.getChildList(currentNode);
        for (IndexedWord childNode : childrenNodes) {
            SemanticGraphEdge edge = dependencyParse.getEdge(currentNode, childNode);
            String relation = edge.getRelation().toString();
            log.info("edge - relation: {}, {}", edge, relation);

            // nsubj is typically the intent
            if (!Objects.equals(relation, "nsubj")) {
                taskDescriptionWords.add(childNode);
                traverseVerbRootTree(dependencyParse, childNode, taskDescriptionWords);
            } else {
                // add verb root when it's not associated with intent - see ex. utterance 6
                taskDescriptionWords.add(currentNode);
            }
        }
    }

    private void traverseNounRootTree(SemanticGraph dependencyParse, IndexedWord currentNode, List<IndexedWord> taskDescriptionWords) {

        if (taskDescriptionWords.isEmpty()) taskDescriptionWords.add(currentNode);

        List<IndexedWord> childrenNodes = dependencyParse.getChildList(currentNode);
        log.info("parent - children: {}, {}", currentNode, childrenNodes);

        for (IndexedWord childNode : childrenNodes) {
            SemanticGraphEdge edge = dependencyParse.getEdge(currentNode, childNode);
            String relation = edge.getRelation().toString();
            log.info("2: edge - relation: {}, {}", edge, relation);

            String pos = childNode.tag();

            // skip intent verb
            if (!Objects.equals(pos, "VB")) {
                taskDescriptionWords.add(childNode);
            }
            traverseNounRootTree(dependencyParse, childNode, taskDescriptionWords);
        }
    }
}
